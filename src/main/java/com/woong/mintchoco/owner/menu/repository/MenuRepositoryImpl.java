package com.woong.mintchoco.owner.menu.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woong.mintchoco.owner.menu.entity.Menu;
import com.woong.mintchoco.owner.menu.entity.QMenu;
import com.woong.mintchoco.owner.menu.entity.QMenuOptionGroupMenu;
import com.woong.mintchoco.owner.menu.model.MenuVO;
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
     *
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
     *
     * @param menuVO 메뉴 정보
     */
    @Transactional
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
     * 메뉴와 연관된 메뉴 옵션 그룹을 조인해서 조회한다.
     *
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


    /**
     * 메뉴와 그와 연결된 메뉴 옵션 그룹 정보를 삭제한다.
     *
     * @param menuId 메뉴 ID
     */
    @Transactional
    @Override
    public void deleteMenu(Long menuId) {
        jpaQueryFactory
                .delete(qMenuOptionGroupMenu)
                .where(qMenuOptionGroupMenu.menu.id.eq(menuId))
                .execute();

        jpaQueryFactory
                .delete(qMenu)
                .where(qMenu.id.eq(menuId))
                .execute();
    }
}
