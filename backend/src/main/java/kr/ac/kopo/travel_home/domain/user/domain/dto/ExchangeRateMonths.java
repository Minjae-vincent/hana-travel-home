package kr.ac.kopo.travel_home.domain.user.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExchangeRateMonths {
    private String codeName;
    private String code;
    private List<Double> exchangeList;
}
