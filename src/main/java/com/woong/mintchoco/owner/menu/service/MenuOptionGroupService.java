package com.woong.mintchoco.owner.menu.service;

import com.woong.mintchoco.global.auth.entity.User;
import com.woong.mintchoco.global.common.ErrorCode;
import com.woong.mintchoco.owner.menu.entity.Menu;
import com.woong.mintchoco.owner.menu.entity.MenuOption;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroup;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroupMenu;
import com.woong.mintchoco.owner.menu.exception.MenuOptionGroupNotFoundException;
import com.woong.mintchoco.owner.menu.model.MenuOptionGroupMenuVO;
import com.woong.mintchoco.owner.menu.model.MenuOptionGroupVO;
import com.woong.mintchoco.owner.menu.model.MenuOptionVO;
import com.woong.mintchoco.owner.menu.model.MenuVO;
import com.woong.mintchoco.owner.menu.repository.MenuRepository;
import com.woong.mintchoco.owner.menu.repository.option.MenuOptionRepository;
import com.woong.mintchoco.owner.menu.repository.option.group.MenuOptionGroupRepository;
import com.woong.mintchoco.owner.menu.repository.option.group.menu.MenuOptionGroupMenuRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuOptionGroupService {

    private final MenuOptionGroupMenuRepository menuOptionGroupMenuRepository;

    private final MenuOptionGroupRepository menuOptionGroupRepository;

    private final MenuOptionRepository menuOptionRepository;

    private final MenuRepository menuRepository;


    /**
     * 모든 메뉴 옵션 그룹과 연관된 옵션들을 조회한다.
     *
     * @param user 인증 정보
     * @return 모든 메뉴 옵션 그룹 및 연관된 옵션들
     */
    public List<MenuOptionGroupVO> selectAllMenuOptionGroupWithMenuOption(User user) {
        Long storeId = user.getStore().getId();

        return menuOptionGroupRepository.selectAllMenuOptionGroupWithMenuOptionOrderByMenuOrder(storeId).stream()
                .map(MenuOptionGroupVO::toMenuOptionGroupVOWithMenuOption).toList();
    }


    /**
     * 모든 메뉴 옵션 그룹을 조회한다.
     *
     * @param user 인증 정보
     * @return 모든 메뉴 옵션 그룹들
     */
    public List<MenuOptionGroupVO> selectAllMenuOptionGroup(User user) {
        Long storeId = user.getStore().getId();

        return menuOptionGroupRepository.findAllByStoreId(storeId).stream().map(MenuOptionGroupVO::toMenuOptionGroupVO)
                .toList();
    }


    /**
     * 메뉴 옵션 그룹과 메뉴 옵션들을 저장한다.
     *
     * @param user              인증 정보
     * @param menuOptionGroupVO 메뉴 옵션 그룹 정보
     * @param menuOptionVO      메뉴 옵션 정보
     */
    @Transactional
    public void insertMenuOptionGroup(User user, MenuOptionGroupVO menuOptionGroupVO, MenuOptionVO menuOptionVO) {
        menuOptionGroupVO.setStore(user.getStore());
        MenuOptionGroup menuOptionGroup = menuOptionGroupRepository.save(
                MenuOptionGroup.toMenuOptionGroup(menuOptionGroupVO));

        List<MenuOption> menuOptions = menuOptionVO.getMenuOptionVOList().stream().map(MenuOption::toMenuOption)
                .toList();
        for (MenuOption data : menuOptions) {
            data.setMenuOptionGroup(menuOptionGroup);
        }
        menuOptionRepository.insertMenuOption(menuOptions);
    }


    /**
     * 단일 메뉴 옵션 그룹과 연관된 메뉴 옵션들을 조회한다.
     *
     * @param menuOptionGroupId 메뉴 옵션 그룹 ID
     * @return 단일 메뉴 옵션 그룹 및 연관된 메뉴 옵션들
     */
    public MenuOptionGroupVO selectMenuOptionGroup(Long menuOptionGroupId) {
        MenuOptionGroup menuOptionGroup = menuOptionGroupRepository.selectMenuOptionGroupWithMenuOptionOrderByMenuOrder(
                menuOptionGroupId);
        if (menuOptionGroup == null) {
            throw new MenuOptionGroupNotFoundException(ErrorCode.MENU_OPTION_GROUP_NOT_FOUND);
        }
        return MenuOptionGroupVO.toMenuOptionGroupVOWithMenuOption(menuOptionGroup);
    }


    /**
     * 메뉴 옵션 그룹 및 연관된 메뉴 옵션들을 수정한다.
     *
     * @param menuOptionGroupVO 메뉴 옵션 그룹 정보
     */
    @Transactional
    public void updateMenuOptionGroup(MenuOptionGroupVO menuOptionGroupVO) {
        menuOptionGroupRepository.updateMenuOptionGroup(menuOptionGroupVO);
        menuOptionRepository.deleteMenuOption(menuOptionGroupVO.getMenuOptionGroupId());

        List<MenuOption> menuOptions = menuOptionGroupVO.getMenuOptionVOList().stream().map(MenuOption::toMenuOption)
                .toList();
        for (MenuOption data : menuOptions) {
            data.setMenuOptionGroup(MenuOptionGroup.toMenuOptionGroup(menuOptionGroupVO));
        }
        menuOptionRepository.insertMenuOption(menuOptions);
    }


    /**
     * 메뉴 옵션 그룹을 삭제한다.
     *
     * @param menuOptionGroupId 메뉴 옵션 그룹 ID
     */
    public void deleteMenuOptionGroup(Long menuOptionGroupId) {
        menuOptionGroupRepository.deleteMenuOptionGroup(menuOptionGroupId);
    }


    /**
     * 메뉴와 연결된 메뉴 옵션 그룹 정보를 조회한다.
     *
     * @param menuId 메뉴 ID
     * @return 메뉴와 연결된 메뉴 옵션 그룹 정보
     */
    public List<MenuOptionGroupMenuVO> selectMenuOptionGroupConnectMenu(Long menuId) {
        List<MenuOptionGroupMenu> menuOptionGroupMenuList = menuOptionGroupMenuRepository.findByMenuIdOrderByMenuId(
                menuId);

        return menuOptionGroupMenuList.stream().map(MenuOptionGroupMenuVO::toMenuOptionGroupMenuVO).toList();
    }


    /**
     * 메뉴 옵션 그룹과 연결된 메뉴를 조회한다.
     *
     * @param menuOptionGroupId 메뉴 옵션 그룹 ID
     * @return 연결된 메뉴 정보
     */
    public List<MenuVO> selectMenuOptionGroupConnectedMenu(Long menuOptionGroupId) {
        List<Menu> menuList = menuRepository.selectMenuOptionGroupConnectedMenu(menuOptionGroupId);

        return menuList.stream().map(MenuVO::toMenuVO).toList();
    }

}
