package com.woong.mintchoco.controller;

import com.woong.mintchoco.common.MessageType;
import com.woong.mintchoco.domain.AttachFile;
import com.woong.mintchoco.service.FileManageService;
import com.woong.mintchoco.service.OwnerService;
import com.woong.mintchoco.service.UserService;
import com.woong.mintchoco.vo.UserVO;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
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
     * @param authentication 인증 정보
     * @param model          모델
     * @return "/views/owner/profile/ownerInfo"
     */
    @RequestMapping("/profile/info")
    public String profileInfo(Authentication authentication, ModelMap model) {
        UserVO userVO = ownerService.getUserInfo(authentication.getName());
        model.addAttribute("userVO", userVO);
        return "/views/owner/profile/ownerInfo";
    }

    /**
     * 사장님 페이지 > 사장님 정보 > 사장님 정보 수정 > 수정 action
     *
     * @param authentication 인증 정보
     * @param userVO         사용자 정보
     * @param model          모델
     * @return "/views/common/message"
     */
    @Transactional
    @RequestMapping("/profile/info/update.do")
    public String profileInfoUpdate(Authentication authentication, UserVO userVO, @RequestParam("file")
    MultipartFile file, ModelMap model) throws IOException {
        userVO.setUserId(authentication.getName());
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
     * @param authentication 인증 정보
     * @return ResponseEntity
     */
    @Transactional
    @RequestMapping("/profile/info/deleteProfileImage.do")
    public ResponseEntity<Void> removeProfileImage(Authentication authentication) {
        UserVO userVO = ownerService.getUserInfo(authentication.getName());
        if (userVO.getProfileImage() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        ownerService.deleteUserProfileImage(userVO);
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
     * @param authentication 인증 정보
     * @param userVO         사용자 정보
     * @param model          모델
     * @return "/views/common/message"
     */
    @Transactional
    @RequestMapping("/profile/password/update.do")
    public String profilePasswordChange(Authentication authentication, UserVO userVO, ModelMap model) {
        if (!userVO.getNewPwd().equals(userVO.getReNewPwd())) {
            model.addAttribute("type", MessageType.msgUrl.getMessage());
            model.addAttribute("message", "새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.");
            model.addAttribute("returnUrl", "/owner/profile/password");
            return "/views/common/message";
        }
        
        userService.changePassword(authentication, userVO);

        model.addAttribute("type", MessageType.msgUrl.getMessage());
        model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다!");
        model.addAttribute("returnUrl", "/owner/profile/password");
        return "/views/common/message";
    }
}
