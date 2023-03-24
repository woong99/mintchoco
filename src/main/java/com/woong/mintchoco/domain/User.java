package com.woong.mintchoco.domain;

import com.woong.mintchoco.vo.UserVO;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class User extends UserBaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String tel;

    private String email;

    private String userId;

    private String userPwd;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    private String hasPermission;

    private String businessNumber;

    @Setter
    private LocalDateTime lastLoginAt;

    public static User toOwnerEntity(UserVO userVO) {
        return User.builder()
                .name(userVO.getName())
                .tel(userVO.getTel())
                .email(userVO.getEmail())
                .userId(userVO.getUserId())
                .userPwd(userVO.getUserPwd())
                .authority(Authority.OWNER)
                .businessNumber(userVO.getBusinessNumber())
                .hasPermission("N")
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.authority.getAuthority()));
    }

    @Override
    public String getPassword() {
        return this.userPwd;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
