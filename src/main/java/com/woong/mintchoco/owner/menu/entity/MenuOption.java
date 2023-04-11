package com.woong.mintchoco.owner.menu.entity;

import com.woong.mintchoco.global.common.entity.BaseEntity;
import com.woong.mintchoco.owner.menu.model.MenuOptionVO;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Getter
public class MenuOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // ID

    private String title;   // 옵션명

    private int price;  // 옵션 가격

    private int menuOrder;  // 순서

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_option_group_id")
    @Exclude
    @Setter
    private MenuOptionGroup menuOptionGroup;

    public static MenuOption toMenuOption(MenuOptionVO menuOptionVO) {
        return MenuOption.builder()
                .id(menuOptionVO.getMenuOptionId())
                .title(menuOptionVO.getMenuOptionTitle())
                .price(menuOptionVO.getMenuOptionPrice())
                .menuOrder(menuOptionVO.getMenuOptionOrder())
                .menuOptionGroup(menuOptionVO.getMenuOptionGroup())
                .build();
    }
}
