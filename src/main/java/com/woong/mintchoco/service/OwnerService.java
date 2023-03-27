package com.woong.mintchoco.service;

import com.woong.mintchoco.domain.AttachFile;
import com.woong.mintchoco.domain.Store;
import com.woong.mintchoco.domain.User;
import com.woong.mintchoco.repository.FileRepository;
import com.woong.mintchoco.repository.store.StoreRepository;
import com.woong.mintchoco.repository.user.UserRepository;
import com.woong.mintchoco.vo.StoreVO;
import com.woong.mintchoco.vo.UserVO;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OwnerService {

    private final UserRepository userRepository;

    private final FileRepository fileRepository;

    private final StoreRepository storeRepository;

    /**
     * 사용자의 아이디로 사용자의 정보를 조회환다.
     *
     * @param user 인증 정보
     * @return 사용자 정보
     */
    public UserVO getUserInfo(User user) {
        AttachFile attachFile = fileRepository.findById(user.getProfileImage().getId())
                .orElseThrow(NoSuchElementException::new);
        user.setProfileImage(attachFile);
        return UserVO.toUserVO(user);
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
