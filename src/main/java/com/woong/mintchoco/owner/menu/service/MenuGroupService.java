package com.woong.mintchoco.owner.menu.service;

import com.woong.mintchoco.global.auth.entity.User;
import com.woong.mintchoco.owner.menu.entity.MenuGroup;
import com.woong.mintchoco.owner.menu.model.MenuGroupVO;
import com.woong.mintchoco.owner.menu.repository.group.MenuGroupRepository;
import com.woong.mintchoco.owner.store.entity.Store;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;


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

        return menuGroupRepository.selectAllMenuGroupWithMenu(storeId).stream().map(MenuGroupVO::toMenuGroupVO)
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

    public void deleteMenuGroup(Long menuGroupId) {
        menuGroupRepository.deleteById(menuGroupId);
    }
}
