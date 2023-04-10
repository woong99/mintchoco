package com.woong.mintchoco.owner.menu.repository.group;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woong.mintchoco.global.file.entity.QAttachFile;
import com.woong.mintchoco.owner.menu.entity.MenuGroup;
import com.woong.mintchoco.owner.menu.entity.QMenu;
import com.woong.mintchoco.owner.menu.entity.QMenuGroup;
import com.woong.mintchoco.owner.menu.entity.QMenuOptionGroupMenu;
import com.woong.mintchoco.owner.menu.model.MenuGroupVO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MenuGroupRepositoryImpl implements MenuGroupRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QMenuGroup qMenuGroup = QMenuGroup.menuGroup;

    QMenu qMenu = QMenu.menu;

    QMenuOptionGroupMenu qMenuOptionGroupMenu = QMenuOptionGroupMenu.menuOptionGroupMenu;

    QAttachFile qAttachFile = QAttachFile.attachFile;


    /**
     * 마지막 groupOrder를 찾아 반환한다.
     *
     * @param storeId storeId
     * @return groupOrder
     */
    @Override
    public int findLastGroupOrder(Long storeId) {
        return Optional.ofNullable(jpaQueryFactory
                        .select(qMenuGroup.groupOrder.max())
                        .from(qMenuGroup)
                        .where(qMenuGroup.store.id.eq(storeId))
                        .fetchOne())
                .orElse(0);
    }


    /**
     * 메뉴그룹을 업데이트한다.
     *
     * @param menuGroupVO 메뉴그룹 정보
     */
    @Override
    public void updateMenuGroup(MenuGroupVO menuGroupVO) {
        jpaQueryFactory
                .update(qMenuGroup)
                .set(List.of(qMenuGroup.title, qMenuGroup.explanation, qMenuGroup.exposure, qMenuGroup.updatedAt),
                        List.of(menuGroupVO.getMenuGroupTitle(), menuGroupVO.getMenuGroupExplanation(),
                                menuGroupVO.getMenuGroupExposure(),
                                LocalDateTime.now()))
                .where(qMenuGroup.id.eq(menuGroupVO.getId()))
                .execute();
    }


    /**
     * 메뉴와 메뉴그룹을 조인해 조회한다.
     *
     * @param storeId 스토어 ID
     * @return 메뉴그룹 및 연관된 메뉴
     */
    @Override
    public List<MenuGroup> selectAllMenuGroupWithMenu(Long storeId) {
        return jpaQueryFactory
                .selectFrom(qMenuGroup)
                .leftJoin(qMenuGroup.menus, qMenu).fetchJoin()
                .where(qMenuGroup.store.id.eq(storeId))
                .orderBy(qMenuGroup.groupOrder.asc(), qMenu.menuOrder.asc())
                .fetch();
    }


    /**
     * 메뉴그룹과 메뉴그룹과 연괸된 메뉴, 메뉴와 연결된 메뉴 옵션 그룹을 삭제한다.
     *
     * @param menuGroupId 메뉴그룹 ID
     */
    @Transactional
    @Override
    public void deleteMenuGroup(Long menuGroupId) {
        jpaQueryFactory
                .delete(qMenuOptionGroupMenu)
                .where(qMenuOptionGroupMenu.menu.id.in(
                        jpaQueryFactory
                                .select(qMenu.id)
                                .from(qMenu)
                                .where(qMenuGroup.id.eq(menuGroupId)).fetch()))
                .execute();

        List<Long> attachId = jpaQueryFactory
                .select(qMenu.menuImage.id)
                .from(qMenu)
                .where(qMenu.menuGroup.id.eq(menuGroupId))
                .fetch();

        jpaQueryFactory
                .delete(qMenu)
                .where(qMenu.menuGroup.id.eq(menuGroupId))
                .execute();

        jpaQueryFactory
                .delete(qAttachFile)
                .where(qAttachFile.id.in(attachId))
                .execute();

        jpaQueryFactory
                .delete(qMenuGroup)
                .where(qMenuGroup.id.eq(menuGroupId))
                .execute();
    }


    /**
     * 메뉴그룹과 연관된 메뉴들을 조회한다.
     *
     * @param menuGroupId 메뉴 그룹 ID
     * @return 메뉴그룹 및 연관된 메뉴들
     */
    @Override
    public MenuGroup selectMenuGroupWithMenus(Long menuGroupId) {
        return jpaQueryFactory
                .select(qMenuGroup)
                .from(qMenuGroup)
                .leftJoin(qMenuGroup.menus, qMenu).fetchJoin()
                .leftJoin(qMenu.menuImage, qAttachFile).fetchJoin()
                .where(qMenuGroup.id.eq(menuGroupId))
                .orderBy(qMenu.menuOrder.asc())
                .fetchOne();

    }
}

