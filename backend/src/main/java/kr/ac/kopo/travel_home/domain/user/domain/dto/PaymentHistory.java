package kr.ac.kopo.travel_home.domain.user.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PaymentHistory {
    private String paymentDate;
    private String paymentTimeStamp;
    private String store;
    private double amount;
    private double lat;
    private double lng;
}
