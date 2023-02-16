package com.shop.prices.application;

import com.shop.prices.domain.Rate;
import com.shop.prices.domain.RateRepository;
import com.shop.prices.domain.exception.DomainEntityNotFoundException;
import com.shop.prices.infrastructure.config.cache.CaffeineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class RateUseCase {
    private final RateRepository rateRepository;
    private final SimpleDateFormat dateFormat;

    public RateUseCase(
        @Autowired RateRepository rateRepository,
        @Autowired SimpleDateFormat dateFormat
    ) {
        this.rateRepository = rateRepository;
        this.dateFormat = dateFormat;
    }

    @Cacheable(value= CaffeineConfiguration.MAIN_CACHE_NAME, key="#rateRequestDto.productId()+'/'+#rateRequestDto.brandId()+'/'+#rateRequestDto.applicationDate()")
    public RateResponseDto getRateByDate(GetRateRequestDto rateRequestDto) throws ParseException, DomainEntityNotFoundException {
        Date applicationDate = dateFormat.parse(rateRequestDto.applicationDate());
        Rate rate = rateRepository
            .findRateByProductIdAndBrandIdAndApplicationDate(rateRequestDto.productId(), rateRequestDto.brandId(), applicationDate)
            .orElseThrow(() -> new DomainEntityNotFoundException("Rate not found"));
        return RateResponseDto.createFromRate(rate, dateFormat);
    }

}
