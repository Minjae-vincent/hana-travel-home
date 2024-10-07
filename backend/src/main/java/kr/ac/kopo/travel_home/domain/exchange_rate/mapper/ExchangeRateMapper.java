package kr.ac.kopo.travel_home.domain.exchange_rate.mapper;

import kr.ac.kopo.travel_home.domain.exchange_rate.domain.dto.Fluctuation;
import kr.ac.kopo.travel_home.domain.exchange_rate.domain.dto.AutoExchangeRate;
import kr.ac.kopo.travel_home.domain.exchange_rate.domain.dto.ExchangeRateCompare;
import kr.ac.kopo.travel_home.domain.exchange_rate.domain.dto.ExchangeRateTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExchangeRateMapper {
    List<Fluctuation> getExchangeRateFluctuation();

    List<String> getNationCode();

    String getCodeFromId(@Param("codeId") String codeId);

    String getNameFromId(@Param("codeId") String code);

    List<ExchangeRateTime> getExchangeRateTime(@Param("codeId") String code);

    List<Double> getExchangeRateMonths(@Param("codeId") String code);

    List<Double> getExchangeRatePerMonth(@Param("codeId") String code);

    ExchangeRateCompare getExchangeRateNow(@Param("code") String code);

    String getIdFromCode(@Param("code") String code);

    double getMinAmountThreeMonth(@Param("code") String code);

    double getMaxAmountThreeMonth(@Param("code") String code);

    AutoExchangeRate getAutoBuy(@Param("code") String code);

    AutoExchangeRate getAutoRefund(@Param("code") String code);

    String getNameFromCode(@Param("code") String codeName);

    String getCodeFromCodeName(@Param("codeName") String codeName);
}
