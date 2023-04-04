package com.woong.mintchoco.repository.menu.option.group;

import com.woong.mintchoco.domain.MenuOptionGroup;
import java.util.List;

public interface MenuOptionGroupRepositoryCustom {

    List<MenuOptionGroup> selectAllMenuOptionGroupWithMenuOptionOrderByMenuOrder(Long storeId);

    MenuOptionGroup selectMenuOptionGroupWithMenuOptionOrderByMenuOrder(Long menuOptionGroupId);
}
