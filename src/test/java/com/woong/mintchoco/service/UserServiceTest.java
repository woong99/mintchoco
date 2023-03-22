package com.woong.mintchoco.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.woong.mintchoco.domain.User;
import com.woong.mintchoco.repository.UserRepository;
import com.woong.mintchoco.vo.UserVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Nested
    class 회원가입 {

        @Test
        @DisplayName("아이디_중복체크_중복O")
        void 아이디_중복체크_중복O() {
            // given
            String id = "test";
            given(userRepository.existsByUserId(id)).willReturn(false);

            // when
            boolean result = userService.duplicateIdCheck(id);

            // then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("아이디_중복체크_중복X")
        void 아이디_중복체크_중복X() {
            // given
            String id = "test";
            given(userRepository.existsByUserId(id)).willReturn(true);

            // when
            boolean result = userService.duplicateIdCheck(id);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("실패")
        void 회원가입_실패() {
            // given
            given(userRepository.existsByUserId("userId")).willReturn(true);

            // when
            User result = userService.signUp(createUserVO());

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("성공")
        void 회원가입_성공() {
            // given
            UserVO userVO = createUserVO();
            given(userRepository.existsByUserId("userId")).willReturn(false);
            given(userRepository.save(any())).willReturn(User.toOwnerEntity(userVO));

            // when
            User result = userService.signUp(userVO);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getUserId()).isEqualTo(userVO.getUserId());
        }
    }

    @Nested
    class 로그인 {

        @Test()
        @DisplayName("로그인 - loadUserByUsername")
        void 로그인_loadUserByUsername() {
            // given
            String username = "test";
            given(userRepository.findByUserId(username)).willReturn(null);

            // when
            assertThatThrownBy(() -> {
                userService.loadUserByUsername(username);
            }).isInstanceOf(UsernameNotFoundException.class)
                    .hasMessageContaining("등록된 사용자가 아닙니다.");

            // then
//            assertThat(result).isNull();
        }
    }

    private User createUser() {
        return User.builder()
                .userId("userId")
                .userPwd("userPwd")
                .name("name")
                .tel("tel")
                .build();
    }

    private UserVO createUserVO() {
        return UserVO.builder()
                .userId("userId")
                .userPwd("userPwd")
                .name("name")
                .tel("tel")
                .build();
    }
}