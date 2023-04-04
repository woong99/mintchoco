package com.woong.mintchoco.repository.menu.option.group;

import com.woong.mintchoco.domain.MenuOptionGroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuOptionGroupRepository extends JpaRepository<MenuOptionGroup, Long>,
        MenuOptionGroupRepositoryCustom {

    List<MenuOptionGroup> findAllByStoreId(Long storeId);
}
