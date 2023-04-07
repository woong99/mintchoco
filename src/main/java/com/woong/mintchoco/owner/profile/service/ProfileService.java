package com.woong.mintchoco.owner.profile.service;

import com.woong.mintchoco.global.auth.entity.User;
import com.woong.mintchoco.global.auth.model.UserVO;
import com.woong.mintchoco.global.auth.repository.UserRepository;
import com.woong.mintchoco.global.file.entity.AttachFile;
import com.woong.mintchoco.global.file.repository.FileRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final FileRepository fileRepository;

    private final UserRepository userRepository;

    /**
     * 사용자의 아이디로 사용자의 정보를 조회환다.
     *
     * @param user 인증 정보
     * @return 사용자 정보
     */
    public UserVO getUserInfo(User user) {
        AttachFile attachFile = fileRepository.findById(user.getProfileImage().getId())
                .orElseThrow(NoSuchElementException::new);
        user.setProfileImage(attachFile);
        return UserVO.toUserVO(user);
    }

    /**
     * 사용자 정보를 업데이트한다.
     *
     * @param userVO 사용자 정보
     */
    public void updateUserInfo(UserVO userVO) {
        userRepository.updateUserInfo(userVO);
    }

    /**
     * 사용자의 프로필 사진을 삭제한다.
     *
     * @param userVO 사용자 정보
     */
    public void deleteUserProfileImage(UserVO userVO) {
        userRepository.deleteUserProfileImage(userVO);
    }
}
