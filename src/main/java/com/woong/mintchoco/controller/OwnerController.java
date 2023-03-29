package com.woong.mintchoco.controller;

import com.woong.mintchoco.annotation.AuthUser;
import com.woong.mintchoco.common.MessageType;
import com.woong.mintchoco.domain.AttachFile;
import com.woong.mintchoco.domain.User;
import com.woong.mintchoco.service.FileManageService;
import com.woong.mintchoco.service.OwnerService;
import com.woong.mintchoco.service.UserService;
import com.woong.mintchoco.vo.StoreVO;
import com.woong.mintchoco.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/owner")
@RequiredArgsConstructor
@Slf4j
public class OwnerController {

    private final UserService userService;

    private final OwnerService ownerService;

    private final FileManageService fileManageService;


    @RequestMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "message", required = false) String message,
            Authentication authentication,
            ModelMap model) {

        if (authentication != null) {
            return "redirect:/owner/main";
        }

        if (error != null && error.equals("true")) {
            model.addAttribute("type", MessageType.msgUrl.getMessage());
            model.addAttribute("message", message);
            model.addAttribute("returnUrl", "/owner/login");
            return "/views/common/message";
        }
        return "/views/owner/login";
    }

    @RequestMapping("/signUp")
    public String signUp() {
        return "/views/owner/signup";
    }

    @RequestMapping("/signUp.do")
    public String signUpAction(UserVO userVO, ModelMap model) {
        if (userService.ownerSignUp(userVO) == null) {
            model.addAttribute("type", MessageType.msgUrl.getMessage());
            model.addAttribute("message", "회원가입에 실패했습니다.");
            model.addAttribute("returnUrl", "/owner/sign-up");
            return "/views/common/message";
        }
        model.addAttribute("type", MessageType.msgUrl.getMessage());
        model.addAttribute("message", "회원가입에 성공했습니다.\n관리자의 승인이 이루어진 이후부터 로그인이 가능합니다.");
        model.addAttribute("returnUrl", "/owner/login");
        return "/views/common/message";
    }

    @RequestMapping("/duplicateCheck.do")
    @ResponseBody
    public boolean duplicateCheck(@RequestParam("id") String id) {
        return userService.duplicateIdCheck(id);
    }

    @RequestMapping("/main")
    public String main() {
        return "/views/owner/main";
    }

    /**
     * 사장님 페이지 > 사장님 정보 > 사장님 정보 수정
     *
     * @param user  인증 정보
     * @param model 모델
     * @return "/views/owner/profile/ownerInfo"
     */
    @RequestMapping("/profile/info")
    public String profileInfo(@AuthUser User user, ModelMap model) {
        UserVO userVO = ownerService.getUserInfo(user);
        model.addAttribute("userVO", userVO);
        return "/views/owner/profile/ownerInfo";
    }

    /**
     * 사장님 페이지 > 사장님 정보 > 사장님 정보 수정 > 수정 action
     *
     * @param user   인증 정보
     * @param userVO 사용자 정보
     * @param model  모델
     * @return "/views/common/message"
     */
    @Transactional
    @RequestMapping("/profile/info/update.do")
    public String profileInfoUpdate(@AuthUser User user, UserVO userVO, @RequestParam("file")
    MultipartFile file, ModelMap model) throws IOException {
        userVO.setUserId(user.getUserId());
        if (!file.isEmpty()) {
            AttachFile profileImage = fileManageService.saveFile(file);
            if (profileImage == null) {
                model.addAttribute("type", MessageType.msgUrl.getMessage());
                model.addAttribute("message", "파일의 이름이나 확장자를 확인해주세요.");
                model.addAttribute("returnUrl", "/owner/profile/info");
                return "/views/common/message";
            }
            userVO.setProfileImage(profileImage);
        }

        ownerService.updateUserInfo(userVO);

        model.addAttribute("type", MessageType.msgUrl.getMessage());
        model.addAttribute("message", "사장님 정보가 수정되었습니다.");
        model.addAttribute("returnUrl", "/owner/profile/info");
        return "/views/common/message";
    }

    /**
     * 사장님 정보 > 사장님 정보 수정 > 프로필 사진 삭제 action
     *
     * @param user 인증 정보
     * @return ResponseEntity
     */
    @Transactional
    @RequestMapping("/profile/info/deleteProfileImage.do")
    public ResponseEntity<Void> removeProfileImage(@AuthUser User user) {
        if (user.getProfileImage() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        ownerService.deleteUserProfileImage(UserVO.toUserVO(user));
        return ResponseEntity.ok().build();
    }

    /**
     * 사장님 정보 > 비밀번호 변경
     *
     * @return "/views/owner/profile/ownerPassword"
     */
    @RequestMapping("/profile/password")
    public String profilePassword() {
        return "/views/owner/profile/ownerPassword";
    }

    /**
     * 사장님 정보 > 비밀번호 변경 > 비밀번호 변경 action
     *
     * @param user   인증 정보
     * @param userVO 사용자 정보
     * @param model  모델
     * @return "/views/common/message"
     */
    @Transactional
    @RequestMapping("/profile/password/update.do")
    public String profilePasswordChange(@AuthUser User user, UserVO userVO,
                                        ModelMap model) {
        if (!userVO.getNewPwd().equals(userVO.getReNewPwd())) {
            model.addAttribute("type", MessageType.msgUrl.getMessage());
            model.addAttribute("message", "새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.");
            model.addAttribute("returnUrl", "/owner/profile/password");
            return "/views/common/message";
        }

        userService.changePassword(user, userVO);

        model.addAttribute("type", MessageType.msgUrl.getMessage());
        model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다!");
        model.addAttribute("returnUrl", "/owner/profile/password");
        return "/views/common/message";
    }


    /**
     * 사장님 페이지 > 가게 정보 > 가게 정보 등록/수정
     *
     * @param user  인증 정보
     * @param model 모델
     * @return "/views/owner/store/storeForm"
     */
    @Transactional
    @RequestMapping("/store/form")
    public String storeForm(@AuthUser User user, ModelMap model) {
        StoreVO storeVO = ownerService.getStoreInfo(user);
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
    @RequestMapping("/store/form/insert.do")
    public String storeFormInsert(@AuthUser User user, StoreVO storeVO, ModelMap model) {
        ownerService.insertStoreInfo(user, storeVO);

        model.addAttribute("type", MessageType.msgUrl.getMessage());
        model.addAttribute("message", "등록이 완료되었습니다.");
        model.addAttribute("returnUrl", "/owner/store/form");
        return "/views/common/message";
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
    @RequestMapping("/store/form/update.do")
    public String storeFormUpdate(@AuthUser User user, StoreVO storeVO, ModelMap model) {
        ownerService.updateStoreInfo(user, storeVO);

        model.addAttribute("type", MessageType.msgUrl.getMessage());
        model.addAttribute("message", "수정이 완료되었습니다.");
        model.addAttribute("returnUrl", "/owner/store/form");
        return "/views/common/message";
    }


    /**
     * 사장님 페이지 > 가게 정보 > 가게 정보 보기
     *
     * @param user  인증 정보
     * @param model 모델
     * @return "/views/owner/store/storeInfo"
     */
    @RequestMapping("/store/info")
    public String storeInfo(@AuthUser User user, ModelMap model) {
        StoreVO storeVO = ownerService.getStoreInfo(user);
        if (storeVO == null) {
            model.addAttribute("type", MessageType.msgUrl.getMessage());
            model.addAttribute("message", "등록된 가게 정보가 없습니다. 가게 정보 등록 후 이용해주세요.");
            model.addAttribute("returnUrl", "/owner/store/form");
            return "/views/common/message";
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
    @RequestMapping("/store/info/map")
    public String storeInfoMap() {
        return "/views/owner/store/storeInfoMap";
    }


    /**
     * Thymeleaf 에서 현재 페이지의 url 을 사용하기 위해 model 객체에 담아주는 메소드
     *
     * @param request request
     * @return url
     */
    @ModelAttribute("url")
    public String contextPath(HttpServletRequest request) {
        return request.getServletPath();
    }


    /**
     * Thymeleaf 에서 HttpServletRequest 를 사용하기 위해 model 객체에 담아주는 메소드
     *
     * @param request request
     * @return request
     */
    @ModelAttribute("request")
    public HttpServletRequest httpServletRequest(HttpServletRequest request) {
        return request;
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
