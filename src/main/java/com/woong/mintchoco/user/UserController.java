package com.woong.mintchoco.user;

import com.woong.mintchoco.global.auth.model.UserVO;
import com.woong.mintchoco.global.auth.service.UserService;
import com.woong.mintchoco.global.common.MessageType;
import com.woong.mintchoco.global.common.ModelUtils;
import com.woong.mintchoco.global.common.URL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @RequestMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "message", required = false) String message,
            Authentication authentication,
            ModelMap model) {

        if (authentication != null) {
            return "redirect:/user/main";
        }

        if (error != null && error.equals("true")) {
            ModelUtils.modelMessage(MessageType.MSG_URL, message, "/user/login", model);
            return URL.MESSAGE.url();
        }
        return "/views/user/login";
    }

    @RequestMapping("/signUp.do")
    public String signUpAction(UserVO userVO, ModelMap model) {
        if (userService.userSignUp(userVO) == null) {
            ModelUtils.modelMessage(MessageType.MSG_URL, "회원가입에 실패했습니다.", "/user/sign-up", model);
            return URL.MESSAGE.url();
        }
        ModelUtils.modelMessage(MessageType.MSG_URL, "회원가입에 성공했습니다.", "/user/login", model);
        return URL.MESSAGE.url();
    }

    @RequestMapping("/duplicateCheck.do")
    @ResponseBody
    public boolean duplicateCheck(@RequestParam("id") String id) {
        return userService.duplicateIdCheck(id);
    }

    @RequestMapping("/main")
    public String main() {
        return "/views/user/main";
    }
}
