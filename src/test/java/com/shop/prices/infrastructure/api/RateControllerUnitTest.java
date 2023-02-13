package com.shop.prices.infrastructure.api;

import com.shop.prices.application.GetRateRequestDto;
import com.shop.prices.application.RateResponseDto;
import com.shop.prices.application.RateService;
import com.shop.prices.domain.exception.DomainEntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RateControllerUnitTest {
    private final RateService rateService = Mockito.mock(RateService.class);

    private RateController rateController;

    @BeforeEach
    public void beforeEach() {
        Mockito.reset(rateService);
        rateController = new RateController(rateService);
    }

    @Test
    public void should_return_a_ResponseEntity_with_a_valid_RateResponseDto() throws ParseException {
        long givenProductId = 35455;
        long givenBrandId = 1;
        String givenDate = "2020-06-15 00:00:00";
        GetRateRequestDto givenGetRateRequestDto = new GetRateRequestDto(givenProductId, givenBrandId, givenDate);
        Mockito.when(rateService.getRateByDate(givenGetRateRequestDto))
            .thenReturn(new RateResponseDto(givenProductId, givenBrandId, 1, "2020-06-14 00:00:00", "2020-12-31 23:59:59", 35.50, "EUR"));

        ResponseEntity<RateResponseDto> response = rateController.getRateByProductBrandAndDate(givenProductId, givenBrandId, givenDate);

        Mockito.verify(rateService, Mockito.times(1)).getRateByDate(givenGetRateRequestDto);
        RateResponseDto expectedRateResponseDto = new RateResponseDto(givenProductId, givenBrandId, 1, "2020-06-14 00:00:00", "2020-12-31 23:59:59", 35.50, "EUR");
        ResponseEntity<RateResponseDto> expectedEntityResponse = ResponseEntity.ok().body(expectedRateResponseDto);

        Assertions.assertEquals(expectedEntityResponse, response);
    }

    @Test
    public void should_return_a_bad_request_when_date_is_not_parseable() throws ParseException {
        long givenProductId = 35455;
        long givenBrandId = 1;
        String givenDate = "2020/06/15T00:00:00";
        GetRateRequestDto givenGetRateRequestDto = new GetRateRequestDto(givenProductId, givenBrandId, givenDate);
        Mockito.when(rateService.getRateByDate(givenGetRateRequestDto))
            .thenThrow(new ParseException("", 1));

        ResponseEntity<RateResponseDto> response = rateController.getRateByProductBrandAndDate(givenProductId, givenBrandId, givenDate);
        Mockito.verify(rateService, Mockito.times(1)).getRateByDate(givenGetRateRequestDto);
        ResponseEntity<RateResponseDto> expectedEntityResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Assertions.assertEquals(expectedEntityResponse, response);
    }

    @Test
    public void should_return_a_not_found_when_rate_is_not_present() throws ParseException {
        long givenProductId = 35455;
        long givenBrandId = 1;
        String givenDate = "2020-06-15 00:00:00";
        GetRateRequestDto givenGetRateRequestDto = new GetRateRequestDto(givenProductId, givenBrandId, givenDate);
        Mockito.when(rateService.getRateByDate(givenGetRateRequestDto))
            .thenThrow(new DomainEntityNotFoundException("Rate not found"));

        ResponseEntity<RateResponseDto> response = rateController.getRateByProductBrandAndDate(givenProductId, givenBrandId, givenDate);
        Mockito.verify(rateService, Mockito.times(1)).getRateByDate(givenGetRateRequestDto);
        ResponseEntity<RateResponseDto> expectedEntityResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Assertions.assertEquals(expectedEntityResponse, response);
    }
}