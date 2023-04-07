package com.woong.mintchoco.owner.profile.controller;

import com.woong.mintchoco.global.annotation.AuthUser;
import com.woong.mintchoco.global.auth.entity.User;
import com.woong.mintchoco.global.auth.model.UserVO;
import com.woong.mintchoco.global.auth.service.UserService;
import com.woong.mintchoco.global.common.MessageType;
import com.woong.mintchoco.global.file.entity.AttachFile;
import com.woong.mintchoco.global.file.service.FileManageService;
import com.woong.mintchoco.owner.profile.service.ProfileService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/owner/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    private final FileManageService fileManageService;

    private final ProfileService profileService;

    /**
     * 사장님 페이지 > 사장님 정보 > 사장님 정보 수정
     *
     * @param user  인증 정보
     * @param model 모델
     * @return "/views/owner/profile/ownerInfo"
     */
    @RequestMapping("/info")
    public String profileInfo(@AuthUser User user, ModelMap model) {
        UserVO userVO = profileService.getUserInfo(user);
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
    @RequestMapping("/info/update.do")
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

        profileService.updateUserInfo(userVO);

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
    @RequestMapping("/info/deleteProfileImage.do")
    public ResponseEntity<Void> removeProfileImage(@AuthUser User user) {
        if (user.getProfileImage() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        profileService.deleteUserProfileImage(UserVO.toUserVO(user));
        return ResponseEntity.ok().build();
    }


    /**
     * 사장님 정보 > 비밀번호 변경
     *
     * @return "/views/owner/profile/ownerPassword"
     */
    @RequestMapping("/password")
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
    @RequestMapping("/password/update.do")
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

}
