package com.woong.mintchoco.owner.menu.repository.option.group;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroup;
import com.woong.mintchoco.owner.menu.entity.QMenuOption;
import com.woong.mintchoco.owner.menu.entity.QMenuOptionGroup;
import com.woong.mintchoco.owner.menu.entity.QMenuOptionGroupMenu;
import com.woong.mintchoco.owner.menu.model.MenuOptionGroupVO;
import groovy.util.logging.Slf4j;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class MenuOptionGroupRepositoryImpl implements MenuOptionGroupRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QMenuOptionGroup qMenuOptionGroup = QMenuOptionGroup.menuOptionGroup;

    QMenuOption qMenuOption = QMenuOption.menuOption;

    QMenuOptionGroupMenu qMenuOptionGroupMenu = QMenuOptionGroupMenu.menuOptionGroupMenu;


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


    /**
     * 메뉴 옵션 그룹을 업데이트한다.
     *
     * @param menuOptionGroupVO 메뉴 옵션 그룹 정보
     */
    @Override
    public void updateMenuOptionGroup(MenuOptionGroupVO menuOptionGroupVO) {
        jpaQueryFactory
                .update(qMenuOptionGroup)
                .set(qMenuOptionGroup.title, menuOptionGroupVO.getMenuOptionGroupTitle())
                .set(qMenuOptionGroup.required, menuOptionGroupVO.getMenuOptionGroupRequired())
                .set(qMenuOptionGroup.maxSelect, menuOptionGroupVO.getMenuOptionGroupMaxSelect())
                .set(qMenuOptionGroup.updatedAt, LocalDateTime.now())
                .where(qMenuOptionGroup.id.eq(menuOptionGroupVO.getMenuOptionGroupId()))
                .execute();
    }


    /**
     * 메뉴 옵션 그룹과 그와 연결된 옵션 그룹, 메뉴 옵션을 삭제한다.
     *
     * @param menuOptionGroupId 메뉴 옵션 그룹 ID
     */
    @Transactional
    @Override
    public void deleteMenuOptionGroup(Long menuOptionGroupId) {
        jpaQueryFactory
                .delete(qMenuOptionGroupMenu)
                .where(qMenuOptionGroupMenu.menuOptionGroup.id.eq(menuOptionGroupId))
                .execute();

        jpaQueryFactory
                .delete(qMenuOption)
                .where(qMenuOption.menuOptionGroup.id.eq(menuOptionGroupId))
                .execute();

        jpaQueryFactory
                .delete(qMenuOptionGroup)
                .where(qMenuOptionGroup.id.eq(menuOptionGroupId))
                .execute();
    }
}
