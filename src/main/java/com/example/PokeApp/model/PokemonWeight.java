package com.example.PokeApp.model;

import com.example.PokeApp.enums.PokemonWeightUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record PokemonWeight(
        @Schema(description = "Pokemon Weight On Earth")
        BigDecimal amount,
        PokemonWeightUnit unit) {
}
