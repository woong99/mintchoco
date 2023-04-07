package com.woong.mintchoco.owner.menu.service;

import com.woong.mintchoco.global.auth.entity.User;
import com.woong.mintchoco.owner.menu.entity.Menu;
import com.woong.mintchoco.owner.menu.entity.MenuOption;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroup;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroupMenu;
import com.woong.mintchoco.owner.menu.model.MenuOptionGroupMenuVO;
import com.woong.mintchoco.owner.menu.model.MenuOptionGroupVO;
import com.woong.mintchoco.owner.menu.model.MenuOptionVO;
import com.woong.mintchoco.owner.menu.model.MenuVO;
import com.woong.mintchoco.owner.menu.repository.MenuRepository;
import com.woong.mintchoco.owner.menu.repository.option.MenuOptionRepository;
import com.woong.mintchoco.owner.menu.repository.option.group.MenuOptionGroupRepository;
import com.woong.mintchoco.owner.menu.repository.option.group.menu.MenuOptionGroupMenuRepository;
import com.woong.mintchoco.owner.store.entity.Store;
import com.woong.mintchoco.owner.store.repository.StoreRepository;
import java.util.List;
import java.util.NoSuchElementException;
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

    private final StoreRepository storeRepository;

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
        Store store = storeRepository.findById(user.getStore().getId()).orElseThrow(NoSuchElementException::new);
        menuOptionGroupVO.setStore(store);
        MenuOptionGroup menuOptionGroup = menuOptionGroupRepository.save(
                MenuOptionGroup.toMenuOptionGroup(menuOptionGroupVO));

        List<MenuOptionVO> menuOptionVOList = menuOptionVO.getMenuOptionVOList();
        for (MenuOptionVO data : menuOptionVOList) {
            data.setMenuOptionGroup(menuOptionGroup);
            menuOptionRepository.save(MenuOption.toMenuOption(data));
        }
    }

    /**
     * 단일 메뉴 옵션 그룹과 연관된 메뉴 옵션들을 조회한다.
     *
     * @param menuOptionGroupId 메뉴 옵션 그룹 ID
     * @return 단일 메뉴 옵션 그룹 및 연관된 메뉴 옵션들
     */
    public MenuOptionGroupVO selectMenuOptionGroup(Long menuOptionGroupId) {
        return MenuOptionGroupVO.toMenuOptionGroupVOWithMenuOption(
                menuOptionGroupRepository.selectMenuOptionGroupWithMenuOptionOrderByMenuOrder(menuOptionGroupId));
    }

    /**
     * 메뉴 옵션 그룹 및 연관된 메뉴 옵션들을 수정한다.
     *
     * @param menuOptionGroupVO 메뉴 옵션 그룹 정보
     */
    @Transactional
    public void updateMenuOptionGroup(MenuOptionGroupVO menuOptionGroupVO) {
        MenuOptionGroup menuOptionGroup = menuOptionGroupRepository.findById(menuOptionGroupVO.getMenuOptionGroupId())
                .orElseThrow(NoSuchElementException::new);
        menuOptionGroup.setTitle(menuOptionGroupVO.getMenuOptionGroupTitle());
        menuOptionGroup.setRequired(menuOptionGroupVO.getMenuOptionGroupRequired());
        menuOptionGroup.setMaxSelect(menuOptionGroupVO.getMenuOptionGroupMaxSelect());

        menuOptionGroup = menuOptionGroupRepository.save(menuOptionGroup);
        menuOptionRepository.deleteByMenuOptionGroupId(menuOptionGroup.getId());

        for (MenuOptionVO data : menuOptionGroupVO.getMenuOptionVOList()) {
            data.setMenuOptionGroup(menuOptionGroup);
            MenuOption menuOption = MenuOption.toMenuOption(data);
            menuOptionRepository.save(menuOption);
        }
    }

    public void deleteMenuOptionGroup(Long menuOptionGroupId) {
        menuOptionGroupRepository.deleteById(menuOptionGroupId);
    }

    public List<MenuOptionGroupMenuVO> selectMenuOptionGroupConnectMenu(Long menuId) {
        List<MenuOptionGroupMenu> menuOptionGroupMenuList = menuOptionGroupMenuRepository.findByMenuIdOrderByMenuId(
                menuId);

        return menuOptionGroupMenuList.stream().map(MenuOptionGroupMenuVO::toMenuOptionGroupMenuVO).toList();
    }


    public List<MenuVO> selectMenuOptionGroupConnectedMenu(Long menuOptionGroupId) {
        List<Menu> menuList = menuRepository.selectMenuOptionGroupConnectedMenu(menuOptionGroupId);

        return menuList.stream().map(MenuVO::toMenuVO).toList();
    }

}
