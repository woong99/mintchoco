package com.woong.mintchoco.owner.menu.repository.group;

import com.woong.mintchoco.owner.menu.entity.MenuGroup;
import com.woong.mintchoco.owner.menu.model.MenuGroupVO;
import java.util.List;

public interface MenuGroupRepositoryCustom {

    int findLastGroupOrder(Long storeId);

    void updateMenuGroup(MenuGroupVO menuGroupVO);

    void updateMenuGroupOrder(Long[] menuGroupIdList);

    List<MenuGroup> selectAllMenuGroupWithMenu(Long storeId);
}
