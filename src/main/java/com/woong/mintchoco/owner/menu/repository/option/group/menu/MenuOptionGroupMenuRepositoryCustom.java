package com.woong.mintchoco.owner.menu.repository.option.group.menu;

import com.woong.mintchoco.owner.menu.entity.MenuOptionGroupMenu;
import java.util.List;

public interface MenuOptionGroupMenuRepositoryCustom {

    void deleteMenuOptionGroupMenu(Long menuId);

    void saveAllMenuOptionGroupMenu(List<MenuOptionGroupMenu> menuOptionGroupMenus);
}
