package com.woong.mintchoco.owner.menu.controller;

import com.woong.mintchoco.global.file.entity.AttachFile;
import com.woong.mintchoco.owner.menu.model.MenuGroupVO;
import com.woong.mintchoco.owner.menu.model.MenuOptionGroupMenuVO;
import com.woong.mintchoco.owner.menu.model.MenuOptionGroupVO;
import com.woong.mintchoco.owner.menu.model.MenuOptionVO;
import com.woong.mintchoco.owner.menu.model.MenuVO;
import java.util.List;

public class BaseMenuControllerTest {

    protected final static Long menuGroupId1 = 1L;
    protected final static Long menuGroupId2 = 2L;
    protected final static Long menuOptionGroupId1 = 3L;
    protected final static Long menuOptionGroupId2 = 4L;
    protected final static Long menuId1 = 5L;
    protected final static Long menuId2 = 6L;
    protected final static Long menuOptionId1 = 7L;
    protected final static Long menuOptionId2 = 8L;
    protected final static Long attachFileId1 = 9L;
    protected final static Long attachFileId2 = 10L;
    protected final static Long menuOptionGroupMenuId = 11L;
    protected final static String commonURL = "/owner/menu/info";

    protected MenuGroupVO createMenuGroupVO(Long menuGroupId) {
        return MenuGroupVO.builder()
                .id(menuGroupId)
                .build();
    }

    protected MenuOptionGroupVO createMenuOptionGroupVO(Long menuOptionGroupId) {
        return MenuOptionGroupVO.builder()
                .menuOptionGroupId(menuOptionGroupId)
                .build();
    }

    protected MenuOptionGroupVO createMenuOptionGroupVO(Long menuOptionGroupId, List<MenuOptionVO> menuOptionVOList) {
        return MenuOptionGroupVO.builder()
                .menuOptionGroupId(menuOptionGroupId)
                .menuOptionVOList(menuOptionVOList)
                .build();
    }

    protected MenuVO createMenuVO(Long menuId) {
        return MenuVO.builder()
                .menuId(menuId)
                .build();
    }

    protected MenuVO createMenuVO(Long menuId, List<MenuOptionGroupVO> menuOptionGroupVOList) {
        return MenuVO.builder()
                .menuId(menuId)
                .menuOptionGroupVOS(menuOptionGroupVOList)
                .build();
    }

    protected MenuVO createMenuVO(Long menuId, AttachFile attachFile) {
        return MenuVO.builder()
                .menuId(menuId)
                .menuImage(attachFile)
                .build();
    }

    protected MenuOptionVO createMenuOptionVO(Long menuOptionId) {
        return MenuOptionVO.builder()
                .menuOptionId(menuOptionId)
                .build();
    }

    protected AttachFile createAttachFile(Long attachFileId) {
        return AttachFile.builder()
                .id(attachFileId)
                .build();
    }

    protected MenuOptionGroupMenuVO createMenuOptionGroupMenuVO(Long menuOptionGroupMenuId) {
        return MenuOptionGroupMenuVO.builder()
                .id(menuOptionGroupMenuId)
                .build();
    }
}
