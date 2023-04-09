package com.woong.mintchoco.owner.menu.repository;

import com.woong.mintchoco.owner.menu.entity.Menu;
import com.woong.mintchoco.owner.menu.model.MenuVO;
import java.util.List;

public interface MenuRepositoryCustom {

    int findLastMenuOrder(Long menuGroupId);

    void updateMenu(MenuVO menuVO);

    List<Menu> selectMenuOptionGroupConnectedMenu(Long menuOptionGroupId);

    void deleteMenu(Long menuId);
}
