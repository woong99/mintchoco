package com.woong.mintchoco.owner.menu.service;

import com.woong.mintchoco.global.auth.entity.User;
import com.woong.mintchoco.global.common.ErrorCode;
import com.woong.mintchoco.owner.menu.entity.MenuGroup;
import com.woong.mintchoco.owner.menu.exception.MenuGroupNotFoundException;
import com.woong.mintchoco.owner.menu.model.MenuGroupVO;
import com.woong.mintchoco.owner.menu.repository.group.MenuGroupRepository;
import com.woong.mintchoco.owner.store.entity.Store;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    private final EntityManager em;


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

        return menuGroupRepository.selectAllMenuGroupWithMenu(storeId).stream()
                .map(MenuGroupVO::toMenuGroupVOWithMenuVO)
                .toList();
    }


    /**
     * 단일 메뉴그룹을 조회한다.
     *
     * @param id 메뉴그룹 ID
     * @return 단일 메뉴그룹(MenuGroupVO)
     */
    public MenuGroupVO selectMenuGroup(Long id) {
        return MenuGroupVO.toMenuGroupVO(
                menuGroupRepository.findById(id).orElseThrow(() -> new MenuGroupNotFoundException(
                        ErrorCode.MENU_GROUP_NOT_FOUND)));
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
    @Transactional
    public void updateMenuGroupOrder(Long[] menuGroupIdList) {
        StringBuilder query = new StringBuilder("update MenuGroup mg set mg.groupOrder=case ");
        for (int i = 0; i < menuGroupIdList.length; i++) {
            query.append("when mg.id = :id").append(i).append(" then :order").append(i).append(" ");
        }
        query.append("end, mg.updatedAt= :now where mg.id in(:ids)");
        Query updateQuery = em.createQuery(query.toString());

        for (int i = 0; i < menuGroupIdList.length; i++) {
            updateQuery.setParameter("id" + i, menuGroupIdList[i]);
            updateQuery.setParameter("order" + i, i + 1);
            updateQuery.setParameter("now", LocalDateTime.now());
        }
        updateQuery.setParameter("ids", Arrays.asList(menuGroupIdList));
        updateQuery.executeUpdate();
    }


    /**
     * 메뉴 그룹을 삭제한다.
     *
     * @param menuGroupId 메뉴 그룹 ID
     */
    public void deleteMenuGroup(Long menuGroupId) {
        menuGroupRepository.deleteMenuGroup(menuGroupId);
    }
}
