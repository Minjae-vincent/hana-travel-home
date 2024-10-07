package kr.ac.kopo.travel_home.global.jwt;

import kr.ac.kopo.travel_home.domain.user.domain.dto.UserDTO;
import lombok.Data;

@Data
public class Claim {
    private UserDTO userDTO;
    private String ImgUrl;
    private Traveling traveling;

    @Data
    public static class Traveling {
        private String countryName;
        private boolean isTraveling;
    }
}
