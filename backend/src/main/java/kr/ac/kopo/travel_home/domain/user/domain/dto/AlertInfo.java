package kr.ac.kopo.travel_home.domain.user.domain.dto;

import lombok.Data;

@Data
public class AlertInfo {
    private String alertUpFlag;
    private double alertUpAmount;
    private String alertDownFlag;
    private double alertDownAmount;
    private double maxAmountThreeMonth;
    private double minAmountThreeMonth;
    private double exchangeRate;
}
