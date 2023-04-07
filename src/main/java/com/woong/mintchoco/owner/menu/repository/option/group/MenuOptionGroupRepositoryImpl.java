package com.woong.mintchoco.owner.menu.repository.option.group;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroup;
import com.woong.mintchoco.owner.menu.entity.QMenuOption;
import com.woong.mintchoco.owner.menu.entity.QMenuOptionGroup;
import groovy.util.logging.Slf4j;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Slf4j
@RequiredArgsConstructor
public class MenuOptionGroupRepositoryImpl implements MenuOptionGroupRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QMenuOptionGroup qMenuOptionGroup = QMenuOptionGroup.menuOptionGroup;

    QMenuOption qMenuOption = QMenuOption.menuOption;


    /**
     * MenuOptionGroup과 MenuOption을 조인하여 조회
     *
     * @param storeId storeID
     * @return MenuOptionGroup 와 이와 연관된 MenuOption
     */
    @Override
    public List<MenuOptionGroup> selectAllMenuOptionGroupWithMenuOptionOrderByMenuOrder(Long storeId) {
        return jpaQueryFactory
                .selectFrom(qMenuOptionGroup)
                .leftJoin(qMenuOptionGroup.menuOptions, qMenuOption).fetchJoin()
                .where(qMenuOptionGroup.store.id.eq(storeId))
                .orderBy(qMenuOption.menuOrder.asc())
                .fetch();
    }


    /**
     * MenuOptionGroup과 MenuOption을 조인하여 단일 조회
     *
     * @param menuOptionGroupId 메뉴 옵션 그룹 ID
     * @return MenuOptionGroup 과 이와 연관된 MenuOption
     */
    @Override
    public MenuOptionGroup selectMenuOptionGroupWithMenuOptionOrderByMenuOrder(Long menuOptionGroupId) {
        return jpaQueryFactory
                .selectFrom(qMenuOptionGroup)
                .leftJoin(qMenuOptionGroup.menuOptions, qMenuOption).fetchJoin()
                .where(qMenuOptionGroup.id.eq(menuOptionGroupId))
                .orderBy(qMenuOption.menuOrder.asc())
                .fetchOne();
    }
}
