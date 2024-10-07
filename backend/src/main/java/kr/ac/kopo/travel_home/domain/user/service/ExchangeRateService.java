package kr.ac.kopo.travel_home.domain.user.service;

import kr.ac.kopo.travel_home.domain.user.domain.dto.*;
import kr.ac.kopo.travel_home.domain.user.mapper.ExchangeRateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateService {
    private final ExchangeRateMapper exchangeRateMapper;

    public List<Fluctuation> getExchangeRateFluctuation() {
        return exchangeRateMapper.getExchangeRateFluctuation();
    }

    public List<RealTimeExchangeRate> getExchangeRateRealTime() {
        List<RealTimeExchangeRate> realTimeExchangeRate = new ArrayList<>();

        List<String> nationCode = getNationCode();
        for (String code : nationCode) {
            RealTimeExchangeRate realTime = new RealTimeExchangeRate();
            realTime.setCode(getCodeFromId(code));
            realTime.setCodeName(getNameFromId(code));
            realTime.setExchangeRateTime(exchangeRateMapper.getExchangeRateTime(code));
            realTimeExchangeRate.add(realTime);
        }
        return realTimeExchangeRate;
    }

    public List<ExchangeRateMonths> getExchangeRateMonths() {
        List<ExchangeRateMonths> exchangeRateMonths = new ArrayList<>();

        List<String> nationCode = getNationCode();
        for (String code : nationCode) {
            ExchangeRateMonths exchangeRateMonth = new ExchangeRateMonths();
            exchangeRateMonth.setCodeName(getNameFromId(code));
            exchangeRateMonth.setCode(getCodeFromId(code));
            exchangeRateMonth.setExchangeList(exchangeRateMapper.getExchangeRateMonths(code));
            exchangeRateMonths.add(exchangeRateMonth);
        }
        return exchangeRateMonths;
    }

    private List<String> getNationCode() {
        return exchangeRateMapper.getNationCode();
    }

    private String getCodeFromId(String codeId) {
        return exchangeRateMapper.getCodeFromId(codeId);
    }

    private String getNameFromId(String code) {
        return exchangeRateMapper.getNameFromId(code);
    }

    private String getIdFromCode(String code) {
        return exchangeRateMapper.getIdFromCode(code);
    }

    public List<ExchangeRatePerMonth> getExchangeRateSeasonal() {
        List<ExchangeRatePerMonth> exchangeRateSeosonal = new ArrayList<>();
        List<String> nationCode = getNationCode();
        for (String code : nationCode) {
            ExchangeRatePerMonth exchangeRate = new ExchangeRatePerMonth();
            exchangeRate.setName(getNameFromId(code));
            exchangeRate.setExchangeList(exchangeRateMapper.getExchangeRatePerMonth(code));
            exchangeRateSeosonal.add(exchangeRate);
        }

     return exchangeRateSeosonal;
    }

    public double getCurrentExchangeRate(String code) {
        return exchangeRateMapper.getExchangeRateNow(code).getCurrentRate();
    }

    public double getMinAmountThreeMonth(String code) {
        return exchangeRateMapper.getMinAmountThreeMonth(code);
    }

    public double getMaxAmountThreeMonth(String code) {
        return exchangeRateMapper.getMaxAmountThreeMonth(code);
    }

    public AutoExchangeRate getAutoBuy(String code) {
        return exchangeRateMapper.getAutoBuy(code);
    }

    public AutoExchangeRate getAutoRefund(String code) {
        return exchangeRateMapper.getAutoRefund(code);
    }
}
