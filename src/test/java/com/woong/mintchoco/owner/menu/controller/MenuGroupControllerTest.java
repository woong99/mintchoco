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
import com.woong.mintchoco.owner.menu.exception.MenuGroupNotFoundException;
import com.woong.mintchoco.owner.menu.model.MenuGroupVO;
import com.woong.mintchoco.owner.menu.service.MenuGroupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("[CONTROLLER 단위 테스트] menuGroupController")
@WebMvcTest(MenuGroupController.class)
class MenuGroupControllerTest extends BaseMenuControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MenuGroupService menuGroupService;


    @DisplayName("[200_OK] - 메뉴 그룹 등록 후 메세지 뷰로 이동")
    @WithAuthUser
    @Test
    void 메뉴그룹등록() throws Exception {
        MenuGroupVO menuGroupVO = createMenuGroupVO(menuGroupId1);
        willDoNothing().given(menuGroupService).insertMenuGroup(any(User.class), eq(menuGroupVO));

        // when, then
        mockMvc.perform(post(commonURL + "/menuGroup/insert.do").with(csrf())
                        .flashAttr("menuGroupVO", menuGroupVO))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuGroupInsert"))
                .andExpect(model().attribute("type", MessageType.MSG_URL.messageType()))
                .andExpect(model().attribute("message", Message.SUCCESS_INSERT.message()))
                .andExpect(model().attribute("returnUrl", URL.MENU_INFO.url()))
                .andExpect(view().name(URL.MESSAGE.url()));

        then(menuGroupService).should().insertMenuGroup(any(User.class), eq(menuGroupVO));
    }

    @DisplayName("[200_OK] - 메뉴그룹 단일 조회 후 뷰로 데이터 전달")
    @WithAuthUser
    @Test
    void 메뉴그룹단일조회() throws Exception {
        // given
        MenuGroupVO menuGroupVO = createMenuGroupVO(menuGroupId1);
        given(menuGroupService.selectMenuGroup(menuGroupId1)).willReturn(menuGroupVO);

        // when, then
        mockMvc.perform(get(commonURL + "/menuGroup/select.do").with(csrf())
                        .param("menuGroupId", menuGroupId1.toString()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuGroupSelect"))
                .andExpect(jsonPath("$.id").value(menuGroupVO.getId()));

        then(menuGroupService).should().selectMenuGroup(menuGroupId1);
    }

    @DisplayName("[404_ERROR] - 조회하는 메뉴그룹이 없는 경우 예외 처리 후 메세지 뷰로 이동")
    @WithAuthUser
    @Test
    void 메뉴그룹단일조회_실패() throws Exception {
        // given
        given(menuGroupService.selectMenuGroup(menuGroupId1)).willThrow(
                new MenuGroupNotFoundException(ErrorCode.MENU_GROUP_NOT_FOUND));

        // when, then
        mockMvc.perform(get(commonURL + "/menuGroup/select.do").with(csrf())
                        .param("menuGroupId", menuGroupId1.toString()))
                .andExpect(status().isNotFound());

        then(menuGroupService).should().selectMenuGroup(menuGroupId1);
    }

    @DisplayName("[200_OK] - 메뉴그룹 수정 후 메세지 뷰로 이동")
    @WithAuthUser
    @Test
    void 메뉴그룹수정() throws Exception {
        // given
        MenuGroupVO menuGroupVO = createMenuGroupVO(menuGroupId1);
        willDoNothing().given(menuGroupService).updateMenuGroup(menuGroupVO);

        // when, then
        mockMvc.perform(post(commonURL + "/menuGroup/update.do").with(csrf())
                        .flashAttr("menuGroupVO", menuGroupVO))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuGroupUpdate"))
                .andExpect(model().attribute("type", MessageType.MSG_URL.messageType()))
                .andExpect(model().attribute("message", Message.SUCCESS_UPDATE.message()))
                .andExpect(model().attribute("returnUrl", URL.MENU_INFO.url()))
                .andExpect(view().name(URL.MESSAGE.url()));

        then(menuGroupService).should().updateMenuGroup(menuGroupVO);
    }

    @DisplayName("[200_OK] - 메뉴그룹 순서변경 후 메세지 뷰로 이동")
    @WithAuthUser
    @Test
    void 메뉴그룹순서변경() throws Exception {
        // given
        Long[] menuGroupIdList = {menuGroupId1, menuGroupId2};
        willDoNothing().given(menuGroupService).updateMenuGroupOrder(menuGroupIdList);

        // when, then
        mockMvc.perform(post(commonURL + "/menuGroup/orderUpdate.do").with(csrf())
                        .param("menuGroupIdList", menuGroupIdList[0].toString())
                        .param("menuGroupIdList", menuGroupIdList[1].toString()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuGroupOrderUpdate"))
                .andExpect(model().attribute("type", MessageType.MSG_URL.messageType()))
                .andExpect(model().attribute("message", Message.SUCCESS_SAVE.message()))
                .andExpect(model().attribute("returnUrl", URL.MENU_INFO.url()))
                .andExpect(view().name(URL.MESSAGE.url()));

        then(menuGroupService).should().updateMenuGroupOrder(menuGroupIdList);
    }

    @DisplayName("[200_OK] - 메뉴그룹 삭제 후 메세지 뷰로 이동")
    @WithAuthUser
    @Test
    void 메뉴그룹삭제() throws Exception {
        // given
        willDoNothing().given(menuGroupService).deleteMenuGroup(menuGroupId1);

        // when, then
        mockMvc.perform(post(commonURL + "/menuGroup/delete.do").with(csrf())
                        .param("menuGroupId", menuGroupId1.toString()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuGroupDelete"))
                .andExpect(model().attribute("type", MessageType.MSG_URL.messageType()))
                .andExpect(model().attribute("message", Message.SUCCESS_DELETE.message()))
                .andExpect(model().attribute("returnUrl", URL.MENU_INFO.url()))
                .andExpect(view().name(URL.MESSAGE.url()));

        then(menuGroupService).should().deleteMenuGroup(menuGroupId1);
    }

    @DisplayName("[200_OK] - 메뉴그룹 조회 후 메뉴그룹 모달 뷰 전달")
    @WithAuthUser
    @Test
    void 메뉴그룹미리보기() throws Exception {
        // given
        MenuGroupVO menuGroupVO = createMenuGroupVO(menuGroupId1);
        given(menuGroupService.selectMenuGroupWithMenus(menuGroupId1)).willReturn(menuGroupVO);

        // when, then
        mockMvc.perform(post(commonURL + "/menuGroup/preview").with(csrf())
                        .param("menuGroupId", menuGroupId1.toString()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuGroupPreview"))
                .andExpect(model().attribute("menuGroupVO", menuGroupVO))
                .andExpect(view().name("/fragments/owner/menu/menuGroupPreviewModal"));

        then(menuGroupService).should().selectMenuGroupWithMenus(menuGroupId1);
    }
}