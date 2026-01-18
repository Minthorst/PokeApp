package com.example.PokeApp.service;

import com.example.PokeApp.enums.PokemonHeightUnit;
import com.example.PokeApp.enums.PokemonWeightUnit;
import com.example.PokeApp.model.Pokemon;
import com.example.PokeApp.mapper.PokemonConverter;
import com.example.PokeApp.model.PokemonEvolutionChain;
import com.example.PokeApp.model.PokemonHeight;
import com.example.PokeApp.model.PokemonWeight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.BaseApi;
import org.openapitools.client.api.PokemonApi;
import org.openapitools.client.model.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PokemonServiceTest {

    @Mock
    private BaseApi baseApi;

    @Mock
    private PokemonApi pokemonApi;

    @Mock
    private PokemonConverter pokemonConverter;

    private PokemonService pokemonService;

    private String bulbasaurJson;

    @BeforeEach
    void setUp() {
        pokemonService = new PokemonService(baseApi, pokemonApi, pokemonConverter);

        bulbasaurJson = """
                {
                  "chain": {
                    "species": { "name": "bulbasaur" },
                    "evolves_to": [
                      {
                        "species": { "name": "ivysaur" },
                        "evolves_to": [
                          {
                            "species": { "name": "venusaur" },
                            "evolves_to": []
                          }
                        ]
                      }
                    ]
                  }
                }
                """;
    }

    @Test
    void retrievePokemonEvolutionChainSuccessfully() {
        PokemonSpeciesDetail speciesDetailMock = mock(PokemonSpeciesDetail.class);
        EvolutionChainSummary evoLinkMock = mock(EvolutionChainSummary.class);

        when(pokemonApi.apiV2PokemonSpeciesRetrieve("bulbasaur")).thenReturn(speciesDetailMock);
        when(speciesDetailMock.getEvolutionChain()).thenReturn(evoLinkMock);
        when(evoLinkMock.getUrl()).thenReturn(URI.create("https://pokeapi.co/api/v2/evolution-chain/1"));

        ResponseEntity<String> responseEntity = ResponseEntity.ok(bulbasaurJson);
        when(baseApi.invokeAPI(anyString(), eq(HttpMethod.GET), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        when(pokemonApi.apiV2PokemonRetrieve(anyString())).thenReturn(new PokemonDetail());

        when(pokemonConverter.toPokemon(any()))
                .thenAnswer(_ -> createMockPokemon());

        PokemonEvolutionChain result = pokemonService.retrievePokemonEvolutionChain("bulbasaur");

        // Verify
        assertNotNull(result);
        assertEquals(1, result.possibleEvolutions().size());
        assertEquals(1, result.possibleEvolutions().get(0).possibleEvolutions().size());

        verify(pokemonApi, times(3)).apiV2PokemonRetrieve(anyString());
        verify(baseApi, times(1)).invokeAPI(anyString(), any(), any());
    }

    @Test
    void testGetFullEvolutionTree_HandlesCircularRecursion() {
        String circularJson = """
                {
                  "chain": {
                    "species": { "name": "bulbasaur" },
                    "evolves_to": [{ "species": { "name": "bulbasaur" }, "evolves_to": [] }]
                  }
                }
                """;
        PokemonSpeciesDetail speciesDetailMock = mock(PokemonSpeciesDetail.class);
        EvolutionChainSummary evoLinkMock = mock(EvolutionChainSummary.class);

        when(pokemonApi.apiV2PokemonSpeciesRetrieve("bulbasaur")).thenReturn(speciesDetailMock);
        when(speciesDetailMock.getEvolutionChain()).thenReturn(evoLinkMock);
        when(evoLinkMock.getUrl()).thenReturn(URI.create("https://pokeapi.co/api/v2/evolution-chain/1"));

        ResponseEntity<String> responseEntity = ResponseEntity.ok(circularJson);
        when(baseApi.invokeAPI(anyString(), eq(HttpMethod.GET), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        when(pokemonApi.apiV2PokemonRetrieve(anyString())).thenReturn(new PokemonDetail());

        when(pokemonConverter.toPokemon(any()))
                .thenAnswer(_ -> createMockPokemon());

        PokemonEvolutionChain result = pokemonService.retrievePokemonEvolutionChain("bulbasaur");

        assertNotNull(result);
        assertTrue(result.possibleEvolutions().isEmpty());
    }


    private Pokemon createMockPokemon() {
        return new Pokemon("pokemon", 3, new PokemonHeight(1, PokemonHeightUnit.CENTIMETER), new PokemonWeight(BigDecimal.ONE, PokemonWeightUnit.KILOGRAM));
    }
}