package com.woong.mintchoco.owner.menu.repository;

import com.woong.mintchoco.owner.menu.entity.Menu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuRepositoryCustom {

    List<Menu> findByMenuGroupIdOrderByMenuOrder(Long menuGroupId);
}
