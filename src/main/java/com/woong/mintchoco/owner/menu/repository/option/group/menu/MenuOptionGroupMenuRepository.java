package com.woong.mintchoco.owner.menu.repository.option.group.menu;

import com.woong.mintchoco.owner.menu.entity.MenuOptionGroupMenu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuOptionGroupMenuRepository extends JpaRepository<MenuOptionGroupMenu, Long>,
        MenuOptionGroupMenuRepositoryCustom {

    List<MenuOptionGroupMenu> findByMenuIdOrderByMenuId(Long menuId);
}
