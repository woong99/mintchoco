package com.woong.mintchoco.owner.store.repository;

import com.woong.mintchoco.owner.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {

}
