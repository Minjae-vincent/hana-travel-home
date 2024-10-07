package kr.ac.kopo.travel_home.domain.exchange_rate.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExchangeRatePerMonth {
    private String name;
    private List<Double> exchangeList;
}
