package com.woong.mintchoco.owner.store.repository;

import com.woong.mintchoco.owner.store.model.StoreVO;

public interface StoreRepositoryCustom {

    void updateStore(Long id, StoreVO storeVO);
}
