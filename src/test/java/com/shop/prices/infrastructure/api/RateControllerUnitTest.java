package com.shop.prices.infrastructure.api;

import com.shop.prices.application.GetRateRequestDto;
import com.shop.prices.application.RateResponseDto;
import com.shop.prices.application.RateUseCase;
import com.shop.prices.domain.exception.DomainEntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RateControllerUnitTest {
    private final RateUseCase rateUseCase = Mockito.mock(RateUseCase.class);

    private RateController rateController;

    @BeforeEach
    public void beforeEach() {
        Mockito.reset(rateUseCase);
        rateController = new RateController(rateUseCase);
    }

    @Test
    public void should_return_a_ResponseEntity_with_a_valid_RateResponseDto() throws ParseException {
        long givenProductId = 35455;
        long givenBrandId = 1;
        String givenDate = "2020-06-15 00:00:00";
        GetRateRequestDto givenGetRateRequestDto = new GetRateRequestDto(givenProductId, givenBrandId, givenDate);
        Mockito.when(rateUseCase.getRateByDate(givenGetRateRequestDto))
            .thenReturn(new RateResponseDto(givenProductId, givenBrandId, 1, "2020-06-14 00:00:00", "2020-12-31 23:59:59", 35.50, "EUR"));

        ResponseEntity<RateResponseDto> response = rateController.getRateByProductBrandAndDate(givenProductId, givenBrandId, givenDate);

        Mockito.verify(rateUseCase, Mockito.times(1)).getRateByDate(givenGetRateRequestDto);
        RateResponseDto expectedRateResponseDto = new RateResponseDto(givenProductId, givenBrandId, 1, "2020-06-14 00:00:00", "2020-12-31 23:59:59", 35.50, "EUR");
        ResponseEntity<RateResponseDto> expectedEntityResponse = ResponseEntity.ok().body(expectedRateResponseDto);

        Assertions.assertEquals(expectedEntityResponse, response);
    }

    @Test
    public void should_throw_a_ParseException_when_date_is_not_parseable() throws ParseException {
        long givenProductId = 35455;
        long givenBrandId = 1;
        String givenDate = "2020/06/15T00:00:00";
        GetRateRequestDto givenGetRateRequestDto = new GetRateRequestDto(givenProductId, givenBrandId, givenDate);
        Mockito.when(rateUseCase.getRateByDate(givenGetRateRequestDto))
            .thenThrow(new ParseException("", 1));

        Assertions.assertThrows(ParseException.class, () ->rateController.getRateByProductBrandAndDate(givenProductId, givenBrandId, givenDate) );
        Mockito.verify(rateUseCase, Mockito.times(1)).getRateByDate(givenGetRateRequestDto);
    }

    @Test
    public void should_throw_a_DomainEntityNotFoundException_when_rate_is_not_present() throws ParseException {
        long givenProductId = 35455;
        long givenBrandId = 1;
        String givenDate = "2020-06-15 00:00:00";
        GetRateRequestDto givenGetRateRequestDto = new GetRateRequestDto(givenProductId, givenBrandId, givenDate);
        Mockito.when(rateUseCase.getRateByDate(givenGetRateRequestDto))
            .thenThrow(new DomainEntityNotFoundException("Rate not found"));

        Assertions.assertThrows(DomainEntityNotFoundException.class, () -> rateController.getRateByProductBrandAndDate(givenProductId, givenBrandId, givenDate));
        Mockito.verify(rateUseCase, Mockito.times(1)).getRateByDate(givenGetRateRequestDto);
    }
}