package com.woong.mintchoco.owner.menu.model;

import com.woong.mintchoco.owner.menu.entity.MenuGroup;
import com.woong.mintchoco.owner.store.entity.Store;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuGroupVO {

    private Long id; // ID

    private String menuGroupTitle; // 메뉴그룹명

    private String menuGroupExplanation; // 메뉴그룹 설명

    private String menuGroupExposure; // 노출/비노출 여부서

    private int menuGroupOrder; // 순서

    private Store store;

    private List<MenuVO> menuVOList;

    public static MenuGroupVO toMenuGroupVOWithMenuVO(MenuGroup menuGroup) {
        MenuGroupVO menuGroupVO = toMenuGroupVO(menuGroup);
        menuGroupVO.setMenuVOList(menuGroup.getMenus().stream().map(MenuVO::toMenuVO).toList());
        return menuGroupVO;
    }

    public static MenuGroupVO toMenuGroupVOWithMenuVOWithMenuImage(MenuGroup menuGroup) {
        MenuGroupVO menuGroupVO = toMenuGroupVO(menuGroup);
        menuGroupVO.setMenuVOList(menuGroup.getMenus().stream().map(MenuVO::toMenuVOWithMenuImage).toList());
        return menuGroupVO;
    }

    public static MenuGroupVO toMenuGroupVO(MenuGroup menuGroup) {
        return MenuGroupVO.builder()
                .id(menuGroup.getId())
                .menuGroupTitle(menuGroup.getTitle())
                .menuGroupExplanation(menuGroup.getExplanation())
                .menuGroupExposure(menuGroup.getExposure())
                .menuGroupOrder(menuGroup.getGroupOrder())
                .build();
    }
}
