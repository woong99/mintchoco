package com.woong.mintchoco.repository.menu.option;

import com.woong.mintchoco.domain.MenuOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuOptionRepository extends JpaRepository<MenuOption, Long> {

    void deleteByMenuOptionGroupId(Long menuGroupId);
}
