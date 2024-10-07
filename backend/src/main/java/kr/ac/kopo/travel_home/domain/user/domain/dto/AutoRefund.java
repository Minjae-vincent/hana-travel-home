package kr.ac.kopo.travel_home.domain.user.domain.dto;

import lombok.Data;

@Data
public class AutoRefund {
    private boolean enableAuto;
    private double exchangeRate;
    private String refundAmount;
    private String period;
}
