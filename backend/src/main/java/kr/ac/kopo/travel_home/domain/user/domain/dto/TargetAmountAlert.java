package kr.ac.kopo.travel_home.domain.user.domain.dto;

import lombok.Data;

@Data
public class TargetAmountAlert {
    private String memberId;
    private String codeId;
    private String balance;
    private String targetAmount;
}
