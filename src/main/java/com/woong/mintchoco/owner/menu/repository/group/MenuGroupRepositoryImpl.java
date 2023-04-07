package com.woong.mintchoco.owner.menu.repository.group;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woong.mintchoco.owner.menu.entity.MenuGroup;
import com.woong.mintchoco.owner.menu.entity.QMenu;
import com.woong.mintchoco.owner.menu.entity.QMenuGroup;
import com.woong.mintchoco.owner.menu.model.MenuGroupVO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MenuGroupRepositoryImpl implements MenuGroupRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QMenuGroup qMenuGroup = QMenuGroup.menuGroup;

    QMenu qMenu = QMenu.menu;


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
     * 메뉴그룹 순서를 업데이트한다.
     *
     * @param menuGroupIdList 메뉴그룹 ID 리스트
     */
    // TODO : 리팩토링 할 때 최적화하기
    @Override
    public void updateMenuGroupOrder(Long[] menuGroupIdList) {
        for (int i = 0; i < menuGroupIdList.length; i++) {
            jpaQueryFactory
                    .update(qMenuGroup)
                    .set(List.of(qMenuGroup.groupOrder, qMenuGroup.updatedAt),
                            List.of(i + 1, LocalDateTime.now()))
                    .where(qMenuGroup.id.eq(menuGroupIdList[i]))
                    .execute();
        }
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
}

