package com.woong.mintchoco.owner.menu.repository.option;

import com.woong.mintchoco.owner.menu.entity.MenuOption;
import java.util.List;

public interface MenuOptionRepositoryCustom {

    void insertMenuOption(List<MenuOption> menuOptions);

    void deleteMenuOption(Long menuOptionGroupId);
}
