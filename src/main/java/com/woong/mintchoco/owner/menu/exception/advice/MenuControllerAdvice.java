package com.woong.mintchoco.owner.menu.exception.advice;

import com.woong.mintchoco.global.common.Message;
import com.woong.mintchoco.global.common.MessageType;
import com.woong.mintchoco.global.common.ModelUtils;
import com.woong.mintchoco.global.common.URL;
import com.woong.mintchoco.owner.menu.controller.MenuController;
import com.woong.mintchoco.owner.menu.exception.MenuNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = MenuController.class)
@Slf4j
public class MenuControllerAdvice {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public String handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e,
                                                                 Model model) {
        String foreignKey = "FOREIGN KEY ";

        if (e.getMessage().contains(foreignKey + "(`menu_option_group_id`)")) {
            log.error("메뉴 옵션 그룹 조회에 실패했습니다.");
            ModelUtils.modelMessage(MessageType.MSG_BACK, "메뉴 옵션 그룹 조회에 실패했습니다.", model);
        } else if (e.getMessage().contains(foreignKey + "(`menu_group_id`)")) {
            log.error("메뉴 그룹 조회에 실패했습니다.");
            ModelUtils.modelMessage(MessageType.MSG_BACK, "메뉴 그룹 조회에 실패했습니다.", model);
        } else {
            log.error("알 수 없는 오류입니다.");
            ModelUtils.modelMessage(MessageType.MSG_BACK, Message.FAIL_UNKNOWN_ERROR, model);
        }

        return URL.MESSAGE.url();
    }


    @ExceptionHandler(MenuNotFoundException.class)
    public ResponseEntity<Void> handleMenuNotFoundException(MenuNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
