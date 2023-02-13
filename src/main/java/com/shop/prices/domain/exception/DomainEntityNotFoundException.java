package com.shop.prices.domain.exception;

public class DomainEntityNotFoundException extends RuntimeException {
    public DomainEntityNotFoundException(String message) {
        super(message);
    }
}
