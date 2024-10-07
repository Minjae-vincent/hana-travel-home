package kr.ac.kopo.travel_home.domain.mail.domain.request;


public record SendAutoSellAlertMailRequest(String memberId, String code, String autoSellAmount, int realAmount, int balance) {
}
