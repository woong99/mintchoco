package com.woong.mintchoco.vo;

import com.woong.mintchoco.domain.MenuOptionGroupMenu;
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
@NoArgsConstructor
@AllArgsConstructor
public class MenuOptionGroupMenuVO {

    private Long id;

    private Long menuId;

    private Long menuOptionGroupId;

    private int menuOptionGroupOrder;

    public static MenuOptionGroupMenuVO toMenuOptionGroupMenuVO(MenuOptionGroupMenu menuOptionGroupMenu) {
        return MenuOptionGroupMenuVO.builder()
                .id(menuOptionGroupMenu.getId())
                .menuId(menuOptionGroupMenu.getMenu().getId())
                .menuOptionGroupId(menuOptionGroupMenu.getMenuOptionGroup().getId())
                .menuOptionGroupOrder(menuOptionGroupMenu.getMenuOptionGroupOrder())
                .build();
    }
}
