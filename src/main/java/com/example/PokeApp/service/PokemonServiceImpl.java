package com.example.PokeApp.service;

import com.example.PokeApp.exception.PokemonNotFoundException;
import com.example.PokeApp.mapper.PokemonConverter;
import com.example.PokeApp.model.Pokemon;
import com.example.PokeApp.model.PokemonEvolutionChain;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openapitools.client.BaseApi;
import org.openapitools.client.api.PokemonApi;
import org.openapitools.client.model.PokemonDetail;
import org.openapitools.client.model.PokemonSpeciesDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PokemonServiceImpl implements PokemonService {


    private static final Logger log = LoggerFactory.getLogger(PokemonServiceImpl.class);
    private final BaseApi baseApi;
    private final PokemonApi pokemonApi;
    private final PokemonConverter pokemonConverter;

    public PokemonServiceImpl(BaseApi baseApi, PokemonApi pokemonApi, PokemonConverter pokemonConverter) {
        this.baseApi = baseApi;
        this.pokemonApi = pokemonApi;
        this.pokemonConverter = pokemonConverter;
    }

    @Override
    public Pokemon retrievePokemon(String pokemonNameOrId) {
        log.info("Getting pokemon detail for: {}", pokemonNameOrId);
        PokemonDetail retrievedPokemon;

        try {
            retrievedPokemon = pokemonApi.apiV2PokemonRetrieve(pokemonNameOrId);
        } catch (HttpClientErrorException.NotFound e) {
            throw new PokemonNotFoundException(pokemonNameOrId);
        }

        return pokemonConverter.toPokemon(retrievedPokemon);
    }

    @Override
    public PokemonEvolutionChain retrievePokemonEvolutionChain(String pokemonNameOrId) {
        //we need species, not pokemon name
        PokemonSpeciesDetail retrievedPokemonSpeciesDetail = pokemonApi.apiV2PokemonSpeciesRetrieve(pokemonNameOrId);
        //extract evolution chain url
        String evolutionChainUrl = retrievedPokemonSpeciesDetail.getEvolutionChain().getUrl().toString();
        ParameterizedTypeReference<String> typeRef = new ParameterizedTypeReference<>() {
        };
        //I could have used the generated evolution client but the generated client did not generate the recursive response correctly and didnt retrieve all the necessary info
        // used a provided BasApi instead to extract info directly from the Json response
        log.info("Starting evolution chain retrieval for: {}", pokemonNameOrId);
        String rawResponseBody = baseApi.invokeAPI(evolutionChainUrl, HttpMethod.GET, typeRef).getBody();
        return getFullEvolutionTree(rawResponseBody);
    }

    private PokemonEvolutionChain getFullEvolutionTree(String rawJson) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(rawJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //start at chain
        return buildEvolutionNode(root.path("chain"), new HashSet<>());
    }

    private PokemonEvolutionChain buildEvolutionNode(JsonNode node, Set<String> visited) {
        if (node == null || node.isMissingNode()) return null;

        //extract current pokemon name
        String speciesName = node.path("species").path("name").asText();

        //make sure we dont have circular recursion
        if (visited.contains(speciesName)) {
            log.warn("Circular evolution detected at: {}. Breaking recursion.", speciesName);
            return null;
        }

        //make sure we dont go "too deep"
        if (visited.size() > 15) {
            log.error("Evolution depth exceeded limit for: {}", speciesName);
            return null;
        }

        visited.add(speciesName);

        Pokemon currentPokemon = retrievePokemon(speciesName);

        //process "evolves_to" branches
        List<PokemonEvolutionChain> branches = new ArrayList<>();
        JsonNode evolvesTo = node.path("evolves_to");

        if (evolvesTo.isArray()) {
            for (JsonNode nextNode : evolvesTo) {
                PokemonEvolutionChain childChain = buildEvolutionNode(nextNode, visited);
                if (childChain != null) {
                    branches.add(childChain);
                }
            }
        }

        return new PokemonEvolutionChain(currentPokemon, branches);
    }
}
