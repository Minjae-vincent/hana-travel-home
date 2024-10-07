package kr.ac.kopo.travel_home.domain.mail.controller;

import jakarta.mail.MessagingException;
import kr.ac.kopo.travel_home.domain.mail.domain.request.SendAlertMailRequest;
import kr.ac.kopo.travel_home.domain.mail.domain.request.SendAutoBuyAlertMailRequest;
import kr.ac.kopo.travel_home.domain.mail.domain.request.SendAutoSellAlertMailRequest;
import kr.ac.kopo.travel_home.domain.mail.service.MailService;
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
@RequestMapping("/api/mail")
@RequiredArgsConstructor
@Slf4j
public class MailController {
    private final MailService mailService;

    @PostMapping("/send-alert")
    public void sendAlertMail(@RequestBody SendAlertMailRequest request) {
        mailService.sendAlertMail(request);
    }

    @PostMapping("/auto-buy-alert")
    public void sendAutoBuyAlertMail(@RequestBody SendAutoBuyAlertMailRequest request) throws MessagingException {
        mailService.sendAutoBuyAlertMail(request);
    }

    @PostMapping("/auto-sell-alert")
    public void sendAutoSellAlertMail(@RequestBody SendAutoSellAlertMailRequest request) throws MessagingException {
        mailService.sendAutoSellAlertMail(request);
    }



}
