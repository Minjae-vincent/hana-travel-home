package kr.ac.kopo.travel_home.domain.exchange_rate.domain.dto;

import lombok.Data;

@Data
public class ExchangeRateCompare {
    private String codeName;
    private String code;
    private double currentRate;
    private double threeMonthRate;
}
