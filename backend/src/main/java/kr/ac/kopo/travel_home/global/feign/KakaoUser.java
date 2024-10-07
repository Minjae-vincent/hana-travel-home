package kr.ac.kopo.travel_home.global.feign;

import kr.ac.kopo.travel_home.domain.oauth.domain.kakao.KakaoUserVO;
import kr.ac.kopo.travel_home.global.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "KakaoUser", url = "https://kapi.kakao.com", configuration = FeignConfig.class)
public interface KakaoUser {


    @GetMapping(value = "/v2/user/me")
    ResponseEntity<KakaoUserVO> getUserInfo(
            @RequestHeader("Authorization") String accessToken
    );

}
