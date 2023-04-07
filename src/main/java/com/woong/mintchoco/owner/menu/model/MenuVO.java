package com.woong.mintchoco.owner.menu.model;

import com.woong.mintchoco.owner.menu.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuVO {

    private long menuId;

    private String menuTitle;

    private String menuExplanation;

    private String menuExposure;

    private int menuOrder;

    private int menuPrice;

    private MenuGroupVO menuGroupVO;

    public static MenuVO toMenuVO(Menu menu) {
        return MenuVO.builder()
                .menuId(menu.getId())
                .menuTitle(menu.getTitle())
                .menuExplanation(menu.getExplanation())
                .menuExposure(menu.getExposure())
                .menuOrder(menu.getMenuOrder())
                .menuPrice(menu.getPrice())
                .build();
    }
}
