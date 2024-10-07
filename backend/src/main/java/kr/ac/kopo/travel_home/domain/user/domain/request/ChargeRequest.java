package kr.ac.kopo.travel_home.domain.user.domain.request;

public record ChargeRequest(String memberId, int foreignAmount, int localAmount, String countryCode) {
}
