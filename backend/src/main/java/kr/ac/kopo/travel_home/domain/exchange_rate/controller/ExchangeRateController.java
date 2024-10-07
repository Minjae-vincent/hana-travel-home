package kr.ac.kopo.travel_home.domain.exchange_rate.controller;

import kr.ac.kopo.travel_home.domain.exchange_rate.service.ExchangeRateService;
import kr.ac.kopo.travel_home.global.payload.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exchange-rate")
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    @GetMapping("/fluctuation")
    public APIResponse<?> getExchangeRateFluctuation() {
        return APIResponse.onSuccess(exchangeRateService.getExchangeRateFluctuation());
    }

    @GetMapping("/real-time")
    public APIResponse<?> getExchangeRateRealTime() {
        return APIResponse.onSuccess(exchangeRateService.getExchangeRateRealTime());
    }

    @GetMapping("/now/{code}")
    public APIResponse<?> getExchangeRateNow(@PathVariable("code") String code) {
        return APIResponse.onSuccess(exchangeRateService.getCurrentExchangeRate(code));
    }

    @GetMapping("/stability")
    public APIResponse<?> getExchangeRateMonths() {
        return APIResponse.onSuccess(exchangeRateService.getExchangeRateMonths());
    }

    @GetMapping("/seasonal")
    public APIResponse<?> getExchangeRateSeasonal() {
        return APIResponse.onSuccess(exchangeRateService.getExchangeRateSeasonal());
    }
}
