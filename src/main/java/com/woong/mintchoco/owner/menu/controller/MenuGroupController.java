package com.woong.mintchoco.owner.menu.controller;

import com.woong.mintchoco.global.annotation.AuthUser;
import com.woong.mintchoco.global.auth.entity.User;
import com.woong.mintchoco.global.common.MessageType;
import com.woong.mintchoco.owner.menu.model.MenuGroupVO;
import com.woong.mintchoco.owner.menu.service.MenuGroupService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/owner/menu")
@RequiredArgsConstructor
public class MenuGroupController {

    private final MenuGroupService menuGroupService;

    /**
     * 사장님 페이지 > 메뉴 정보 > 메뉴 관리 > 메뉴그룹 추가 > 등록 action
     *
     * @param user        인증 정보
     * @param menuGroupVO 메뉴 그룹 정보
     * @param model       모델
     * @return "/views/common/message"
     */
    @RequestMapping("/info/menuGroup/insert.do")
    public String menuGroupInsert(@AuthUser User user, MenuGroupVO menuGroupVO, ModelMap model) {
        menuGroupService.insertMenuGroup(user, menuGroupVO);

        model.addAttribute("type", MessageType.msgUrl.getMessage());
        model.addAttribute("message", "등록이 완료되었습니다.");
        model.addAttribute("returnUrl", "/owner/menu/info");
        return "/views/common/message";
    }


    /**
     * 사장님 페이지 > 메뉴 정보 > 메뉴 관리 > 메뉴그룹 수정 > 메뉴그룹 단일 조회
     *
     * @param id 메뉴그룹 ID
     * @return ResponseEntity
     */
    @RequestMapping("/info/menuGroup/select.do")
    public ResponseEntity<MenuGroupVO> menuGroupSelect(@RequestParam("id") Long id) {
        MenuGroupVO menuGroupVO;
        try {
            menuGroupVO = menuGroupService.selectMenuGroup(id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(menuGroupVO);
    }


    /**
     * 사장님 페이지 > 메뉴 정보 > 메뉴 관리 > 메뉴그룹 수정 > 수정 action
     *
     * @param menuGroupVO 메뉴그룹 정보
     * @param model       모델
     * @return "/views/common/message"
     */
    @Transactional
    @RequestMapping("/info/menuGroup/update.do")
    public String menuGroupUpdate(MenuGroupVO menuGroupVO, ModelMap model) {
        menuGroupService.updateMenuGroup(menuGroupVO);

        model.addAttribute("type", MessageType.msgUrl.getMessage());
        model.addAttribute("message", "수정이 완료되었습니다.");
        model.addAttribute("returnUrl", "/owner/menu/info");
        return "/views/common/message";
    }


    /**
     * 사장님 페이지 > 메뉴 관리 > 메뉴그룹 순서변경 > 순서변경 action
     *
     * @param menuGroupIdList 메뉴그룹 ID 리스트
     * @param model           모델
     * @return "/views/common/message"
     */
    @Transactional
    @RequestMapping("/info/menuGroup/orderUpdate.do")
    public String menuGroupOrderUpdate(@RequestParam("menuGroupIdList") Long[] menuGroupIdList, ModelMap model) {
        menuGroupService.updateMenuGroupOrder(menuGroupIdList);

        model.addAttribute("type", MessageType.msgUrl.getMessage());
        model.addAttribute("message", "저장이 완료되었습니다.");
        model.addAttribute("returnUrl", "/owner/menu/info");
        return "/views/common/message";
    }

    /**
     * 사장님 페이지 > 메뉴 관리 > 메뉴 설정 > 메뉴 그룹 삭제 > 삭제 action
     *
     * @param menuGroupId 메뉴 그룹 ID
     * @param model       모델
     * @return "/views/common/message"
     */
    @RequestMapping("/info/menuGroup/delete.do")
    public String menuGroupDelete(@RequestParam("menuGroupId") Long menuGroupId, ModelMap model) {
        menuGroupService.deleteMenuGroup(menuGroupId);

        model.addAttribute("type", MessageType.msgUrl.getMessage());
        model.addAttribute("message", "삭제가 완료되었습니다.");
        model.addAttribute("returnUrl", "/owner/menu/info");
        return "/views/common/message";
    }
}
