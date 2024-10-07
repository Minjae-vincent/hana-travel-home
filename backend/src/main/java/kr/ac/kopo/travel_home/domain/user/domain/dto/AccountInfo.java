package kr.ac.kopo.travel_home.domain.user.domain.dto;

import lombok.Data;

@Data
public class AccountInfo {
    private String name;
    private String accountNumber;
    private int balance;
}
