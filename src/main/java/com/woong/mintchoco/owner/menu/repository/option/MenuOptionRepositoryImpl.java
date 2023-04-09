package com.woong.mintchoco.owner.menu.repository.option;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woong.mintchoco.owner.menu.entity.MenuOption;
import com.woong.mintchoco.owner.menu.entity.QMenuOption;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class MenuOptionRepositoryImpl implements MenuOptionRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    private final JdbcTemplate jdbcTemplate;

    QMenuOption qMenuOption = QMenuOption.menuOption;


    /**
     * 메뉴 옵션들을 저장한다.
     *
     * @param menuOptions 메뉴 옵션 정보들
     */
    @Override
    public void insertMenuOption(List<MenuOption> menuOptions) {
        String sql =
                "INSERT INTO menu_option (price, title, menu_option_group_id, created_at, menu_order) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        MenuOption menuOption = menuOptions.get(i);
                        ps.setInt(1, menuOption.getPrice());
                        ps.setString(2, menuOption.getTitle());
                        ps.setLong(3, menuOption.getMenuOptionGroup().getId());
                        ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                        ps.setInt(5, menuOption.getMenuOrder());
                    }

                    @Override
                    public int getBatchSize() {
                        return menuOptions.size();
                    }
                });
    }


    /**
     * 메뉴 옵션을 삭제한다.
     *
     * @param menuOptionGroupId 메뉴 옵션 그룹 ID
     */
    @Override
    public void deleteMenuOption(Long menuOptionGroupId) {
        jpaQueryFactory
                .delete(qMenuOption)
                .where(qMenuOption.menuOptionGroup.id.eq(menuOptionGroupId))
                .execute();
    }
}
