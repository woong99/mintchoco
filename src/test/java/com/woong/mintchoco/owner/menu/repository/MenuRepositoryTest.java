package com.woong.mintchoco.owner.menu.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woong.mintchoco.common.QueryDslConfig;
import com.woong.mintchoco.global.config.JpaAuditingConfig;
import com.woong.mintchoco.global.file.entity.AttachFile;
import com.woong.mintchoco.global.file.repository.FileRepository;
import com.woong.mintchoco.owner.menu.entity.Menu;
import com.woong.mintchoco.owner.menu.entity.MenuGroup;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroup;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroupMenu;
import com.woong.mintchoco.owner.menu.model.MenuVO;
import com.woong.mintchoco.owner.menu.repository.group.MenuGroupRepository;
import com.woong.mintchoco.owner.menu.repository.option.group.MenuOptionGroupRepository;
import com.woong.mintchoco.owner.menu.repository.option.group.menu.MenuOptionGroupMenuRepository;
import jakarta.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("[REPOSITORY 단위 테스트] menuRepository")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import({QueryDslConfig.class, JpaAuditingConfig.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
class MenuRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuOptionGroupRepository menuOptionGroupRepository;
    @Autowired
    private MenuOptionGroupMenuRepository menuOptionGroupMenuRepository;

    @Nested
    @DisplayName("findByMenuGroupIdOrderByMenuOrder")
    class findByMenuGroupIdOrderByMenuOrder {
        @DisplayName("메뉴그룹 ID로 메뉴를 menuOrder 순으로 조회")
        @Test
        void 메뉴그룹ID로메뉴조회() {
            // given
            MenuGroup menuGroup = MenuGroup.builder().id(1L).build();
            menuGroup = menuGroupRepository.save(menuGroup);
            Menu menu1 = Menu.builder().id(1L).menuGroup(menuGroup).menuOrder(1).build();
            Menu menu2 = Menu.builder().id(2L).menuGroup(menuGroup).menuOrder(2).build();
            menuRepository.saveAll(Arrays.asList(menu1, menu2));

            // when
            List<Menu> menus = menuRepository.findByMenuGroupIdOrderByMenuOrder(menuGroup.getId());

            // then
            assertThat(menus).hasSize(2);
            assertThat(menus.get(0).getMenuGroup().getId()).isEqualTo(menuGroup.getId());
            assertThat(menus.get(1).getMenuGroup().getId()).isEqualTo(menuGroup.getId());
            assertThat(menus.get(0).getMenuOrder()).isLessThan(menus.get(1).getMenuOrder());
        }
    }

    @Nested
    @DisplayName("findLastMenuOrder")
    class findLastMenuOrder {
        @DisplayName("메뉴가 있는 경우 마지막 menuOrder 반환")
        @Test
        void 마지막menuOrder조회() {
            // given
            MenuGroup menuGroup = MenuGroup.builder().id(1L).build();
            menuGroup = menuGroupRepository.save(menuGroup);
            Menu menu1 = Menu.builder().id(1L).menuGroup(menuGroup).menuOrder(1).build();
            Menu menu2 = Menu.builder().id(2L).menuGroup(menuGroup).menuOrder(2).build();
            menuRepository.saveAll(Arrays.asList(menu1, menu2));

            // when
            int lastOrder = menuRepository.findLastMenuOrder(menuGroup.getId());

            // then
            assertThat(lastOrder).isEqualTo(2);
        }

        @DisplayName("메뉴가 없는 경우 0 반환")
        @Test
        void 마지막_menuOrder_조회_0() {
            // given
            MenuGroup menuGroup = MenuGroup.builder().id(1L).build();
            menuGroup = menuGroupRepository.save(menuGroup);

            // when
            int lastOrder = menuRepository.findLastMenuOrder(menuGroup.getId());

            // then
            assertThat(lastOrder).isZero();
        }
    }

    @Nested
    @DisplayName("updateMenu")
    class updateMenu {
        @DisplayName("메뉴 업데이트")
        @Test
        void 메뉴업데이트() {
            // given
            Menu menu = Menu.builder()
                    .title("title")
                    .menuOrder(1)
                    .explanation("explanation")
                    .exposure("Y")
                    .price(1000)
                    .build();
            menu = menuRepository.save(menu);
            MenuVO menuVO = MenuVO.builder()
                    .menuId(menu.getId())
                    .menuTitle("afterTitle")
                    .menuExplanation("afterExplanation")
                    .menuExposure("N")
                    .menuPrice(2000)
                    .build();

            // when
            menuRepository.updateMenu(menuVO);
            em.clear();

            // then
            Menu afterMenu = menuRepository.findById(menu.getId()).orElseThrow();
            assertThat(afterMenu.getTitle()).isEqualTo("afterTitle");
            assertThat(afterMenu.getExplanation()).isEqualTo("afterExplanation");
            assertThat(afterMenu.getExposure()).isEqualTo("N");
            assertThat(afterMenu.getPrice()).isEqualTo(2000);
        }
    }

    @Nested
    @DisplayName("selectMenuOptionGroupConnectedMenu")
    class selectMenuOptionGroupConnectedMenu {
        @DisplayName("메뉴옵션그룹 ID를 통해 메뉴 및 연관된 메뉴 옵션 그룹을 조인해서 조회")
        @Test
        void 메뉴및연관된메뉴옵션그룹조회() {
            // given
            Menu menu1 = Menu.builder()
                    .build();
            menu1 = menuRepository.save(menu1);
            Menu menu2 = Menu.builder()
                    .build();
            menu2 = menuRepository.save(menu2);
            MenuOptionGroup menuOptionGroup = MenuOptionGroup.builder()
                    .build();
            menuOptionGroup = menuOptionGroupRepository.save(menuOptionGroup);
            MenuOptionGroupMenu menuOptionGroupMenu1 = MenuOptionGroupMenu.builder()
                    .menu(menu1)
                    .menuOptionGroup(menuOptionGroup)
                    .build();
            MenuOptionGroupMenu menuOptionGroupMenu2 = MenuOptionGroupMenu.builder()
                    .menu(menu2)
                    .menuOptionGroup(menuOptionGroup)
                    .build();
            menuOptionGroupMenuRepository.saveAll(Arrays.asList(menuOptionGroupMenu1, menuOptionGroupMenu2));

            // when
            List<Menu> menus = menuRepository.selectMenuOptionGroupConnectedMenu(menuOptionGroup.getId());

            // then
            assertThat(menus).hasSize(2);
            assertThat(menus.get(0).getId()).isEqualTo(menu1.getId());
            assertThat(menus.get(1).getId()).isEqualTo(menu2.getId());
        }
    }

    @Nested
    @DisplayName("deleteMenu")
    class deleteMenu {
        @DisplayName("메뉴 ID로 메뉴 및 연결된 메뉴 옵션 그룹 정보 삭제")
        @Test
        void 메뉴삭제() {
            // given
            Menu menu = Menu.builder().build();
            menu = menuRepository.save(menu);
            MenuOptionGroupMenu menuOptionGroupMenu = MenuOptionGroupMenu.builder()
                    .menu(menu).build();
            menuOptionGroupMenuRepository.save(menuOptionGroupMenu);

            // when
            menuRepository.deleteMenu(menu.getId());

            // then
            List<Menu> menus = menuRepository.findAll();
            List<MenuOptionGroupMenu> menuOptionGroupMenus = menuOptionGroupMenuRepository.findAll();
            assertThat(menus).isEmpty();
            assertThat(menuOptionGroupMenus).isEmpty();
        }
    }

    @Nested
    @DisplayName("selectMenuWithMenuImage")
    class selectMenuWithMenuImage {
        @DisplayName("메뉴 ID를 통해 메뉴 및 메뉴 이미지를 조회")
        @Test
        void 메뉴와메뉴이미지조회() {
            // given
            AttachFile attachFile = AttachFile.builder()
                    .savedName("savedName")
                    .build();
            attachFile = fileRepository.save(attachFile);
            Menu menu = Menu.builder()
                    .title("title")
                    .menuImage(attachFile)
                    .build();
            menu = menuRepository.save(menu);

            // when
            Menu savedMenu = menuRepository.selectMenuWithMenuImage(menu.getId());

            // then
            assertThat(savedMenu.getId()).isEqualTo(menu.getId());
            assertThat(savedMenu.getTitle()).isEqualTo("title");
            assertThat(savedMenu.getMenuImage()).isNotNull();
            assertThat(savedMenu.getMenuImage().getSavedName()).isEqualTo("savedName");
        }
    }

    @Nested
    @DisplayName("insertMenuImage")
    class insertMenuImage {
        @DisplayName("메뉴에 이미지를 추가해 저장한다.")
        @Test
        void 메뉴이미지저장() {
            // given
            Menu menu = Menu.builder().build();
            menu = menuRepository.save(menu);
            AttachFile attachFile = AttachFile.builder()
                    .savedName("savedName")
                    .build();
            attachFile = fileRepository.save(attachFile);

            // when
            menuRepository.insertMenuImage(attachFile, menu.getId());
            em.clear();

            // then
            Menu thenMenu = menuRepository.findById(menu.getId()).orElseThrow();
            assertThat(thenMenu.getMenuImage()).isNotNull();
            assertThat(thenMenu.getMenuImage().getSavedName()).isEqualTo("savedName");
        }
    }

    @Nested
    @DisplayName("deleteMenuImage")
    class deleteMenuImage {
        @DisplayName("몌뉴의 이미지를 삭제한다.")
        @Test
        void 메뉴이미지삭제() {
            // given
            AttachFile givenAttachFile = AttachFile.builder().id(1L).build();
            givenAttachFile = fileRepository.save(givenAttachFile);
            Menu givenMenu = Menu.builder()
                    .id(1L)
                    .menuImage(givenAttachFile)
                    .build();
            givenMenu = menuRepository.save(givenMenu);

            // when
            menuRepository.deleteMenuImage(givenMenu.getId());
            em.clear();

            // then
            Menu thenMenu = menuRepository.findById(givenMenu.getId()).orElse(null);
            AttachFile thenAttachFile = fileRepository.findById(givenAttachFile.getId()).orElse(null);
            assertThat(thenMenu).isNotNull();
            assertThat(thenMenu.getMenuImage()).isNull();
            assertThat(thenAttachFile).isNull();
        }
    }
}