package com.woong.mintchoco.owner.menu.exception.advice;

import com.woong.mintchoco.owner.menu.controller.MenuGroupController;
import com.woong.mintchoco.owner.menu.exception.MenuGroupNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = MenuGroupController.class)
@Slf4j
public class MenuGroupControllerAdvice {

    @ExceptionHandler(MenuGroupNotFoundException.class)
    public ResponseEntity<Void> handleMenuGroupNotFoundException(MenuGroupNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
