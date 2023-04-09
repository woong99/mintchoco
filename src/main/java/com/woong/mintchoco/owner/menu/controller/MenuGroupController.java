package com.woong.mintchoco.owner.menu.controller;

import com.woong.mintchoco.global.annotation.AuthUser;
import com.woong.mintchoco.global.auth.entity.User;
import com.woong.mintchoco.global.common.Message;
import com.woong.mintchoco.global.common.MessageType;
import com.woong.mintchoco.global.common.ModelUtils;
import com.woong.mintchoco.global.common.URL;
import com.woong.mintchoco.owner.menu.model.MenuGroupVO;
import com.woong.mintchoco.owner.menu.service.MenuGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/owner/menu/info")
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
    @RequestMapping("/menuGroup/insert.do")
    public String menuGroupInsert(@AuthUser User user, MenuGroupVO menuGroupVO, ModelMap model) {
        menuGroupService.insertMenuGroup(user, menuGroupVO);

        ModelUtils.modelMessage(MessageType.MSG_URL, Message.SUCCESS_INSERT, URL.MENU_INFO, model);
        return URL.MESSAGE.url();
    }


    /**
     * 사장님 페이지 > 메뉴 정보 > 메뉴 관리 > 메뉴그룹 수정 > 메뉴그룹 단일 조회
     *
     * @param id 메뉴그룹 ID
     * @return ResponseEntity
     */
    @RequestMapping("/menuGroup/select.do")
    public ResponseEntity<MenuGroupVO> menuGroupSelect(@RequestParam("id") Long id) {
        MenuGroupVO menuGroupVO = menuGroupService.selectMenuGroup(id);

        return ResponseEntity.ok(menuGroupVO);
    }


    /**
     * 사장님 페이지 > 메뉴 정보 > 메뉴 관리 > 메뉴그룹 수정 > 수정 action
     *
     * @param menuGroupVO 메뉴그룹 정보
     * @param model       모델
     * @return "/views/common/message"
     */
    @RequestMapping("/menuGroup/update.do")
    public String menuGroupUpdate(MenuGroupVO menuGroupVO, ModelMap model) {
        menuGroupService.updateMenuGroup(menuGroupVO);

        ModelUtils.modelMessage(MessageType.MSG_URL, Message.SUCCESS_UPDATE, URL.MENU_INFO, model);
        return URL.MESSAGE.url();
    }


    /**
     * 사장님 페이지 > 메뉴 관리 > 메뉴그룹 순서변경 > 순서변경 action
     *
     * @param menuGroupIdList 메뉴그룹 ID 리스트
     * @param model           모델
     * @return "/views/common/message"
     */
    @RequestMapping("/menuGroup/orderUpdate.do")
    public String menuGroupOrderUpdate(@RequestParam("menuGroupIdList") Long[] menuGroupIdList, ModelMap model) {
        menuGroupService.updateMenuGroupOrder(menuGroupIdList);

        ModelUtils.modelMessage(MessageType.MSG_URL, Message.SUCCESS_SAVE, URL.MENU_INFO, model);
        return URL.MESSAGE.url();
    }


    /**
     * 사장님 페이지 > 메뉴 관리 > 메뉴 설정 > 메뉴 그룹 삭제 > 삭제 action
     *
     * @param menuGroupId 메뉴 그룹 ID
     * @param model       모델
     * @return "/views/common/message"
     */
    @RequestMapping("/menuGroup/delete.do")
    public String menuGroupDelete(@RequestParam("menuGroupId") Long menuGroupId, ModelMap model) {
        menuGroupService.deleteMenuGroup(menuGroupId);

        ModelUtils.modelMessage(MessageType.MSG_URL, Message.SUCCESS_DELETE, URL.MENU_INFO, model);
        return URL.MESSAGE.url();
    }


    /**
     * 사장님 페이지 > 메뉴 관리 > 메뉴 설정 > 메뉴그룹 미리보기
     *
     * @param menuGroupId 메뉴 그룹 ID
     * @param model       모델
     * @return "/views/common/message"
     */
    @RequestMapping("/menuGroup/preview")
    public String menuGroupPreview(@RequestParam("menuGroupId") Long menuGroupId, ModelMap model) {
        MenuGroupVO menuGroupVO = menuGroupService.selectMenuGroupWithMenus(menuGroupId);

        model.addAttribute("menuGroupVO", menuGroupVO);
        return "/fragments/owner/menu/menuGroupPreviewModal";
    }
}
