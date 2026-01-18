package com.example.PokeApp.mapper;

import com.example.PokeApp.enums.PokemonHeightUnit;
import com.example.PokeApp.enums.PokemonWeightUnit;
import com.example.PokeApp.model.Pokemon;
import com.example.PokeApp.model.PokemonHeight;
import com.example.PokeApp.model.PokemonWeight;
import org.openapitools.client.model.PokemonDetail;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PokemonConverter {
    public Pokemon toPokemon(PokemonDetail retrievedPokemon) {
        return new Pokemon(retrievedPokemon.getName(), retrievedPokemon.getId(), toPokemonHeight(retrievedPokemon.getHeight()), toPokemonWeight(retrievedPokemon.getWeight()));
    }

    private PokemonWeight toPokemonWeight(Integer weight) {
        //as per documentation weight is provided by PokeApi as hectograms
        BigDecimal weightInKilos = BigDecimal.valueOf(weight * 0.1);
        return new PokemonWeight(weightInKilos, PokemonWeightUnit.KILOGRAM);

    }

    private PokemonHeight toPokemonHeight(int height) {
        //as per documentation height is provided by PokeApi as decimetres
        int heightInCentimeter = height * 10;
        return new PokemonHeight(heightInCentimeter, PokemonHeightUnit.CENTIMETER);
    }
}
