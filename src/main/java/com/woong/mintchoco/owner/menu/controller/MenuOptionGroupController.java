package com.woong.mintchoco.owner.menu.controller;

import com.woong.mintchoco.global.annotation.AuthUser;
import com.woong.mintchoco.global.auth.entity.User;
import com.woong.mintchoco.global.common.Message;
import com.woong.mintchoco.global.common.MessageType;
import com.woong.mintchoco.global.common.ModelUtils;
import com.woong.mintchoco.global.common.URL;
import com.woong.mintchoco.owner.menu.model.MenuOptionGroupMenuVO;
import com.woong.mintchoco.owner.menu.model.MenuOptionGroupVO;
import com.woong.mintchoco.owner.menu.model.MenuOptionVO;
import com.woong.mintchoco.owner.menu.model.MenuVO;
import com.woong.mintchoco.owner.menu.service.MenuOptionGroupService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/owner/menu")
@RequiredArgsConstructor
public class MenuOptionGroupController {

    private final MenuOptionGroupService menuOptionGroupService;

    /**
     * 사장님 페이지 > 메뉴 관리 > 옵션 설정 > 옵션 추가 > 등록 action
     *
     * @param user              인증 정보
     * @param menuOptionGroupVO 메뉴 옵션 그룹 정보
     * @param menuOptionVO      메뉴 옵션 정보
     * @param model             모델
     * @return "/views/common/message"
     */
    @RequestMapping("/menu/info/menuGroup/menuOptionGroup/insert.do")
    public String menuOptionGroupInsert(@AuthUser User user, MenuOptionGroupVO menuOptionGroupVO,
                                        MenuOptionVO menuOptionVO, ModelMap model) {
        menuOptionGroupService.insertMenuOptionGroup(user, menuOptionGroupVO, menuOptionVO);

        ModelUtils.modelMessage(MessageType.MSG_URL, Message.SUCCESS_INSERT, URL.MENU_INFO, model);
        return URL.MESSAGE.url();
    }


    /**
     * 사장님 페이지 > 메뉴 관리 > 옵션 설정 > 옵션 상세 조회
     *
     * @param menuOptionGroupId 메뉴 옵션 그룹 ID
     * @return ResponseEntity
     */
    @RequestMapping("/menu/info/menuGroup/menuOptionGroup/select.do")
    public ResponseEntity<MenuOptionGroupVO> menuOptionGroupSelect(@RequestParam("id") Long menuOptionGroupId) {
        MenuOptionGroupVO menuOptionGroupVO = menuOptionGroupService.selectMenuOptionGroup(menuOptionGroupId);

        return ResponseEntity.ok(menuOptionGroupVO);
    }


    /**
     * 사장님 페이지 > 메뉴 관리 > 옵션 설정 > 옵션 수정 > 수정 action
     *
     * @param menuOptionGroupVO 메유 옵션 그룹 정보
     * @param model             모델
     * @return "/views/common/message"
     */
    @RequestMapping("/menu/info/menuGroup/menuOptionGroup/update.do")
    public String menuOptionGroupUpdate(MenuOptionGroupVO menuOptionGroupVO, ModelMap model) {
        menuOptionGroupService.updateMenuOptionGroup(menuOptionGroupVO);

        ModelUtils.modelMessage(MessageType.MSG_URL, Message.SUCCESS_UPDATE, URL.MENU_INFO, model);
        return URL.MESSAGE.url();
    }


    /**
     * 사장님 페이지 > 메뉴 관리 > 메뉴 설정 > 옵션그룹 연결
     *
     * @param user   인증 정보
     * @param menuId 메뉴 ID
     * @param model  모델
     * @return "/fragments/owner/menu/optionGroupConnectModal"
     */
    @RequestMapping("/menu/info/menuOptionGroup")
    public String menuOptionGroup(@AuthUser User user, @RequestParam("menuId") Long menuId, ModelMap model) {
        List<MenuOptionGroupVO> menuOptionGroupVOList = menuOptionGroupService.selectAllMenuOptionGroup(user);
        List<MenuOptionGroupMenuVO> menuOptionGroupMenuVOList = menuOptionGroupService.selectMenuOptionGroupConnectMenu(
                menuId);

        model.addAttribute("menuOptionGroupVOList", menuOptionGroupVOList);
        model.addAttribute("menuOptionGroupMenuVOList", menuOptionGroupMenuVOList);
        return "/fragments/owner/menu/optionGroupConnectModal";
    }


    /**
     * 사장님 페이지 > 메뉴 관리 > 옵션 설정 > 메뉴와 연결된 옵션 그룹 조회
     *
     * @param menuOptionGroupId 메뉴 옵션 그룹 ID
     * @return 메뉴와 연결된 옵션 그룹 정보
     */
    @RequestMapping("/menu/info/menuOptionGroup/connectedMenu")
    public ResponseEntity<List<MenuVO>> menuOptionGroupConnectedMenu(
            @RequestParam("menuOptionGroupId") Long menuOptionGroupId) {
        List<MenuVO> menuVOList = menuOptionGroupService.selectMenuOptionGroupConnectedMenu(menuOptionGroupId);

        return ResponseEntity.ok(menuVOList);
    }


    /**
     * 사장님 페이지 > 메뉴 관리 > 옵션 설정 > 옵션 그룹 삭제 > 삭제 action
     *
     * @param menuOptionGroupId 메뉴 옵션 그룹 ID
     * @param model             모델
     * @return "/views/common/message"
     */
    @RequestMapping("/menu/info/menuOptionGroup/delete.do")
    public String menuOptionGroupDelete(@RequestParam("menuOptionGroupId") Long menuOptionGroupId, ModelMap model) {
        menuOptionGroupService.deleteMenuOptionGroup(menuOptionGroupId);

        ModelUtils.modelMessage(MessageType.MSG_URL, Message.SUCCESS_DELETE, URL.MENU_INFO, model);
        return URL.MESSAGE.url();
    }

}
