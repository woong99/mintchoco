package com.woong.mintchoco.repository.menu.option.group.menu;

import com.woong.mintchoco.domain.MenuOptionGroupMenu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuOptionGroupMenuRepository extends JpaRepository<MenuOptionGroupMenu, Long> {

    void deleteByMenuId(Long menuId);

    List<MenuOptionGroupMenu> findByMenuIdOrderByMenuId(Long menuId);
}
