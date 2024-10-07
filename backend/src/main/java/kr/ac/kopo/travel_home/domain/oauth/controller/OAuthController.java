package kr.ac.kopo.travel_home.domain.oauth.controller;

import kr.ac.kopo.travel_home.domain.oauth.domain.kakao.KakaoUserVO;
import kr.ac.kopo.travel_home.domain.oauth.service.OAuthService;
import kr.ac.kopo.travel_home.global.payload.APIResponse;
import kr.ac.kopo.travel_home.global.payload.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;

    @GetMapping("/kakao/callback")
    public APIResponse<KakaoUserVO> callback(@QueryParam("code") String code) {
        KakaoUserVO userInfo = oAuthService.getUserInfo(code, "register-redirect");
        if (userInfo == null) {
            return APIResponse.onFailure(ErrorStatus._INTERNAL_SERVER_ERROR.getCode(), "카카오 로그인에 실패하였습니다.", null);
        }
        return APIResponse.onSuccess(userInfo);
    }

    @GetMapping("/kakao/login")
    public APIResponse<String> login(@QueryParam("code") String code) {
        KakaoUserVO userInfo = oAuthService.getUserInfo(code, "login-redirect");
        if (userInfo == null || !oAuthService.isUserExist(userInfo)) {
            return APIResponse.onFailure(ErrorStatus._NOT_FOUND.getCode(), "존재하지 않는 회원입니다.", null);
        }

        String accessToken = oAuthService.login(userInfo);

        return APIResponse.onSuccess(accessToken);
    }
}
