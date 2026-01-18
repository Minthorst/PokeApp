package com.example.PokeApp.service;

import com.example.PokeApp.model.Pokemon;
import com.example.PokeApp.model.PokemonEvolutionChain;

public interface PokemonService {

    Pokemon retrievePokemon(String pokemonNameOrId);

    PokemonEvolutionChain retrievePokemonEvolutionChain(String pokemonNameOrId);

}

