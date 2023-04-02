package com.woong.mintchoco.vo;

import com.woong.mintchoco.domain.MenuGroup;
import com.woong.mintchoco.domain.Store;
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
