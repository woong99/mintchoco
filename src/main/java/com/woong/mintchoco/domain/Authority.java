package com.woong.mintchoco.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Authority {
    ADMIN("ROLE_ADMIN"),
    OWNER("ROLE_OWNER"),
    USER("ROLE_USER");

    private String authority;
}
