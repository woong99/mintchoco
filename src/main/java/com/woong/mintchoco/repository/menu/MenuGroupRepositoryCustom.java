package com.woong.mintchoco.repository.menu;

import com.woong.mintchoco.vo.MenuGroupVO;

public interface MenuGroupRepositoryCustom {

    int findLastGroupOrder(Long storeId);

    void updateMenuGroup(MenuGroupVO menuGroupVO);

    void updateMenuGroupOrder(Long[] menuGroupIdList);
}
