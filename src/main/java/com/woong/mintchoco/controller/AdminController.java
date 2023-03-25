package com.woong.mintchoco.controller;

import com.woong.mintchoco.common.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    @RequestMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "message", required = false) String message,
            Authentication authentication,
            ModelMap model) {
        if (authentication != null) {
            return "redirect:/admin/main";
        }

        if (error != null && error.equals("true")) {
            model.addAttribute("type", MessageType.msgUrl.getMessage());
            model.addAttribute("message", message);
            model.addAttribute("returnUrl", "/admin/login");
            return "/views/common/message";
        }
        return "/views/admin/login";
    }

    @RequestMapping("/main")
    public String main() {
        return "/views/admin/main";
    }
}
