package com.woong.mintchoco.adapter;

import com.woong.mintchoco.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAdapter extends org.springframework.security.core.userdetails.User {

    private User user;

    public UserAdapter(User user) {
        super(user.getUserId(), user.getPassword(), user.getAuthorities());
        this.user = user;
    }
}
