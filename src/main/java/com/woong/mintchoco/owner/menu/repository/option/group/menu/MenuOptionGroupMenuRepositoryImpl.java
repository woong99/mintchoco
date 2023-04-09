package com.woong.mintchoco.owner.menu.repository.option.group.menu;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroupMenu;
import com.woong.mintchoco.owner.menu.entity.QMenuOptionGroupMenu;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class MenuOptionGroupMenuRepositoryImpl implements MenuOptionGroupMenuRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    private final JdbcTemplate jdbcTemplate;

    QMenuOptionGroupMenu qMenuOptionGroupMenu = QMenuOptionGroupMenu.menuOptionGroupMenu;


    /**
     * 메뉴와 연결된 메뉴 옵션 그룹을 삭제한다.
     *
     * @param menuId 메뉴 ID
     */
    @Override
    public void deleteMenuOptionGroupMenu(Long menuId) {
        jpaQueryFactory
                .delete(qMenuOptionGroupMenu)
                .where(qMenuOptionGroupMenu.menu.id.eq(menuId))
                .execute();
    }


    /**
     * 메뉴와 연결된 메뉴 옵션 그룹들을 저장한다.
     *
     * @param menuOptionGroupMenus 메뉴와 연결된 메뉴 옵션 그룹들
     */
    @Override
    public void saveAllMenuOptionGroupMenu(List<MenuOptionGroupMenu> menuOptionGroupMenus) {
        String sql =
                "INSERT INTO menu_option_group_menu (menu_option_group_order, menu_id, menu_option_group_id, created_at) VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        MenuOptionGroupMenu menuOptionGroupMenu = menuOptionGroupMenus.get(i);
                        ps.setInt(1, menuOptionGroupMenu.getMenuOptionGroupOrder());
                        ps.setLong(2, menuOptionGroupMenu.getMenu().getId());
                        ps.setLong(3, menuOptionGroupMenu.getMenuOptionGroup().getId());
                        ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                    }

                    @Override
                    public int getBatchSize() {
                        return menuOptionGroupMenus.size();
                    }
                });
    }
}
