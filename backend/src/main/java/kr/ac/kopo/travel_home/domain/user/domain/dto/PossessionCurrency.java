package kr.ac.kopo.travel_home.domain.user.domain.dto;

import lombok.Data;

@Data
public class PossessionCurrency {
    private String code;
    private String codeName;
    private int balance;
    private int targetAmount;
}
