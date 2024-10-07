package kr.ac.kopo.travel_home.domain.user.domain.request;

import lombok.Data;

@Data
public class AutoBuyRequest {
    private String memberId;
    private double exchangeRate;
    private boolean enableAuto;
    private double rechargeAmount;
    private String period;
}
