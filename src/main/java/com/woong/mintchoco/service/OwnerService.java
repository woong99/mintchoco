package com.woong.mintchoco.service;

import com.woong.mintchoco.domain.Store;
import com.woong.mintchoco.domain.User;
import com.woong.mintchoco.repository.store.StoreRepository;
import com.woong.mintchoco.repository.user.UserRepository;
import com.woong.mintchoco.vo.StoreVO;
import com.woong.mintchoco.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OwnerService {

    private final UserRepository userRepository;

    private final StoreRepository storeRepository;

    /**
     * 사용자의 아이디로 사용자의 정보를 조회환다.
     *
     * @param userId 사용자 아이디
     * @return 사용자 정보
     */
    public UserVO getUserInfo(String userId) {
        return UserVO.toUserVO(userRepository.findByUserId(userId));
    }

    /**
     * 사용자 정보를 업데이트한다.
     *
     * @param userVO 사용자 정보
     */
    public void updateUserInfo(UserVO userVO) {
        userRepository.updateUserInfo(userVO);
    }

    /**
     * 사용자의 프로필 사진을 삭제한다.
     *
     * @param userVO 사용자 정보
     */
    public void deleteUserProfileImage(UserVO userVO) {
        userRepository.deleteUserProfileImage(userVO);
    }


    /**
     * 가게 정보를 등록한다.
     *
     * @param authentication 인증 정보
     * @param storeVO        가게 정보
     */
    @Transactional
    public void insertStoreInfo(Authentication authentication, StoreVO storeVO) {
        Store store = storeRepository.save(Store.toEntity(storeVO));
        User user = userRepository.findByUserId(authentication.getName());
        user.setStore(store);
        userRepository.save(user);
    }


    /**
     * 가게 정보를 조회한다.
     *
     * @param authentication 인증 정보
     * @return 가게 정보
     */
    public StoreVO getStoreInfo(Authentication authentication) {
        User user = userRepository.findByUserId(authentication.getName());
        if (user.getStore() == null) {
            return null;
        }
        return StoreVO.toStoreVO(user.getStore());
    }


    /**
     * 가게 정보를 수정한다.
     *
     * @param authentication 인증 정보
     * @param storeVO        가게 정보
     */
    public void updateStoreInfo(Authentication authentication, StoreVO storeVO) {
        User user = userRepository.findByUserId(authentication.getName());
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
