package com.woong.mintchoco.owner.menu.service;

import com.woong.mintchoco.global.auth.entity.User;
import com.woong.mintchoco.global.file.entity.AttachFile;
import com.woong.mintchoco.owner.menu.entity.Menu;
import com.woong.mintchoco.owner.menu.entity.MenuGroup;
import com.woong.mintchoco.owner.menu.entity.MenuOption;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroup;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroupMenu;
import com.woong.mintchoco.owner.menu.model.MenuOptionVO;
import com.woong.mintchoco.owner.menu.model.MenuVO;
import com.woong.mintchoco.owner.store.entity.Store;
import java.util.Collections;
import java.util.List;

public class BaseMenuServiceTest {

    protected final static Long menuId1 = 1L;
    protected final static Long menuId2 = 2L;
    protected final static Long menuGroupId1 = 3L;
    protected final static Long menuGroupId2 = 4L;
    protected final static Long attachFileId1 = 5L;
    protected final static Long attachFileId2 = 6L;
    protected final static Long menuOptionGroupId1 = 7L;
    protected final static Long menuOptionGroupId2 = 8L;
    protected final static Long menuOptionId1 = 9L;
    protected final static Long menuOptionId2 = 10L;
    protected final static Long userId = 11L;
    protected final static Long storeId = 12L;
    protected final static Long menuId3 = 13L;
    protected final static Long menuId4 = 14L;
    protected final static Long menuOptionId3 = 15L;
    protected final static Long menuOptionId4 = 16L;
    protected final static Long menuOptionGroupMenuId1 = 17L;
    protected final static Long menuOptionGroupMenuId2 = 18L;


    protected Menu makeMenu(Long menuId) {
        return Menu.builder()
                .id(menuId)
                .title("test")
                .explanation("test")
                .exposure("Y")
                .menuOrder(1)
                .price(1000)
                .menuImage(makeAttachFile(1L))
                .menuGroup(MenuGroup.builder().id(1L).build())
                .menuOptionGroupMenus(Collections.singletonList(MenuOptionGroupMenu.builder().id(1L).build()))
                .build();
    }

    protected MenuOption makeMenuOption(Long menuOptionId) {
        return MenuOption.builder()
                .id(menuOptionId)
                .build();
    }

    protected AttachFile makeAttachFile(Long attachFileId) {
        return AttachFile.builder()
                .id(attachFileId)
                .build();
    }

    protected Menu makeMenuWithAttachFile(Long menuId, AttachFile attachFile) {
        return Menu.builder()
                .id(menuId)
                .menuImage(attachFile)
                .build();
    }


    protected Menu makeMenu(Long menuId, int menuOrder) {
        return Menu.builder()
                .id(menuId)
                .title("test")
                .explanation("test")
                .exposure("Y")
                .menuOrder(menuOrder)
                .price(1000)
                .menuImage(makeAttachFile(1L))
                .menuGroup(MenuGroup.builder().id(1L).build())
                .menuOptionGroupMenus(Collections.singletonList(MenuOptionGroupMenu.builder().id(1L).build()))
                .build();
    }

    protected MenuVO createMenuVO(Long menuId) {
        return MenuVO.builder()
                .menuId(menuId)
                .build();
    }

    protected MenuGroup createMenuGroup(Long menuGroupId, List<Menu> menus) {
        return MenuGroup.builder()
                .id(menuGroupId)
                .menus(menus)
                .build();
    }

    protected Menu createMenu(Long menuId) {
        return Menu.builder()
                .id(menuId)
                .build();
    }

    protected Store createStore(Long storeId) {
        return Store.builder()
                .id(storeId)
                .build();
    }

    protected User createUser(Long userId, Store store) {
        return User.builder()
                .id(userId)
                .store(store)
                .build();
    }


    protected User createUser(Store store) {
        return User.builder()
                .id(MenuOptionGroupServiceTest.userId)
                .store(store)
                .build();
    }

    protected MenuOption createMenuOption(Long menuOptionId, int menuOrder) {
        return MenuOption.builder()
                .id(menuOptionId)
                .menuOrder(menuOrder)
                .build();
    }

    protected MenuOptionVO createMenuOptionVO(Long menuOptionId, int menuOrder) {
        return MenuOptionVO.builder()
                .menuOptionId(menuOptionId)
                .menuOptionOrder(menuOrder)
                .build();
    }

    protected MenuOptionGroup createMenuOptionGroup(Long menuOptionGroupId, List<MenuOption> menuOptionList) {
        return MenuOptionGroup.builder()
                .id(menuOptionGroupId)
                .menuOptions(menuOptionList)
                .build();
    }

    protected MenuOptionGroupMenu createMenuOptionGroupMenu(Long menuOptionGroupMenuId) {
        return MenuOptionGroupMenu.builder()
                .id(menuOptionGroupMenuId)
                .menu(createMenu())
                .menuOptionGroup(createMenuOptionGroup(menuOptionGroupId1, List.of()))
                .build();
    }

    protected Menu createMenu() {
        return Menu.builder()
                .id(100L)
                .build();
    }
}
