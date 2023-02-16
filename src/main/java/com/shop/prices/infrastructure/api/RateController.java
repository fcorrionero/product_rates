package com.shop.prices.infrastructure.api;

import com.shop.prices.application.GetRateRequestDto;
import com.shop.prices.application.RateResponseDto;
import com.shop.prices.application.RateUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;

@Controller
public class RateController {

    private final RateUseCase rateUseCase;

    public RateController(
        @Autowired RateUseCase rateUseCase
    ) {
        this.rateUseCase = rateUseCase;
    }

    @GetMapping("/rate")
    @ResponseBody
    public ResponseEntity<RateResponseDto> getRateByProductBrandAndDate(
        @RequestParam("product_id") long productId,
        @RequestParam("brand_id") long brandId,
        @RequestParam("application_date") String applicationDate
    ) throws ParseException {
        return ResponseEntity.ok().body(
            rateUseCase.getRateByDate(new GetRateRequestDto(productId, brandId, applicationDate))
        );
    }
}
