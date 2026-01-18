package com.example.PokeApp.config;

import org.openapitools.client.ApiClient;
import org.openapitools.client.api.PokemonApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {
    @Bean
    public ApiClient apiClient() {
        return new ApiClient();
    }

    @Bean
    public PokemonApi pokemonApi(ApiClient apiClient) {
        return new PokemonApi(apiClient);
    }

    //@Bean
    //public EvolutionApi evolutionApi(ApiClient apiClient) {
        //return new EvolutionApi(apiClient);
    //}
}