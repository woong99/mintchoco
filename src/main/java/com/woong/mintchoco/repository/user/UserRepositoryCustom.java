package com.woong.mintchoco.repository.user;

import com.woong.mintchoco.vo.UserVO;

public interface UserRepositoryCustom {

    void updateUserInfo(UserVO userVO);

    void deleteUserProfileImage(UserVO userVO);

    void updateUserPwd(UserVO userVO);
}
