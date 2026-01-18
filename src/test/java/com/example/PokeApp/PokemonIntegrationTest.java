package com.example.PokeApp;

import com.example.PokeApp.model.Pokemon;
import com.example.PokeApp.service.PokemonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.openapitools.client.api.PokemonApi;
import org.openapitools.client.model.PokemonDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.File;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PokemonIntegrationTest {

    @Autowired
    private PokemonService pokemonService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private PokemonApi mockApi;

    @Test
    void shouldReturnMappedPokemonFromLocalJson() throws Exception {

        File jsonFile = new File("src/test/resources/getDitto.json");
        PokemonDetail mockDto = objectMapper.readValue(jsonFile, PokemonDetail.class);

        when(mockApi.apiV2PokemonRetrieve(anyString())).thenReturn(mockDto);

        Pokemon result = pokemonService.retrievePokemon("ditto");

        assertNotNull(result);
        assertEquals("ditto", result.name());
        assertEquals(30, result.height().amount());
        assertEquals(132, result.id());
        assertEquals(BigDecimal.valueOf(4.0), result.weight().amount());
    }
}