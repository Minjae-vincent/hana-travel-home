package kr.ac.kopo.travel_home.domain.user.domain.dto;

public record TargetCurrency(String currencyCode, String memberId, int targetAmount, boolean isEnabled) {
}
