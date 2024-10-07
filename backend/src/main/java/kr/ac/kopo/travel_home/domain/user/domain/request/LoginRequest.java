package kr.ac.kopo.travel_home.domain.user.domain.request;

import lombok.Value;

@Value
public class LoginRequest {
    String id;
    String password;
}
