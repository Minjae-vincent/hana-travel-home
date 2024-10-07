package kr.ac.kopo.travel_home.domain.user.domain.dto;

import lombok.Data;

@Data
public class TravelCustomRecommend {
    private String codeName;
    private String code;
    private double exchangeRate;
    private double averageSpending;
}
