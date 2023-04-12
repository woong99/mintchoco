package com.woong.mintchoco.owner.menu.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
import com.woong.mintchoco.global.file.entity.AttachFile;
import com.woong.mintchoco.global.file.exception.UploadFileErrorException;
import com.woong.mintchoco.global.file.exception.UploadFileNotFoundException;
import com.woong.mintchoco.owner.menu.exception.MenuNotFoundException;
import com.woong.mintchoco.owner.menu.model.MenuGroupVO;
import com.woong.mintchoco.owner.menu.model.MenuOptionGroupVO;
import com.woong.mintchoco.owner.menu.model.MenuOptionVO;
import com.woong.mintchoco.owner.menu.model.MenuVO;
import com.woong.mintchoco.owner.menu.service.MenuGroupService;
import com.woong.mintchoco.owner.menu.service.MenuOptionGroupService;
import com.woong.mintchoco.owner.menu.service.MenuService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("[CONTROLLER 단위 테스트] menuController")
@WebMvcTest(MenuController.class)
class MenuControllerTest extends BaseMenuControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MenuService menuService;
    @MockBean
    private MenuGroupService menuGroupService;
    @MockBean
    private MenuOptionGroupService menuOptionGroupService;

    @DisplayName("[200_OK] - 메뉴 정보와 옵션 정보를 조회해 메뉴 관리 뷰로 이동하는 컨트롤러 검증")
    @WithAuthUser
    @Test
    void 메뉴관리() throws Exception {
        // given
        List<MenuGroupVO> menuGroupVOList = Arrays.asList(createMenuGroupVO(menuGroupId1),
                createMenuGroupVO(menuGroupId2));
        given(menuGroupService.selectAllMenuGroup(any(User.class))).willReturn(menuGroupVOList);

        List<MenuOptionGroupVO> menuOptionGroupVOList = Arrays.asList(createMenuOptionGroupVO(menuOptionGroupId1),
                createMenuOptionGroupVO(menuOptionGroupId2));
        given(menuOptionGroupService.selectAllMenuOptionGroupWithMenuOption(any(User.class))).willReturn(
                menuOptionGroupVOList);

        // when, then
        mockMvc.perform(post(commonURL)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuInfo"))
                .andExpect(view().name("/views/owner/menu/menuInfo"))
                .andExpect(model().attribute("menuGroupVOList", menuGroupVOList))
                .andExpect(model().attribute("menuOptionGroupVOList", menuOptionGroupVOList));

        then(menuGroupService).should().selectAllMenuGroup(any(User.class));
        then(menuOptionGroupService).should().selectAllMenuOptionGroupWithMenuOption(any(User.class));
    }

    @DisplayName("[200_OK] - 메뉴 정보는 없고 옵션 정보만 존재하는 경우 조회 후 메뉴 관리 뷰로 이동하는 컨트롤러 검증")
    @WithAuthUser
    @Test
    void 메뉴관리_메뉴그룹X() throws Exception {
        // given
        given(menuGroupService.selectAllMenuGroup(any(User.class))).willReturn(List.of());

        List<MenuOptionGroupVO> menuOptionGroupVOList = Arrays.asList(createMenuOptionGroupVO(menuOptionGroupId1),
                createMenuOptionGroupVO(menuOptionGroupId2));
        given(menuOptionGroupService.selectAllMenuOptionGroupWithMenuOption(any(User.class))).willReturn(
                menuOptionGroupVOList);

        // when, then
        mockMvc.perform(post(commonURL).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuInfo"))
                .andExpect(view().name("/views/owner/menu/menuInfo"))
                .andExpect(model().attribute("menuGroupVOList", List.of()))
                .andExpect(model().attribute("menuOptionGroupVOList", menuOptionGroupVOList));

        then(menuGroupService).should().selectAllMenuGroup(any(User.class));
        then(menuOptionGroupService).should().selectAllMenuOptionGroupWithMenuOption(any(User.class));
    }

    @DisplayName("[200_OK] - 메뉴 정보는 있고 옵션 정보가 없는 경우 조회 후 메뉴 관리 뷰로 이동하는 컨트롤러 검증")
    @WithAuthUser
    @Test
    void 메뉴관리_옵션그룹X() throws Exception {
        // given
        List<MenuGroupVO> menuGroupVOList = Arrays.asList(createMenuGroupVO(menuGroupId1),
                createMenuGroupVO(menuGroupId2));
        given(menuGroupService.selectAllMenuGroup(any(User.class))).willReturn(menuGroupVOList);

        given(menuOptionGroupService.selectAllMenuOptionGroupWithMenuOption(any(User.class))).willReturn(
                List.of());

        // when, then
        mockMvc.perform(post(commonURL).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuInfo"))
                .andExpect(view().name("/views/owner/menu/menuInfo"))
                .andExpect(model().attribute("menuGroupVOList", menuGroupVOList))
                .andExpect(model().attribute("menuOptionGroupVOList", List.of()));

        then(menuGroupService).should().selectAllMenuGroup(any(User.class));
        then(menuOptionGroupService).should().selectAllMenuOptionGroupWithMenuOption(any(User.class));
    }

    @DisplayName("[200_OK] - 메뉴 등록 후 메세지 뷰로 이동")
    @WithAuthUser
    @Test
    void 메뉴등록() throws Exception {
        // given
        MenuVO menuVO = createMenuVO(menuId1);
        willDoNothing().given(menuService).insertMenu(menuGroupId1, menuVO);
        // when, then
        mockMvc.perform(post(commonURL + "/menu/insert.do").with(csrf())
                        .param("menuGroupId", menuGroupId1.toString())
                        .flashAttr("menuVO", menuVO))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuInsert"))
                .andExpect(model().attribute("type", MessageType.MSG_URL.messageType()))
                .andExpect(model().attribute("message", Message.SUCCESS_INSERT.message()))
                .andExpect(model().attribute("returnUrl", URL.MENU_INFO.url()))
                .andExpect(view().name(URL.MESSAGE.url()));

        then(menuService).should().insertMenu(menuGroupId1, menuVO);
    }

    @DisplayName("[200_OK] - 메뉴 ID로 메뉴를 조회해 뷰로 전달")
    @WithAuthUser
    @Test
    void 메뉴단일조회() throws Exception {
        // given
        MenuVO menuVO = createMenuVO(menuId1);
        given(menuService.selectMenu(menuId1)).willReturn(menuVO);

        // when, then
        mockMvc.perform(get(commonURL + "/menu/select.do").with(csrf())
                        .param("menuId", menuId1.toString()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuSelect"))
                .andExpect(jsonPath("$.menuId").value(menuVO.getMenuId()));

        then(menuService).should().selectMenu(menuId1);
    }

    @DisplayName("[404_ERROR] - 존재하지 않는 메뉴 ID를 전달한 경우")
    @WithAuthUser
    @Test
    void 메뉴단일조회_실패() throws Exception {
        // given
        given(menuService.selectMenu(menuId1)).willThrow(new MenuNotFoundException(ErrorCode.MENU_NOT_FOUND));

        // when, then
        mockMvc.perform(get(commonURL + "/menu/select.do").with(csrf())
                        .param("menuId", menuId1.toString()))
                .andExpect(status().isNotFound())
                .andExpect(handler().methodName("menuSelect"));

        then(menuService).should().selectMenu(menuId1);
    }

    @DisplayName("[200_OK] - 메뉴 수정 후 메세지 뷰로 이동")
    @WithAuthUser
    @Test
    void 메뉴수정() throws Exception {
        // given
        MenuVO menuVO = createMenuVO(menuId1);
        willDoNothing().given(menuService).updateMenu(menuVO);

        // when, then
        mockMvc.perform(post(commonURL + "/menu/update.do").with(csrf())
                        .flashAttr("menuVO", menuVO))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuUpdate"))
                .andExpect(model().attribute("type", MessageType.MSG_URL.messageType()))
                .andExpect(model().attribute("message", Message.SUCCESS_UPDATE.message()))
                .andExpect(model().attribute("returnUrl", URL.MENU_INFO.url()))
                .andExpect(view().name(URL.MESSAGE.url()));

        then(menuService).should().updateMenu(menuVO);
    }

    @DisplayName("[200_OK] - 메뉴 다수 조회 후 뷰로 전달")
    @WithAuthUser
    @Test
    void 메뉴다수조회() throws Exception {
        // given
        MenuVO menuVO1 = createMenuVO(menuId1);
        MenuVO menuVO2 = createMenuVO(menuId2);
        given(menuService.selectMenus(menuGroupId1)).willReturn(Arrays.asList(menuVO1, menuVO2));

        // when, then
        mockMvc.perform(get(commonURL + "/menus/select.do").with(csrf())
                        .param("menuGroupId", menuGroupId1.toString()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menusSelect"))
                .andExpect(jsonPath("$[0].menuId").value(menuVO1.getMenuId()))
                .andExpect(jsonPath("$[1].menuId").value(menuVO2.getMenuId()));

        then(menuService).should().selectMenus(menuGroupId1);
    }

    @DisplayName("[200_OK] - 메뉴 순서 변경 후 메세지 뷰로 이동")
    @WithAuthUser
    @Test
    void 메뉴순서변경() throws Exception {
        // given
        Long[] menuIdList = {menuId1, menuId2};
        willDoNothing().given(menuService).updateMenusOrder(menuIdList);

        // when, then
        mockMvc.perform(post(commonURL + "/menus/orderUpdate.do").with(csrf())
                        .param("menuIdList", menuIdList[0].toString())
                        .param("menuIdList", menuIdList[1].toString()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menusOrderUpdate"))
                .andExpect(model().attribute("type", MessageType.MSG_URL.messageType()))
                .andExpect(model().attribute("message", Message.SUCCESS_SAVE.message()))
                .andExpect(model().attribute("returnUrl", URL.MENU_INFO.url()))
                .andExpect(view().name(URL.MESSAGE.url()));

        then(menuService).should().updateMenusOrder(menuIdList);
    }

    @DisplayName("[200_OK] - 메뉴 삭제 후 메세지 뷰로 이동")
    @WithAuthUser
    @Test
    void 메뉴삭제() throws Exception {
        // given
        willDoNothing().given(menuService).deleteMenu(menuId1);

        // when, then
        mockMvc.perform(post(commonURL + "/menu/delete.do").with(csrf())
                        .param("menuId", menuId1.toString()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuDelete"))
                .andExpect(model().attribute("type", MessageType.MSG_URL.messageType()))
                .andExpect(model().attribute("message", Message.SUCCESS_DELETE.message()))
                .andExpect(model().attribute("returnUrl", URL.MENU_INFO.url()))
                .andExpect(view().name(URL.MESSAGE.url()));

        then(menuService).should().deleteMenu(menuId1);
    }

    @DisplayName("[200_OK] - 메뉴 옵션그룹 등록 후 메세지 뷰로 이동")
    @WithAuthUser
    @Test
    void 메뉴옵션그룹연결() throws Exception {
        // given
        Long[] optionGroupIdList = {menuOptionGroupId1, menuOptionGroupId2};
        willDoNothing().given(menuService).insertMenuOptionGroupConnectMenu(optionGroupIdList, menuId1);

        // when, then
        mockMvc.perform(post(commonURL + "/menu/connectOptionGroup").with(csrf())
                        .param("optionGroupIdList", optionGroupIdList[0].toString())
                        .param("optionGroupIdList", optionGroupIdList[1].toString())
                        .param("menuId", menuId1.toString()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("connectOptionGroup"))
                .andExpect(model().attribute("type", MessageType.MSG_URL.messageType()))
                .andExpect(model().attribute("message", Message.SUCCESS_SAVE.message()))
                .andExpect(model().attribute("returnUrl", URL.MENU_INFO.url()))
                .andExpect(view().name(URL.MESSAGE.url()));

        then(menuService).should().insertMenuOptionGroupConnectMenu(optionGroupIdList, menuId1);
    }

    @DisplayName("[200_OK] - 메뉴와 연결된 메뉴 옵션 그룹, 연결된 메뉴 옵션을 조회 후 모델에 담아 뷰로 전달")
    @WithAuthUser
    @Test
    void 메뉴미리보기() throws Exception {
        // given
        MenuOptionVO menuOptionVO1 = createMenuOptionVO(menuOptionId1);
        MenuOptionVO menuOptionVO2 = createMenuOptionVO(menuOptionId2);
        MenuOptionGroupVO menuOptionGroupVO = createMenuOptionGroupVO(menuOptionGroupId1,
                Arrays.asList(menuOptionVO1, menuOptionVO2));
        MenuVO menuVO = createMenuVO(menuId1, List.of(menuOptionGroupVO));
        given(menuService.selectMenuWithMenuOptions(menuId1)).willReturn(menuVO);

        // when, then
        mockMvc.perform(post(commonURL + "/menu/preview").with(csrf())
                        .param("menuId", menuId1.toString()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuPreview"))
                .andExpect(model().attribute("menuVO", menuVO))
                .andExpect(view().name("/fragments/owner/menu/menuPreviewModal"));

        then(menuService).should().selectMenuWithMenuOptions(menuId1);
    }

    @DisplayName("[200_OK] - 메뉴와 메뉴 이미지를 조회 후 모델에 담아 뷰로 전달")
    @WithAuthUser
    @Test
    void 메뉴이미지설정() throws Exception {
        // given
        AttachFile attachFile = createAttachFile(attachFileId1);
        MenuVO menuVO = createMenuVO(menuId1, attachFile);
        given(menuService.selectMenuWithMenuImage(menuId1)).willReturn(menuVO);

        // when, then
        mockMvc.perform(post(commonURL + "/menu/image").with(csrf())
                        .param("menuId", menuId1.toString()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuImage"))
                .andExpect(model().attribute("menuVO", menuVO))
                .andExpect(view().name("/fragments/owner/menu/menuImageModal"));

        then(menuService).should().selectMenuWithMenuImage(menuId1);
    }

    @DisplayName("[200_OK] - 메뉴 이미지 정상적으로 저장 후 메세지 뷰로 이동")
    @WithAuthUser
    @Test
    void 메뉴이미지저장() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "Test Image".getBytes());
        willDoNothing().given(menuService).insertMenuImage(menuId1, file);

        // when, then
        mockMvc.perform(multipart(commonURL + "/menu/image/update.do")
                        .file(file).contentType(MediaType.MULTIPART_FORM_DATA).with(csrf())
                        .param("menuId", menuId1.toString()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuImageUpdate"))
                .andExpect(model().attribute("type", MessageType.MSG_URL.messageType()))
                .andExpect(model().attribute("message", Message.SUCCESS_SAVE.message()))
                .andExpect(model().attribute("returnUrl", URL.MENU_INFO.url()))
                .andExpect(view().name(URL.MESSAGE.url()));

        then(menuService).should().insertMenuImage(menuId1, file);
    }

    @DisplayName("[200_OK] - 메뉴 이미지 파일이 없는 경우 예외 처리 후 메세지 뷰로 이동")
    @WithAuthUser
    @Test
    void 메뉴이미지저장_파일이없는경우() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "Test Image".getBytes());
        willThrow(new UploadFileNotFoundException(ErrorCode.UPLOAD_FILE_NOT_FOUND)).given(menuService)
                .insertMenuImage(menuId1, file);

        // when, then
        mockMvc.perform(multipart(commonURL + "/menu/image/update.do")
                        .file(file).contentType(MediaType.MULTIPART_FORM_DATA).with(csrf())
                        .param("menuId", menuId1.toString()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuImageUpdate"))
                .andExpect(model().attribute("type", MessageType.MSG_BACK.messageType()))
                .andExpect(model().attribute("message", ErrorCode.UPLOAD_FILE_NOT_FOUND.Message()))
                .andExpect(model().attribute("returnUrl", ""))
                .andExpect(view().name(URL.MESSAGE.url()));

        then(menuService).should().insertMenuImage(menuId1, file);
    }

    @DisplayName("[200_OK] - 메뉴 이미지 저장에 실패한 경우 예외 처리 후 메세지 뷰로 이동")
    @WithAuthUser
    @Test
    void 메뉴이미지저장_파일저장실패() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "Test Image".getBytes());
        willThrow(new UploadFileErrorException(ErrorCode.UPLOAD_FILE_ERROR)).given(menuService)
                .insertMenuImage(menuId1, file);

        // when, then
        mockMvc.perform(multipart(commonURL + "/menu/image/update.do")
                        .file(file).contentType(MediaType.MULTIPART_FORM_DATA).with(csrf())
                        .param("menuId", menuId1.toString()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuImageUpdate"))
                .andExpect(model().attribute("type", MessageType.MSG_BACK.messageType()))
                .andExpect(model().attribute("message", ErrorCode.UPLOAD_FILE_ERROR.Message()))
                .andExpect(model().attribute("returnUrl", ""))
                .andExpect(view().name(URL.MESSAGE.url()));

        then(menuService).should().insertMenuImage(menuId1, file);
    }

    @DisplayName("[200_OK] - 메뉴 이미지 삭제 후 메세지 뷰로 이동")
    @WithAuthUser
    @Test
    void 메뉴이미지삭제() throws Exception {
        // given
        willDoNothing().given(menuService).deleteMenuImage(menuId1);

        // when, then
        mockMvc.perform(post(commonURL + "/menu/image/delete.do").with(csrf())
                        .param("menuId", menuId1.toString()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("menuImageDelete"))
                .andExpect(model().attribute("type", MessageType.MSG_URL.messageType()))
                .andExpect(model().attribute("message", Message.SUCCESS_DELETE.message()))
                .andExpect(model().attribute("returnUrl", URL.MENU_INFO.url()))
                .andExpect(view().name(URL.MESSAGE.url()));

        then(menuService).should().deleteMenuImage(menuId1);
    }
}