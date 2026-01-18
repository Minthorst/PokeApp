package com.example.PokeApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PokemonNotFoundException extends RuntimeException {

    public PokemonNotFoundException(String identifier) {
        super(String.format("Pokemon with name or ID '%s' not found.", identifier));
    }
}
