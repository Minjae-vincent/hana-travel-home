package kr.ac.kopo.travel_home.global.jwt.service;

import kr.ac.kopo.travel_home.domain.user.domain.dto.UserDTO;
import kr.ac.kopo.travel_home.domain.user.mapper.UserMapper;
import kr.ac.kopo.travel_home.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserMapper userMapper;

    @Override
    public CustomUserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        UserDTO userDTO = userMapper.getUserById(id);
        if (userDTO == null) {
            throw new UsernameNotFoundException("해당하는 유저가 없습니다.");
        }
        return new CustomUserDetails(userDTO);
    }
}
