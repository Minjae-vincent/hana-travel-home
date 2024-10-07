package kr.ac.kopo.travel_home.domain.mail.domain.request;


public record SendAutoBuyAlertMailRequest(String memberId, String code, int autoBuyAmount) {
}
