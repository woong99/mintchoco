package com.woong.mintchoco.owner.menu.repository.option.group;

import com.woong.mintchoco.owner.menu.entity.MenuOptionGroup;
import com.woong.mintchoco.owner.menu.model.MenuOptionGroupVO;
import java.util.List;

public interface MenuOptionGroupRepositoryCustom {

    List<MenuOptionGroup> selectAllMenuOptionGroupWithMenuOptionOrderByMenuOrder(Long storeId);

    MenuOptionGroup selectMenuOptionGroupWithMenuOptionOrderByMenuOrder(Long menuOptionGroupId);

    void updateMenuOptionGroup(MenuOptionGroupVO menuOptionGroupVO);

    void deleteMenuOptionGroup(Long menuOptionGroupId);
}
