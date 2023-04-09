package com.woong.mintchoco.owner.menu.exception.advice;

import com.woong.mintchoco.owner.menu.controller.MenuOptionGroupController;
import com.woong.mintchoco.owner.menu.exception.MenuOptionGroupNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = MenuOptionGroupController.class)
@Slf4j
public class MenuOptionGroupControllerAdvice {

    @ExceptionHandler(MenuOptionGroupNotFoundException.class)
    public ResponseEntity<Void> handleMenuOptionGroupNotFoundException(MenuOptionGroupNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
