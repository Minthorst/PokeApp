package com.example.PokeApp.exception;

public record ApiError(
        String message,
        int statusCode,
        long timestamp
) {}
