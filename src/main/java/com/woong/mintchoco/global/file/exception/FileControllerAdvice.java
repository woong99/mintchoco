package com.woong.mintchoco.global.file.exception;

import com.woong.mintchoco.global.common.MessageType;
import com.woong.mintchoco.global.common.ModelUtils;
import com.woong.mintchoco.global.common.URL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
@Slf4j
public class FileControllerAdvice {

    /**
     * 설정한 파일 최대 업로드 크기보다 큰 파일이 업로드되는 경우의 예의를 처리하는 메소드
     *
     * @param model 모델
     * @return "/views/common/message"
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String maxUploadSizeExceededException(Model model) {
        log.info("설정된 파일 최대 업로드 크기보다 큰 파일이 입력되었습니다.");
        model.addAttribute("type", "msg+back");
        model.addAttribute("message", "업로드할 수 없는 크기의 파일입니다.");
        return URL.MESSAGE.url();
    }

    @ExceptionHandler(UploadFileErrorException.class)
    public String handleUploadFileErrorException(UploadFileErrorException e, Model model) {
        log.error(e.getMessage());
        ModelUtils.modelMessage(MessageType.MSG_BACK, e.getMessage(), model);
        return URL.MESSAGE.url();
    }

    @ExceptionHandler(UploadFileNotFoundException.class)
    public String handleUploadFileNotFoundException(UploadFileNotFoundException e, Model model) {
        log.error(e.getMessage());
        ModelUtils.modelMessage(MessageType.MSG_BACK, e.getMessage(), model);
        return URL.MESSAGE.url();
    }

    @ExceptionHandler(UploadFileOnlyOneException.class)
    public String handleUploadFileOnlyOneException(UploadFileOnlyOneException e, Model model) {
        log.error(e.getMessage());
        ModelUtils.modelMessage(MessageType.MSG_BACK, e.getMessage(), model);
        return URL.MESSAGE.url();
    }
}
