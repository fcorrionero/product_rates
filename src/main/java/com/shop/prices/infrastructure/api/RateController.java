package com.shop.prices.infrastructure.api;

import com.shop.prices.application.GetRateRequestDto;
import com.shop.prices.application.RateResponseDto;
import com.shop.prices.application.RateService;
import com.shop.prices.domain.exception.DomainEntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;

@Controller
public class RateController {

    private final RateService rateService;

    public RateController(
        @Autowired RateService rateService
    ) {
        this.rateService = rateService;
    }

    @GetMapping("/rate")
    public ResponseEntity<RateResponseDto> getRateByProductBrandAndDate(
        @RequestParam("product_id") long productId,
        @RequestParam("brand_id") long brandId,
        @RequestParam("application_date") String applicationDate
    ) {
        try {
            return ResponseEntity.ok().body(
                rateService.getRateByDate(new GetRateRequestDto(productId, brandId, applicationDate))
            );
        } catch (ParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (DomainEntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
