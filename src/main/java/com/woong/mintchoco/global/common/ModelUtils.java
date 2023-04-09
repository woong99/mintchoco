package com.woong.mintchoco.global.common;

import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

public class ModelUtils {

    private ModelUtils() {
    }

    public static void modelMessage(MessageType type, Message message, URL returnUrl, ModelMap model) {
        modelMessage(type.messageType(), message.message(), returnUrl.url(), model);
    }

    public static void modelMessage(MessageType type, String message, String returnUrl, ModelMap model) {
        modelMessage(type.messageType(), message, returnUrl, model);
    }

    public static void modelMessage(MessageType type, Message message, String returnUrl, ModelMap model) {
        modelMessage(type.messageType(), message.message(), returnUrl, model);
    }

    public static void modelMessage(String type, String message, String returnUrl, ModelMap model) {
        model.addAttribute("type", type);
        model.addAttribute("message", message);
        model.addAttribute("returnUrl", returnUrl);
    }

    public static void modelMessage(MessageType type, String message, Model model) {
        modelMessage(type.messageType(), message, "", model);
    }

    public static void modelMessage(MessageType type, Message message, Model model) {
        modelMessage(type.messageType(), message.message(), "", model);
    }

    public static void modelMessage(String type, String message, String returnUrl, Model model) {
        model.addAttribute("type", type);
        model.addAttribute("message", message);
        model.addAttribute("returnUrl", returnUrl);
    }
}
