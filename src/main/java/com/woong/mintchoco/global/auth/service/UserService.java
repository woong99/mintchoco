package com.woong.mintchoco.global.auth.service;

import com.woong.mintchoco.global.adapter.UserAdapter;
import com.woong.mintchoco.global.auth.entity.User;
import com.woong.mintchoco.global.auth.exception.password.DuplicatePasswordException;
import com.woong.mintchoco.global.auth.exception.password.InvalidPasswordException;
import com.woong.mintchoco.global.auth.model.UserVO;
import com.woong.mintchoco.global.auth.repository.UserRepository;
import com.woong.mintchoco.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public UserAdapter loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(username);
        if (user == null) {
            throw new UsernameNotFoundException("등록된 사용자가 아닙니다.");
        }
        return new UserAdapter(user);
    }

    /**
     * 비밀번호를 변경한다. 입력받은 비밀번호와 DB에 저장된 비밀번호가 일치하지 않으면 InvalidPasswordException 발생 입력받은 비밀번호가 사용중인 비밀번호와 일치하면
     * DuplicatePasswordException 발생
     *
     * @param user   인증 정보
     * @param userVO 유저 정보
     */
    public void changePassword(User user, UserVO userVO) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(userVO.getUserPwd(), user.getPassword())) {
            throw new InvalidPasswordException(ErrorCode.INVALID_PASSWORD);
        }
        if (passwordEncoder.matches(userVO.getNewPwd(), user.getPassword())) {
            throw new DuplicatePasswordException(ErrorCode.DUPLICATE_PASSWORD);
        }
        userVO.setUserId(user.getUserId());
        userVO.setNewPwd(passwordEncoder.encode(userVO.getNewPwd()));
        userRepository.updateUserPwd(userVO);
    }
}
