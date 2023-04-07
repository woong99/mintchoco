package com.woong.mintchoco.owner.menu.repository.option.group;

import com.woong.mintchoco.owner.menu.entity.MenuOptionGroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuOptionGroupRepository extends JpaRepository<MenuOptionGroup, Long>,
        MenuOptionGroupRepositoryCustom {

    List<MenuOptionGroup> findAllByStoreId(Long storeId);
}
