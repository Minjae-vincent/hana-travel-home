package kr.ac.kopo.travel_home.domain.user.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserDTO {
    private String memberId;
    private String codeId;
    private String name;
    private String ssnFirstPart;
    private String ssnLastPart;
    private String userId;
    private String userPw;
    private String email;
    private String phoneNumber;
    private String address;
    private Date registerDt;
    private Date modifiedDt;
}
