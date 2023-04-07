package com.woong.mintchoco.repository.menu;

import com.woong.mintchoco.domain.Menu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuRepositoryCustom {

    List<Menu> findByMenuGroupId(Long menuGroupId);
}
