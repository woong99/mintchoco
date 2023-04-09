package com.woong.mintchoco.owner.menu.service;

import com.woong.mintchoco.owner.menu.entity.Menu;
import com.woong.mintchoco.owner.menu.entity.MenuGroup;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroup;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroupMenu;
import com.woong.mintchoco.owner.menu.model.MenuVO;
import com.woong.mintchoco.owner.menu.repository.MenuRepository;
import com.woong.mintchoco.owner.menu.repository.group.MenuGroupRepository;
import com.woong.mintchoco.owner.menu.repository.option.group.MenuOptionGroupRepository;
import com.woong.mintchoco.owner.menu.repository.option.group.menu.MenuOptionGroupMenuRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private final MenuGroupRepository menuGroupRepository;

    private final MenuOptionGroupRepository menuOptionGroupRepository;

    private final MenuOptionGroupMenuRepository menuOptionGroupMenuRepository;

    private final EntityManager em;


    /**
     * 메뉴를 삭제한다.
     *
     * @param menuId 메뉴 ID
     */
    public void deleteMenu(Long menuId) {
        menuRepository.deleteMenu(menuId);
    }


    /**
     * 메뉴를 저장한다.
     *
     * @param menuGroupId 메뉴 그룹 ID
     * @param menuVO      메뉴 정보
     */
    public void insertMenu(Long menuGroupId, MenuVO menuVO) {
        MenuGroup menuGroup = menuGroupRepository.getReferenceById(menuGroupId);
        Menu menu = Menu.toMenu(menuVO);
        menu.setMenuGroup(menuGroup);

        int lastMenuOrder = menuRepository.findLastMenuOrder(menuGroupId);
        menu.setMenuOrder(lastMenuOrder + 1);

        menuRepository.save(menu);
    }


    /**
     * 메뉴를 조회한다.
     *
     * @param menuId 메뉴 ID
     * @return 메뉴 정보
     */
    public MenuVO selectMenu(Long menuId) {
        return MenuVO.toMenuVO(menuRepository.findById(menuId).orElseThrow(NoSuchElementException::new));
    }


    /**
     * 메뉴를 업데이트한다.
     *
     * @param menuVO 메뉴 정보
     */
    public void updateMenu(MenuVO menuVO) {
        menuRepository.updateMenu(menuVO);
    }


    /**
     * 메뉴 그룹 ID로 메뉴들을 조회한다.
     *
     * @param menuGroupId 메뉴 그룹 ID
     * @return 메뉴들
     */
    public List<MenuVO> selectMenus(Long menuGroupId) {
        return menuRepository.findByMenuGroupIdOrderByMenuOrder(menuGroupId).stream().map(MenuVO::toMenuVO).toList();
    }


    /**
     * 메뉴들의 순서를 업데이트한다.
     *
     * @param menuIdList 메뉴 ID 리스트
     */
    public void updateMenusOrder(Long[] menuIdList) {
        StringBuilder query = new StringBuilder("update Menu m set m.menuOrder=case ");
        for (int i = 0; i < menuIdList.length; i++) {
            query.append("when m.id = :id").append(i).append(" then :order").append(i).append(" ");
        }
        query.append("end, m.updatedAt= :now where m.id in(:ids)");
        Query updateQuery = em.createQuery(query.toString());

        for (int i = 0; i < menuIdList.length; i++) {
            updateQuery.setParameter("id" + i, menuIdList[i]);
            updateQuery.setParameter("order" + i, i + 1);
            updateQuery.setParameter("now", LocalDateTime.now());
        }
        updateQuery.setParameter("ids", Arrays.asList(menuIdList));
        updateQuery.executeUpdate();
    }


    /**
     * 메뉴와 연결된 메뉴 옵션 그룹들을 저장한다.
     *
     * @param optionGroupIdList 메뉴 옵션 그룹 ID 리스트
     * @param menuId            메뉴 ID
     */
    @Transactional
    public void insertMenuOptionGroupConnectMenu(Long[] optionGroupIdList, Long menuId) {
        menuOptionGroupMenuRepository.deleteMenuOptionGroupMenu(menuId);
        Menu menu = menuRepository.getReferenceById(menuId);

        List<MenuOptionGroupMenu> menuOptionGroupMenus = new ArrayList<>();
        for (int i = 0; i < optionGroupIdList.length; i++) {
            MenuOptionGroup menuOptionGroup = menuOptionGroupRepository.getReferenceById(optionGroupIdList[i]);
            MenuOptionGroupMenu menuOptionGroupMenu = new MenuOptionGroupMenu();
            menuOptionGroupMenu.setMenuOptionGroup(menuOptionGroup);
            menuOptionGroupMenu.setMenu(menu);
            menuOptionGroupMenu.setMenuOptionGroupOrder(i + 1);
            menuOptionGroupMenus.add(menuOptionGroupMenu);
        }
        menuOptionGroupMenuRepository.saveAllMenuOptionGroupMenu(menuOptionGroupMenus);
    }

}
