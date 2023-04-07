package com.woong.mintchoco.repository.menu;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woong.mintchoco.domain.Menu;
import com.woong.mintchoco.domain.QMenu;
import com.woong.mintchoco.domain.QMenuOptionGroupMenu;
import com.woong.mintchoco.vo.MenuVO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QMenu qMenu = QMenu.menu;

    QMenuOptionGroupMenu qMenuOptionGroupMenu = QMenuOptionGroupMenu.menuOptionGroupMenu;


    /**
     * 마지막 메뉴 순서를 조회한다.
     * @param menuGroupId 메뉴 그룹 ID
     * @return 마지막 메뉴 순서
     */
    @Override
    public int findLastMenuOrder(Long menuGroupId) {
        return Optional.ofNullable(jpaQueryFactory
                        .select(qMenu.menuOrder.max())
                        .from(qMenu)
                        .where(qMenu.menuGroup.id.eq(menuGroupId))
                        .fetchOne())
                .orElse(0);
    }


    /**
     * 메뉴를 업데이트한다.
     * @param menuVO 메뉴 정보
     */
    @Override
    public void updateMenu(MenuVO menuVO) {
        jpaQueryFactory
                .update(qMenu)
                .set(qMenu.title, menuVO.getMenuTitle())
                .set(qMenu.explanation, menuVO.getMenuExplanation())
                .set(qMenu.exposure, menuVO.getMenuExposure())
                .set(qMenu.price, menuVO.getMenuPrice())
                .set(qMenu.updatedAt, LocalDateTime.now())
                .where(qMenu.id.eq(menuVO.getMenuId()))
                .execute();
    }


    /**
     * 메뉴 순서를 업데이트한다.
     * @param menuIdList 메뉴 ID 리스트
     */
    @Transactional
    @Override
    public void updateMenusOrder(Long[] menuIdList) {
        for (int i = 0; i < menuIdList.length; i++) {
            jpaQueryFactory
                    .update(qMenu)
                    .set(qMenu.menuOrder, i + 1)
                    .set(qMenu.updatedAt, LocalDateTime.now())
                    .where(qMenu.id.eq(menuIdList[i]))
                    .execute();
        }
    }


    /**
     * 메뉴와 연관된 메뉴 옵션 그룹을 조인해서 조회한다.
     * @param menuOptionGroupId 메뉴 옵션 그룹 ID
     * @return 메뉴 및 연관된 메뉴 옵션 그룹
     */
    @Override
    public List<Menu> selectMenuOptionGroupConnectedMenu(Long menuOptionGroupId) {
        return jpaQueryFactory
                .selectFrom(qMenu)
                .leftJoin(qMenu.menuOptionGroupMenus, qMenuOptionGroupMenu).fetchJoin()
                .where(qMenuOptionGroupMenu.menuOptionGroup.id.eq(menuOptionGroupId))
                .fetch();
    }
}
