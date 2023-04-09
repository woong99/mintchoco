package com.woong.mintchoco.owner.menu.exception;

import com.woong.mintchoco.global.common.ErrorCode;
import com.woong.mintchoco.global.common.exception.CustomException;
import java.io.Serial;

public class MenuGroupNotFoundException extends CustomException {


    @Serial
    private static final long serialVersionUID = 2741872983710336856L;

    public MenuGroupNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
