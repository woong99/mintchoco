package com.woong.mintchoco.owner.menu.service;

import com.woong.mintchoco.owner.menu.entity.Menu;
import com.woong.mintchoco.owner.menu.entity.MenuGroup;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroup;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroupMenu;
import com.woong.mintchoco.owner.menu.model.MenuVO;
import com.woong.mintchoco.owner.menu.repository.MenuRepository;
import com.woong.mintchoco.owner.menu.repository.group.MenuGroupRepository;
import com.woong.mintchoco.owner.menu.repository.option.group.MenuOptionGroupRepository;
import com.woong.mintchoco.owner.menu.repository.option.group.menu.MenuOptionGroupMenuRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private final MenuGroupRepository menuGroupRepository;

    private final MenuOptionGroupRepository menuOptionGroupRepository;

    private final MenuOptionGroupMenuRepository menuOptionGroupMenuRepository;

    public void deleteMenu(Long menuId) {
        menuRepository.deleteById(menuId);
    }

    public void insertMenu(Long menuGroupId, MenuVO menuVO) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuGroupId).orElseThrow(NoSuchElementException::new);
        Menu menu = Menu.toMenu(menuVO);
        menu.setMenuGroup(menuGroup);

        int lastMenuOrder = menuRepository.findLastMenuOrder(menuGroupId);
        menu.setMenuOrder(lastMenuOrder + 1);

        menuRepository.save(menu);
    }


    public MenuVO selectMenu(Long menuId) {
        return MenuVO.toMenuVO(menuRepository.findById(menuId).orElseThrow(NoSuchElementException::new));
    }


    public void updateMenu(MenuVO menuVO) {
        menuRepository.updateMenu(menuVO);
    }


    public List<MenuVO> selectMenus(Long menuGroupId) {
        return menuRepository.findByMenuGroupId(menuGroupId).stream().map(MenuVO::toMenuVO).toList();
    }


    public void updateMenusOrder(Long[] menuIdList) {
        menuRepository.updateMenusOrder(menuIdList);
    }

    @Transactional
    public void insertMenuOptionGroupConnectMenu(Long[] optionGroupIdList, Long menuId) {
        menuOptionGroupMenuRepository.deleteByMenuId(menuId);
        Menu menu = menuRepository.getReferenceById(menuId);

        for (int i = 0; i < optionGroupIdList.length; i++) {
            MenuOptionGroup menuOptionGroup = menuOptionGroupRepository.getReferenceById(optionGroupIdList[i]);
            MenuOptionGroupMenu menuOptionGroupMenu = new MenuOptionGroupMenu();
            menuOptionGroupMenu.setMenuOptionGroup(menuOptionGroup);
            menuOptionGroupMenu.setMenu(menu);
            menuOptionGroupMenu.setMenuOptionGroupOrder(i + 1);
            menuOptionGroupMenuRepository.save(menuOptionGroupMenu);
        }
    }

}
