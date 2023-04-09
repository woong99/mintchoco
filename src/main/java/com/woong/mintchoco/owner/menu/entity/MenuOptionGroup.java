package com.woong.mintchoco.owner.menu.entity;

import com.woong.mintchoco.global.common.entity.BaseEntity;
import com.woong.mintchoco.owner.menu.model.MenuOptionGroupVO;
import com.woong.mintchoco.owner.store.entity.Store;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class MenuOptionGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // ID

    private String title;   // 옵션그룹명

    private String required;    // 필수/선택

    private int maxSelect;  // 최대 선택 가능 옵션 수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @Exclude
    private Store store;

    @OneToMany(mappedBy = "menuOptionGroup", fetch = FetchType.LAZY)
    @Exclude
    private List<MenuOption> menuOptions;

    @OneToMany(mappedBy = "menuOptionGroup", fetch = FetchType.LAZY)
    @Exclude
    private List<MenuOptionGroupMenu> menuOptionGroupMenus;

    public static MenuOptionGroup toMenuOptionGroup(MenuOptionGroupVO menuOptionGroupVO) {
        return MenuOptionGroup.builder()
                .id(menuOptionGroupVO.getMenuOptionGroupId())
                .title(menuOptionGroupVO.getMenuOptionGroupTitle())
                .required(menuOptionGroupVO.getMenuOptionGroupRequired())
                .maxSelect(menuOptionGroupVO.getMenuOptionGroupMaxSelect())
                .store(menuOptionGroupVO.getStore())
                .build();
    }
}
