package com.woong.mintchoco.owner.menu.service;

import com.woong.mintchoco.global.common.ErrorCode;
import com.woong.mintchoco.global.file.entity.AttachFile;
import com.woong.mintchoco.global.file.exception.UploadFileErrorException;
import com.woong.mintchoco.global.file.exception.UploadFileNotFoundException;
import com.woong.mintchoco.global.file.service.FileManageService;
import com.woong.mintchoco.owner.menu.entity.Menu;
import com.woong.mintchoco.owner.menu.entity.MenuGroup;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroup;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroupMenu;
import com.woong.mintchoco.owner.menu.exception.MenuNotFoundException;
import com.woong.mintchoco.owner.menu.model.MenuOptionGroupVO;
import com.woong.mintchoco.owner.menu.model.MenuVO;
import com.woong.mintchoco.owner.menu.repository.MenuRepository;
import com.woong.mintchoco.owner.menu.repository.group.MenuGroupRepository;
import com.woong.mintchoco.owner.menu.repository.option.group.MenuOptionGroupRepository;
import com.woong.mintchoco.owner.menu.repository.option.group.menu.MenuOptionGroupMenuRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {

    private final MenuRepository menuRepository;

    private final MenuGroupRepository menuGroupRepository;

    private final MenuOptionGroupRepository menuOptionGroupRepository;

    private final MenuOptionGroupMenuRepository menuOptionGroupMenuRepository;

    private final EntityManager em;

    private final FileManageService fileManageService;


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
        return MenuVO.toMenuVO(
                menuRepository.findById(menuId)
                        .orElseThrow(() -> new MenuNotFoundException(ErrorCode.MENU_NOT_FOUND)));
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
    @Transactional
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


    /**
     * 메뉴 및 메뉴와 연결된 메뉴 옵션들을 조회
     *
     * @param menuId 메뉴 ID
     * @return 메뉴 및 메뉴와 연결된 메뉴 옵션들
     */
    public MenuVO selectMenuWithMenuOptions(Long menuId) {
        MenuVO menuVO = MenuVO.toMenuVOWithMenuImage(menuRepository.selectMenuWithMenuImage(menuId));
        List<MenuOptionGroupVO> menuOptionGroupVOs = menuOptionGroupRepository.selectMenuOptionGroupWithMenuOptions(
                menuId).stream().map(MenuOptionGroupVO::toMenuOptionGroupVOWithMenuOption).toList();
        menuVO.setMenuOptionGroupVOS(menuOptionGroupVOs);

        return menuVO;
    }


    /**
     * 메뉴 및 메뉴와 연결된 메뉴 이미지 조회
     *
     * @param menuId 메뉴 ID
     * @return 메뉴 및 메뉴와 연결된 메뉴 이미지 정보
     */
    public MenuVO selectMenuWithMenuImage(Long menuId) {
        Menu menu = menuRepository.selectMenuWithMenuImage(menuId);

        return MenuVO.toMenuVOWithMenuImage(menu);
    }


    /**
     * 메뉴 이미지를 저장한다.
     *
     * @param menuId 메뉴 ID
     * @param file   메뉴 이미지
     * @throws IOException
     */
    @Transactional
    public void insertMenuImage(Long menuId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new UploadFileNotFoundException(ErrorCode.UPLOAD_FILE_NOT_FOUND);
        }

        AttachFile attachFile = fileManageService.saveFile(file);
        if (attachFile == null) {
            throw new UploadFileErrorException(ErrorCode.UPLOAD_FILE_ERROR);
        }
        menuRepository.insertMenuImage(attachFile, menuId);
    }


    /**
     * 메뉴 이미지를 삭제한다.
     *
     * @param menuId 메뉴 ID
     */
    public void deleteMenuImage(Long menuId) {
        menuRepository.deleteMenuImage(menuId);
    }
}
