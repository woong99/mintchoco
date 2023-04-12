package com.woong.mintchoco.owner.menu.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.woong.mintchoco.common.WithAuthUser;
import com.woong.mintchoco.global.auth.entity.User;
import com.woong.mintchoco.global.common.ErrorCode;
import com.woong.mintchoco.global.common.Message;
import com.woong.mintchoco.global.common.MessageType;
import com.woong.mintchoco.global.common.URL;
import com.woong.mintchoco.owner.menu.exception.MenuOptionGroupNotFoundException;
import com.woong.mintchoco.owner.menu.model.MenuOptionGroupMenuVO;
import com.woong.mintchoco.owner.menu.model.MenuOptionGroupVO;
import com.woong.mintchoco.owner.menu.model.MenuOptionVO;
import com.woong.mintchoco.owner.menu.model.MenuVO;
import com.woong.mintchoco.owner.menu.service.MenuOptionGroupService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("[CONTROLLER 단위 테스트] menuOptionGroupController")
@WebMvcTest(MenuOptionGroupController.class)
class MenuOptionGroupControllerTest extends BaseMenuControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MenuOptionGroupService menuOptionGroupService;


    @DisplayName("[200_OK] - 메뉴 옵션 그룹 등록 후 메세지 뷰로 이동")
    @WithAuthUser
    @Test
    void 메뉴옵션그룹추가() throws Exception {
        // given
        MenuOptionGroupVO menuOptionGroupVO = createMenuOptionGroupVO(menuOptionGroupId1);
        MenuOptionVO menuOptionVO = createMenuOptionVO(menuOptionId1);
        willDoNothing().given(menuOptionGroupService)
                .insertMenuOptionGroup(any(User.class), eq(menuOptionGroupVO), eq(menuOptionVO));

        // when, then
        mockMvc.perform(post(commonURL + "/menuGroup/menuOptionGroup/insert.do").with(csrf())
                        .flashAttr("menuOptionGroupVO", menuOptionGroupVO)
                        .flashAttr("menuOptionVO", menuOptionVO))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuOptionGroupInsert"))
                .andExpect(model().attribute("type", MessageType.MSG_URL.messageType()))
                .andExpect(model().attribute("message", Message.SUCCESS_INSERT.message()))
                .andExpect(model().attribute("returnUrl", URL.MENU_INFO.url()))
                .andExpect(view().name(URL.MESSAGE.url()));

        then(menuOptionGroupService).should()
                .insertMenuOptionGroup(any(User.class), eq(menuOptionGroupVO), eq(menuOptionVO));
    }

    @DisplayName("[200_OK] - 메뉴 옵션 그룹 상세 조회 후 뷰로 데이터 전달")
    @WithAuthUser
    @Test
    void 메뉴옵션그룹상세조회() throws Exception {
        // given
        MenuOptionGroupVO menuOptionGroupVO = createMenuOptionGroupVO(menuOptionGroupId1);
        given(menuOptionGroupService.selectMenuOptionGroup(menuOptionGroupId1)).willReturn(menuOptionGroupVO);

        // when, then
        mockMvc.perform(get(commonURL + "/menuGroup/menuOptionGroup/select.do").with(csrf())
                        .param("id", menuOptionGroupId1.toString()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuOptionGroupSelect"))
                .andExpect(jsonPath("$.menuOptionGroupId").value(menuOptionGroupVO.getMenuOptionGroupId()));

        then(menuOptionGroupService).should().selectMenuOptionGroup(menuOptionGroupId1);
    }

    @DisplayName("[404_ERROR] - 메뉴 옵션 그룹 상세 조회 시 메뉴 옵션 그룹이 없는 경우")
    @WithAuthUser
    @Test
    void 메뉴옵션그룹상세조회_예외() throws Exception {
        // given
        given(menuOptionGroupService.selectMenuOptionGroup(menuOptionGroupId1)).willThrow(
                new MenuOptionGroupNotFoundException(
                        ErrorCode.MENU_OPTION_GROUP_NOT_FOUND));

        // when, then
        mockMvc.perform(get(commonURL + "/menuGroup/menuOptionGroup/select.do").with(csrf())
                        .param("id", menuOptionGroupId1.toString()))
                .andExpect(status().isNotFound())
                .andExpect(handler().methodName("menuOptionGroupSelect"));

        then(menuOptionGroupService).should().selectMenuOptionGroup(menuOptionGroupId1);
    }

    @DisplayName("[200_OK] - 메뉴 옵션 수정 후 메세지 뷰로 이동")
    @WithAuthUser
    @Test
    void 메뉴옵션수정() throws Exception {
        // given
        MenuOptionGroupVO menuOptionGroupVO = createMenuOptionGroupVO(menuOptionGroupId1);
        willDoNothing().given(menuOptionGroupService).updateMenuOptionGroup(menuOptionGroupVO);

        // when, then
        mockMvc.perform(post(commonURL + "/menuGroup/menuOptionGroup/update.do").with(csrf())
                        .flashAttr("menuOptionGroupVO", menuOptionGroupVO))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuOptionGroupUpdate"))
                .andExpect(model().attribute("type", MessageType.MSG_URL.messageType()))
                .andExpect(model().attribute("message", Message.SUCCESS_UPDATE.message()))
                .andExpect(model().attribute("returnUrl", URL.MENU_INFO.url()))
                .andExpect(view().name(URL.MESSAGE.url()));

        then(menuOptionGroupService).should().updateMenuOptionGroup(menuOptionGroupVO);
    }

    @DisplayName("[200_OK] - 메뉴 옵션그룹 연결 후 모달 뷰 전송")
    @WithAuthUser
    @Test
    void 메뉴옵션그룹연결() throws Exception {
        // given
        List<MenuOptionGroupVO> menuOptionGroupVOList = List.of(createMenuOptionGroupVO(menuOptionGroupId1));
        List<MenuOptionGroupMenuVO> menuOptionGroupMenuVOList = List.of(
                createMenuOptionGroupMenuVO(menuOptionGroupMenuId));
        given(menuOptionGroupService.selectAllMenuOptionGroup(any(User.class))).willReturn(menuOptionGroupVOList);
        given(menuOptionGroupService.selectMenuOptionGroupConnectMenu(menuId1)).willReturn(menuOptionGroupMenuVOList);

        // when, then
        mockMvc.perform(post(commonURL + "/menuOptionGroup").with(csrf())
                        .param("menuId", menuId1.toString()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuOptionGroup"))
                .andExpect(model().attribute("menuOptionGroupVOList", menuOptionGroupVOList))
                .andExpect(model().attribute("menuOptionGroupMenuVOList", menuOptionGroupMenuVOList))
                .andExpect(view().name("/fragments/owner/menu/optionGroupConnectModal"));

        then(menuOptionGroupService).should().selectAllMenuOptionGroup(any(User.class));
        then(menuOptionGroupService).should().selectMenuOptionGroupConnectMenu(menuId1);
    }

    @DisplayName("[200_OK] - 메뉴와 연결된 옵션 그룹을 조회 후 뷰로 전송")
    @WithAuthUser
    @Test
    void 메뉴와연결된옵션그룹조회() throws Exception {
        // given
        List<MenuVO> menuVOList = Arrays.asList(createMenuVO(menuId1), createMenuVO(menuId2));
        given(menuOptionGroupService.selectMenuOptionGroupConnectedMenu(menuOptionGroupId1)).willReturn(menuVOList);

        // when, then
        mockMvc.perform(get(commonURL + "/menuOptionGroup/connectedMenu").with(csrf())
                        .param("menuOptionGroupId", menuOptionGroupId1.toString()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuOptionGroupConnectedMenu"))
                .andExpect(jsonPath("$[0].menuId").value(menuVOList.get(0).getMenuId()))
                .andExpect(jsonPath("$[1].menuId").value(menuVOList.get(1).getMenuId()));

        then(menuOptionGroupService).should().selectMenuOptionGroupConnectedMenu(menuOptionGroupId1);
    }

    @DisplayName("[200_OK] - 메뉴 옵션 그룹 삭제 후 메세지 뷰로 이동")
    @WithAuthUser
    @Test
    void 메뉴옵션그룹삭제() throws Exception {
        // given
        willDoNothing().given(menuOptionGroupService).deleteMenuOptionGroup(menuOptionGroupId1);

        // when, then
        mockMvc.perform(post(commonURL + "/menuOptionGroup/delete.do").with(csrf())
                        .param("menuOptionGroupId", menuOptionGroupId1.toString()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuOptionGroupDelete"))
                .andExpect(model().attribute("type", MessageType.MSG_URL.messageType()))
                .andExpect(model().attribute("message", Message.SUCCESS_DELETE.message()))
                .andExpect(model().attribute("returnUrl", URL.MENU_INFO.url()))
                .andExpect(view().name(URL.MESSAGE.url()));

        then(menuOptionGroupService).should().deleteMenuOptionGroup(menuOptionGroupId1);
    }
}