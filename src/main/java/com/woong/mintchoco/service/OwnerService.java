package com.woong.mintchoco.service;

import com.woong.mintchoco.domain.AttachFile;
import com.woong.mintchoco.domain.MenuGroup;
import com.woong.mintchoco.domain.Store;
import com.woong.mintchoco.domain.User;
import com.woong.mintchoco.repository.FileRepository;
import com.woong.mintchoco.repository.menu.MenuGroupRepository;
import com.woong.mintchoco.repository.store.StoreRepository;
import com.woong.mintchoco.repository.user.UserRepository;
import com.woong.mintchoco.vo.MenuGroupVO;
import com.woong.mintchoco.vo.StoreVO;
import com.woong.mintchoco.vo.UserVO;
import java.util.List;
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

    private final MenuGroupRepository menuGroupRepository;

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
     * 메뉴그룹을 추가한다.
     *
     * @param user        인증 정보
     * @param menuGroupVO 메뉴그룹 정보
     */
    public void insertMenuGroup(User user, MenuGroupVO menuGroupVO) {
        Store store = user.getStore();
        int lastGroupOrder = menuGroupRepository.findLastGroupOrder(store.getId());
        menuGroupVO.setMenuGroupOrder(lastGroupOrder + 1);
        menuGroupVO.setStore(store);
        menuGroupRepository.save(MenuGroup.toMenuGroup(menuGroupVO));
    }


    /**
     * 모든 메뉴그룹을 조회한다.
     *
     * @param user 인증 정보
     * @return 메뉴그룹 리스트(List<MenuGroupVO>)
     */
    public List<MenuGroupVO> selectAllMenuGroup(User user) {
        Long storeId = user.getStore().getId();
        return menuGroupRepository.findAllByStoreIdOrderByGroupOrder(storeId).stream().map(MenuGroupVO::toMenuGroupVO)
                .toList();
    }


    /**
     * 단일 메뉴그룹을 조회한다.
     *
     * @param id 메뉴그룹 ID
     * @return 단일 메뉴그룹(MenuGroupVO)
     */
    public MenuGroupVO selectMenuGroup(Long id) {
        return MenuGroupVO.toMenuGroupVO(menuGroupRepository.findById(id).orElseThrow(NoSuchElementException::new));
    }


    /**
     * 메뉴그룹을 수정한다.
     *
     * @param menuGroupVO 메뉴그룹 정보
     */
    public void updateMenuGroup(MenuGroupVO menuGroupVO) {
        menuGroupRepository.updateMenuGroup(menuGroupVO);
    }


    /**
     * 메뉴그룹의 순서를 수정한다.
     *
     * @param menuGroupIdList 메뉴그룹 ID 리스트
     */
    public void updateMenuGroupOrder(Long[] menuGroupIdList) {
        menuGroupRepository.updateMenuGroupOrder(menuGroupIdList);
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
