package kr.ac.kopo.travel_home.global.scheduler;

import kr.ac.kopo.travel_home.domain.initDB.mapper.InitMapper;
import kr.ac.kopo.travel_home.domain.user.domain.dto.TargetAmountAlert;
import kr.ac.kopo.travel_home.domain.user.mapper.ExchangeRateMapper;
import kr.ac.kopo.travel_home.domain.user.mapper.UserMapper;
import kr.ac.kopo.travel_home.global.mapper.AlertMapper;
import kr.ac.kopo.travel_home.domain.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExchangeScheduler {

    private final InitMapper initMapper;
    private final AlertMapper alertMapper;
    private final ExchangeRateMapper exchangeRateMapper;
    private final MailService mailService;
    private final UserMapper userMapper;

    // 매일 00시 10분 00초에 실행
    @Scheduled(cron = "0 10 0 * * *")
    @Transactional
    public void initialExchangeRate() {
        initMapper.migrateExchangeRate();
        initMapper.removeProcessedExchangeRateData();
    }

//    5분마다 한 번씩 동작
    @Scheduled(fixedDelay = 300000)
    @Transactional
    public void alertTargetAmount() {
        List<TargetAmountAlert> targetAmountAlert = alertMapper.getTargetAmountAlert();

        for (TargetAmountAlert alert : targetAmountAlert) {
            String targetEmail = alertMapper.getMemberEmail(alert.getMemberId());
            mailService.sendTargetExchangeRateMail(targetEmail, exchangeRateMapper.getNameFromId(alert.getCodeId()), alert.getTargetAmount(), alert.getBalance());
            userMapper.setTargetAmount(alert.getMemberId(), exchangeRateMapper.getCodeFromId(alert.getCodeId()), 0, false);
        }

    }
}
