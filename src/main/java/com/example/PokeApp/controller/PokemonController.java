package com.example.PokeApp.controller;

import com.example.PokeApp.model.Pokemon;
import com.example.PokeApp.model.PokemonEvolutionChain;
import com.example.PokeApp.service.PokemonService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PokemonController {
     private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @Operation(summary = "Get Pokemon by Name or Id", description = "Returns size and weight for a specific Pokemon.")
    @GetMapping(value = "/pokemon/{pokemonNameOrId}")
    public Pokemon getPokemon(@PathVariable String pokemonNameOrId) {
        return pokemonService.retrievePokemon(pokemonNameOrId);
    }

    @Operation(summary = "Get Pokemon Evolution Chain by Name or Id", description = "Returns possible evolutions and the corresponding Pokemon details.")
    @GetMapping(value = "/pokemon/evolutionChain/{pokemonNameOrId}")
    public PokemonEvolutionChain getPokemonEvolutions(@PathVariable String pokemonNameOrId) {
        return pokemonService.retrievePokemonEvolutionChain(pokemonNameOrId);
    }
}
