package kr.ac.kopo.travel_home.domain.travel.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TravelJournals {
    private String travelRecordId;
    private Date startDt;
    private Date endDt;
    private double totalAmountSpent;
    private String travelName;
    private String code;
}
