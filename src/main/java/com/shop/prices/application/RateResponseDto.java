package com.shop.prices.application;

import com.shop.prices.domain.Rate;

import java.text.SimpleDateFormat;

public record RateResponseDto(
    long productId,
    long brandId,
    long priceList,
    String startDate,
    String endDate,
    double price,
    String curr
) {
    public static RateResponseDto createFromRate(Rate rate, SimpleDateFormat dateFormat) {
        return new RateResponseDto(
            rate.getProductId(),
            rate.getBrandId(),
            rate.getId(),
            dateFormat.format(rate.getStartDate()),
            dateFormat.format(rate.getEndDate()),
            rate.getPrice(),
            rate.getCurr().toString()
        );
    }

    public boolean equals(Object o) {
        if (o.getClass() == RateResponseDto.class) {
            return ((RateResponseDto) o).productId == this.productId
                && ((RateResponseDto) o).brandId == this.brandId
                && ((RateResponseDto) o).priceList == this.priceList
                && ((RateResponseDto) o).startDate.equals(this.startDate)
                && ((RateResponseDto) o).endDate.equals(this.endDate)
                && ((RateResponseDto) o).price == this.price
                && ((RateResponseDto) o).curr.equals(this.curr);

        }
        return false;
    }
}
