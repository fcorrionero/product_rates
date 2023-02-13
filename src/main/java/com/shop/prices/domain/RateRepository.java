package com.shop.prices.domain;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface RateRepository extends CrudRepository<Rate, Long> {

    @Query(
        """
            SELECT r FROM Rate r WHERE
                r.productId = ?1
                AND r.brandId = ?2
                AND ?3 BETWEEN r.startDate AND r.endDate
                ORDER BY r.priority DESC LIMIT 1
        """
    )
    Optional<Rate> findRateByProductIdAndBrandIdAndApplicationDate(
        long productId,
        long brandId,
        @NotNull Date applicationDate
    );
}
