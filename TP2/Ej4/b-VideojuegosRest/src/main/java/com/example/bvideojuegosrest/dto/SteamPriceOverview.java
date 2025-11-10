package com.example.bvideojuegosrest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamPriceOverview {
    private String currency;
    private Integer initial;
    @JsonProperty("final")
    private Integer finalPrice;
    private Integer discount_percent;

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Integer getInitial() { return initial; }
    public void setInitial(Integer initial) { this.initial = initial; }
    public Integer getFinalPrice() { return finalPrice; }
    public void setFinalPrice(Integer finalPrice) { this.finalPrice = finalPrice; }
    public Integer getDiscount_percent() { return discount_percent; }
    public void setDiscount_percent(Integer discount_percent) { this.discount_percent = discount_percent; }
}
