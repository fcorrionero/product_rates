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
import java.util.Optional;

@Service
public class RateService {
    private final RateRepository rateRepository;
    private final SimpleDateFormat dateFormat;

    public RateService(
        @Autowired RateRepository rateRepository,
        @Autowired SimpleDateFormat dateFormat
    ) {
        this.rateRepository = rateRepository;
        this.dateFormat = dateFormat;
    }

    @Cacheable(value= CaffeineConfiguration.MAIN_CACHE_NAME, key="#rateRequestDto.productId()+'/'+#rateRequestDto.brandId()+'/'+#rateRequestDto.applicationDate()")
    public RateResponseDto getRateByDate(GetRateRequestDto rateRequestDto) throws ParseException, DomainEntityNotFoundException {
        Date applicationDate = dateFormat.parse(rateRequestDto.applicationDate());
        Optional<Rate> rate = rateRepository.findRateByProductIdAndBrandIdAndApplicationDate(rateRequestDto.productId(), rateRequestDto.brandId(), applicationDate);
        if (rate.isPresent()) {
            return RateResponseDto.createFromRate(rate.get(), dateFormat);
        }

        throw new DomainEntityNotFoundException("Rate not found");
    }

}
