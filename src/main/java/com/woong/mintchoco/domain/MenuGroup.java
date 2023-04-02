package com.woong.mintchoco.domain;

import com.woong.mintchoco.vo.MenuGroupVO;
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
import lombok.ToString;
import lombok.ToString.Exclude;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MenuGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // ID

    private String title; // 메뉴그룹명

    private String explanation; // 메뉴그룹 설명

    private String exposure;    // 노출/비노출 여부

    private int groupOrder; // 순서

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @Exclude
    private Store store;

    public static MenuGroup toMenuGroup(MenuGroupVO menuGroupVO) {
        return MenuGroup.builder()
                .title(menuGroupVO.getMenuGroupTitle())
                .explanation(menuGroupVO.getMenuGroupExplanation())
                .exposure(menuGroupVO.getMenuGroupExposure())
                .groupOrder(menuGroupVO.getMenuGroupOrder())
                .store(menuGroupVO.getStore())
                .build();
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
