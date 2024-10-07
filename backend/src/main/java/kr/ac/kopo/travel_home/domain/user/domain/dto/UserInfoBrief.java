package kr.ac.kopo.travel_home.domain.user.domain.dto;

import lombok.Data;

@Data
public class UserInfoBrief {
    private int numOfForeignCurrency;
    private int numOfHistory;
    private int numOfNotifications;
    private int numOfAutoPurchaseRefund;
}
