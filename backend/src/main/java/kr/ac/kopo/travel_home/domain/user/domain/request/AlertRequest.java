package kr.ac.kopo.travel_home.domain.user.domain.request;

import lombok.Data;

@Data
public class AlertRequest {
    private String conditionType;
    private boolean alertFlag;
    private double targetCurrency;
}
