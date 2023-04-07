package com.woong.mintchoco.owner.menu.controller;

import com.woong.mintchoco.global.annotation.AuthUser;
import com.woong.mintchoco.global.auth.entity.User;
import com.woong.mintchoco.global.common.MessageType;
import com.woong.mintchoco.owner.menu.model.MenuGroupVO;
import com.woong.mintchoco.owner.menu.model.MenuOptionGroupVO;
import com.woong.mintchoco.owner.menu.model.MenuVO;
import com.woong.mintchoco.owner.menu.service.MenuGroupService;
import com.woong.mintchoco.owner.menu.service.MenuOptionGroupService;
import com.woong.mintchoco.owner.menu.service.MenuService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/owner/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    private final MenuGroupService menuGroupService;

    private final MenuOptionGroupService menuOptionGroupService;


    /**
     * 사장님 페이지 > 메뉴 정보 > 메뉴 관리
     *
     * @param user  인증 정보
     * @param model 모델
     * @return "/views/owner/menu/menuInfo"
     */
    @RequestMapping("/info")
    public String menuInfo(@AuthUser User user, ModelMap model) {
        List<MenuGroupVO> menuGroupVOList = menuGroupService.selectAllMenuGroup(user);
        List<MenuOptionGroupVO> menuOptionGroupVOList = menuOptionGroupService.selectAllMenuOptionGroupWithMenuOption(
                user);

        model.addAttribute("menuGroupVOList", menuGroupVOList);
        model.addAttribute("menuOptionGroupVOList", menuOptionGroupVOList);
        return "/views/owner/menu/menuInfo";
    }

    /**
     * 사장님 페이지 > 메뉴 관리 > 메뉴 설정 > 메뉴 추가 > 등록 action
     *
     * @param menuGroupId 메뉴 그룹 ID
     * @param menuVO      메뉴 정보
     * @param model       모델
     * @return "/views/common/message"
     */
    @RequestMapping("/info/menu/insert.do")
    public String menuInsert(@RequestParam("menuGroupId") Long menuGroupId, MenuVO menuVO, ModelMap model) {
        menuService.insertMenu(menuGroupId, menuVO);

        model.addAttribute("type", MessageType.msgUrl.getMessage());
        model.addAttribute("message", "추가가 완료되었습니다.");
        model.addAttribute("returnUrl", "/owner/menu/info");
        return "/views/common/message";
    }


    /**
     * 사장님 페이지 > 메뉴 관리 > 메뉴 설정 > 메뉴 단일 조회
     *
     * @param menuId 메뉴 ID
     * @return 단일 메뉴 정보
     */
    @RequestMapping("/info/menu/select.do")
    public ResponseEntity<MenuVO> menuSelect(@RequestParam("menuId") Long menuId) {
        MenuVO menuVO = menuService.selectMenu(menuId);

        return ResponseEntity.ok(menuVO);
    }


    /**
     * 사장님 페이지 > 메뉴 관리 > 메뉴 설정 > 메뉴 수정 > 수정 action
     *
     * @param menuVO 메뉴 정보
     * @param model  모델
     * @return "/views/common/message"
     */
    @Transactional
    @RequestMapping("/info/menu/update.do")
    public String menuUpdate(MenuVO menuVO, ModelMap model) {
        menuService.updateMenu(menuVO);

        model.addAttribute("type", MessageType.msgUrl.getMessage());
        model.addAttribute("message", "수정이 완료되었습니다.");
        model.addAttribute("returnUrl", "/owner/menu/info");
        return "/views/common/message";
    }


    /**
     * 사장님 페이지 > 메뉴 관리 > 메뉴 설정 > 메뉴 다수 조회
     *
     * @param menuGroupId 메뉴 그룹 ID
     * @return 다수 메뉴 정보
     */
    @RequestMapping("/info/menus/select.do")
    public ResponseEntity<List<MenuVO>> menusSelect(@RequestParam("menuGroupId") Long menuGroupId) {
        return ResponseEntity.ok(menuService.selectMenus(menuGroupId));
    }


    /**
     * 사장님 페이지 > 메뉴 관리 > 메뉴 설정 > 메뉴 순서 변경
     *
     * @param menuIdList 메뉴 ID 리스트
     * @param model      모델
     * @return "/views/common/message"
     */
    @RequestMapping("/info/menus/orderUpdate.do")
    public String menusOrderUpdate(@RequestParam("menuIdList") Long[] menuIdList, ModelMap model) {
        menuService.updateMenusOrder(menuIdList);

        model.addAttribute("type", MessageType.msgUrl.getMessage());
        model.addAttribute("message", "저장이 완료되었습니다.");
        model.addAttribute("returnUrl", "/owner/menu/info");
        return "/views/common/message";
    }

    /**
     * 사장님 페이지 > 메뉴 관리 > 메뉴 설정 > 메뉴 삭제 > 삭제 action
     *
     * @param menuId 메뉴 ID
     * @param model  모델
     * @return "/views/common/message"
     */
    @RequestMapping("/info/menu/delete.do")
    public String menuDelete(@RequestParam("menuId") Long menuId, ModelMap model) {
        menuService.deleteMenu(menuId);

        model.addAttribute("type", MessageType.msgUrl.getMessage());
        model.addAttribute("message", "삭제가 완료되었습니다.");
        model.addAttribute("returnUrl", "/owner/menu/info");
        return "/views/common/message";
    }

    /**
     * 사장님 페이지 > 메뉴 관리 > 메뉴 설정 > 옵션그룹 연결 > 등록 action
     *
     * @param optionGroupIdList 메뉴 옵션 그룹 ID 리스트
     * @param menuId            메뉴 ID
     * @param model             모델
     * @return "/views/common/message"
     */
    @RequestMapping("/info/menu/connectOptionGroup")
    public String connectOptionGroup(@RequestParam("optionGroupIdList") Long[] optionGroupIdList,
                                     @RequestParam("menuId") Long menuId, ModelMap model) {
        menuService.insertMenuOptionGroupConnectMenu(optionGroupIdList, menuId);

        model.addAttribute("type", MessageType.msgUrl.getMessage());
        model.addAttribute("message", "저장이 완료되었습니다.");
        model.addAttribute("returnUrl", "/owner/menu/info");
        return "/views/common/message";
    }
}
