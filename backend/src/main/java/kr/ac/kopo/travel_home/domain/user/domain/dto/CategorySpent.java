package kr.ac.kopo.travel_home.domain.user.domain.dto;

import lombok.Data;

@Data
public class CategorySpent {
    private String category;
    private double totalAmountSpent;
}
