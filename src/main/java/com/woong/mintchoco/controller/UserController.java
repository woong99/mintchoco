package com.woong.mintchoco.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

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
            model.addAttribute("message", message);
            model.addAttribute("returnUrl", "/user/login");
            return "/views/common/message";
        }
        return "/views/user/login";
    }

    @RequestMapping("/main")
    public String main() {
        return "/views/user/main";
    }
}
