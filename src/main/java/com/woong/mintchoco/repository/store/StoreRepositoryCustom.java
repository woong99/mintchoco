package com.woong.mintchoco.repository.store;

import com.woong.mintchoco.vo.StoreVO;

public interface StoreRepositoryCustom {

    void updateStore(Long id, StoreVO storeVO);
}
