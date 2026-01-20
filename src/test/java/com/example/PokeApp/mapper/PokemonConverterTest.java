package com.example.PokeApp.mapper;

import com.example.PokeApp.enums.PokemonHeightUnit;
import com.example.PokeApp.enums.PokemonWeightUnit;
import com.example.PokeApp.model.Pokemon;
import com.example.PokeApp.model.PokemonHeight;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.PokemonDetail;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PokemonConverterTest {

    private final PokemonConverter pokemonConverter = new PokemonConverter();

    private final String pokemonName = "pika";
    private final Integer pokemonId = 1;

    @Test
    void toPokemon_mapsCorrectly() {
        PokemonDetail pokemonDetail = new PokemonDetail(pokemonId, "123")
                .weight(5)
                .height(7)
                .name(pokemonName);

        Pokemon pokemon = pokemonConverter.toPokemon(pokemonDetail);

        assertEquals(PokemonWeightUnit.KILOGRAM, pokemon.weight().unit());
        assertEquals(BigDecimal.valueOf(0.5), pokemon.weight().amount());
        assertEquals(PokemonHeightUnit.CENTIMETER, pokemon.height().unit());
        assertEquals(70, pokemon.height().amount());
        assertEquals(pokemonId, pokemon.id());
        assertEquals(pokemonName, pokemon.name());

    }

    @Test
    void toPokemon_handlesNullValues() {
        PokemonDetail pokemonDetail = new PokemonDetail(pokemonId, "")
                .weight(null)
                .height(null)
                .name(pokemonName);

        Pokemon pokemon = pokemonConverter.toPokemon(pokemonDetail);

        assertNull(pokemon.weight());
        assertNull(pokemon.height());
        assertEquals(pokemonId, pokemon.id());
        assertEquals(pokemonName, pokemon.name());
    }
}