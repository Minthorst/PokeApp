package com.example.PokeApp.model;

import com.example.PokeApp.enums.PokemonHeightUnit;
import io.swagger.v3.oas.annotations.media.Schema;

public record PokemonHeight(
        @Schema(description = "Pokemon Height Measured Head To Tail")
        Integer amount,
        PokemonHeightUnit unit) {
}
