package com.woong.mintchoco.vo;

import com.woong.mintchoco.domain.AttachFile;
import com.woong.mintchoco.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class UserVO {
    private String userId;

    private String userPwd;

    private String newPwd;

    private String reNewPwd;

    private String name;

    private String tel;

    private String businessNumber;

    private String email;

    private AttachFile profileImage;

    public static UserVO toUserVO(User user) {
        return UserVO.builder()
                .userId(user.getUserId())
                .userPwd(user.getUserPwd())
                .name(user.getName())
                .tel(user.getTel())
                .email(user.getEmail())
                .businessNumber(user.getBusinessNumber())
                .profileImage(user.getProfileImage())
                .build();
    }
}
