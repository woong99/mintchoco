package com.woong.mintchoco.owner.menu.repository.group;

import static org.assertj.core.api.Assertions.assertThat;

import com.woong.mintchoco.common.QueryDslConfig;
import com.woong.mintchoco.global.config.JpaAuditingConfig;
import com.woong.mintchoco.global.file.entity.AttachFile;
import com.woong.mintchoco.global.file.repository.FileRepository;
import com.woong.mintchoco.owner.menu.entity.Menu;
import com.woong.mintchoco.owner.menu.entity.MenuGroup;
import com.woong.mintchoco.owner.menu.entity.MenuOptionGroupMenu;
import com.woong.mintchoco.owner.menu.model.MenuGroupVO;
import com.woong.mintchoco.owner.menu.repository.MenuRepository;
import com.woong.mintchoco.owner.menu.repository.option.group.menu.MenuOptionGroupMenuRepository;
import com.woong.mintchoco.owner.store.entity.Store;
import com.woong.mintchoco.owner.store.repository.StoreRepository;
import jakarta.persistence.EntityManager;
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

@DisplayName("[REPOSITORY 단위 테스트] menuGroupRepository")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import({QueryDslConfig.class, JpaAuditingConfig.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
class MenuGroupRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuOptionGroupMenuRepository menuOptionGroupMenuRepository;

    @Nested
    @DisplayName("findLastGroupOrder")
    class findLastGroupOrder {
        @DisplayName("메뉴 그룹이 있는 경우 마지막 groupOrder 반환")
        @Test
        void 마지막_groupOrder_조회() {
            // given
            Store store = Store.builder().id(1L).build();
            store = storeRepository.save(store);
            MenuGroup menuGroup = MenuGroup.builder().id(1L).groupOrder(1).store(store).build();
            menuGroupRepository.save(menuGroup);

            // when
            int lastGroupOrder = menuGroupRepository.findLastGroupOrder(store.getId());

            // then
            assertThat(lastGroupOrder).isEqualTo(1);
        }

        @DisplayName("메뉴 그룹이 없는 경우 0 반환")
        @Test
        void 마지막_groupOrder_조회_0() {
            // given
            Store store = Store.builder().id(1L).build();
            store = storeRepository.save(store);

            // when
            int lastGroupOrder = menuGroupRepository.findLastGroupOrder(store.getId());

            // then
            assertThat(lastGroupOrder).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("updateMenuGroup")
    class updateMenuGroup {
        @DisplayName("메뉴 그룹을 업데이트")
        @Test
        void 메뉴그룹업데이트() {
            // given
            MenuGroup menuGroup = MenuGroup.builder()
                    .id(1L)
                    .title("beforeTitle")
                    .explanation("beforeExplanation")
                    .exposure("beforeExposure")
                    .build();
            menuGroup = menuGroupRepository.save(menuGroup);
            MenuGroupVO menuGroupVO = MenuGroupVO.builder()
                    .id(menuGroup.getId())
                    .menuGroupTitle("afterTitle")
                    .menuGroupExplanation("afterExplanation")
                    .menuGroupExposure("afterExposure")
                    .build();

            // when
            menuGroupRepository.updateMenuGroup(menuGroupVO);
            em.clear();

            // then
            MenuGroup thenMenuGroup = menuGroupRepository.findById(menuGroup.getId()).orElseThrow();
            assertThat(thenMenuGroup.getTitle()).isEqualTo("afterTitle");
            assertThat(thenMenuGroup.getExplanation()).isEqualTo("afterExplanation");
            assertThat(thenMenuGroup.getExposure()).isEqualTo("afterExposure");
        }
    }

    @Nested
    @DisplayName("selectAllMenuGroupWithMenu")
    class selectAllMenuGroupWithMenu {
        @DisplayName("메뉴 그룹을 조회하는데 메뉴가 존재하면 메뉴도 조회")
        @Test
        void 메뉴_및_메뉴그룹_조회() {
            // given
            Store store = Store.builder().id(1L).build();
            store = storeRepository.save(store);
            Menu menu = Menu.builder().id(1L).build();
            menu = menuRepository.save(menu);
            MenuGroup menuGroup = MenuGroup.builder().id(1L).store(store).menus(List.of(menu)).build();
            menuGroupRepository.save(menuGroup);

            // when
            List<MenuGroup> menuGroupList = menuGroupRepository.selectAllMenuGroupWithMenu(store.getId());

            // then
            assertThat(menuGroupList).hasSize(1);
            assertThat(menuGroupList.get(0).getMenus()).hasSize(1);
        }

        @DisplayName("메뉴 그룹을 조회하는데 메뉴가 없으면 메뉴 그룹만 반환")
        @Test
        void 메뉴그룹_및_빈_메뉴_조회() {
            // given
            Store store = Store.builder().id(1L).build();
            store = storeRepository.save(store);
            MenuGroup menuGroup = MenuGroup.builder().id(1L).store(store).build();
            menuGroupRepository.save(menuGroup);

            // when
            List<MenuGroup> menuGroupList = menuGroupRepository.selectAllMenuGroupWithMenu(store.getId());

            // then
            assertThat(menuGroupList).hasSize(1);
            assertThat(menuGroupList.get(0).getMenus()).isNull();
        }
    }

    @Nested
    @DisplayName("deleteMenuGroup")
    class deleteMenuGroup {
        @DisplayName("메뉴그룹을 삭제하면 연관된 메뉴, 메뉴와 연결된 메뉴 옵션 그룹, 메뉴 이미지 삭제")
        @Test
        void 메뉴그룹_삭제() {
            // given
            MenuGroup menuGroup = MenuGroup.builder().id(1L).build();
            menuGroup = menuGroupRepository.save(menuGroup);
            AttachFile attachFile = AttachFile.builder().id(1L).build();
            attachFile = fileRepository.save(attachFile);
            Menu menu = Menu.builder().id(1L).menuGroup(menuGroup).menuImage(attachFile).build();
            menu = menuRepository.save(menu);
            MenuOptionGroupMenu menuOptionGroupMenu = MenuOptionGroupMenu.builder().id(1L).menu(menu).build();
            menuOptionGroupMenu = menuOptionGroupMenuRepository.save(menuOptionGroupMenu);

            // when
            menuGroupRepository.deleteMenuGroup(menuGroup.getId());
            em.clear();

            // then
            MenuGroup thenMenuGroup = menuGroupRepository.findById(menuGroup.getId()).orElse(null);
            AttachFile thenAttachFile = fileRepository.findById(attachFile.getId()).orElse(null);
            Menu thenMenu = menuRepository.findById(menu.getId()).orElse(null);
            MenuOptionGroupMenu thenMenuOptionGroupMenu = menuOptionGroupMenuRepository.findById(
                    menuOptionGroupMenu.getId()).orElse(null);
            assertThat(thenMenuGroup).isNull();
            assertThat(thenAttachFile).isNull();
            assertThat(thenMenu).isNull();
            assertThat(thenMenuOptionGroupMenu).isNull();
        }
    }

    @Nested
    @DisplayName("selectMenuGroupWithMenus")
    class selectMenuGroupWithMenus {
        @DisplayName("메뉴그룹과 연관된 메뉴를 조회")
        @Test
        void 메뉴그룹_및_연관된_메뉴_조회() {
            // given
            AttachFile attachFile = AttachFile.builder().id(1L).build();
            attachFile = fileRepository.save(attachFile);
            Menu menu = Menu.builder().id(1L).menuImage(attachFile).build();
            menu = menuRepository.save(menu);
            MenuGroup menuGroup = MenuGroup.builder().id(1L).menus(List.of(menu)).build();
            menuGroup = menuGroupRepository.save(menuGroup);

            // when
            MenuGroup thenMenuGroup = menuGroupRepository.selectMenuGroupWithMenus(menuGroup.getId());

            // then
            assertThat(thenMenuGroup.getId()).isEqualTo(menuGroup.getId());
            assertThat(thenMenuGroup.getMenus()).hasSize(1);
            assertThat(thenMenuGroup.getMenus().get(0).getId()).isEqualTo(menu.getId());
            assertThat(thenMenuGroup.getMenus().get(0).getMenuImage().getId()).isEqualTo(attachFile.getId());
        }
    }
}