package com.woong.mintchoco.repository.menu.group;

import com.woong.mintchoco.domain.MenuGroup;
import com.woong.mintchoco.vo.MenuGroupVO;
import java.util.List;

public interface MenuGroupRepositoryCustom {

    int findLastGroupOrder(Long storeId);

    void updateMenuGroup(MenuGroupVO menuGroupVO);

    void updateMenuGroupOrder(Long[] menuGroupIdList);

    List<MenuGroup> selectAllMenuGroupWithMenu(Long storeId);
}
