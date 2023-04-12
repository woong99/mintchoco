package com.woong.mintchoco.owner.menu.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.woong.mintchoco.global.auth.entity.User;
import com.woong.mintchoco.global.common.ErrorCode;
import com.woong.mintchoco.owner.menu.entity.MenuGroup;
import com.woong.mintchoco.owner.menu.exception.MenuGroupNotFoundException;
import com.woong.mintchoco.owner.menu.model.MenuGroupVO;
import com.woong.mintchoco.owner.menu.repository.group.MenuGroupRepository;
import com.woong.mintchoco.owner.store.entity.Store;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
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

@DisplayName("[SERVICE 단위 테스트] menuGroupService")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest extends BaseMenuServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private EntityManager em;


    @DisplayName("성공 - 메뉴그룹 정상적으로 추가(insertMenuGroup)")
    @Test
    void 메뉴그룹_추가() {
        // given
        int groupOrder = 0;

        Store store = createStore(storeId);
        User user = createUser(userId, store);
        MenuGroupVO menuGroupVO = MenuGroupVO.builder()
                .id(menuGroupId1)
                .build();

        given(menuGroupRepository.findLastGroupOrder(store.getId())).willReturn(groupOrder);

        // when
        menuGroupService.insertMenuGroup(user, menuGroupVO);

        // then
        then(menuGroupRepository).should().findLastGroupOrder(store.getId());
        then(menuGroupRepository).should().save(any(MenuGroup.class));

        ArgumentCaptor<MenuGroup> captor = ArgumentCaptor.forClass(MenuGroup.class);
        then(menuGroupRepository).should().save(captor.capture());
        MenuGroup menuGroup = captor.getValue();

        assertThat(menuGroup.getId()).isEqualTo(menuGroupId1);
        assertThat(menuGroup.getGroupOrder()).isEqualTo(groupOrder + 1);
        assertThat(menuGroup.getStore().getId()).isEqualTo(storeId);
    }

    @DisplayName("성공 - 모든 메뉴그룹 및 연관된 메뉴 정상적으로 조회(selectAllMenuGroup)")
    @Test
    void 모든_메뉴그룹_조회() {
        // given
        Store store = createStore(storeId);
        User user = createUser(userId, store);

        MenuGroup menuGroup1 = createMenuGroup(menuGroupId1, Arrays.asList(createMenu(menuId1), createMenu(menuId2)));

        MenuGroup menuGroup2 = createMenuGroup(menuGroupId2, Arrays.asList(createMenu(menuId3), createMenu(menuId4)));

        given(menuGroupRepository.selectAllMenuGroupWithMenu(storeId)).willReturn(
                Arrays.asList(menuGroup1, menuGroup2));

        // when
        List<MenuGroupVO> menuGroupVOList = menuGroupService.selectAllMenuGroup(user);

        // then
        then(menuGroupRepository).should().selectAllMenuGroupWithMenu(storeId);

        assertThat(menuGroupVOList).hasSize(2);
        assertThat(menuGroupVOList.get(0).getId()).isEqualTo(menuGroupId1);
        assertThat(menuGroupVOList.get(1).getId()).isEqualTo(menuGroupId2);
        assertThat(menuGroupVOList.get(0).getMenuVOList()).hasSize(2);
        assertThat(menuGroupVOList.get(1).getMenuVOList()).hasSize(2);
        assertThat(menuGroupVOList.get(0).getMenuVOList().get(0).getMenuId()).isEqualTo(menuId1);
        assertThat(menuGroupVOList.get(0).getMenuVOList().get(1).getMenuId()).isEqualTo(menuId2);
        assertThat(menuGroupVOList.get(1).getMenuVOList().get(0).getMenuId()).isEqualTo(menuId3);
        assertThat(menuGroupVOList.get(1).getMenuVOList().get(1).getMenuId()).isEqualTo(menuId4);
    }

    @DisplayName("성공 - 메뉴가 없는 상태에서 메뉴그룹 조회 시 정상적인 메뉴그룹 및 빈 메뉴 리스트 반환(selectAllMenuGroup)")
    @Test
    void 모든_메뉴그룹_조회_메뉴가_없는_경우() {
        // given
        Store store = createStore(storeId);
        User user = createUser(userId, store);

        MenuGroup menuGroup = createMenuGroup(menuGroupId1, List.of());

        given(menuGroupRepository.selectAllMenuGroupWithMenu(storeId)).willReturn(Collections.singletonList(menuGroup));

        // when
        List<MenuGroupVO> menuGroupVOList = menuGroupService.selectAllMenuGroup(user);

        // then
        then(menuGroupRepository).should().selectAllMenuGroupWithMenu(storeId);

        assertThat(menuGroupVOList).hasSize(1);
        assertThat(menuGroupVOList.get(0).getId()).isEqualTo(menuGroupId1);
        assertThat(menuGroupVOList.get(0).getMenuVOList()).isEmpty();
    }

    @DisplayName("성공 - 단일 메뉴그룹만 조회, 하위 메뉴들은 조회하지 않음(selectMenuGroup)")
    @Test
    void 단일_메뉴그룹_조회_성공() {
        // given

        MenuGroup menuGroup = createMenuGroup(menuGroupId1, Arrays.asList(createMenu(2L), createMenu(3L)));

        given(menuGroupRepository.findById(menuGroupId1)).willReturn(Optional.ofNullable(menuGroup));

        // when
        MenuGroupVO menuGroupVO = menuGroupService.selectMenuGroup(menuGroupId1);

        // then
        then(menuGroupRepository).should().findById(menuGroupId1);

        assertThat(menuGroupVO.getId()).isEqualTo(menuGroupId1);
        assertThat(menuGroupVO.getMenuVOList()).isNull();
    }

    @DisplayName("실패 - 존재하지 않는 메뉴그룹 ID가 넘어가면 예외(selectMenuGroup)")
    @Test
    void 단일_메뉴그룹_조회_예외() {
        // given
        given(menuGroupRepository.findById(menuGroupId1)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> menuGroupService.selectMenuGroup(menuGroupId1))
                .isInstanceOf(MenuGroupNotFoundException.class)
                .hasMessageContaining(ErrorCode.MENU_GROUP_NOT_FOUND.Message());
    }

    @DisplayName("성공 - 메뉴그룹 수정(updateMenuGroup)")
    @Test
    void 메뉴그룹_수정() {
        // given
        MenuGroupVO menuGroupVO = MenuGroupVO.builder()
                .id(menuGroupId1)
                .build();

        willDoNothing().given(menuGroupRepository).updateMenuGroup(menuGroupVO);

        // when
        menuGroupService.updateMenuGroup(menuGroupVO);

        // then
        then(menuGroupRepository).should().updateMenuGroup(menuGroupVO);
    }

    @DisplayName("성공 - 메뉴그룹 순서 정상적으로 수정(updateMenuGroupOrder)")
    @Test
    void 메뉴그룹_순서_수정() {
        // given
        Long[] menuGroupIdList = {1L, 2L, 3L};
        Query query = mock(Query.class);
        given(em.createQuery(anyString())).willReturn(query);

        // when
        menuGroupService.updateMenuGroupOrder(menuGroupIdList);

        // then
        then(em).should().createQuery(anyString());
        then(query).should().setParameter(eq("id0"), eq(menuGroupIdList[0]));
        then(query).should().setParameter(eq("order0"), eq(1));
        then(query).should().setParameter(eq("id1"), eq(menuGroupIdList[1]));
        then(query).should().setParameter(eq("order1"), eq(2));
        then(query).should().setParameter(eq("id2"), eq(menuGroupIdList[2]));
        then(query).should().setParameter(eq("order2"), eq(3));
        then(query).should().setParameter("ids", Arrays.asList(menuGroupIdList));
        then(query).should().executeUpdate();
    }

    @DisplayName("성공 - 메뉴그룹 정상적으로 삭제(deleteMenuGroup)")
    @Test
    void 메뉴그룹_삭제() {
        // given
        willDoNothing().given(menuGroupRepository).deleteMenuGroup(menuGroupId1);

        // when
        menuGroupService.deleteMenuGroup(menuGroupId1);

        // then
        then(menuGroupRepository).should().deleteMenuGroup(menuGroupId1);
    }

    @DisplayName("성공 - 단일 메뉴그룹 및 연결된 메뉴들 정상적으로 조회(selectMenuGroupWithMenus)")
    @Test
    void 메뉴그룹_및_연결된_메뉴들_조회() {
        // given
        MenuGroup menuGroup = createMenuGroup(menuGroupId1, Arrays.asList(createMenu(menuId1), createMenu(menuId2)));
        given(menuGroupRepository.selectMenuGroupWithMenus(menuGroupId1)).willReturn(menuGroup);

        // when
        MenuGroupVO menuGroupVO = menuGroupService.selectMenuGroupWithMenus(menuGroupId1);

        // then
        then(menuGroupRepository).should().selectMenuGroupWithMenus(menuGroupId1);

        assertThat(menuGroupVO.getId()).isEqualTo(menuGroupId1);
        assertThat(menuGroupVO.getMenuVOList()).hasSize(2);
        assertThat(menuGroupVO.getMenuVOList().get(0).getMenuId()).isEqualTo(menuId1);
        assertThat(menuGroupVO.getMenuVOList().get(1).getMenuId()).isEqualTo(menuId2);
    }

    @DisplayName("성공 - 단일 메뉴그룹 조회 시 연결된 메뉴가 없는 경우 단일 메뉴그룹 정보와 빈 메뉴 리스트 반환(selectMenuGroupWithMenus)")
    @Test
    void 메뉴그룹_조회_시_연결된_메뉴가_없는_경우() {
        // given
        MenuGroup menuGroup = createMenuGroup(menuGroupId1, List.of());
        given(menuGroupRepository.selectMenuGroupWithMenus(menuGroupId1)).willReturn(menuGroup);

        // when
        MenuGroupVO menuGroupVO = menuGroupService.selectMenuGroupWithMenus(menuGroupId1);

        // then
        then(menuGroupRepository).should().selectMenuGroupWithMenus(menuGroupId1);

        assertThat(menuGroupVO.getId()).isEqualTo(menuGroupId1);
        assertThat(menuGroupVO.getMenuVOList()).isEmpty();
    }
}