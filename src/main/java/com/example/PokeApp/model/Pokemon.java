package com.example.PokeApp.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record Pokemon(
        @Schema(description = "The official name of the Pokemon", example = "bulbasaur")
        String name,
        @Schema(description = "The official ID of the Pokemon", example = "25")
        int id,
        PokemonHeight height,
        PokemonWeight weight) {
}
