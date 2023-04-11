package com.woong.mintchoco.owner.menu.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.woong.mintchoco.global.auth.entity.User;
import com.woong.mintchoco.global.common.ErrorCode;
import com.woong.mintchoco.owner.menu.entity.Menu;
import com.woong.mintchoco.owner.menu.entity.MenuOption;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroup;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroupMenu;
import com.woong.mintchoco.owner.menu.exception.MenuOptionGroupNotFoundException;
import com.woong.mintchoco.owner.menu.model.MenuOptionGroupMenuVO;
import com.woong.mintchoco.owner.menu.model.MenuOptionGroupVO;
import com.woong.mintchoco.owner.menu.model.MenuOptionVO;
import com.woong.mintchoco.owner.menu.model.MenuVO;
import com.woong.mintchoco.owner.menu.repository.MenuRepository;
import com.woong.mintchoco.owner.menu.repository.option.MenuOptionRepository;
import com.woong.mintchoco.owner.menu.repository.option.group.MenuOptionGroupRepository;
import com.woong.mintchoco.owner.menu.repository.option.group.menu.MenuOptionGroupMenuRepository;
import com.woong.mintchoco.owner.store.entity.Store;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("[SERVICE 단위 테스트] menuOptionGroupService")
@ExtendWith(MockitoExtension.class)
class MenuOptionGroupServiceTest {

    private static final Long userId = 1L;
    private static final Long storeId = 2L;
    private static final Long menuOptionGroupId1 = 3L;
    private static final Long menuOptionGroupId2 = 4L;
    private static final Long menuOptionId1 = 5L;
    private static final Long menuOptionId2 = 6L;
    private static final Long menuOptionId3 = 7L;
    private static final Long menuOptionId4 = 8L;
    private static final Long menuOptionGroupMenuId1 = 9L;
    private static final Long menuOptionGroupMenuId2 = 10L;
    private static final Long menuId = 11L;
    @InjectMocks
    private MenuOptionGroupService menuOptionGroupService;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuOptionRepository menuOptionRepository;
    @Mock
    private MenuOptionGroupRepository menuOptionGroupRepository;
    @Mock
    private MenuOptionGroupMenuRepository menuOptionGroupMenuRepository;


    @DisplayName("성공 - 모든 메뉴 옵션 그룹들을 조회할 때 연관된 메뉴 옵션들도 조회(selectAllMenuOptionGroupWithMenuOption)")
    @Test
    void 모든_메뉴옵션그룹_및_연관된_옵션들_조회() {
        // given
        User user = createUser(createStore());

        MenuOptionGroup menuOptionGroup1 = MenuOptionGroup.builder()
                .id(menuOptionGroupId1)
                .menuOptions(Arrays.asList(createMenuOption(menuOptionId1, 1), createMenuOption(menuOptionId2, 2)))
                .build();

        MenuOptionGroup menuOptionGroup2 = MenuOptionGroup.builder()
                .id(menuOptionGroupId2)
                .menuOptions(Arrays.asList(createMenuOption(menuOptionId3, 3), createMenuOption(menuOptionId4, 4)))
                .build();

        given(menuOptionGroupRepository.selectAllMenuOptionGroupWithMenuOptionOrderByMenuOrder(storeId)).willReturn(
                Arrays.asList(menuOptionGroup1, menuOptionGroup2));

        // when
        List<MenuOptionGroupVO> menuOptionGroupVOList = menuOptionGroupService.selectAllMenuOptionGroupWithMenuOption(
                user);

        // then
        then(menuOptionGroupRepository).should().selectAllMenuOptionGroupWithMenuOptionOrderByMenuOrder(storeId);

        assertThat(menuOptionGroupVOList).hasSize(2);
        assertThat(menuOptionGroupVOList.get(0).getMenuOptionGroupId()).isEqualTo(menuOptionGroupId1);
        assertThat(menuOptionGroupVOList.get(1).getMenuOptionGroupId()).isEqualTo(menuOptionGroupId2);
        assertThat(menuOptionGroupVOList.get(0).getMenuOptionVOList()).hasSize(2);
        assertThat(menuOptionGroupVOList.get(1).getMenuOptionVOList()).hasSize(2);
        assertThat(menuOptionGroupVOList.get(0).getMenuOptionVOList().get(0).getMenuOptionId()).isEqualTo(
                menuOptionId1);
        assertThat(menuOptionGroupVOList.get(0).getMenuOptionVOList().get(1).getMenuOptionId()).isEqualTo(
                menuOptionId2);
        assertThat(menuOptionGroupVOList.get(1).getMenuOptionVOList().get(0).getMenuOptionId()).isEqualTo(
                menuOptionId3);
        assertThat(menuOptionGroupVOList.get(1).getMenuOptionVOList().get(1).getMenuOptionId()).isEqualTo(
                menuOptionId4);
    }

    @DisplayName("성공 - 모든 메뉴 옵션 그룹 조회 시 연결된 메뉴 옵션이 없는 경우 빈 리스트 반환(selectAllMenuOptionGroupWithMenuOption)")
    @Test
    void 모든_메뉴옵션그룹_조회시_연관된_옵션들이_없는_경우() {
        // given
        User user = createUser(createStore());

        MenuOptionGroup menuOptionGroup1 = MenuOptionGroup.builder()
                .id(menuOptionGroupId1)
                .menuOptions(List.of())
                .build();

        MenuOptionGroup menuOptionGroup2 = MenuOptionGroup.builder()
                .id(menuOptionGroupId2)
                .menuOptions(List.of())
                .build();

        given(menuOptionGroupRepository.selectAllMenuOptionGroupWithMenuOptionOrderByMenuOrder(storeId)).willReturn(
                Arrays.asList(menuOptionGroup1, menuOptionGroup2));

        // when
        List<MenuOptionGroupVO> menuOptionGroupVOList = menuOptionGroupService.selectAllMenuOptionGroupWithMenuOption(
                user);

        // then
        then(menuOptionGroupRepository).should().selectAllMenuOptionGroupWithMenuOptionOrderByMenuOrder(storeId);

        assertThat(menuOptionGroupVOList).hasSize(2);
        assertThat(menuOptionGroupVOList.get(0).getMenuOptionGroupId()).isEqualTo(menuOptionGroupId1);
        assertThat(menuOptionGroupVOList.get(1).getMenuOptionGroupId()).isEqualTo(menuOptionGroupId2);
        assertThat(menuOptionGroupVOList.get(0).getMenuOptionVOList()).isEmpty();
        assertThat(menuOptionGroupVOList.get(1).getMenuOptionVOList()).isEmpty();
    }

    @DisplayName("성공 - 메뉴 옵션 그룹 조회 시 연관된 메뉴 옵션은 조회하지 않음(selectAllMenuOptionGroup)")
    @Test
    void 모든_메뉴옵션그룹_조회() {
        // given
        User user = createUser(createStore());

        MenuOptionGroup menuOptionGroup1 = createMenuOptionGroup(menuOptionGroupId1,
                Arrays.asList(createMenuOption(menuOptionId1, 1), createMenuOption(menuOptionId2, 2)));
        MenuOptionGroup menuOptionGroup2 = createMenuOptionGroup(menuOptionGroupId2,
                Arrays.asList(createMenuOption(menuOptionId3, 3), createMenuOption(menuOptionId4, 4)));

        given(menuOptionGroupRepository.findAllByStoreId(storeId)).willReturn(
                Arrays.asList(menuOptionGroup1, menuOptionGroup2));

        // when
        List<MenuOptionGroupVO> menuOptionGroupVOList = menuOptionGroupService.selectAllMenuOptionGroup(user);

        // then
        then(menuOptionGroupRepository).should().findAllByStoreId(storeId);

        assertThat(menuOptionGroupVOList).hasSize(2);
        assertThat(menuOptionGroupVOList.get(0).getMenuOptionGroupId()).isEqualTo(menuOptionGroupId1);
        assertThat(menuOptionGroupVOList.get(1).getMenuOptionGroupId()).isEqualTo(menuOptionGroupId2);
        assertThat(menuOptionGroupVOList.get(0).getMenuOptionVOList()).isNull();
        assertThat(menuOptionGroupVOList.get(1).getMenuOptionVOList()).isNull();
    }

    @DisplayName("성공 - 메뉴 옵션 그룹과 연관된 메뉴 옵션들을 정상적으로 저정(insertMenuOptionGroup)")
    @Test
    void 메뉴옵션그룹_및_메뉴옵션들_저장() {
        // given
        Store store = createStore();
        User user = createUser(store);

        MenuOptionGroupVO menuOptionGroupVO = MenuOptionGroupVO.builder()
                .menuOptionGroupId(menuOptionGroupId1)
                .build();

        MenuOptionVO menuOptionVO1 = createMenuOptionVO(menuOptionId1, 1);
        MenuOptionVO menuOptionVO2 = createMenuOptionVO(menuOptionId2, 2);

        List<MenuOptionVO> menuOptionVOList = Arrays.asList(menuOptionVO1, menuOptionVO2);

        MenuOptionVO menuOptionVO = MenuOptionVO.builder()
                .menuOptionVOList(menuOptionVOList)
                .build();

        MenuOptionGroup menuOptionGroup = MenuOptionGroup.toMenuOptionGroup(menuOptionGroupVO);
        given(menuOptionGroupRepository.save(any(MenuOptionGroup.class))).willReturn(menuOptionGroup);

        // when
        menuOptionGroupService.insertMenuOptionGroup(user, menuOptionGroupVO, menuOptionVO);

        // then
        then(menuOptionGroupRepository).should().save(any(MenuOptionGroup.class));
        then(menuOptionRepository).should().insertMenuOption(anyList());

        ArgumentCaptor<MenuOptionGroup> captor = ArgumentCaptor.forClass(MenuOptionGroup.class);
        then(menuOptionGroupRepository).should().save(captor.capture());
        MenuOptionGroup captorMenuOptionGroup = captor.getValue();
        assertThat(captorMenuOptionGroup.getId()).isEqualTo(menuOptionGroupId1);
    }

    @DisplayName("성공 - 단일 메뉴 옵션 그룹과 연관된 메뉴 옵션들을 정상적으로 오름차순으로 조회(selectMenuOptionGroup)")
    @Test
    void 단일메뉴옵션그룹과_연관된메뉴옵션들_조회() {
        // given
        MenuOptionGroup menuOptionGroup = createMenuOptionGroup(menuOptionGroupId1,
                Arrays.asList(createMenuOption(menuOptionId1, 1), createMenuOption(menuOptionId2, 2)));

        given(menuOptionGroupRepository.selectMenuOptionGroupWithMenuOptionOrderByMenuOrder(
                menuOptionGroupId1)).willReturn(menuOptionGroup);

        // when
        MenuOptionGroupVO menuOptionGroupVO = menuOptionGroupService.selectMenuOptionGroup(menuOptionGroupId1);

        // then
        then(menuOptionGroupRepository).should()
                .selectMenuOptionGroupWithMenuOptionOrderByMenuOrder(menuOptionGroupId1);

        assertThat(menuOptionGroupVO.getMenuOptionGroupId()).isEqualTo(menuOptionGroup.getId());
        assertThat(menuOptionGroupVO.getMenuOptionVOList()).hasSize(2);
        assertThat(menuOptionGroupVO.getMenuOptionVOList().get(0).getMenuOptionId()).isEqualTo(menuOptionId1);
        assertThat(menuOptionGroupVO.getMenuOptionVOList().get(1).getMenuOptionId()).isEqualTo(menuOptionId2);
        assertThat(menuOptionGroupVO.getMenuOptionVOList().get(0).getMenuOptionOrder()).isLessThan(
                menuOptionGroupVO.getMenuOptionVOList().get(1).getMenuOptionOrder());
    }

    @DisplayName("성공 - 단일 메뉴 옵션 그룹을 정상적으로 조회하고 메뉴 옵션들은 빈 리스트를 반환(selectMenuOptionGroup)")
    @Test
    void 단일메뉴옵션그룹과_연관된메뉴옵션들_조회_메뉴옵션없는경우() {
        // given
        MenuOptionGroup menuOptionGroup = createMenuOptionGroup(menuOptionGroupId1, List.of());

        given(menuOptionGroupRepository.selectMenuOptionGroupWithMenuOptionOrderByMenuOrder(
                menuOptionGroupId1)).willReturn(menuOptionGroup);

        // when
        MenuOptionGroupVO menuOptionGroupVO = menuOptionGroupService.selectMenuOptionGroup(menuOptionGroupId1);

        // then
        then(menuOptionGroupRepository).should()
                .selectMenuOptionGroupWithMenuOptionOrderByMenuOrder(menuOptionGroupId1);

        assertThat(menuOptionGroupVO.getMenuOptionGroupId()).isEqualTo(menuOptionGroupId1);
        assertThat(menuOptionGroupVO.getMenuOptionVOList()).hasSize(0);
    }

    @DisplayName("실패 - 잘못된 그룹 아이디 전달 시 예외 발생(selectMenuOptionGroup)")
    @Test
    void 단일메뉴옵션그룹_조회실패_잘못된그룹아이디() {
        // given
        given(menuOptionGroupRepository.selectMenuOptionGroupWithMenuOptionOrderByMenuOrder(
                menuOptionGroupId1)).willReturn(null);

        // when, then
        assertThatThrownBy(() -> menuOptionGroupService.selectMenuOptionGroup(menuOptionGroupId1))
                .isInstanceOf(MenuOptionGroupNotFoundException.class)
                .hasMessageContaining(ErrorCode.MENU_OPTION_GROUP_NOT_FOUND.Message());
    }

    @DisplayName("성공 - 메뉴 옵션 그룹 및 연관된 메뉴 옵션들 정상 수정(updateMenuOptionGroup)")
    @Test
    void 메뉴옵션그룹_및_연관된메뉴옵션들_수정() {
        // given
        MenuOptionVO menuOptionVO = createMenuOptionVO(menuOptionId1, 1);
        List<MenuOptionVO> menuOptionVOList = Collections.singletonList(menuOptionVO);
        MenuOptionGroupVO menuOptionGroupVO = MenuOptionGroupVO.builder()
                .menuOptionGroupId(menuOptionGroupId1)
                .menuOptionVOList(menuOptionVOList)
                .build();

        willDoNothing().given(menuOptionGroupRepository).updateMenuOptionGroup(menuOptionGroupVO);
        willDoNothing().given(menuOptionRepository).deleteMenuOption(menuOptionGroupVO.getMenuOptionGroupId());
        willDoNothing().given(menuOptionRepository).insertMenuOption(anyList());

        // when
        menuOptionGroupService.updateMenuOptionGroup(menuOptionGroupVO);

        // then
        then(menuOptionGroupRepository).should().updateMenuOptionGroup(menuOptionGroupVO);
        then(menuOptionRepository).should().deleteMenuOption(menuOptionGroupVO.getMenuOptionGroupId());
        then(menuOptionRepository).should().insertMenuOption(anyList());
    }

    @DisplayName("성공 - 메뉴 옵션 그룹 정상 삭제(deleteMenuOptionGroup)")
    @Test
    void 메뉴옵션그룹_삭제() {
        // given
        willDoNothing().given(menuOptionGroupRepository).deleteMenuOptionGroup(menuOptionGroupId1);

        // when
        menuOptionGroupService.deleteMenuOptionGroup(menuOptionGroupId1);

        // then
        then(menuOptionGroupRepository).should().deleteMenuOptionGroup(menuOptionGroupId1);
    }

    @DisplayName("성공 - 메뉴와 연결된 메뉴 옵션 그룹 정보 정상 조회(selectMenuOptionGroupConnectMenu)")
    @Test
    void 메뉴와_연결된_메뉴옵션그룹정보_조회() {
        // given
        List<MenuOptionGroupMenu> menuOptionGroupMenus = Arrays.asList(
                createMenuOptionGroupMenu(menuOptionGroupMenuId1), createMenuOptionGroupMenu(menuOptionGroupMenuId2));
        given(menuOptionGroupMenuRepository.findByMenuIdOrderByMenuId(menuId)).willReturn(menuOptionGroupMenus);

        // when
        List<MenuOptionGroupMenuVO> menuOptionGroupMenuVOList = menuOptionGroupService.selectMenuOptionGroupConnectMenu(
                menuId);

        // then
        then(menuOptionGroupMenuRepository).should().findByMenuIdOrderByMenuId(menuId);

        assertThat(menuOptionGroupMenuVOList).hasSize(menuOptionGroupMenus.size());
        assertThat(menuOptionGroupMenuVOList.get(0).getId()).isEqualTo(menuOptionGroupMenuId1);
        assertThat(menuOptionGroupMenuVOList.get(1).getId()).isEqualTo(menuOptionGroupMenuId2);
    }

    @DisplayName("성공 - 메뉴 옵션 그룹과 연결된 메뉴 정상적으로 조회(selectMenuOptionGroupConnectedMenu)")
    @Test
    void 메뉴옵션그룹과_연결된메뉴조회() {
        // given
        Menu menu = Menu.builder()
                .id(menuId)
                .build();
        List<Menu> menus = Collections.singletonList(menu);
        given(menuRepository.selectMenuOptionGroupConnectedMenu(menuOptionGroupMenuId1)).willReturn(menus);

        // when
        List<MenuVO> menuVOS = menuOptionGroupService.selectMenuOptionGroupConnectedMenu(menuOptionGroupMenuId1);

        // then
        then(menuRepository).should().selectMenuOptionGroupConnectedMenu(menuOptionGroupMenuId1);

        assertThat(menuVOS).hasSize(1);
        assertThat(menuVOS.get(0).getMenuId()).isEqualTo(menuId);
    }

    private Store createStore() {
        return Store.builder()
                .id(MenuOptionGroupServiceTest.storeId)
                .build();
    }

    private User createUser(Store store) {
        return User.builder()
                .id(MenuOptionGroupServiceTest.userId)
                .store(store)
                .build();
    }

    private MenuOption createMenuOption(Long menuOptionId, int menuOrder) {
        return MenuOption.builder()
                .id(menuOptionId)
                .menuOrder(menuOrder)
                .build();
    }

    private MenuOptionVO createMenuOptionVO(Long menuOptionId, int menuOrder) {
        return MenuOptionVO.builder()
                .menuOptionId(menuOptionId)
                .menuOptionOrder(menuOrder)
                .build();
    }

    private MenuOptionGroup createMenuOptionGroup(Long menuOptionGroupId, List<MenuOption> menuOptionList) {
        return MenuOptionGroup.builder()
                .id(menuOptionGroupId)
                .menuOptions(menuOptionList)
                .build();
    }

    private MenuOptionGroupMenu createMenuOptionGroupMenu(Long menuOptionGroupMenuId) {
        return MenuOptionGroupMenu.builder()
                .id(menuOptionGroupMenuId)
                .menu(createMenu())
                .menuOptionGroup(createMenuOptionGroup(menuOptionGroupId1, List.of()))
                .build();
    }

    private Menu createMenu() {
        return Menu.builder()
                .id(100L)
                .build();
    }

}