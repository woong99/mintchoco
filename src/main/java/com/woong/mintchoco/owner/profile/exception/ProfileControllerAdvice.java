package com.woong.mintchoco.owner.profile.exception;

import com.woong.mintchoco.global.auth.exception.password.DuplicatePasswordException;
import com.woong.mintchoco.global.auth.exception.password.InvalidPasswordException;
import com.woong.mintchoco.global.common.MessageType;
import com.woong.mintchoco.owner.profile.controller.ProfileController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {ProfileController.class})
@Slf4j
public class ProfileControllerAdvice {

    /**
     * 입력받은 비밀번호와 DB에 저장된 비밀번호가 일치하지 않는 경우 InvalidPasswordException 예외를 처리하는 메소드
     *
     * @param e     InvalidPasswordException
     * @param model 모델
     * @return "views/common/message"
     */
    @ExceptionHandler(InvalidPasswordException.class)
    public String invalidPasswordExceptionHandler(InvalidPasswordException e, Model model) {
        log.info(e.getMessage());
        model.addAttribute("type", MessageType.msgBack.getMessage());
        model.addAttribute("message", e.getMessage());
        return "views/common/message";
    }

    /**
     * 새로운 비밀번호와 기존에 사용하던 비밀번호가 일치하는 경우 DuplicatePasswordException 예외를 처리하는 메소드
     *
     * @param e     DuplicatePasswordException
     * @param model 모델
     * @return "views/common/message"
     */
    @ExceptionHandler(DuplicatePasswordException.class)
    public String duplicatePasswordExceptionHandler(DuplicatePasswordException e, Model model) {
        log.info(e.getMessage());
        model.addAttribute("type", MessageType.msgBack.getMessage());
        model.addAttribute("message", e.getMessage());
        return "views/common/message";
    }
}
