package kr.ac.kopo.travel_home.domain.user.domain.dto;

import lombok.Data;

@Data
public class TravelSpace {
   private String codeName;
   private String location;
   private double lat;
    private double lon;
}
