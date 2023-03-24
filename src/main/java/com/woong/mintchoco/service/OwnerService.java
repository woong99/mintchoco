package com.woong.mintchoco.service;

import com.woong.mintchoco.repository.user.UserRepository;
import com.woong.mintchoco.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OwnerService {

    private final UserRepository userRepository;

    /**
     * 사용자의 아이디로 사용자의 정보를 조회환다.
     *
     * @param userId 사용자 아이디
     * @return 사용자 정보
     */
    public UserVO getUserInfo(String userId) {
        return UserVO.toUserVO(userRepository.findByUserId(userId));
    }

    /**
     * 사용자 정보를 업데이트한다.
     *
     * @param userVO 사용자 정보
     */
    public void updateUserInfo(UserVO userVO) {
        userRepository.updateUserInfo(userVO);
    }
}
