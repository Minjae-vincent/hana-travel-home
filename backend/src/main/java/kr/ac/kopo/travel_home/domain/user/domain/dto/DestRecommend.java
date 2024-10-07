package kr.ac.kopo.travel_home.domain.user.domain.dto;

import lombok.Data;

@Data
public class DestRecommend {
    private String codeName;
    private String location;
    private double distance;
    private String category;
    private double lat;
    private double lon;
}
