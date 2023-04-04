package com.woong.mintchoco.repository.menu.group;

import com.woong.mintchoco.domain.MenuGroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long>, MenuGroupRepositoryCustom {

    List<MenuGroup> findAllByStoreIdOrderByGroupOrder(Long storeId);
}
