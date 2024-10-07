package kr.ac.kopo.travel_home.domain.travel.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class TravelRecommend {
    private int totalNum;
    private List<TravelLocation> travelLocationList;
}
