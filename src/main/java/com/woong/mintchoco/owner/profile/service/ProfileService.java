package com.woong.mintchoco.owner.profile.service;

import com.woong.mintchoco.global.auth.entity.User;
import com.woong.mintchoco.global.auth.model.UserVO;
import com.woong.mintchoco.global.auth.repository.UserRepository;
import com.woong.mintchoco.global.file.entity.AttachFile;
import com.woong.mintchoco.global.file.repository.FileRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
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
        if (user.getProfileImage() == null) {
            return UserVO.toUserVO(user);
        }
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
    @Transactional
    public void updateUserInfo(UserVO userVO) {
        userRepository.updateUserInfo(userVO);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User authenticatedUser
                && (authenticatedUser.getUserId().equals(userVO.getUserId()))) {
            User updatedUser = userRepository.findByUserId(userVO.getUserId());
            authentication = new UsernamePasswordAuthenticationToken(updatedUser, updatedUser.getPassword(),
                    updatedUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
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
