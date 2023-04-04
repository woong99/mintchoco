package com.woong.mintchoco.vo;

import com.woong.mintchoco.domain.MenuOptionGroup;
import com.woong.mintchoco.domain.Store;
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
@Builder
@ToString
public class MenuOptionGroupVO {

    List<MenuOptionVO> menuOptionVOList;

    private Long menuOptionGroupId; // ID

    private String menuOptionGroupTitle;    // 옵션그룹명

    private String menuOptionGroupRequired; // 필수/선택

    private int menuOptionGroupMaxSelect;   // 최대 선택 가능 옵션 수

    private Store store;

    public static MenuOptionGroupVO toMenuOptionGroupVO(MenuOptionGroup menuOptionGroup) {
        return MenuOptionGroupVO.builder()
                .menuOptionGroupId(menuOptionGroup.getId())
                .menuOptionGroupTitle(menuOptionGroup.getTitle())
                .menuOptionGroupRequired(menuOptionGroup.getRequired())
                .menuOptionGroupMaxSelect(menuOptionGroup.getMaxSelect())
                .menuOptionVOList(menuOptionGroup.getMenuOptions().stream().map(MenuOptionVO::toMenuOptionVO).toList())
                .build();
    }
}
