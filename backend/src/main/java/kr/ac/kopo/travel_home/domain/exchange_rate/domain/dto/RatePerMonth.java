package kr.ac.kopo.travel_home.domain.exchange_rate.domain.dto;

import lombok.Data;

@Data
public class RatePerMonth {
    private String month;
    private double rate;
}
