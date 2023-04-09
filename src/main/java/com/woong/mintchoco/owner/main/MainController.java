package com.woong.mintchoco.owner.main;

import com.woong.mintchoco.global.auth.model.UserVO;
import com.woong.mintchoco.global.auth.service.UserService;
import com.woong.mintchoco.global.common.MessageType;
import com.woong.mintchoco.global.common.ModelUtils;
import com.woong.mintchoco.global.common.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/owner")
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

    @RequestMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "message", required = false) String message,
            Authentication authentication,
            ModelMap model) {

        if (authentication != null) {
            return "redirect:/owner/main";
        }

        if (error != null && error.equals("true")) {
            ModelUtils.modelMessage(MessageType.MSG_URL, message, "/owner/login", model);
            return URL.MESSAGE.url();
        }
        return "/views/owner/login";
    }

    @RequestMapping("/signUp")
    public String signUp() {
        return "/views/owner/signup";
    }

    @RequestMapping("/signUp.do")
    public String signUpAction(UserVO userVO, ModelMap model) {
        if (userService.ownerSignUp(userVO) == null) {
            ModelUtils.modelMessage(MessageType.MSG_URL, "회원가입에 실패했습니다.", "/owner/sign-up", model);
            return URL.MESSAGE.url();
        }
        ModelUtils.modelMessage(MessageType.MSG_URL, "회원가입에 성공했습니다.\n관리자의 승인이 이루어진 이후부터 로그인이 가능합니다.", "/owner/login",
                model);
        return URL.MESSAGE.url();
    }

    @RequestMapping("/duplicateCheck.do")
    @ResponseBody
    public boolean duplicateCheck(@RequestParam("id") String id) {
        return userService.duplicateIdCheck(id);
    }

    @RequestMapping("/main")
    public String main() {
        return "/views/owner/main";
    }
}
