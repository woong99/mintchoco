package com.woong.mintchoco.owner.store.service;

import com.woong.mintchoco.global.auth.entity.User;
import com.woong.mintchoco.global.auth.repository.UserRepository;
import com.woong.mintchoco.owner.store.entity.Store;
import com.woong.mintchoco.owner.store.model.StoreVO;
import com.woong.mintchoco.owner.store.repository.StoreRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    private final UserRepository userRepository;

    /**
     * 가게 정보를 등록한다.
     *
     * @param user    인증 정보
     * @param storeVO 가게 정보
     */
    @Transactional
    public void insertStoreInfo(User user, StoreVO storeVO) {
        Store store = storeRepository.save(Store.toEntity(storeVO));
        // TODO : save Dirty Checking 확인
        user.setStore(store);
        userRepository.save(user);
    }


    /**
     * 가게 정보를 조회한다.
     *
     * @param user 인증 정보
     * @return 가게 정보
     */
    @Transactional
    public StoreVO getStoreInfo(User user) {
        if (user.getStore() == null) {
            return null;
        }
        Store store = storeRepository.findById(user.getStore().getId()).orElseThrow(NoSuchElementException::new);
        return StoreVO.toStoreVO(store);
    }


    /**
     * 가게 정보를 수정한다.
     *
     * @param user    인증 정보
     * @param storeVO 가게 정보
     */
    public void updateStoreInfo(User user, StoreVO storeVO) {
        checkIsOpen(storeVO);
        if (user.getStore() != null) {
            storeRepository.updateStore(user.getStore().getId(), storeVO);
        }
    }

    /**
     * 영업 유무에 따른 정보를 변경하는 메소드
     *
     * @param storeVO 가게 정보
     */
    private void checkIsOpen(StoreVO storeVO) {
        if (storeVO.getSaturdayIsOpen() == null) {
            storeVO.setSaturdayIsOpen("N");
            storeVO.setSaturdayStart(null);
            storeVO.setSaturdayEnd(null);
        }
        if (storeVO.getSundayIsOpen() == null) {
            storeVO.setSundayIsOpen("N");
            storeVO.setSundayStart(null);
            storeVO.setSundayEnd(null);
        }
        if (storeVO.getSaturdayIsOpen() == null) {
            storeVO.setSaturdayIsOpen("N");
            storeVO.setSaturdayStart(null);
            storeVO.setSaturdayEnd(null);
        }
    }
}
