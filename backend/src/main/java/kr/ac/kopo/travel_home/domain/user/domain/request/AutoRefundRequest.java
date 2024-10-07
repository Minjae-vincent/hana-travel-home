package kr.ac.kopo.travel_home.domain.user.domain.request;

import lombok.Data;

@Data
public class AutoRefundRequest {
    private String memberId;
    private double exchangeRate;
    private boolean enableAuto;
    private String refundAmount;
    private String period;
}
