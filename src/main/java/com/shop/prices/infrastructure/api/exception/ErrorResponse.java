package com.shop.prices.infrastructure.api.exception;

public record ErrorResponse(int status, String title, String description) {
}
