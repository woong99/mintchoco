package com.woong.mintchoco.owner.menu.repository.group;

import com.woong.mintchoco.owner.menu.entity.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long>, MenuGroupRepositoryCustom {

}
