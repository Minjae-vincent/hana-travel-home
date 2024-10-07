package kr.ac.kopo.travel_home.domain.travel.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TravelRecord {
    private String paymentHistoryId;
    private String location;
    private Date paymentDt;
    private double paymentAmount;
    private double lat;
    private double lon;
    private String status;
}
