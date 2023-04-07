package com.woong.mintchoco.owner.menu.repository.option;

import com.woong.mintchoco.owner.menu.entity.MenuOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuOptionRepository extends JpaRepository<MenuOption, Long> {

    void deleteByMenuOptionGroupId(Long menuGroupId);
}
