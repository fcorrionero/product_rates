package com.shop.prices.infrastructure.api;

import com.shop.prices.PricesApplication;
import com.shop.prices.application.RateResponseDto;
import com.shop.prices.infrastructure.config.cache.CaffeineConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;
import java.util.stream.Stream;

@SpringBootTest(
    classes = PricesApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureWebTestClient
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RateControllerAcceptanceTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void beforeEach() {
        Objects.requireNonNull(cacheManager.getCache(CaffeineConfiguration.MAIN_CACHE_NAME)).clear();
    }

    private static Stream<Arguments> acceptanceTestArguments() {
        return Stream.of(
            Arguments.of(35455, 1, "2020-06-14 10:00:00", new RateResponseDto(
                35455, 1, 1, "2020-06-14 00:00:00", "2020-12-31 23:59:59", 35.5, "EUR"
            )),
            Arguments.of(35455, 1, "2020-06-14 16:00:00", new RateResponseDto(
                35455, 1, 2, "2020-06-14 15:00:00", "2020-06-14 18:30:00", 25.45, "EUR"
            )),
            Arguments.of(35455, 1, "2020-06-14 21:00:00", new RateResponseDto(
                35455, 1, 1, "2020-06-14 00:00:00", "2020-12-31 23:59:59", 35.5, "EUR"
            )),
            Arguments.of(35455, 1, "2020-06-15 10:00:00", new RateResponseDto(
                35455, 1, 3, "2020-06-15 00:00:00", "2020-06-15 11:00:00", 30.5, "EUR"
            )),
            Arguments.of(35455, 1, "2020-06-16 21:00:00", new RateResponseDto(
                35455, 1, 4, "2020-06-15 16:00:00", "2020-12-31 23:59:59", 38.95, "EUR"
            ))
        );
    }

    @ParameterizedTest
    @MethodSource("acceptanceTestArguments")
    public void should_return_a_RateResponse(
        long givenProductId,
        long givenBrandId,
        String givenApplicationDate,
        RateResponseDto expectedResponse
    ) {
        EntityExchangeResult<RateResponseDto> response = webTestClient.get()
            .uri(
                String.format("/rate?product_id=%s&brand_id=%s&application_date=%s",
                    givenProductId,
                    givenBrandId,
                    givenApplicationDate
                )
            )
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(RateResponseDto.class)
            .returnResult();

        Assertions.assertEquals(expectedResponse, response.getResponseBody());
    }

    @Test
    public void should_return_a_bad_request_if_date_is_not_parseable() {
        String givenProductId = "35455";
        String givenBrandId = "1";
        String givenApplicationDate = "2020-06-15";

        EntityExchangeResult<RateResponseDto> response = webTestClient.get()
            .uri(
                String.format("/rate?product_id=%s&brand_id=%s&application_date=%s",
                    givenProductId,
                    givenBrandId,
                    givenApplicationDate
                )
            )
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(RateResponseDto.class)
            .returnResult();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
    }

    @Test
    public void should_return_a_not_found_response_if_there_is_no_rate() {
        String givenProductId = "8888";
        String givenBrandId = "1";
        String givenApplicationDate = "2020-06-15 00:00:00";

        EntityExchangeResult<RateResponseDto> response = webTestClient.get()
            .uri(
                String.format("/rate?product_id=%s&brand_id=%s&application_date=%s",
                    givenProductId,
                    givenBrandId,
                    givenApplicationDate
                )
            )
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(RateResponseDto.class)
            .returnResult();

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    @Test
    public void should_persist_RateResponseDto_in_cache() {
        String givenProductId = "35455";
        String givenBrandId = "1";
        String givenApplicationDate = "2020-06-15 00:00:00";

        webTestClient.get()
            .uri(
                String.format("/rate?product_id=%s&brand_id=%s&application_date=%s",
                    givenProductId,
                    givenBrandId,
                    givenApplicationDate
                )
            )
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(RateResponseDto.class)
            .returnResult();

        Assertions.assertNotNull(Objects.requireNonNull(
                cacheManager.getCache(CaffeineConfiguration.MAIN_CACHE_NAME)
            ).get(givenProductId + '/' + givenBrandId + '/' + givenApplicationDate)
        );
        RateResponseDto expectedCacheObject = new RateResponseDto(
            35455,
            1,
            3,
            "2020-06-15 00:00:00",
            "2020-06-15 11:00:00",
            30.50,
            "EUR"
        );
        Object cacheObject = Objects.requireNonNull(
            Objects.requireNonNull(cacheManager.getCache(CaffeineConfiguration.MAIN_CACHE_NAME)).get(givenProductId + '/' + givenBrandId + '/' + givenApplicationDate)
        ).get();

        Assertions.assertEquals(expectedCacheObject, cacheObject);
    }
}
