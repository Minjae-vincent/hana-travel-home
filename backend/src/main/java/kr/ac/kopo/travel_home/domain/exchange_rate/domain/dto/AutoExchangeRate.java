package kr.ac.kopo.travel_home.domain.exchange_rate.domain.dto;

import lombok.Data;

@Data
public class AutoExchangeRate {
    private double minExchangeRate3m;
    private double minExchangeRate1m;
    private double minExchangeRate1w;
    private double currentExchangeRate;
}


