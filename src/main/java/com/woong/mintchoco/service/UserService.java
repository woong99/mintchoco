package com.woong.mintchoco.service;

import com.woong.mintchoco.domain.User;
import com.woong.mintchoco.repository.user.UserRepository;
import com.woong.mintchoco.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public boolean duplicateIdCheck(String id) {
        return userRepository.existsByUserId(id);
    }

    public User ownerSignUp(UserVO userVO) {
        if (!userRepository.existsByUserId(userVO.getUserId())) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userVO.setUserPwd(passwordEncoder.encode(userVO.getUserPwd()));
            return userRepository.save(User.toOwnerEntity(userVO));
        } else {
            return null;
        }
    }

    public User userSignUp(UserVO userVO) {
        if (!userRepository.existsByUserId(userVO.getUserId())) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userVO.setUserPwd(passwordEncoder.encode(userVO.getUserPwd()));
            return userRepository.save(User.toOwnerEntity(userVO));
        } else {
            return null;
        }
    }


    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(username);
        if (user == null) {
            throw new UsernameNotFoundException("등록된 사용자가 아닙니다.");
        }
        return user;
    }
}
