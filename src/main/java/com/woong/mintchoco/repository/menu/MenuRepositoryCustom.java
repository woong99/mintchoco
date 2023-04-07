package com.woong.mintchoco.repository.menu;

import com.woong.mintchoco.domain.Menu;
import com.woong.mintchoco.vo.MenuVO;
import java.util.List;

public interface MenuRepositoryCustom {

    int findLastMenuOrder(Long menuGroupId);

    void updateMenu(MenuVO menuVO);

    void updateMenusOrder(Long[] menuIdList);

    List<Menu> selectMenuOptionGroupConnectedMenu(Long menuOptionGroupId);
}
