package com.example.PokeApp.mapper;

import com.example.PokeApp.enums.PokemonHeightUnit;
import com.example.PokeApp.enums.PokemonWeightUnit;
import com.example.PokeApp.model.Pokemon;
import com.example.PokeApp.model.PokemonHeight;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.PokemonDetail;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PokemonConverterTest {

    private final PokemonConverter pokemonConverter = new PokemonConverter();

    @Test
    void toPokemon_mapsCorrectly() {
        PokemonDetail pokemonDetail = new PokemonDetail(1,"123")
                .weight(5)
                .height(7)
                .name("testimon");

        Pokemon pokemon = pokemonConverter.toPokemon(pokemonDetail);

        assertEquals(PokemonWeightUnit.KILOGRAM, pokemon.weight().unit());
        assertEquals(BigDecimal.valueOf(0.5), pokemon.weight().amount());
        assertEquals(PokemonHeightUnit.CENTIMETER, pokemon.height().unit());
        assertEquals(70, pokemon.height().amount());
    }
}