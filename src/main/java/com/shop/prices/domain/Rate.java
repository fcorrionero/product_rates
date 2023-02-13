package com.shop.prices.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;

import java.util.Currency;
import java.util.Date;

@Entity
@Table(name = "prices")
public class Rate {

    @Id
    @Column(name = "price_list")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "brand_id", nullable = false)
    private long brandId;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "product_id", nullable = false)
    private long productId;

    @Column(name = "priority", nullable = false)
    private int priority;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "curr", nullable = false)
    private Currency curr;

    public Rate(long id, long brandId, Date startDate, Date endDate, long productId, int priority, double price, Currency curr) {
        this.id = id;
        this.brandId = brandId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.productId = productId;
        this.priority = priority;
        this.price = price;
        this.curr = curr;
    }

    protected Rate() {
    }

    public long getId() {
        return id;
    }

    public long getBrandId() {
        return brandId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public long getProductId() {
        return productId;
    }

    public double getPrice() {
        return price;
    }

    public Currency getCurr() {
        return curr;
    }
}
