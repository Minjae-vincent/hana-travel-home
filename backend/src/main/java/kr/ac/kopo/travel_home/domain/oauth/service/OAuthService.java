package kr.ac.kopo.travel_home.domain.oauth.service;

import kr.ac.kopo.travel_home.domain.oauth.domain.kakao.KakaoAuthVO;
import kr.ac.kopo.travel_home.domain.oauth.domain.kakao.KakaoUserVO;
import kr.ac.kopo.travel_home.domain.user.domain.dto.UserDTO;
import kr.ac.kopo.travel_home.domain.user.mapper.UserMapper;
import kr.ac.kopo.travel_home.domain.user.service.UserService;
import kr.ac.kopo.travel_home.global.feign.KakaoAuth;
import kr.ac.kopo.travel_home.global.feign.KakaoUser;
import kr.ac.kopo.travel_home.global.jwt.Claim;
import kr.ac.kopo.travel_home.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {
    private final KakaoAuth kakaoFeignClient;
    private final KakaoUser kakaoAuthClient;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    public KakaoUserVO getUserInfo(String code, String s) {

        ResponseEntity<KakaoAuthVO> kakaoAuth = kakaoFeignClient.getAccessToken(
                "authorization_code",
                "15b6f9d7154890e2195a6c7ddcf716cb",
                "http://travel-home.s3-website.ap-northeast-2.amazonaws.com/" + s,
                code
        );

        ResponseEntity<KakaoUserVO> kakaoUser = kakaoAuthClient.getUserInfo(
                "Bearer " + kakaoAuth.getBody().getAccessToken()
        );

        kakaoUser.getBody().getKakaoAccount().setPhoneNumber(kakaoUser.getBody().getKakaoAccount().getPhoneNumber().replace("+82 ", "0"));

        return kakaoUser.getBody();
    }

    public boolean isUserExist(KakaoUserVO userInfo) {
        return userService.isUserExist(userInfo.getKakaoAccount().getName(), userInfo.getKakaoAccount().getBirthyear().substring(2) + userInfo.getKakaoAccount().getBirthday(), userInfo.getKakaoAccount().getPhoneNumber());
    }

    public String login(KakaoUserVO userInfo) {

        UserDTO userDTO = userMapper.getUserByNamePhoneNumber(userInfo.getKakaoAccount().getName(), userInfo.getKakaoAccount().getPhoneNumber());

        Claim.Traveling traveling = new Claim.Traveling();

        String countryName = userMapper.isTraveling(userDTO.getMemberId());

        if (countryName != null) {
            traveling.setCountryName(countryName);
            traveling.setTraveling(true);
        } else {
            traveling.setTraveling(false);
        }

        Claim claim = new Claim();
        claim.setUserDTO(userDTO);
        claim.setImgUrl(userInfo.getKakaoAccount().getProfile().getThumbnailImageUrl());
        claim.setTraveling(traveling);

        return jwtUtil.createAccessToken(claim);
    }
}
