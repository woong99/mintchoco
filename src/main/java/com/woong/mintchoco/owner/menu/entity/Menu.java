package com.woong.mintchoco.owner.menu.entity;

import com.woong.mintchoco.global.common.entity.BaseEntity;
import com.woong.mintchoco.global.file.entity.AttachFile;
import com.woong.mintchoco.owner.menu.model.MenuVO;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String explanation;

    private String exposure;

    private int menuOrder;

    private int price;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_image_id")
    @Setter
    @Exclude
    private AttachFile menuImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    @Exclude
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
    @Exclude
    private List<MenuOptionGroupMenu> menuOptionGroupMenus;

    public static Menu toMenu(MenuVO menuVO) {
        return Menu.builder()
                .title(menuVO.getMenuTitle())
                .explanation(menuVO.getMenuExplanation())
                .exposure(menuVO.getMenuExposure())
                .menuOrder(menuVO.getMenuOrder())
                .price(menuVO.getMenuPrice())
                .build();
    }
}
