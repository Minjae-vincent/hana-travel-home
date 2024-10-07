package kr.ac.kopo.travel_home.domain.user.mapper;

import kr.ac.kopo.travel_home.domain.user.domain.dto.*;
import kr.ac.kopo.travel_home.domain.user.domain.request.LoginRequest;
import kr.ac.kopo.travel_home.domain.user.domain.request.RegisterRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface UserMapper {
    boolean isUserExist(@Param("name") String name, @Param("birth") String birth, @Param("phoneNumber") String phoneNumber);

    boolean isHanaUserExist(@Param("ssnLast") String ssnLast);

    boolean isIdExist(@Param("id") String id);

    void register(RegisterRequest request);

    boolean isAccountExist(@Param("id") String id);

    boolean isCorrectPassword(LoginRequest request);

    UserDTO getUser(@Param("id") String id, @Param("password") String password);

    UserDTO getUserById(@Param("id") String id);

    UserDTO getUserByNamePhoneNumber(@Param("name") String name, @Param("phoneNumber") String phoneNumber);

    String isTraveling(@Param("memberId") String memberId);

    List<TravelJournals> getTravelJournals(@Param("memberId") String memberId);

    List<TravelRecord> getTravelRecord(@Param("travelRecordId") String travelRecordId);

    TravelSpace getTraveling(@Param("memberId") String userId);

    List<DestRecommend> getTravelRecommendation(@Param("memberId") String memberId, @Param("lat") double lat, @Param("lon") double lon);

    int getTargetTotalTravelNum(@Param("month") String month, @Param("ageRange") String ageRange, @Param("gender") String gender, @Param("memberId") String userId);

    List<TravelLocation> getTargetCountryList(@Param("month") String month, @Param("ageRange") String ageRange, @Param("gender") String gender, @Param("memberId") String userId);

    List<TravelInfo> getTravelHistory(@Param("memberId") String userId);

    List<TravelCustomRecommend> getTravelRecommendCustom(@Param("memberId") String userId);

    AccountInfo getAccountInfo(@Param("memberId") String userId);

    List<PossessionCurrency> getTravelPossession(@Param("memberId") String userId);

    List<PaymentHistory> getPaymentHistory(@Param("memberId") String userId, @Param("code") String currency);

    void chargePossession(@Param("code") String code, @Param("amount") int foreignAmount, @Param("memberId") String memberId);

    void withdraw(@Param("memberId") String memberId, @Param("amount") int localAmount);

    void setTargetAmount(@Param("memberId") String memberId, @Param("code") String code, @Param("targetAmount") int targetAmount, @Param("isEnabled") boolean isEnabled);

    int getTravelTargetAmount(@Param("memberId") String userId, @Param("code") String code);

    AlertInfo getExchangeRateAlertInfo(@Param("memberId") String userId, @Param("code") String code);

    void setExchangeRateAlertUpper(@Param("memberId") String userId, @Param("code") String code, @Param("targetCurrency") double targetCurrency, @Param("flag") char flag);

    void setExchangeRateAlertLower(@Param("memberId") String userId, @Param("code") String code, @Param("targetCurrency") double targetCurrency, @Param("flag") char flag);

    void setAutoBuy(@Param("memberId") String memberId, @Param("code") String code, @Param("exchangeRate") double exchangeRate, @Param("rechargeAmount") double rechargeAmount, @Param("period") String period, @Param("flag") char flag);

    AutoTrade getAutoBuyInfo(@Param("memberId") String memberId, @Param("code") String code);

    void setAutoRefund(@Param("memberId") String memberId, @Param("code") String code, @Param("exchangeRate") double exchangeRate, @Param("refundAmount") String refundAmount, @Param("period") String period, @Param("flag") char flag);

    AutoRefund getAutoRefundInfo(@Param("memberId") String memberId, @Param("code") String code);

    List<MostSpent> getMostSpent(@Param("memberId") String userId);

    List<CategorySpent> getCategoryStatics(@Param("memberId") String userId);

    CompareAvgAmount getCompareStatics(@Param("memberId") String userId);

    UserInfoBrief getUserInfoBrief(@Param("memberId") String userId);

    String getEmailFromId(@Param("memberId") String userId);

    int getCurrencyRemained(@Param("memberId") String userId, String code);
}
