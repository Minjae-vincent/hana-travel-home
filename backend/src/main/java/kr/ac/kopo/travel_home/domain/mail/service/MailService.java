package kr.ac.kopo.travel_home.domain.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.ac.kopo.travel_home.domain.mail.domain.request.SendAlertMailRequest;
import kr.ac.kopo.travel_home.domain.mail.domain.request.SendAutoBuyAlertMailRequest;
import kr.ac.kopo.travel_home.domain.mail.domain.request.SendAutoSellAlertMailRequest;
import kr.ac.kopo.travel_home.domain.user.mapper.ExchangeRateMapper;
import kr.ac.kopo.travel_home.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final UserMapper userMapper;
    private final ExchangeRateMapper exchangeRateMapper;

    private MimeMessage createMail(String test) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom("alswo9853@gmail.com");
            message.setRecipients(MimeMessage.RecipientType.TO, test);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    public void sendMail(String mail) {
        MimeMessage message = createMail(mail);
        javaMailSender.send(message);
    }

    public void sendTargetExchangeRateMail(String targetEmail, String codeName, String targetAmount, String balance) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom("alswo9853@gmail.com");
            message.setRecipients(MimeMessage.RecipientType.TO, targetEmail);
            message.setSubject("[하나 트래블홈] 목표 외화 달성 알림 - " + codeName);
            targetAmount = targetAmount.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
            balance = balance.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
            String currency = exchangeRateMapper.getCodeFromCodeName(codeName);

            String body = ""
                    + "<!DOCTYPE html>"
                    + "<html lang='ko'>"
                    + "<head>"
                    + "  <meta charset='UTF-8'>"
                    + "  <meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                    + "  <title>목표 외화 달성 알림</title>"
                    + "</head>"
                    + "<body style='margin:0; padding:0; font-family: Arial, sans-serif; background-color: #f4f4f4;'>"
                    + "  <table width='100%' cellpadding='0' cellspacing='0' border='0'>"
                    + "    <tr>"
                    + "      <td align='center' style='padding: 20px 0;'>"
                    + "        <table width='600' cellpadding='0' cellspacing='0' border='0' style='background-color: #ffffff; overflow: hidden; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>"
                    + "          <tr>"
                    + "            <td align='center' style='background-color: #e6f7f1; padding: 20px; border: 1px solid #d9d9d9;'>"
                    + "              <img src='https://hana-travel-home.s3.ap-northeast-2.amazonaws.com/logo.png' alt='하나 트래블홈' style='width: 200px;'>"
                    + "            </td>"
                    + "          </tr>"
                    + "          <tr>"
                    + "            <td style='padding: 30px;'>"
                    + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                    + "                현재 보유하신 <strong style='font-size:20px; color: #03ac83;'>" + codeName + "</strong>의 잔액은 <strong>" + balance + "(" + currency + ")</strong>이며."
                    + "                목표 외화 <strong>" + targetAmount + "(" + currency + ")</strong>을 달성하셨습니다."
                    + "              </p>"
                    + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                    + "                목표를 완료하여, 설정하신 목표 외화는 초기화됩니다."
                    + "              </p>"
                    + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                    + "                자세한 내용은 하나 트래블홈에서 확인해주세요."
                    + "              </p>"
                    + "              <div style='border-left: 4px solid #03ac8e; background-color: #e6f7f1; padding: 15px; margin: 20px 0; font-family: Arial, sans-serif;'>"
                    + "                <h3 style='margin-top: 0; color: #03ac8e;'>하나 트래블홈에서 설정하신 목표 외화 달성 알림입니다.</h3>"
                    + "                <p style='margin: 0; color: #555555;'>축하드립니다! 목표를 성공적으로 달성하셨습니다.</p>"
                    + "              </div>"
                    + "              <p style='font-size: 14px; color: #999999;'>"
                    + "                이 메일은 자동 발송된 메일입니다. 문의 사항이 있으시면 고객 지원팀으로 연락해주세요."
                    + "              </p>"
                    + "            </td>"
                    + "          </tr>"
                    + "          <tr>"
                    + "            <td align='center' style='background-color: #f4f4f4; padding: 20px;'>"
                    + "              <p style='font-size: 12px; color: #777777; margin: 0;'>"
                    + "                &copy; 2024 하나 트래블홈. All rights reserved."
                    + "              </p>"
                    + "            </td>"
                    + "          </tr>"
                    + "        </table>"
                    + "      </td>"
                    + "    </tr>"
                    + "  </table>"
                    + "</body>"
                    + "</html>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(message);
    }

    public void sendAlertMail(SendAlertMailRequest request) {
        MimeMessage message = null;
        if (request.alertType().equals("UP")) {
            try {
                message = createAlertMailUp(request);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                message = createAlertMailDown(request);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        assert message != null;
        javaMailSender.send(message);
    }

    private MimeMessage createAlertMailUp(SendAlertMailRequest request) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom("alswo9853@gmail.com");
        message.setRecipients(MimeMessage.RecipientType.TO, userMapper.getEmailFromId(request.memberId()));
        String nationName = exchangeRateMapper.getNameFromCode(request.code());
        String alertAmount = String.valueOf(request.alertAmount()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", "");
        String dealBasR = String.valueOf(request.dealBasR()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", "");

        message.setSubject("[하나 트래블홈] 환율 알림(이상) - " + nationName + "(" + request.code() + ")");
        String body = ""
                + "<!DOCTYPE html>"
                + "<html lang='ko'>"
                + "<head>"
                + "  <meta charset='UTF-8'>"
                + "  <meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "  <title>환율 알림(이상)</title>"
                + "</head>"
                + "<body style='margin:0; padding:0; font-family: Arial, sans-serif; background-color: #f4f4f4;'>"
                + "  <table width='100%' cellpadding='0' cellspacing='0' border='0'>"
                + "    <tr>"
                + "      <td align='center' style='padding: 20px 0;'>"
                + "        <table width='600' cellpadding='0' cellspacing='0' border='0' style='background-color: #ffffff; overflow: hidden; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>"
                + "          <tr>"
                + "            <td align='center' style='background-color: #e6f7f1; padding: 20px; border: 1px solid #d9d9d9;'>"
                + "              <img src='https://hana-travel-home.s3.ap-northeast-2.amazonaws.com/logo.png' alt='하나 트래블홈' style='width: 200px;'>"
                + "            </td>"
                + "          </tr>"
                + "          <tr>"
                + "            <td style='padding: 30px;'>"
                + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                + "                <strong style='font-size:20px; color: #03ac83;'>" + nationName + "(" + request.code() + ")</strong>의 환율이"
                + "                 설정하신 <strong>" + alertAmount + "</strong>원을 넘었습니다!"
                + "              </p>"
                + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                + "                 현재 " + nationName + "의 환율은 <strong>" + dealBasR + "</strong>원입니다."
                + "              </p>"
                + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                + "                자세한 내용은 하나 트래블홈에서 확인해주세요."
                + "              </p>"
                + "              <div style='border-left: 4px solid #03ac8e; background-color: #e6f7f1; padding: 15px; margin: 20px 0; font-family: Arial, sans-serif;'>"
                + "                <h3 style='margin-top: 0; color: #03ac8e;'>하나 트래블홈에서 설정하신 환율 알림(이상)입니다.</h3>"
                + "                <p style='margin: 0; color: #555555;'>" + nationName + "의 환율이 설정하신 환율 이상이 되었습니다.</p>"
                + "              </div>"
                + "              <p style='font-size: 14px; color: #999999;'>"
                + "                이 메일은 자동 발송된 메일입니다. 문의 사항이 있으시면 고객 지원팀으로 연락해주세요."
                + "              </p>"
                + "            </td>"
                + "          </tr>"
                + "          <tr>"
                + "            <td align='center' style='background-color: #f4f4f4; padding: 20px;'>"
                + "              <p style='font-size: 12px; color: #777777; margin: 0;'>"
                + "                &copy; 2024 하나 트래블홈. All rights reserved."
                + "              </p>"
                + "            </td>"
                + "          </tr>"
                + "        </table>"
                + "      </td>"
                + "    </tr>"
                + "  </table>"
                + "</body>"
                + "</html>";
        message.setText(body, "UTF-8", "html");

        return message;
    }

    private MimeMessage createAlertMailDown(SendAlertMailRequest request) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom("alswo9853@gmail.com");
        message.setRecipients(MimeMessage.RecipientType.TO, userMapper.getEmailFromId(request.memberId()));
        String nationName = exchangeRateMapper.getNameFromCode(request.code());
        message.setSubject("[하나 트래블홈] 환율 알림(이하) - " + nationName + "(" + request.code() + ")");
        String alertAmount = String.valueOf(request.alertAmount()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", "");
        String dealBasR = String.valueOf(request.dealBasR()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", "");

        String body = ""
                + "<!DOCTYPE html>"
                + "<html lang='ko'>"
                + "<head>"
                + "  <meta charset='UTF-8'>"
                + "  <meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "  <title>환율 알림(이하)</title>"
                + "</head>"
                + "<body style='margin:0; padding:0; font-family: Arial, sans-serif; background-color: #f4f4f4;'>"
                + "  <table width='100%' cellpadding='0' cellspacing='0' border='0'>"
                + "    <tr>"
                + "      <td align='center' style='padding: 20px 0;'>"
                + "        <table width='600' cellpadding='0' cellspacing='0' border='0' style='background-color: #ffffff; overflow: hidden; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>"
                + "          <tr>"
                + "            <td align='center' style='background-color: #e6f7f1; padding: 20px; border: 1px solid #d9d9d9;'>"
                + "              <img src='https://hana-travel-home.s3.ap-northeast-2.amazonaws.com/logo.png' alt='하나 트래블홈' style='width: 200px;'>"
                + "            </td>"
                + "          </tr>"
                + "          <tr>"
                + "            <td style='padding: 30px;'>"
                + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                + "                <strong style='font-size:20px; color: #03ac83;'>" + nationName + "(" + request.code() + ")</strong>의 환율이"
                + "                 설정하신 <strong>" + alertAmount + "</strong>원 아래로 떨어졌습니다!"
                + "              </p>"
                + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                + "                 현재 " + nationName + "의 환율은 <strong>" + dealBasR + "</strong>원 입니다."
                + "              </p>"
                + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                + "                자세한 내용은 하나 트래블홈에서 확인해주세요."
                + "              </p>"
                + "              <div style='border-left: 4px solid #03ac8e; background-color: #e6f7f1; padding: 15px; margin: 20px 0; font-family: Arial, sans-serif;'>"
                + "                <h3 style='margin-top: 0; color: #03ac8e;'>하나 트래블홈에서 설정하신 환율 알림(이하)입니다.</h3>"
                + "                <p style='margin: 0; color: #555555;'>" + nationName + "의 환율이 설정하신 환율 이하가 되었습니다.</p>"
                + "              </div>"
                + "              <p style='font-size: 14px; color: #999999;'>"
                + "                이 메일은 자동 발송된 메일입니다. 문의 사항이 있으시면 고객 지원팀으로 연락해주세요."
                + "              </p>"
                + "            </td>"
                + "          </tr>"
                + "          <tr>"
                + "            <td align='center' style='background-color: #f4f4f4; padding: 20px;'>"
                + "              <p style='font-size: 12px; color: #777777; margin: 0;'>"
                + "                &copy; 2024 하나 트래블홈. All rights reserved."
                + "              </p>"
                + "            </td>"
                + "          </tr>"
                + "        </table>"
                + "      </td>"
                + "    </tr>"
                + "  </table>"
                + "</body>"
                + "</html>";

        message.setText(body, "UTF-8", "html");
        return message;
    }

    public void sendAutoBuyAlertMail(SendAutoBuyAlertMailRequest request) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom("alswo9853@gmail.com");
        message.setRecipients(MimeMessage.RecipientType.TO, userMapper.getEmailFromId(request.memberId()));
        String nationName = exchangeRateMapper.getNameFromCode(request.code());
        double dealBasR = exchangeRateMapper.getExchangeRateNow(request.code()).getCurrentRate();
        int balance = userMapper.getAccountInfo(request.memberId()).getBalance();
        String realAmount = String.valueOf((int)Math.ceil(request.autoBuyAmount()*dealBasR)).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
        int remainedAmount = userMapper.getCurrencyRemained(request.memberId(), request.code());

        String balacneStr = String.valueOf(balance).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");

        message.setSubject("[하나 트래블홈] 자동 구매 알림 - " + nationName + "(" + request.code() + ")");
        String body = ""
                + "<!DOCTYPE html>"
                + "<html lang='ko'>"
                + "<head>"
                + "  <meta charset='UTF-8'>"
                + "  <meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "  <title>자동 구매 알림</title>"
                + "</head>"
                + "<body style='margin:0; padding:0; font-family: Arial, sans-serif; background-color: #f4f4f4;'>"
                + "  <table width='100%' cellpadding='0' cellspacing='0' border='0'>"
                + "    <tr>"
                + "      <td align='center' style='padding: 20px 0;'>"
                + "        <table width='600' cellpadding='0' cellspacing='0' border='0' style='background-color: #ffffff; overflow: hidden; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>"
                + "          <tr>"
                + "            <td align='center' style='background-color: #e6f7f1; padding: 20px; border: 1px solid #d9d9d9;'>"
                + "              <img src='https://hana-travel-home.s3.ap-northeast-2.amazonaws.com/logo.png' alt='하나 트래블홈' style='width: 200px;'>"
                + "            </td>"
                + "          </tr>"
                + "          <tr>"
                + "            <td style='padding: 30px;'>"
                + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                + "               자동 구매 설정하신 <strong style='font-size:20px; color: #03ac83;'>" + nationName + "(" + request.code() + ")</strong>의 구입이 완료되었습니다! "
                + "              </p>"
                + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                + "                 총 <strong>" + request.autoBuyAmount() + "</strong> " + request.code() + "(<strong>" + realAmount + "</strong>원)이 구매되었습니다."
                + "              </p>"
                + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                + "                 현재 " + nationName + "의 환율은 <strong>" + dealBasR + "</strong>원 입니다."
                + "              </p>"
                + "              <br/>"
                + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                + "                 계좌 잔액은 <strong>" + balacneStr + "</strong>원이며"
                + "              </p>"
                + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                + "                 잔여 <strong>" + nationName + "(" + request.code() + ")</strong>은(는) <strong>" + remainedAmount + "</strong>원 입니다."
                + "              </p>"
                + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                + "                자세한 내용은 하나 트래블홈에서 확인해주세요."
                + "              </p>"
                + "              <div style='border-left: 4px solid #03ac8e; background-color: #e6f7f1; padding: 15px; margin: 20px 0; font-family: Arial, sans-serif;'>"
                + "                <h3 style='margin-top: 0; color: #03ac8e;'>하나 트래블홈에서 설정하신 자동 구매입니다.</h3>"
                + "                <p style='margin: 0; color: #555555;'>" + nationName + "(" + request.code() + ")" + " 의 자동 구매가 완료되었습니다.</p>"
                + "              </div>"
                + "              <p style='font-size: 14px; color: #999999;'>"
                + "                이 메일은 자동 발송된 메일입니다. 문의 사항이 있으시면 고객 지원팀으로 연락해주세요."
                + "              </p>"
                + "            </td>"
                + "          </tr>"
                + "          <tr>"
                + "            <td align='center' style='background-color: #f4f4f4; padding: 20px;'>"
                + "              <p style='font-size: 12px; color: #777777; margin: 0;'>"
                + "                &copy; 2024 하나 트래블홈. All rights reserved."
                + "              </p>"
                + "            </td>"
                + "          </tr>"
                + "        </table>"
                + "      </td>"
                + "    </tr>"
                + "  </table>"
                + "</body>"
                + "</html>";

        message.setText(body, "UTF-8", "html");
        javaMailSender.send(message);
    }

    public void sendAutoSellAlertMail(SendAutoSellAlertMailRequest request) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom("alswo9853@gmail.com");
        message.setRecipients(MimeMessage.RecipientType.TO, userMapper.getEmailFromId(request.memberId()));
        String nationName = exchangeRateMapper.getNameFromCode(request.code());
        double dealBasR = exchangeRateMapper.getExchangeRateNow(request.code()).getCurrentRate();
        int balance = userMapper.getAccountInfo(request.memberId()).getBalance();
        int remainedAmount = request.balance() - request.realAmount();

        String autoSellAmountStr = "";

        if(request.autoSellAmount().equals("ALL")) {
            autoSellAmountStr = "전액";
        } else if(request.autoSellAmount().equals("HALF")) {
            autoSellAmountStr = "50%";
        } else {
            autoSellAmountStr = "25%";

        }


        String amountSellStr = String.valueOf((int)Math.ceil(request.realAmount()*dealBasR)).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
        String balanceStr = String.valueOf(balance).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");

        message.setSubject("[하나 트래블홈] 자동 환급 알림 - " + nationName + "(" + request.code() + ")");
        String body = ""
                + "<!DOCTYPE html>"
                + "<html lang='ko'>"
                + "<head>"
                + "  <meta charset='UTF-8'>"
                + "  <meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "  <title>자동 환급 알림</title>"
                + "</head>"
                + "<body style='margin:0; padding:0; font-family: Arial, sans-serif; background-color: #f4f4f4;'>"
                + "  <table width='100%' cellpadding='0' cellspacing='0' border='0'>"
                + "    <tr>"
                + "      <td align='center' style='padding: 20px 0;'>"
                + "        <table width='600' cellpadding='0' cellspacing='0' border='0' style='background-color: #ffffff; overflow: hidden; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>"
                + "          <tr>"
                + "            <td align='center' style='background-color: #e6f7f1; padding: 20px; border: 1px solid #d9d9d9;'>"
                + "              <img src='https://hana-travel-home.s3.ap-northeast-2.amazonaws.com/logo.png' alt='하나 트래블홈' style='width: 200px;'>"
                + "            </td>"
                + "          </tr>"
                + "          <tr>"
                + "            <td style='padding: 30px;'>"
                + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                + "               자동 환급 설정하신 <strong style='font-size:20px; color: #03ac83;'>" + nationName + "(" + request.code() + ")</strong>의 환급이 완료되었습니다! "
                + "              </p>"
                + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                + "                 설정하신 자동 환급량은 <strong>" + autoSellAmountStr + "</strong> " + request.code() + "이며"
                + "              </p>"
                + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                + "                 총 <strong>" + amountSellStr + "</strong>원 (<strong>" + request.realAmount() + "</strong> " + request.code() + ")이 환급되었습니다."
                + "              </p>"
                + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                + "                 현재 <strong>" + nationName + "</strong> " + "의 환율은 <strong>" + dealBasR + "</strong>원 입니다."
                + "              </p>"
                + "              <br/>"
                + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                + "                 계좌 잔액은 <strong>" + balanceStr + "</strong>원이며"
                + "              </p>"
                + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                + "                 잔여 <strong>" + nationName + "(" + request.code() + ")</strong>은(는) <strong>" + remainedAmount + "</strong>원 입니다."
                + "              </p>"
                + "              <p style='font-size: 16px; line-height: 1.5; color: #333333; text-align: center;'>"
                + "                자세한 내용은 하나 트래블홈에서 확인해주세요."
                + "              </p>"
                + "              <div style='border-left: 4px solid #03ac8e; background-color: #e6f7f1; padding: 15px; margin: 20px 0; font-family: Arial, sans-serif;'>"
                + "                <h3 style='margin-top: 0; color: #03ac8e;'>하나 트래블홈에서 설정하신 자동 환급입니다.</h3>"
                + "                <p style='margin: 0; color: #555555;'>" + nationName + "(" + request.code() + ")" + " 의 자동 환급이 완료되었습니다.</p>"
                + "              </div>"
                + "              <p style='font-size: 14px; color: #999999;'>"
                + "                이 메일은 자동 발송된 메일입니다. 문의 사항이 있으시면 고객 지원팀으로 연락해주세요."
                + "              </p>"
                + "            </td>"
                + "          </tr>"
                + "          <tr>"
                + "            <td align='center' style='background-color: #f4f4f4; padding: 20px;'>"
                + "              <p style='font-size: 12px; color: #777777; margin: 0;'>"
                + "                &copy; 2024 하나 트래블홈. All rights reserved."
                + "              </p>"
                + "            </td>"
                + "          </tr>"
                + "        </table>"
                + "      </td>"
                + "    </tr>"
                + "  </table>"
                + "</body>"
                + "</html>";

        message.setText(body, "UTF-8", "html");
        javaMailSender.send(message);
    }
}
