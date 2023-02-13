package com.shop.prices.application;

import com.shop.prices.domain.Rate;
import com.shop.prices.domain.RateRepository;
import com.shop.prices.domain.exception.DomainEntityNotFoundException;
import com.shop.prices.infrastructure.config.DateConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Optional;

import static org.mockito.Mockito.times;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RateServiceUnitTest {

    private final RateRepository rateRepository = Mockito.mock(RateRepository.class);

    private final SimpleDateFormat simpleDateFormat = new DateConfig().dateFormat("yyyy-MM-dd HH:mm:ss");

    private RateService rateService;

    @BeforeEach
    public void beforeEach() {
        Mockito.reset(rateRepository);
        rateService = new RateService(rateRepository, simpleDateFormat);
    }

    @Test
    public void should_return_a_valid_response() throws ParseException {
        GetRateRequestDto givenGetRateRequestDto = new GetRateRequestDto(35455, 1, "2020-06-15 00:00:00");
        Rate givenRate = new Rate(
            1,
            1,
            simpleDateFormat.parse("2020-06-14 00:00:00"),
            simpleDateFormat.parse("2020-12-31 23:59:59"),
            givenGetRateRequestDto.productId(),
            1,
            35.50,
            Currency.getInstance("EUR")
        );
        Mockito.when(rateRepository
                .findRateByProductIdAndBrandIdAndApplicationDate(
                    givenGetRateRequestDto.productId(),
                    givenGetRateRequestDto.brandId(),
                    simpleDateFormat.parse(givenGetRateRequestDto.applicationDate())))
            .thenReturn(Optional.of(givenRate));

        RateResponseDto rateResponseDto = rateService.getRateByDate(givenGetRateRequestDto);

        RateResponseDto expectedRateResponseDto = RateResponseDto.createFromRate(givenRate, simpleDateFormat);
        Assertions.assertEquals(expectedRateResponseDto, rateResponseDto);
    }

    @Test
    public void should_throw_a_DomainEntityNotFoundException_when_there_is_no_rate() throws ParseException {
        GetRateRequestDto givenGetRateRequestDto = new GetRateRequestDto(35455, 1, "2020-06-15 00:00:00");
        Mockito.when(rateRepository
                .findRateByProductIdAndBrandIdAndApplicationDate(
                    givenGetRateRequestDto.productId(),
                    givenGetRateRequestDto.brandId(),
                    simpleDateFormat.parse(givenGetRateRequestDto.applicationDate())))
            .thenReturn(Optional.empty());

        Assertions.assertThrows(
            DomainEntityNotFoundException.class,
            () -> rateService.getRateByDate(givenGetRateRequestDto)
        );
        Mockito.verify(rateRepository, times(1))
            .findRateByProductIdAndBrandIdAndApplicationDate(
                givenGetRateRequestDto.productId(),
                givenGetRateRequestDto.brandId(),
                simpleDateFormat.parse(givenGetRateRequestDto.applicationDate())
        );
    }

}