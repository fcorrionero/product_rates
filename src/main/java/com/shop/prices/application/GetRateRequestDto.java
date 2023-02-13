package com.shop.prices.application;

public record GetRateRequestDto(
    long productId,
    long brandId,
    String applicationDate
) {
}