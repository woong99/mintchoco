package com.woong.mintchoco.vo;

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

    private String name;

    private String tel;

    private String businessNumber;
}
