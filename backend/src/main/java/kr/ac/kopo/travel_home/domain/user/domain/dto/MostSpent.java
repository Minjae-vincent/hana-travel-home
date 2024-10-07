package kr.ac.kopo.travel_home.domain.user.domain.dto;

import lombok.Data;

@Data
public class MostSpent {
    private String code;
    private String codeName;
    private String totalAmountSpent;
}
