package kr.ac.kopo.travel_home.domain.user.controller;

import kr.ac.kopo.travel_home.domain.user.domain.dto.TargetCurrency;
import kr.ac.kopo.travel_home.domain.user.domain.request.*;
import kr.ac.kopo.travel_home.domain.user.service.ExchangeRateService;
import kr.ac.kopo.travel_home.domain.user.service.TravelService;
import kr.ac.kopo.travel_home.domain.user.service.UserService;
import kr.ac.kopo.travel_home.global.payload.APIResponse;
import kr.ac.kopo.travel_home.global.payload.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final ExchangeRateService exchangeRateService;
    private final TravelService travelService;

    @GetMapping("/id-check")
    public boolean idCheck(@QueryParam("id") String id) {
        return userService.idCheck(id);
    }

    @PostMapping("/register")
    public APIResponse<?> register(@RequestBody RegisterRequest request) {
        if (userService.isUserExist(request.getName(), request.getBirthDate(), request.getPhoneNumber())) {
            return APIResponse.onFailure(String.valueOf(HttpStatus.CONFLICT.value()), "이미 가입된 회원입니다.", null);
        }

        if(!userService.isHanaUserExist(request.getSsnLast())) {
            return APIResponse.onFailure(String.valueOf(HttpStatus.NOT_FOUND.value()), "하나은행에 가입되어 있지 않습니다.", null);
        }

        if(userService.idCheck(request.getId())) {
            return APIResponse.onFailure(String.valueOf(HttpStatus.CONFLICT.value()), "이미 사용 중인 아이디입니다.", null);
        }

        try {
            userService.register(request);
        } catch (Exception e) {
            e.printStackTrace();
            return APIResponse.onFailure(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), "회원가입 중 오류가 발생했습니다.", null);
        }

        return APIResponse.onSuccess(null);
    }

    @PostMapping("/identity-verification")
    public APIResponse<?> identityVerification(@RequestBody RegisterRequest request) {
        try {
            String result = userService.sendSMS(request.getPhoneNumber());
//            String result = "123456";
            return APIResponse.onSuccess(result);
        } catch (Exception e) {
            return APIResponse.onFailure(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), "인증번호 전송 중 오류가 발생했습니다.", null);
        }
    }
    @PostMapping("login")
    public APIResponse<?> login(@RequestBody LoginRequest request) {
        if (!userService.isAccountExist(request.getId())) {
            return APIResponse.onFailure(String.valueOf(ErrorStatus._NOT_FOUND.getCode()), "존재하지 않는 회원입니다.", null);
        }

        if (!userService.isCorrectPassword(request)){
            return APIResponse.onFailure(String.valueOf(ErrorStatus._UNAUTHORIZED.getCode()), "비밀번호가 일치하지 않습니다.", null);
        }

        try {
            String accessToken = userService.signIn(request);
            return APIResponse.onSuccess(accessToken);
        } catch (Exception e) {
            e.printStackTrace();
            return APIResponse.onFailure(String.valueOf(ErrorStatus._INTERNAL_SERVER_ERROR.getCode()), "로그인 중 오류가 발생했습니다.", null);
        }
    }

    @GetMapping("/{userId}/info-brief")
    public APIResponse<?> getUserInfoBrief(@PathVariable("userId") String userId) {
        return APIResponse.onSuccess(userService.getUserInfoBrief(userId));
    }

//    마이 하우스

    @GetMapping("/travel-journals")
    public APIResponse<?> getTravelJournals(@QueryParam("userId") String userId) {
        return APIResponse.onSuccess(userService.getTravelJournals(userId));
    }

    @GetMapping("/travel-record/{travelRecordId}")
    public APIResponse<?> getTravelRecord(@PathVariable("travelRecordId") String travelRecordId) {
        return APIResponse.onSuccess(userService.getTravelRecord(travelRecordId));
    }

    @GetMapping("/travel-now/{userId}")
    public APIResponse<?> isTraveling(@PathVariable("userId") String userId) {
        return APIResponse.onSuccess(userService.getTravelingSpace(userId));
    }

    @GetMapping("/travel-recommendation/{userId}")
    public APIResponse<?> getTravelRecommendation(@PathVariable("userId") String userId) {
        return APIResponse.onSuccess(userService.getTravelRecommendation(userId));
    }

    @GetMapping("/travel-recommendation")
    public APIResponse<?> getTravelRecommendationList(@RequestParam("month") String month, @RequestParam("ageRange") String ageRange, @RequestParam("gender") String gender, @RequestParam("userId") String userId) {
        if(userId.equals("")) {
            userId = "not-login";
        }
        return APIResponse.onSuccess(userService.getTravelRecommendationList(month, ageRange, gender, userId));
    }

//    환율 하우스

    @GetMapping("/exchange-rate/fluctuation")
    public APIResponse<?> getExchangeRateFluctuation() {
        return APIResponse.onSuccess(exchangeRateService.getExchangeRateFluctuation());
    }

    @GetMapping("/exchange-rate/real-time")
    public APIResponse<?> getExchangeRateRealTime() {
        return APIResponse.onSuccess(exchangeRateService.getExchangeRateRealTime());
    }

    @GetMapping("/exchange-rate/now/{code}")
    public APIResponse<?> getExchangeRateNow(@PathVariable("code") String code) {
        return APIResponse.onSuccess(exchangeRateService.getCurrentExchangeRate(code));
    }

    @GetMapping("/exchange-rate/stability")
    public APIResponse<?> getExchangeRateMonths() {
        return APIResponse.onSuccess(exchangeRateService.getExchangeRateMonths());
    }

    @GetMapping("/exchange-rate/seasonal")
    public APIResponse<?> getExchangeRateSeasonal() {
        return APIResponse.onSuccess(exchangeRateService.getExchangeRateSeasonal());
    }

//    여행 하우스

    @GetMapping("/travel/real-time")
    public APIResponse<?> getTravelRealTime() {
        return APIResponse.onSuccess(travelService.getTravelRealTime());
    }

    @GetMapping("/travel/history/{userId}")
    public APIResponse<?> getTravelHistory(@PathVariable("userId") String userId) {
        return APIResponse.onSuccess(userService.getTravelHistory(userId));
    }

    @GetMapping("/travel/recommendation/{userId}")
    public APIResponse<?> getTravelRecommendCustom(@PathVariable("userId") String userId) {
        return APIResponse.onSuccess(userService.getTravelRecommendCustom(userId));
    }

    @GetMapping("/travel/recommendation")
    public APIResponse<?> getTravelRecommendNow() {
        return APIResponse.onSuccess(userService.getTravelRecommendNow());
    }

//    마이페이지

    @GetMapping("/manage/account/{userId}")
    public APIResponse<?> getAccountInfo(@PathVariable("userId") String userId) {
        return APIResponse.onSuccess(userService.getAccountInfo(userId));
    }

    @GetMapping("/manage/possession/{userId}")
    public APIResponse<?> getTravelPossession(@PathVariable("userId") String userId) {
        return APIResponse.onSuccess(userService.getTravelPossession(userId));
    }

    @GetMapping("/manage/possession/{userId}/{currency}/payment-history")
    public APIResponse<?> getPaymentHistory(@PathVariable("userId") String userId, @PathVariable("currency") String currency) {
        return APIResponse.onSuccess(userService.getPaymentHistory(userId, currency));
    }

    @PostMapping("/manage/possession/charge")
    public APIResponse chargePossession(@RequestBody ChargeRequest request) {
        return APIResponse.onSuccess(userService.chargePossession(request));
    }

    @PostMapping("/manage/possession/refund")
    public APIResponse refundPossession(@RequestBody ChargeRequest request) {
        return APIResponse.onSuccess(userService.refundPossession(request));
    }

    @GetMapping("/manage/possession/target/{userId}/{code}")
    public APIResponse<?> getTravelTargetAmount(@PathVariable("userId") String userId, @PathVariable("code") String code) {
        return APIResponse.onSuccess(userService.getTravelTargetAmount(userId, code));
    }

    @PostMapping("/manage/possession/target")
    public APIResponse<?> setTravelTargetAmount(@RequestBody TargetCurrency targetCurrency) {
        return APIResponse.onSuccess(userService.setTravelTargetAmount(targetCurrency));
    }

    @GetMapping("/manage/possession/alert/{userId}/{code}")
    public APIResponse<?> getTravelAlertAmount(@PathVariable("userId") String userId, @PathVariable("code") String code) {
        return APIResponse.onSuccess(userService.getExchangeRateAlertInfo(userId, code));
    }

    @PostMapping("/manage/possession/alert/{userId}/{code}")
    public APIResponse<?> setTravelAlertAmount(@RequestBody AlertRequest request, @PathVariable("userId") String userId, @PathVariable("code") String code) {
        return APIResponse.onSuccess(userService.setExchangeRateAlertInfo(request, userId, code));
    }

    @GetMapping("/manage/possession/auto-buy/{code}")
    public APIResponse<?> getAutoBuy(@PathVariable("code") String code) {
        return APIResponse.onSuccess(exchangeRateService.getAutoBuy(code));
    }

    @GetMapping("/manage/possession/auto-buy/{code}/{userId}")
    public APIResponse<?> getAutoBuyInfo(@PathVariable("code") String code, @PathVariable("userId") String userId) {
        return APIResponse.onSuccess(userService.getAutoBuyInfo(code, userId));
    }

    @PostMapping("/manage/possession/auto-buy/{code}")
    public APIResponse<?> setAutoBuy(@RequestBody AutoBuyRequest request, @PathVariable("code") String code) {
        return APIResponse.onSuccess(userService.setAutoBuy(request, code));
    }

    @GetMapping("/manage/possession/auto-refund/{code}")
    public APIResponse<?> getAutoRefund(@PathVariable("code") String code) {
        return APIResponse.onSuccess(exchangeRateService.getAutoRefund(code));
    }

    @GetMapping("/manage/possession/auto-refund/{code}/{userId}")
    public APIResponse<?> getAutoRefundInfo(@PathVariable("code") String code, @PathVariable("userId") String userId) {
        return APIResponse.onSuccess(userService.getAutoRefundInfo(code, userId));
    }

    @PostMapping("/manage/possession/auto-refund/{code}")
    public APIResponse<?> setAutoRefund(@RequestBody AutoRefundRequest request, @PathVariable("code") String code) {
        return APIResponse.onSuccess(userService.setAutoRefund(request, code));
    }

    @GetMapping("/manage/consumption-report/{userId}/most-spent")
    public APIResponse<?> getMostSpent(@PathVariable("userId") String userId) {
        return APIResponse.onSuccess(userService.getMostSpent(userId));
    }

    @GetMapping("/manage/consumption-report/{userId}/category-statics")
    public APIResponse<?> getCategoryStatics(@PathVariable("userId") String userId) {
        return APIResponse.onSuccess(userService.getCategoryStatics(userId));
    }

    @GetMapping("/manage/consumption-report/{userId}/compare")
    public APIResponse<?> getCompareStatics(@PathVariable("userId") String userId) {
        return APIResponse.onSuccess(userService.getCompareStatics(userId));
    }

}
