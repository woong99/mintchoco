package com.woong.mintchoco.owner.menu.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;

import com.woong.mintchoco.global.common.ErrorCode;
import com.woong.mintchoco.global.file.entity.AttachFile;
import com.woong.mintchoco.global.file.exception.UploadFileErrorException;
import com.woong.mintchoco.global.file.exception.UploadFileNotFoundException;
import com.woong.mintchoco.global.file.service.FileManageService;
import com.woong.mintchoco.owner.menu.entity.Menu;
import com.woong.mintchoco.owner.menu.entity.MenuGroup;
import com.woong.mintchoco.owner.menu.entity.MenuOption;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroup;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroupMenu;
import com.woong.mintchoco.owner.menu.exception.MenuNotFoundException;
import com.woong.mintchoco.owner.menu.model.MenuVO;
import com.woong.mintchoco.owner.menu.repository.MenuRepository;
import com.woong.mintchoco.owner.menu.repository.group.MenuGroupRepository;
import com.woong.mintchoco.owner.menu.repository.option.group.MenuOptionGroupRepository;
import com.woong.mintchoco.owner.menu.repository.option.group.menu.MenuOptionGroupMenuRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@DisplayName("[SERVICE 단위 테스트] menuService")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    FileManageService fileManageService;
    @InjectMocks
    private MenuService menuService;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private MenuOptionGroupRepository menuOptionGroupRepository;
    @Mock
    private MenuOptionGroupMenuRepository menuOptionGroupMenuRepository;
    @Mock
    private EntityManager em;


    @DisplayName("성공 - 메뉴 정상적으로 삭제(deleteMenu)")
    @Test
    void 메뉴_삭제() {
        // given
        Long menuId = 1L;

        // when
        menuService.deleteMenu(menuId);

        // then
        then(menuRepository).should().deleteMenu(menuId);
    }

    @DisplayName("성공 - 연관된 정보 없이 메뉴만 정상적으로 조회(selectMenu)")
    @Test
    void 메뉴_조회_성공() {
        // given
        Long menuId = 1L;
        Menu mockMenu = makeMenu(menuId);

        given(menuRepository.findById(menuId)).willReturn(Optional.of(mockMenu));

        // when
        MenuVO result = menuService.selectMenu(menuId);

        // then
        then(menuRepository).should().findById(menuId);
        assertThat(result.getMenuId()).isEqualTo(menuId);
        assertThat(result.getMenuTitle()).isEqualTo(mockMenu.getTitle());
        assertThat(result.getMenuPrice()).isEqualTo(mockMenu.getPrice());
        assertThat(result.getMenuOrder()).isEqualTo(mockMenu.getMenuOrder());
        assertThat(result.getMenuExplanation()).isEqualTo(mockMenu.getExplanation());
        assertThat(result.getMenuExposure()).isEqualTo(mockMenu.getExposure());
        assertThat(result.getMenuImage()).isNull();
        assertThat(result.getMenuOptionGroupVOS()).isNull();
        assertThat(result.getMenuGroupVO()).isNull();
    }

    @DisplayName("실패 - 잘못된 menuId가 주어지면 예외 발생(selectMenu)")
    @Test
    void 메뉴_조회_실패() {
        // given
        given(menuRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> menuService.selectMenu(1L))
                .isInstanceOf(MenuNotFoundException.class)
                .hasMessageContaining(ErrorCode.MENU_NOT_FOUND.Message());
    }

    @DisplayName("성공 - 메뉴 정상적으로 업데이트(updateMenu)")
    @Test
    void 메뉴_업데이트() {
        // given
        MenuVO menuVO = new MenuVO();
        menuVO.setMenuId(1L);
        willDoNothing().given(menuRepository).updateMenu(menuVO);

        // when
        menuService.updateMenu(menuVO);

        // then
        then(menuRepository).should().updateMenu(menuVO);
    }

    @DisplayName("성공 - 메뉴 정상적으로 저장(insertMenu)")
    @Test
    void 메뉴_저장() {
        // given
        Long menuGroupId = 1L;
        MenuVO menuVO = new MenuVO();
        menuVO.setMenuTitle("TEST");

        MenuGroup menuGroup = new MenuGroup();
        given(menuGroupRepository.getReferenceById(menuGroupId)).willReturn(menuGroup);

        int lastMenuOrder = 1;
        given(menuRepository.findLastMenuOrder(menuGroupId)).willReturn(lastMenuOrder);

        ArgumentCaptor<Menu> menuCaptor = ArgumentCaptor.forClass(Menu.class);
        given(menuRepository.save(menuCaptor.capture())).willReturn(new Menu());

        // when
        menuService.insertMenu(menuGroupId, menuVO);

        // then
        then(menuGroupRepository).should().getReferenceById(menuGroupId);
        then(menuRepository).should().findLastMenuOrder(menuGroupId);
        then(menuRepository).should().save(any(Menu.class));

        Menu savedMenu = menuCaptor.getValue();
        assertThat(savedMenu).isNotNull();
        assertThat(savedMenu.getTitle()).isEqualTo(menuVO.getMenuTitle());
        assertThat(savedMenu.getMenuGroup()).isEqualTo(menuGroup);
        assertThat(savedMenu.getMenuOrder()).isEqualTo(lastMenuOrder + 1);
    }

    @DisplayName("성공 - 메뉴들을 조회 시 연관된 다른 정보 없이 menuOrder 순으로 정상적으로 조회(selectMenus)")
    @Test
    void 메뉴들_조회() {
        // given
        Long menuGroupId = 1L;
        Menu menu1 = makeMenu(1L, 2);
        Menu menu2 = makeMenu(2L, 1);
        given(menuRepository.findByMenuGroupIdOrderByMenuOrder(menuGroupId)).willReturn(Arrays.asList(menu1, menu2));

        // when
        List<MenuVO> result = menuService.selectMenus(menuGroupId);

        // then
        then(menuRepository).should().findByMenuGroupIdOrderByMenuOrder(menuGroupId);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getMenuTitle()).isEqualTo(menu1.getTitle());
        assertThat(result.get(0).getMenuImage()).isNull();
        assertThat(result.get(1).getMenuTitle()).isEqualTo(menu2.getTitle());
        assertThat(result.get(1).getMenuImage()).isNull();
        assertThat(result.get(0).getMenuOrder()).isGreaterThan(result.get(1).getMenuOrder());
    }

    @DisplayName("성공 - 존재하지 않는 menuGroupId를 넘기면 메뉴들을 조회 시 빈 리스트 반환(selectMenus)")
    @Test
    void 메뉴들_조회_빈_리스트() {
        // given
        Long menuGroupId = 999L;
        given(menuRepository.findByMenuGroupIdOrderByMenuOrder(menuGroupId)).willReturn(List.of());

        // when
        List<MenuVO> result = menuService.selectMenus(menuGroupId);

        // then
        then(menuRepository).should().findByMenuGroupIdOrderByMenuOrder(menuGroupId);
        assertThat(result).isEmpty();
    }

    @DisplayName("성공 - 메뉴 순서 업데이트(updateMenusOrder)")
    @Test
    void 메뉴들_순서_업데이트() {
        // given
        Long[] menuIdList = {1L, 2L, 3L};
        Query mockedQuery = mock(Query.class);
        given(em.createQuery(anyString())).willReturn(mockedQuery);

        // when
        menuService.updateMenusOrder(menuIdList);

        // then
        then(em).should().createQuery(anyString());
        then(mockedQuery).should().setParameter(eq("id0"), eq(menuIdList[0]));
        then(mockedQuery).should().setParameter(eq("order0"), eq(1));
        then(mockedQuery).should().setParameter(eq("id1"), eq(menuIdList[1]));
        then(mockedQuery).should().setParameter(eq("order1"), eq(2));
        then(mockedQuery).should().setParameter(eq("id2"), eq(menuIdList[2]));
        then(mockedQuery).should().setParameter(eq("order2"), eq(3));
        then(mockedQuery).should().setParameter(eq("ids"), eq(Arrays.asList(menuIdList)));
        then(mockedQuery).should().executeUpdate();
    }

    @DisplayName("성공 - 메뉴와 연결된 메뉴 옵션 그룹들 저장(insertMenuOptionGroupConnectMenu)")
    @Test
    void 메뉴_및_연결된_메뉴_옵션_그룹들_저장() {
        // given
        Long menuId = 1L;
        Long[] optionGroupIdList = {2L, 3L, 4L};

        willDoNothing().given(menuOptionGroupMenuRepository).deleteMenuOptionGroupMenu(menuId);

        Menu menu = Menu.builder()
                .id(menuId)
                .build();
        given(menuRepository.getReferenceById(menuId)).willReturn(menu);

        MenuOptionGroup menuOptionGroup1 = MenuOptionGroup.builder()
                .id(optionGroupIdList[0])
                .build();
        given(menuOptionGroupRepository.getReferenceById(optionGroupIdList[0])).willReturn(menuOptionGroup1);

        MenuOptionGroup menuOptionGroup2 = MenuOptionGroup.builder()
                .id(optionGroupIdList[1])
                .build();
        given(menuOptionGroupRepository.getReferenceById(optionGroupIdList[1])).willReturn(menuOptionGroup2);

        MenuOptionGroup menuOptionGroup3 = MenuOptionGroup.builder()
                .id(optionGroupIdList[2])
                .build();
        given(menuOptionGroupRepository.getReferenceById(optionGroupIdList[2])).willReturn(menuOptionGroup3);

        // when
        menuService.insertMenuOptionGroupConnectMenu(optionGroupIdList, menuId);

        // then
        then(menuOptionGroupMenuRepository).should().deleteMenuOptionGroupMenu(menuId);
        then(menuRepository).should().getReferenceById(menuId);
        then(menuOptionGroupRepository).should().getReferenceById(optionGroupIdList[0]);
        then(menuOptionGroupRepository).should().getReferenceById(optionGroupIdList[1]);
        then(menuOptionGroupRepository).should().getReferenceById(optionGroupIdList[2]);
        then(menuOptionGroupMenuRepository).should().saveAllMenuOptionGroupMenu(anyList());

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<MenuOptionGroupMenu>> captor = ArgumentCaptor.forClass(List.class);
        then(menuOptionGroupMenuRepository).should().saveAllMenuOptionGroupMenu(captor.capture());
        List<MenuOptionGroupMenu> savedMenuOptionGroupMenus = captor.getValue();
        assertThat(savedMenuOptionGroupMenus).hasSize(optionGroupIdList.length);

        MenuOptionGroupMenu savedMenuOptionGroupMenu1 = savedMenuOptionGroupMenus.get(0);
        assertThat(menuOptionGroup1).isEqualTo(savedMenuOptionGroupMenu1.getMenuOptionGroup());
        assertThat(menu).isEqualTo(savedMenuOptionGroupMenu1.getMenu());

        MenuOptionGroupMenu savedMenuOptionGroupMenu2 = savedMenuOptionGroupMenus.get(1);
        assertThat(menuOptionGroup2).isEqualTo(savedMenuOptionGroupMenu2.getMenuOptionGroup());
        assertThat(menu).isEqualTo(savedMenuOptionGroupMenu2.getMenu());

        MenuOptionGroupMenu savedMenuOptionGroupMenu3 = savedMenuOptionGroupMenus.get(2);
        assertThat(menuOptionGroup3).isEqualTo(savedMenuOptionGroupMenu3.getMenuOptionGroup());
        assertThat(menu).isEqualTo(savedMenuOptionGroupMenu3.getMenu());
    }

    @DisplayName("성공 - 메뉴 및 메뉴 이미지, 메뉴와 연결된 메뉴 옵션들 조회(selectMenuWithMenuOptions)")
    @Test
    void 메뉴_및_연결된_메뉴_옵션들_조회() {
        // given
        Long menuId = 1L;
        Long attachFileId = 2L;
        Long menuOptionGroupId = 3L;
        Long menuOptionId1 = 4L;
        Long menuOptionId2 = 5L;

        AttachFile attachFile = makeAttachFile(attachFileId);
        Menu menu = makeMenuWithAttachFile(menuId, attachFile);
        List<MenuOption> menuOptions = Arrays.asList(makeMenuOption(menuOptionId1), makeMenuOption(menuOptionId2));
        List<MenuOptionGroup> menuOptionGroups = new ArrayList<>();
        MenuOptionGroup menuOptionGroup = MenuOptionGroup.builder()
                .id(menuOptionGroupId)
                .menuOptions(menuOptions)
                .build();
        menuOptionGroups.add(menuOptionGroup);

        given(menuRepository.selectMenuWithMenuImage(menuId)).willReturn(menu);
        given(menuOptionGroupRepository.selectMenuOptionGroupWithMenuOptions(menuId)).willReturn(menuOptionGroups);

        // when
        MenuVO menuVO = menuService.selectMenuWithMenuOptions(menuId);

        // then
        then(menuRepository).should().selectMenuWithMenuImage(menuId);
        then(menuOptionGroupRepository).should().selectMenuOptionGroupWithMenuOptions(menuId);

        assertThat(menuVO.getMenuId()).isEqualTo(menuId);
        assertThat(menuVO.getMenuImage().getId()).isEqualTo(attachFileId);
        assertThat(menuVO.getMenuOptionGroupVOS()).hasSize(1);
        assertThat(menuVO.getMenuOptionGroupVOS().get(0).getMenuOptionGroupId()).isEqualTo(menuOptionGroupId);
        assertThat(menuVO.getMenuOptionGroupVOS().get(0).getMenuOptionVOList()).hasSize(2);
        assertThat(menuVO.getMenuOptionGroupVOS().get(0).getMenuOptionVOList().get(0).getMenuOptionId()).isEqualTo(
                menuOptionId1);
        assertThat(menuVO.getMenuOptionGroupVOS().get(0).getMenuOptionVOList().get(1).getMenuOptionId()).isEqualTo(
                menuOptionId2);
    }


    @DisplayName("성공 - 메뉴 및 메뉴와 연결된 메뉴 이미지 조회(selectMenuWithMenuImage)")
    @Test
    void 메뉴_및_메뉴와_연결된_메뉴_이미지_조회() {
        // given
        Long menuId = 1L;
        Long attachFileId = 2L;

        AttachFile attachFile = makeAttachFile(attachFileId);
        Menu menu = makeMenuWithAttachFile(menuId, attachFile);

        given(menuRepository.selectMenuWithMenuImage(menuId)).willReturn(menu);

        // when
        MenuVO menuVO = menuService.selectMenuWithMenuImage(menuId);

        // then
        then(menuRepository).should().selectMenuWithMenuImage(menuId);

        assertThat(menuVO.getMenuId()).isEqualTo(menuId);
        assertThat(menuVO.getMenuImage()).isNotNull();
        assertThat(menuVO.getMenuImage().getId()).isEqualTo(attachFileId);
    }

    @DisplayName("성공 - 메뉴 이미지 저장(insertMenuImage)")
    @Test
    void 메뉴_이미지_저장_성공() throws IOException {
        // given
        Long menuId = 1L;
        Long attachFileId = 2L;
        MultipartFile file = mock(MultipartFile.class);

        given(file.getOriginalFilename()).willReturn("test.jpg");
        given(file.isEmpty()).willReturn(false);

        AttachFile attachFile = AttachFile.builder()
                .id(attachFileId)
                .originName("test.jpg")
                .savedPath("/path/to/test.jpg")
                .build();
        given(fileManageService.saveFile(file)).willReturn(attachFile);

        // when
        menuService.insertMenuImage(menuId, file);

        // then
        then(menuRepository).should().insertMenuImage(attachFile, menuId);

        ArgumentCaptor<AttachFile> fileCaptor = ArgumentCaptor.forClass(AttachFile.class);
        ArgumentCaptor<Long> menuIdCaptor = ArgumentCaptor.forClass(Long.class);
        then(menuRepository).should().insertMenuImage(fileCaptor.capture(), menuIdCaptor.capture());
        AttachFile savedAttachFile = fileCaptor.getValue();
        Long savedMenuId = menuIdCaptor.getValue();

        assertThat(savedAttachFile.getOriginName()).isEqualTo(file.getOriginalFilename());
        assertThat(savedAttachFile.getSavedPath()).isEqualTo(attachFile.getSavedPath());
        assertThat(savedMenuId).isEqualTo(menuId);
    }

    @DisplayName("실패 - 메뉴 이미지 저장 시 파일이 넘어오지 않은 경우 예외(insertMenuImage)")
    @Test
    void 메뉴_이미지_저장_실패_파일이_없는_경우() {
        // given
        Long menuId = 1L;
        MultipartFile file = mock(MultipartFile.class);

        given(file.isEmpty()).willReturn(true);

        // when, then
        assertThatThrownBy(() -> menuService.insertMenuImage(menuId, file))
                .isInstanceOf(UploadFileNotFoundException.class)
                .hasMessageContaining(ErrorCode.UPLOAD_FILE_NOT_FOUND.Message());
    }

    @DisplayName("실패 - 메뉴 이미지 저장 시 파일 저장에 실패한 경우 예외(insertMenuImage)")
    @Test
    void 메뉴_이미지_저장_실패_파일_저장_실패한_경우() throws IOException {
        // given
        Long menuId = 1L;
        MultipartFile file = mock(MultipartFile.class);

        given(file.isEmpty()).willReturn(false);
        given(fileManageService.saveFile(file)).willReturn(null);

        // when, then
        assertThatThrownBy(() -> menuService.insertMenuImage(menuId, file))
                .isInstanceOf(UploadFileErrorException.class)
                .hasMessageContaining(ErrorCode.UPLOAD_FILE_ERROR.Message());
    }

    @DisplayName("성공 - 메뉴 이미지 삭제(deleteMenuImage)")
    @Test
    void 메뉴_이미지_삭제() {
        // given
        Long menuId = 1L;
        willDoNothing().given(menuRepository).deleteMenuImage(menuId);

        // when
        menuService.deleteMenuImage(menuId);

        // then
        then(menuRepository).should().deleteMenuImage(menuId);
    }

    private MenuOption makeMenuOption(Long menuOptionId) {
        return MenuOption.builder()
                .id(menuOptionId)
                .build();
    }

    private AttachFile makeAttachFile(Long attachFileId) {
        return AttachFile.builder()
                .id(attachFileId)
                .build();
    }

    private Menu makeMenuWithAttachFile(Long menuId, AttachFile attachFile) {
        return Menu.builder()
                .id(menuId)
                .menuImage(attachFile)
                .build();
    }

    private Menu makeMenu(Long menuId) {
        return Menu.builder()
                .id(menuId)
                .title("test")
                .explanation("test")
                .exposure("Y")
                .menuOrder(1)
                .price(1000)
                .menuImage(makeAttachFile(1L))
                .menuGroup(MenuGroup.builder().id(1L).build())
                .menuOptionGroupMenus(Collections.singletonList(MenuOptionGroupMenu.builder().id(1L).build()))
                .build();
    }

    private Menu makeMenu(Long menuId, int menuOrder) {
        return Menu.builder()
                .id(menuId)
                .title("test")
                .explanation("test")
                .exposure("Y")
                .menuOrder(menuOrder)
                .price(1000)
                .menuImage(makeAttachFile(1L))
                .menuGroup(MenuGroup.builder().id(1L).build())
                .menuOptionGroupMenus(Collections.singletonList(MenuOptionGroupMenu.builder().id(1L).build()))
                .build();
    }
}
