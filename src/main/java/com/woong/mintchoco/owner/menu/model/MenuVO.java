package com.woong.mintchoco.owner.menu.model;

import com.woong.mintchoco.global.file.entity.AttachFile;
import com.woong.mintchoco.owner.menu.entity.Menu;
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

    private AttachFile menuImage;

    private MenuGroupVO menuGroupVO;

    private List<MenuOptionGroupVO> menuOptionGroupVOS;

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

    public static MenuVO toMenuVOWithMenuImage(Menu menu) {
        return MenuVO.builder()
                .menuId(menu.getId())
                .menuTitle(menu.getTitle())
                .menuExplanation(menu.getExplanation())
                .menuExposure(menu.getExposure())
                .menuOrder(menu.getMenuOrder())
                .menuPrice(menu.getPrice())
                .menuImage(menu.getMenuImage())
                .build();
    }

}
