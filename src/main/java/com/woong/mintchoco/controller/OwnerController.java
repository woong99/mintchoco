package com.woong.mintchoco.controller;

import com.woong.mintchoco.service.UserService;
import com.woong.mintchoco.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/owner")
@RequiredArgsConstructor
@Slf4j
public class OwnerController {

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
            model.addAttribute("message", message);
            model.addAttribute("returnUrl", "/owner/login");
            return "/views/common/message";
        }
        return "/views/owner/login";
    }

    @RequestMapping("/signUp")
    public String signUp() {
        return "/views/owner/signup";
    }

    @RequestMapping("/signUp.do")
    public String signUpAction(UserVO userVO, ModelMap model) {
        if (userService.signUp(userVO) == null) {
            model.addAttribute("message", "회원가입에 실패했습니다.");
            model.addAttribute("returnUrl", "/owner/sign-up");
            return "/views/common/message";
        }
        model.addAttribute("message", "회원가입에 성공했습니다.\n관리자의 승인이 이루어진 이후부터 로그인이 가능합니다.");
        model.addAttribute("returnUrl", "/owner/login");
        return "/views/common/message";
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
