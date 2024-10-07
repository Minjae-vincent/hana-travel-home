package kr.ac.kopo.travel_home.domain.mail.domain.request;

public record SendAlertMailRequest(String memberId, String code, String alertType, double alertAmount,
                                   double dealBasR) {
}
