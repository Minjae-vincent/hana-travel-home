package kr.ac.kopo.travel_home.domain.user.domain.dto;

import lombok.Data;

@Data
public class AutoTrade {
    private boolean enableAuto;
    private double exchangeRate;
    private double rechargeAmount;
    private String period;
}
