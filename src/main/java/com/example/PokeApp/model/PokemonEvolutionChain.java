package com.example.PokeApp.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record PokemonEvolutionChain(
        @Schema(description = "The current Pokemon in the chain")
        Pokemon pokemon,
        @Schema(description = "List of potential next-stage evolutions")
        List<PokemonEvolutionChain> possibleEvolutions) {
}
