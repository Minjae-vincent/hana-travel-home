package kr.ac.kopo.travel_home.domain.user.service;

import kr.ac.kopo.travel_home.domain.user.domain.request.AutoRefundRequest;
import kr.ac.kopo.travel_home.domain.user.domain.dto.*;
import kr.ac.kopo.travel_home.domain.user.domain.request.*;
import kr.ac.kopo.travel_home.domain.user.mapper.ExchangeRateMapper;
import kr.ac.kopo.travel_home.domain.user.mapper.UserMapper;
import kr.ac.kopo.travel_home.global.jwt.Claim;
import kr.ac.kopo.travel_home.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    final DefaultMessageService defaultMessageService = NurigoApp.INSTANCE.initialize("NCSYTOK9MOBSTLA5", "HD7UGAYJADWCIA9OA1KFWPXS1O9BNKYK", "https://api.coolsms.co.kr");
    private final JwtUtil jwtUtil;
    private final ExchangeRateService exchangeRateService;
    private final ExchangeRateMapper exchangeRateMapper;

    public boolean isUserExist(String name, String birth, String phoneNumber) {
        return userMapper.isUserExist(name, birth, phoneNumber);
    }

    public boolean isHanaUserExist(String ssnLast) {
        return userMapper.isHanaUserExist(ssnLast);
    }

    public boolean idCheck(String id) {
        return userMapper.isIdExist(id);
    }

    public void register(RegisterRequest request) {
        userMapper.register(request);
    }

    public String sendSMS(String phoneNumber) {
        String randomNum = makeRandomNumber();
        String msgString = """
                [Travel Home] \n
                회원가입을 위한 인증번호는 %s 입니다.
                """.formatted(randomNum);
        Message message1 = new Message();
        message1.setFrom("01040533513");
        phoneNumber = phoneNumber.replaceAll("-", "");
        message1.setTo(phoneNumber);
        message1.setText(msgString);

        defaultMessageService.sendOne(new SingleMessageSendingRequest(message1));
        return randomNum;
    }

    private String makeRandomNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }

    public boolean isAccountExist(String id) {
        return userMapper.isAccountExist(id);
    }

    public boolean isCorrectPassword(LoginRequest request) {
        return userMapper.isCorrectPassword(request);
    }

    public String signIn(LoginRequest request) {
        UserDTO user = userMapper.getUser(request.getId(), request.getPassword());
        Claim claim = new Claim();
        claim.setUserDTO(user);
        return jwtUtil.createAccessToken(claim);
    }

    public List<TravelJournals> getTravelJournals(String id) {
        return userMapper.getTravelJournals(id);
    }

    public List<TravelRecord> getTravelRecord(String travelRecordId) {
        return userMapper.getTravelRecord(travelRecordId);
    }

    public TravelSpace getTravelingSpace(String userId) {
        return userMapper.getTraveling(userId);

    }

    public List<DestRecommend> getTravelRecommendation(String userId) {
        TravelSpace travelSpace = userMapper.getTraveling(userId);
        return userMapper.getTravelRecommendation(userId, travelSpace.getLat(), travelSpace.getLon());
    }

    public TravelRecommend getTravelRecommendationList(String month, String ageRange, String gender, String userId) {

        TravelRecommend travelRecommends = new TravelRecommend();
        travelRecommends.setTotalNum(userMapper.getTargetTotalTravelNum(month, ageRange, gender, userId));
        travelRecommends.setTravelLocationList(userMapper.getTargetCountryList(month, ageRange, gender, userId));


        return travelRecommends;
    }

    public List<TravelInfo> getTravelHistory(String userId) {
        return userMapper.getTravelHistory(userId);
    }

    public List<TravelCustomRecommend> getTravelRecommendCustom(String userId) {
        return userMapper.getTravelRecommendCustom(userId);
    }

    public ExchangeRateCompare getTravelRecommendNow() {
        List<Fluctuation> fluctuationList = exchangeRateMapper.getExchangeRateFluctuation();
        return exchangeRateMapper.getExchangeRateNow(fluctuationList.get(0).getCode());
    }

    public AccountInfo getAccountInfo(String userId) {
        return userMapper.getAccountInfo(userId);
    }

    public List<PossessionCurrency> getTravelPossession(String userId) {
        return userMapper.getTravelPossession(userId);
    }

    public List<PaymentHistory> getPaymentHistory(String userId, String currency) {
        return userMapper.getPaymentHistory(userId, currency);
    }

    @Transactional
    public String chargePossession(ChargeRequest request) {
        userMapper.chargePossession(request.countryCode(), request.foreignAmount(), request.memberId());
        userMapper.withdraw(request.memberId(), request.localAmount());
        return null;
    }

    @Transactional
    public String refundPossession(ChargeRequest request) {
        int localAmount = request.localAmount();
        if (request.countryCode().equals("JPY")) {
            localAmount = localAmount / 100;
        }
        userMapper.chargePossession(request.countryCode(), request.foreignAmount() * -1, request.memberId());
        userMapper.withdraw(request.memberId(), localAmount * -1);
        return null;
    }

    public String setTravelTargetAmount(TargetCurrency targetCurrency) {
        if (targetCurrency.isEnabled()) userMapper.setTargetAmount(targetCurrency.memberId(), targetCurrency.currencyCode(), targetCurrency.targetAmount(), true);
        else userMapper.setTargetAmount(targetCurrency.memberId(), targetCurrency.currencyCode(), 0, false);

        return null;
    }

    public int getTravelTargetAmount(String userId, String code) {
        return userMapper.getTravelTargetAmount(userId, code);
    }

    public AlertInfo getExchangeRateAlertInfo(String userId, String code) {
        AlertInfo alertInfo = userMapper.getExchangeRateAlertInfo(userId, code);
        alertInfo.setMinAmountThreeMonth(exchangeRateService.getMinAmountThreeMonth(code));
        alertInfo.setMaxAmountThreeMonth(exchangeRateService.getMaxAmountThreeMonth(code));
        alertInfo.setExchangeRate(exchangeRateService.getCurrentExchangeRate(code));


        return alertInfo;
    }

    public String setExchangeRateAlertInfo(AlertRequest request, String userId, String code) {
        if(request.getConditionType().equals("이상")){
            if(request.isAlertFlag()) userMapper.setExchangeRateAlertUpper(userId, code, request.getTargetCurrency(), 'Y');
            else userMapper.setExchangeRateAlertUpper(userId, code, 0, 'N');
        }
        else{
            if(request.isAlertFlag()) userMapper.setExchangeRateAlertLower(userId, code, request.getTargetCurrency(), 'Y');
            else userMapper.setExchangeRateAlertLower(userId, code, 0, 'N');
        }
        return null;
    }

    public String setAutoBuy(AutoBuyRequest request, String code) {
        if(request.isEnableAuto()){
            userMapper.setAutoBuy(request.getMemberId(), code, request.getExchangeRate(), request.getRechargeAmount(), request.getPeriod(), 'Y');
        }
        else{
            userMapper.setAutoBuy(request.getMemberId(), code, 0, 0, "ONCE", 'N');
        }

        return null;
    }

    public AutoTrade getAutoBuyInfo(String code, String userId) {
        return userMapper.getAutoBuyInfo(userId, code);
    }


    public String setAutoRefund(AutoRefundRequest request, String code) {
        if(request.isEnableAuto()){
            userMapper.setAutoRefund(request.getMemberId(), code, request.getExchangeRate(), request.getRefundAmount(), request.getPeriod(), 'Y');
        }
        else{
            userMapper.setAutoRefund(request.getMemberId(), code, 0, null, "ONCE", 'N');
        }

        return null;
    }

    public AutoRefund getAutoRefundInfo(String code, String userId) {
        return userMapper.getAutoRefundInfo(userId, code);
    }

    public List<MostSpent> getMostSpent(String userId) {
        return userMapper.getMostSpent(userId);
    }

    public List<CategorySpent> getCategoryStatics(String userId) {
        return userMapper.getCategoryStatics(userId);

    }

    public CompareAvgAmount getCompareStatics(String userId) {
        return userMapper.getCompareStatics(userId);
    }

    public UserInfoBrief getUserInfoBrief(String userId) {
        return userMapper.getUserInfoBrief(userId);
    }
}
