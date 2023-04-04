package com.woong.mintchoco.vo;

import com.woong.mintchoco.domain.MenuOption;
import com.woong.mintchoco.domain.MenuOptionGroup;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MenuOptionVO {

    List<MenuOptionVO> menuOptionVOList;

    private Long menuOptionId;  // ID

    private String menuOptionTitle; // 옵션명

    private int menuOptionPrice;    // 옵션 가격

    private int menuOptionOrder;    // 순서

    private MenuOptionGroup menuOptionGroup;

    public static MenuOptionVO toMenuOptionVO(MenuOption menuOption) {
        return MenuOptionVO.builder()
                .menuOptionId(menuOption.getId())
                .menuOptionTitle(menuOption.getTitle())
                .menuOptionPrice(menuOption.getPrice())
                .menuOptionOrder(menuOption.getMenuOrder())
                .build();
    }
}
