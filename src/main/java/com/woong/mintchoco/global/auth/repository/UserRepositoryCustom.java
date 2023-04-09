package com.woong.mintchoco.global.auth.repository;

import com.woong.mintchoco.global.auth.model.UserVO;

public interface UserRepositoryCustom {

    void updateUserInfo(UserVO userVO);

    void deleteUserProfileImage(UserVO userVO);

    void updateUserPwd(UserVO userVO);
}
