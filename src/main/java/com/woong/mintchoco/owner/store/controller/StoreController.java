package com.woong.mintchoco.owner.store.controller;

import com.woong.mintchoco.global.annotation.AuthUser;
import com.woong.mintchoco.global.auth.entity.User;
import com.woong.mintchoco.global.auth.model.UserVO;
import com.woong.mintchoco.global.common.Message;
import com.woong.mintchoco.global.common.MessageType;
import com.woong.mintchoco.global.common.ModelUtils;
import com.woong.mintchoco.global.common.URL;
import com.woong.mintchoco.owner.store.model.StoreVO;
import com.woong.mintchoco.owner.store.service.StoreService;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/owner/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;


    /**
     * 사장님 페이지 > 가게 정보 > 가게 정보 등록/수정
     *
     * @param user  인증 정보
     * @param model 모델
     * @return "/views/owner/store/storeForm"
     */
    @Transactional
    @RequestMapping("/form")
    public String storeForm(@AuthUser User user, ModelMap model) {
        StoreVO storeVO = storeService.getStoreInfo(user);
        String command = storeVO == null ? "insert" : "update";

        model.addAttribute("command", command);
        model.addAttribute("storeVO", storeVO);
        return "/views/owner/store/storeForm";
    }


    /**
     * 사장님 페이지 > 가게 정보 > 가게 정보 등록/수정 > 등록 action
     *
     * @param user    인증 정보
     * @param storeVO 가게 정보
     * @param model   모델
     * @return "/views/common/message"
     */
    @RequestMapping("/form/insert.do")
    public String storeFormInsert(@AuthUser User user, StoreVO storeVO, ModelMap model) {
        storeService.insertStoreInfo(user, storeVO);

        ModelUtils.modelMessage(MessageType.MSG_URL, Message.SUCCESS_REGISTRATION, "/owner/store/form", model);
        return URL.MESSAGE.url();
    }


    /**
     * 사장님 페이지 > 가게 정보 > 가게 정보 등록/수정 > 수정 action
     *
     * @param user    인증 정보
     * @param storeVO 가게 정보
     * @param model   모델
     * @return "/views/common/message"
     */
    @Transactional
    @RequestMapping("/form/update.do")
    public String storeFormUpdate(@AuthUser User user, StoreVO storeVO, ModelMap model) {
        storeService.updateStoreInfo(user, storeVO);

        ModelUtils.modelMessage(MessageType.MSG_URL, Message.SUCCESS_UPDATE, "/owner/store/form", model);
        return URL.MESSAGE.url();
    }


    /**
     * 사장님 페이지 > 가게 정보 > 가게 정보 보기
     *
     * @param user  인증 정보
     * @param model 모델
     * @return "/views/owner/store/storeInfo"
     */
    @RequestMapping("/info")
    public String storeInfo(@AuthUser User user, ModelMap model) {
        StoreVO storeVO = storeService.getStoreInfo(user);
        if (storeVO == null) {
            ModelUtils.modelMessage(MessageType.MSG_URL, "등록된 가게 정보가 없습니다. 가게 정보 등록 후 이용해주세요.", "/owner/store/form",
                    model);
            return URL.MESSAGE.url();
        }

        UserVO userVO = UserVO.toUserVO(user);
        formattingOpeningHours(storeVO);
        model.addAttribute("storeVO", storeVO);
        model.addAttribute("userVO", userVO);
        return "/views/owner/store/storeInfo";
    }


    /**
     * 사장님 페이지 > 가게 정보 > 가게 정보 보기 > 위치안내 > 지도
     *
     * @return "/views/owner/store/storeInfoMap"
     */
    @RequestMapping("/info/map")
    public String storeInfoMap() {
        return "/views/owner/store/storeInfoMap";
    }


    /**
     * 평일, 토요일, 일요일 오픈 시간을 포매팅해주는 메소드
     *
     * @param storeVO 가게 정보
     */
    private void formattingOpeningHours(StoreVO storeVO) {
        if (storeVO.getWeekdayIsOpen() != null && storeVO.getWeekdayIsOpen().equals("Y")) {
            storeVO.setFormattedWeekdayOpeningHours(
                    formattingOpeningHour(storeVO.getWeekdayStart(), storeVO.getWeekdayEnd()));
        }

        if (storeVO.getSaturdayIsOpen() != null && storeVO.getSaturdayIsOpen().equals("Y")) {
            storeVO.setFormattedSaturdayOpeningHours(
                    formattingOpeningHour(storeVO.getSaturdayStart(), storeVO.getSaturdayEnd()));
        }

        if (storeVO.getSundayIsOpen() != null && storeVO.getSundayIsOpen().equals("Y")) {
            storeVO.setFormattedSundayOpeningHours(
                    formattingOpeningHour(storeVO.getSundayStart(), storeVO.getSundayEnd()));
        }
    }


    /**
     * 오픈 시간을 포매팅해주는 메소드
     *
     * @param startTime 시작 시간
     * @param endTime   종료 시간
     * @return 포매팅 된 시간
     */
    private String formattingOpeningHour(String startTime, String endTime) {
        LocalTime time = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
        String formattedTime = time.format(DateTimeFormatter.ofPattern("a h:mm"));
        time = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"));
        formattedTime = formattedTime + " ~ " + time.format(DateTimeFormatter.ofPattern("a h:mm"));
        return formattedTime;
    }
}
