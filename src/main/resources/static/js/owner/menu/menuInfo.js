$(function () {
    const sortable = $("#sortable");

    /* 메뉴그룹 추가/수정 이름 글자수 제한 */
    $("#menuGroupTitle").keyup(function (e) {
        countContentLengthWithText($(this), $("#menuGroupTitleLength"), 20);
    })

    /* 메뉴그룹 추가/수정 설명 글자수 제한 */
    $("#menuGroupExplanation").keyup(function (e) {
        countContentLengthWithText($(this), $("#menuGroupExplanationLength"), 70);
    })

    /* 메뉴그룹 추가/수정 모달 취소 버튼 이벤트 */
    $("#menuGroupModalCancel").click(() => {
        if (confirm("작성하신 내용은 저장되지 않습니다. 정말 취소하시겠습니까?")) {
            $("#menuGroupModal").modal('hide');
            const menuGroupModalSubmit = $("#menuGroupModalSubmit");
            $("#menuGroupModalLabel").text("메뉴그룹 추가")
            $("#menuGroupTitle").val("");
            $("#menuGroupExplanation").val("");
            $("#menuGroupExposure").prop("checked", true);
            $("#menuGroupTitleLength").text("0/20");
            $("#menuGroupExplanationLength").text("0/70");
            menuGroupModalSubmit.text("추가");
            menuGroupModalSubmit.attr("data-command", "insert");
        }
    });

    /* 메뉴그룹 추가/수정 모달 추가/수정 버튼 이벤트 */
    $("#menuGroupModalSubmit").click(function () {
        const menuGroupTitle = $("#menuGroupTitle");
        const frm = $("#menuGroupModalForm");
        const command = $("#menuGroupModalSubmit").attr("data-command");

        if (isEmpty(menuGroupTitle.val())) {
            alert("메뉴그룹명을 입력해주세요. ");
            menuGroupTitle.focus();
            return false;
        }

        if (command === 'insert') {
            if (confirm("추가하시겠습니까?")) {
                frm.attr("action", "/owner/menu/info/menuGroup/insert.do");
                frm.submit();
            }
        } else if (command === 'update') {
            if (confirm("수정하시겠습니까?")) {
                frm.attr("action", "/owner/menu/info/menuGroup/update.do");
                frm.submit();
            }
        } else {
            alert("잘못된 접근입니다.");
            location.reload();
        }
    });


    /* 메뉴그룹 수정 버튼 이벤트 */
    $(".menu-group-edit").click(function (e) {
        $.ajax({
            url: "/owner/menu/info/menuGroup/select.do",
            data: {id: $(this).data("id")}
        })
            .done((res) => {
                const menuGroupTitle = $("#menuGroupTitle");
                const menuGroupExplanation = $("#menuGroupExplanation");
                const menuGroupModalSubmit = $("#menuGroupModalSubmit");
                $("#menuGroupModal").modal('show');
                $("#menuGroupModalLabel").text("메뉴그룹 수정")
                menuGroupTitle.val(res.menuGroupTitle);
                menuGroupExplanation.val(res.menuGroupExplanation);
                $("#menuGroupTitleLength").text(menuGroupTitle.val().length + "/20");
                $("#menuGroupExplanationLength").text(menuGroupExplanation.val().length + "/70");
                if (res.menuGroupExposure === 'Y') {
                    $("#menuGroupExposure").prop("checked", "true");
                } else {
                    $("#menuGroupNonExposure").prop("checked", "true");
                }
                menuGroupModalSubmit.text("수정");
                menuGroupModalSubmit.attr('data-command', 'update');
                $("#menuGroupId").val($(this).data("id"));
            })
            .fail(() => {
                alert("메뉴그룹을 조회할 수 없습니다.");
            })
    })

    /* 메뉴그룹 순서변경 모달 취소 버튼 이벤트 */
    $("#menuGroupOrderModalCancel").click(() => {
        if (confirm("변경하신 내용은 저장되지 않습니다. 정말 취소하시겠습니까?")) {
            location.reload();
        }
    })

    /* 메뉴그룹 순서변경 모달 저장 버튼 이벤트 */
    $("#menuGroupOrderModalSubmit").click(() => {
        const frm = $("#menuGroupOrderFrm");
        let menuGroupIdList = [];
        $(".list-group").each((index, data) => {
            menuGroupIdList.push($(data).attr("data-id"));
        })

        if (confirm("저장하시겠습니까?")) {
            $("#menuGroupIdList").val(menuGroupIdList);
            frm.attr("action", "/owner/menu/info/menuGroup/orderUpdate.do");
            frm.submit();
        }
    })

    /* 메뉴그룹 순서변경 모달 drag&drop 이벤트 */
    sortable.sortable({
        revert: true,
    });
    sortable.disableSelection();

    /* 옵션그룹 추가/수정 모달 옵션그룹명 글자수 제한 */
    $("#menuOptionGroupTitle").keyup(function (e) {
        countContentLengthWithText($(this), $("#menuOptionGroupTitleLength"), 20);
    })

    /* 옵션그룹 추가/수정 모달 옵션추가 버튼 클릭 시 요소 추가 */
    $("#addOption").click(function () {
        $(".menu-option-content").last().after(optionContent)
    })

    /* 옵션그룹 추가/수정 모달 취소 버튼 이벤트 */
    $("#menuOptionGroupModalCancel").click(() => {
        if (confirm("작성하신 내용은 저장되지 않습니다. 정말 취소하시겠습니까?")) {
            const menuOptionGroupModalSubmit = $("#menuOptionGroupModalSubmit");
            $("#menuOptionGroupModal").modal('hide');
            $("#menuOptionGroupTitle").val("");
            $("#menuOptionGroupModalLabel").text("옵션그룹 추가");
            menuOptionGroupModalSubmit.text("추가");
            menuOptionGroupModalSubmit.attr("data-command", "insert");
            $(".menu-option-content").not(":first").remove();
            $(".menu-option").val("");
            $(".menu-option-price").val("");
            $("#menuOptionLength").text("0/20");
        }
    })

    /* 옵션그룹 추가/수정 모달 다음 버튼 이벤트 */
    $("#menuOptionGroupModalNext").click((e) => {
        const menuOptionGroupTitle = $("#menuOptionGroupTitle");
        const menuOption = $(".menu-option");
        const menuOptionPrice = $(".menu-option-price")
        if (isEmpty(menuOptionGroupTitle.val())) {
            alert("옵션그룹명을 입력해주세요.");
            menuOptionGroupTitle.focus();
            return false;
        }

        if (areEmpty(menuOption, "옵션 내용을 입력해주세요.")) {
            return false;
        }

        if (areEmpty(menuOptionPrice, "가격을 입력해주세요.")) {
            return false;
        }

        const selectRequiredOptionCount = $("#selectRequiredOptionCount");
        const selectNoRequiredOptionCount = $("#selectNoRequiredOptionCount");

        $("#requiredOptionCount").text("총 " + menuOption.length + "개");
        $("#noRequiredOptionCount").text("총 " + menuOption.length + "개");
        selectRequiredOptionCount.text(menuOption.length + "개")
        selectNoRequiredOptionCount.text(menuOption.length + "개")

        const requiredOptionMinus = $("#requiredOptionMinus");
        const requiredOptionPlus = $("#requiredOptionPlus");
        const noRequiredOptionMinus = $("#noRequiredOptionMinus");
        const noRequiredOptionPlus = $("#noRequiredOptionPlus");

        if (menuOption.length === 1) {
            addGrayClass(requiredOptionMinus);
            addGrayClass(requiredOptionPlus);
            addGrayClass(noRequiredOptionMinus);
            addGrayClass(noRequiredOptionMinus);
        } else {
            addGrayClass(requiredOptionPlus);
            addGrayClass(noRequiredOptionPlus);
            removeGrayClass(requiredOptionMinus);
            removeGrayClass(noRequiredOptionMinus);
        }

        $("#menuOptionGroupModalFirstPage").addClass("d-none");
        $("#menuOptionGroupModalSecondPage").removeClass("d-none");
        $("#menuOptionGroupModalPrev").removeClass("d-none");
        $("#menuOptionGroupModalSubmit").removeClass("d-none")
        $("#menuOptionGroupModalNext").addClass("d-none");
        $("#menuOptionGroupModalCancel").addClass("d-none");
    })

    /* 옵션그룹 추가/수정 모달 이전 버튼 이벤트 */
    $("#menuOptionGroupModalPrev").click(() => {
        $("#menuOptionGroupModalFirstPage").removeClass("d-none");
        $("#menuOptionGroupModalSecondPage").addClass("d-none");
        $("#menuOptionGroupModalPrev").addClass("d-none");
        $("#menuOptionGroupModalSubmit").addClass("d-none")
        $("#menuOptionGroupModalNext").removeClass("d-none");
        $("#menuOptionGroupModalCancel").removeClass("d-none");
    })

    /* 옵션그룹 추가/수정 모달 필수 옵션 버튼 이벤트 */
    $("#requiredOption").click(() => {
        $("#requiredOptionView").removeClass("d-none");
        $("#noRequiredOptionView").addClass("d-none");
    })

    /* 옵션그룹 추가/수정 모달 선택 옵션 버튼 이벤트 */
    $("#noRequiredOption").click(() => {
        $("#requiredOptionView").addClass("d-none")
        $("#noRequiredOptionView").removeClass("d-none");
    })

    /* 옵션그룹 추가/수정 모달 최대 선택 개수 지정 버튼 이벤트 */
    $("#maxOptions").click(() => {
        $("#maxSelectView").removeClass("d-none");
    })

    /* 옵션그룹 추가/수정 모달 모든 옵션 선택 가능 버튼 이벤트 */
    $("#allOptions").click(() => {
        $("#maxSelectView").addClass("d-none");
    })

    const requiredOptionMinus = $("#requiredOptionMinus");
    const selectRequiredOptionCount = $("#selectRequiredOptionCount");
    const requiredOptionPlus = $("#requiredOptionPlus");
    const noRequiredOptionMinus = $("#noRequiredOptionMinus");
    const noRequiredOptionPlus = $("#noRequiredOptionPlus");
    const selectNoRequiredOptionCount = $("#selectNoRequiredOptionCount");

    /* 옵션그룹 추가/수정 모달 필수 옵션 옵션 수 마이너스 버튼 이벤트 */
    requiredOptionMinus.click(() => {
        let currentCount = parseInt(selectRequiredOptionCount.text().replaceAll("개", ""));
        if (currentCount > 2) {
            selectRequiredOptionCount.text((currentCount - 1) + "개");
        } else if (currentCount === 2) {
            selectRequiredOptionCount.text("1개");
            addGrayClass(requiredOptionMinus);
        }
        removeGrayClass(requiredOptionPlus);
    });

    /* 옵션그룹 추가/수정 모달 필수 옵션 옵션 수 플러스 버튼 이벤트 */
    requiredOptionPlus.click(() => {
        let currentCount = parseInt(selectRequiredOptionCount.text().replaceAll("개", ""));
        const maxLength = $(".menu-option").length - 1
        if (currentCount === maxLength) {
            selectRequiredOptionCount.text((currentCount + 1) + "개");
            addGrayClass(requiredOptionPlus);
        } else if (currentCount < maxLength) {
            selectRequiredOptionCount.text((currentCount + 1) + "개");
        }
        removeGrayClass(requiredOptionMinus);
    })

    /* 옵션그룹 추가/수정 모달 선택 옵션 옵션 수 플러스 버튼 이벤트 */
    noRequiredOptionMinus.click(() => {
        let currentCount = parseInt(selectNoRequiredOptionCount.text().replaceAll("개", ""));
        if (currentCount > 2) {
            selectNoRequiredOptionCount.text((currentCount - 1) + "개");
        } else if (currentCount === 2) {
            selectNoRequiredOptionCount.text("1개");
            addGrayClass(noRequiredOptionMinus);
        }
        removeGrayClass(noRequiredOptionPlus);
    });

    /* 옵션그룹 추가/수정 모달 선택 옵션 옵션 수 마이너스 버튼 이벤트 */
    noRequiredOptionPlus.click(() => {
        let currentCount = parseInt(selectNoRequiredOptionCount.text().replaceAll("개", ""));
        const maxLength = $(".menu-option").length - 1
        if (currentCount === maxLength) {
            selectNoRequiredOptionCount.text((currentCount + 1) + "개");
            addGrayClass(noRequiredOptionPlus);
        } else if (currentCount < maxLength) {
            selectNoRequiredOptionCount.text((currentCount + 1) + "개");
        }
        removeGrayClass(noRequiredOptionMinus);
    })

    /* 옵션그룹 추가/수정 모달 추가/수정 버튼 이벤트 */
    $("#menuOptionGroupModalSubmit").click(() => {
        const menuOptionGroupTitle = $("#menuOptionGroupTitle");
        const menuOption = $(".menu-option");
        const menuOptionPrice = $(".menu-option-price");
        const required = $("input[name='menuOptionGroupRequired']:checked");
        const optionCountButton = $("input[name='optionCountButton']");
        const maxSelect = $("#maxSelect");
        const selectRequiredOptionCount = $("#selectRequiredOptionCount");
        const selectNoRequiredOptionCount = $("#selectNoRequiredOptionCount");
        const menuOptionGroupModalForm = $("#menuOptionGroupModalForm");

        if (isEmpty(menuOptionGroupTitle.val())) {
            alert("옵션그룹명을 입력해주세요.");
            menuOptionGroupTitle.focus();
            return false;
        }

        if (areEmpty(menuOption, "옵션 내용을 입력해주세요.")) {
            return false;
        }

        if (areEmpty(menuOptionPrice, "가격을 입력해주세요.")) {
            return false;
        }

        if (required.val() === 'Y') {
            maxSelect.val(selectRequiredOptionCount.text().replaceAll("개", ""));
        } else if (required.val() === 'N') {
            if (optionCountButton.val() === 'Y') {
                maxSelect.val(selectNoRequiredOptionCount.text().replaceAll("개", ""));
            } else if (optionCountButton.val() === 'N') {
                maxSelect.val(menuOption.length);
            } else {
                alert("잘못된 접근입니다.");
                location.reload();
            }
        } else {
            alert("잘못된 접근입니다.");
            location.reload();
        }

        menuOption.each((index, data) => {
            $(data).attr("name", `menuOptionVOList[${index}].menuOptionTitle`);
        })

        menuOptionPrice.each((index, data) => {
            $(data).attr("name", `menuOptionVOList[${index}].menuOptionPrice`);
            $(data).val($(data).val().replaceAll(",", ""));
        })

        const menuOptionOrder = $(".menuOptionOrder");
        menuOptionOrder.each((index, data) => {
            $(data).attr("name", `menuOptionVOList[${index}].menuOptionOrder`);
            $(data).val(index + 1)
        })

        const command = $("#menuOptionGroupModalSubmit").attr("data-command");
        if (command === 'insert') {
            if (confirm("추가하시겠습니까?")) {
                menuOptionGroupModalForm.attr("action", "/owner/menu/info/menuGroup/menuOptionGroup/insert.do");
                menuOptionGroupModalForm.submit();
            }
        } else if (command === 'update') {
            if (confirm("수정하시겠습니까?")) {
                menuOptionGroupModalForm.attr("action", "/owner/menu/info/menuGroup/menuOptionGroup/update.do");
                menuOptionGroupModalForm.submit();
            }
        } else {
            alert("잘못된 접근입니다.");
            location.reload();
        }
    })

    /* 옵션그룹 추가/수정 모달 순서 변경 버튼 이벤트 */
    const changeOrderBtn = $("#changeOrderBtn");
    const menuOptionSortable = $("#menuOptionSortable");
    changeOrderBtn.click(() => {
        if (changeOrderBtn.hasClass("btn-outline-primary")) {   // 순서 변경 On
            changeOrderBtn.removeClass("btn-outline-primary");
            changeOrderBtn.addClass("btn-primary");
            $(".menu-option-delete").each((index, data) => {
                $(data).removeClass("menu-option-delete");
                $(data).addClass("menu-option-reorder");
                $(data).children().replaceWith("<i class=\"bx bx-list-ul fs-2\"></i>");
            })
            $(".menu-option").each((index, data) => {
                $(data).attr("readonly", true);
            })
            $(".menu-option-price").each((index, data) => {
                $(data).attr("readonly", true);
            })
            menuOptionSortable.sortable({
                cancel: changeOrderBtn,
                revert: true
            });
            menuOptionSortable.disableSelection();
        } else {    // 순서 변경 Off
            changeOrderBtn.addClass("btn-outline-primary");
            changeOrderBtn.removeClass("btn-primary");
            $(".menu-option-reorder").each((index, data) => {
                $(data).addClass("menu-option-delete");
                $(data).removeClass("menu-option-reorder");
                $(data).children().replaceWith("<i class=\"bx bx-x fs-2\"></i>");
            })
            $(".menu-option").each((index, data) => {
                $(data).attr("readonly", false);
            })
            $(".menu-option-price").each((index, data) => {
                $(data).attr("readonly", false);
            })
            menuOptionSortable.sortable("destroy");
        }
    });

    /* 옵션설정 옵션그룹 수정 버튼 이벤트 */
    $(".menu-option-group-edit").click(function (e) {
        $.ajax({
            url: "/owner/menu/info/menuGroup/menuOptionGroup/select.do",
            data: {id: $(this).attr("data-id")}
        })
            .done((res) => {
                const menuOptionGroupModal = $("#menuOptionGroupModal");
                const menuOptionGroupTitle = $("#menuOptionGroupTitle");
                $("#menuOptionGroupModalLabel").text("옵션그룹 수정");
                menuOptionGroupTitle.val(res.menuOptionGroupTitle);
                res.menuOptionVOList.forEach((data, index) => {
                    if (index === 0) {
                        $("#menuOptionTitle").val(data.menuOptionTitle);
                        $("#menuOptionPrice").val(data.menuOptionPrice)
                    } else {
                        $(".menu-option-content").last().after(optionContent);
                        $(".menu-option-content").last().find("#menuOptionTitle").val(data.menuOptionTitle);
                        $(".menu-option-content").last().find("#menuOptionPrice").val(data.menuOptionPrice);
                    }
                })
                const menuOptionGroupModalSubmit = $("#menuOptionGroupModalSubmit");
                menuOptionGroupModalSubmit.text("수정");
                menuOptionGroupModalSubmit.attr("data-command", "update");
                $("#menuOptionGroupId").val(res.menuOptionGroupId);
                $("#menuOptionGroupTitleLength").text(menuOptionGroupTitle.val().length + "/20");
                $(".menu-option-content").each((index, data) => {
                    $(data).find("#menuOptionLength").text($(data).find("#menuOptionTitle").val().length + "/20");
                })
                if (res.menuOptionGroupRequired === 'Y') {
                    $("input[name='menuOptionGroupRequired']:radio[value='Y']").prop("checked", true);
                } else {
                    $("input[name='menuOptionGroupRequired']:radio[value='N']").prop("checked", true);
                    $("#requiredOptionView").addClass("d-none")
                    $("#noRequiredOptionView").removeClass("d-none");
                }
                menuOptionGroupModal.modal('show');
            })
    })

    /* 메뉴 추가 버튼 이벤트 */
    $(".menuAddBtn").click(function () {
        $("#menuModalMenuGroupId").val($(this).attr("data-id"));
        $("#menuModal").modal("show");
    })

    /* 메뉴 추가/수정 메뉴명 글자수 제한 */
    $("#menuTitle").keyup(function (e) {
        countContentLengthWithText($(this), $("#menuTitleLength"), 20);
    })

    /* 메뉴 추가/수정 메뉴 설명 글자수 제한 */
    $("#menuExplanation").keyup(function (e) {
        countContentLengthWithText($(this), $("#menuExplanationLength"), 70);
    })

    /* 메뉴 추가/수정 메뉴 가격 글자수 제한 및 유효성 검사 */
    $("#menuPrice").keyup(function (e) {
        if (e.keyCode !== 37 && e.keyCode !== 39) {
            $(this).val($(this).val().replaceAll(/[^0-9.]/g, ''));
            $(this).val($(this).val().replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,'));
            countContentLength($(this), 11);
        }
    })

    /* 메뉴 추가/수정 모달 취소 이벤트 */
    $("#menuModalCancel").click(() => {
        if (confirm("작성하신 내용은 저장되지 않습니다. 정말 취소하시겠습니까?")) {
            $("#menuModal").modal('hide');
            const menuModalSubmit = $("#menuModalSubmit");
            $("#menuModalLabel").text("메뉴 추가")
            $("#menuTitle").val("");
            $("#menuExplanation").val("");
            $("#menuPrice").val("");
            $("#menuExposure").prop("checked", true);
            $("#menuTitleLength").text("0/20");
            $("#menuExplanationLength").text("0/70");
            $("#menuModalMenuId").val("");
            menuModalSubmit.text("추가");
            menuModalSubmit.attr("data-command", "insert");
            $("#menuModalMenuGroupId").val("");
        }
    })

    /* 메뉴 추가/수정 모달 추가/수정 이벤트 */
    $("#menuModalSubmit").click(() => {
        const menuTitle = $("#menuTitle");
        const menuExplanation = $("#menuExplanation");
        const menuPrice = $("#menuPrice");
        const command = $("#menuModalSubmit").attr("data-command");
        const frm = $("#menuModalForm");

        if (isEmpty(menuTitle.val())) {
            alert("메뉴명을 입력해주세요.");
            menuTitle.focus();
            return false;
        }

        if (isEmpty(menuExplanation.val())) {
            alert("메뉴 설명을 입력해주세요.");
            menuExplanation.focus();
            return false;
        }

        if (isEmpty(menuPrice.val())) {
            alert("메뉴 가격을 입력해주세요.");
            menuPrice.focus();
            return false;
        }

        menuPrice.val(menuPrice.val().replaceAll(",", ""));

        if (command === 'insert') {
            if (confirm("추가하시겠습니까?")) {
                frm.attr("action", "/owner/menu/info/menu/insert.do");
                $("#menuModalMenuId").attr("disabled", true);
                frm.submit();
            }
        } else if (command === 'update') {
            if (confirm("수정하시겠습니까?")) {
                frm.attr("action", "/owner/menu/info/menu/update.do");
                frm.submit();
            }
        } else {
            alert("잘못된 접근입니다.");
            location.reload();
        }
    })

    /* 메뉴 수정 버튼 이벤트 */
    $(".menu-edit").click(function (e) {
        $.ajax({
            url: '/owner/menu/info/menu/select.do',
            data: {menuId: $(this).attr("data-id")}
        })
            .done(res => {
                $("#menuModalLabel").text("메뉴 수정");
                $("#menuTitle").val(res.menuTitle);
                $("#menuTitleLength").text(res.menuTitle.length + "/20");
                $("#menuExplanation").val(res.menuExplanation);
                $("#menuExplanationLength").text(res.menuExplanation.length + "/70");
                $("#menuPrice").val(res.menuPrice);
                if (res.menuExposure === 'Y') {
                    $("input[name='menuExposure']:radio[value='Y']").prop("checked", true);
                } else {
                    $("input[name='menuExposure']:radio[value='N']").prop("checked", true);
                }
                const menuModalSubmit = $("#menuModalSubmit");
                menuModalSubmit.text("수정");
                menuModalSubmit.attr("data-command", 'update');
                $("#menuModalMenuId").val($(this).attr("data-id"));
                $("#menuModal").modal("show");
            })
            .fail(res => {
                alert("메뉴를 조회할 수 없습니다.");
            })
    });

    /* 메뉴 순서변경 모달 drag&drop 이벤트 */
    $("#menuSortable").sortable({
        revert: true
    }).disableSelection();

    /* 메뉴 순서변경 버튼 이벤트 */
    $(".menuReOrderBtn").click(function () {
        $.ajax({
            url: "/owner/menu/info/menus/select.do",
            data: {menuGroupId: $(this).attr("data-id")}
        })
            .done((res) => {
                let content = "";
                res.forEach((data) => {
                    content += `
                                <div class="list-group align-items-center menu-order-modal-list-group" data-id="${data.menuId}">
                                        <div class="list-group-item d-flex align-items-center justify-content-between col-12">
                                        <span>${data.menuTitle}</span>
                                            <div class="icon">
                                                <i class="bx bx-list-ul fs-2"></i>
                                           </div>
                                         </div>
                                </div>
                                `
                })
                $("#menuOrderFrm").after(content)
                $("#menuOrderModal").modal("show");
            })
            .error(() => {
                alert("메뉴 조회에 실패했습니다.");
            })
    })

    /* 메뉴 순서변경 모달 취소 버튼 이벤트 */
    $("#menuOrderModalCancel").click(() => {
        if (confirm(cancelMessage)) {
            $("#menuOrderModal").modal("hide");
        }
    })

    /* 메뉴 순서변경 모달 저장 버튼 이벤트 */
    $("#menuOrderModalSubmit").click(() => {
        const frm = $("#menuOrderFrm");
        let menuIdList = [];
        $(".menu-order-modal-list-group").each((index, data) => {
            menuIdList.push($(data).attr("data-id"));
        });

        if (confirm(saveMessage)) {
            $("#menuIdList").val(menuIdList);
            frm.attr("action", "/owner/menu/info/menus/orderUpdate.do");
            frm.submit();
        }
    });

    /* 옵션그룹 연결 버튼 이벤트 */
    $(".menu-add-option-group").click(function () {
        const menuId = parseInt($(this).attr("data-id"));
        $.ajax({
            url: "/owner/menu/info/menuOptionGroup",
            data: {menuId}
        })
            .done(res => {
                $("#optionGroupConnectModalWrapper").append(res);
                $("#optionGroupConnectModal").modal("show");
                $("#menuId").val(menuId);
                $("#menuOptionConnectSortable").sortable({
                    revert: true,
                    items: ".menu-option-connect-sortable"
                }).disableSelection();
            })
            .fail(() => {
                alert("옵션그룹을 조회할 수 없습니다.")
            })
    })

    /* 옵션그룹 삭제 버튼 이벤트 */
    $(".menu-option-group-delete").click(function () {
        const menuOptionGroupId = parseInt($(this).attr("data-id"));
        $.ajax({
            url: "/owner/menu/info/menuOptionGroup/connectedMenu",
            data: {menuOptionGroupId}
        })
            .done(res => {
                let message = deleteMessage + " 이 옵션그룹과 연결된 메뉴에서도 삭제됩니다.";
                if (res.length > 0) {
                    message += "\n\n===== 연결된 메뉴 =====\n"
                    res.forEach(item => {
                        message += "\n" + item.menuTitle;
                    });
                }

                if (confirm(message)) {
                    const menuOptionGroupFrm = $("#menuOptionGroupFrm");
                    $("#menuOptionGroupIdToRemove").val(menuOptionGroupId);
                    menuOptionGroupFrm.attr("action", "/owner/menu/info/menuOptionGroup/delete.do");
                    menuOptionGroupFrm.submit();
                }
            })
            .fail(() => {
                alert("삭제에 실패했습니다.");
            })
    })

    /* 메뉴 삭제 버튼 이벤트 */
    $(".menu-remove").click((function () {
        if (confirm(deleteMessage)) {
            const menuRemoveFrm = $("#menuRemoveFrm");
            $("#menuRemoveId").val($(this).attr("data-id"));
            menuRemoveFrm.attr("action", "/owner/menu/info/menu/delete.do");
            menuRemoveFrm.submit();
        }
    }))

    /* 메뉴 그룹 삭제 버튼 이벤트 */
    $(".menu-group-delete").click(function () {
        if (confirm(deleteMessage)) {
            const menuRemoveFrm = $("#menuRemoveFrm");
            $("#menuGroupRemoveId").val($(this).attr("data-id"));
            menuRemoveFrm.attr("action", "/owner/menu/info/menuGroup/delete.do");
            menuRemoveFrm.submit();
        }
    })
})

/* 최대 글자수 체크 및 글자수 표시 텍스트 갱신 */
function countContentLengthWithText(content, lengthText, maxLength) {
    const contentValue = content.val();
    lengthText.text(contentValue.length + "/" + maxLength);
    if (contentValue.length >= maxLength) {
        content.val(contentValue.substring(0, parseInt(maxLength)));
        lengthText.text(content.val().length + "/" + maxLength);
        lengthText.addClass("vibration");
        setTimeout(() => {
            lengthText.removeClass("vibration");
        }, 400);
    }
}

/* 최대 글자수 체크 */
function countContentLength(content, maxLength) {
    const contentValue = content.val();
    if (contentValue.length >= maxLength) {
        content.val(contentValue.substring(0, parseInt(maxLength)));
    }
}

/* 옵션그룹 추가/수정 모달 옵션 내용 글자수 제한 */
$(document).on("keyup", ".menu-option", function () {
    countContentLengthWithText($(this), $(this).siblings("#menuOptionLength"), 20);
})

/* 옵션그룹 추가/수정 모달 숫자만 입력하도록 제한, 글자수 제한 */
$(document).on("keyup", ".menu-option-price", function (e) {
    if (e.keyCode !== 37 && e.keyCode !== 39) {
        $(this).val($(this).val().replaceAll(/[^0-9.]/g, ''));
        $(this).val($(this).val().replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,'));
        countContentLength($(this), 11);
    }
})

/* 옵션그룹 추가/수정 모달 옵션 내용 삭제 버튼 이벤트 */
$(document).on("click", ".menu-option-delete", function () {
    if ($(".menu-option-content").length !== 1) {
        $(this).closest(".menu-option-content").remove();
    } else {
        const toastLiveExample = document.getElementById("toast");
        const toast = new bootstrap.Toast(toastLiveExample, {delay: 1800});
        toast.show();
    }
})

/* 옵션그룹 연결 모달 연결 추가 버튼 이벤트 */
$(document).on("click", ".menu-option-connect-btn", function () {
    $(this).removeClass("menu-option-connect-btn");
    $(this).addClass("menu-option-disconnect-btn");
    $(this).children("i").removeClass("bxs-plus-circle");
    $(this).children("i").addClass("bxs-minus-circle");
    $(".menu-option-connected").append($(this).closest(".list-group"));
    $(this).closest(".list-group").addClass("menu-option-connect-sortable");
    $(this).replaceWith(optionDisconnectNode);
});

/* 옵션그룹 연결 모달 연결 삭제 버튼 이벤트 */
$(document).on("click", ".menu-option-disconnect-btn", function () {
    $(this).removeClass("menu-option-disconnect-btn");
    $(this).addClass("menu-option-connect-btn");
    $(this).children("i").removeClass("bxs-minus-circle");
    $(this).children("i").addClass("bxs-plus-circle");
    $(".menu-option-unconnected").append($(this).closest(".list-group"));
    $(this).closest(".list-group").removeClass("menu-option-connect-sortable")
    $(this).parent().replaceWith(optionConnectNode);
});


/* 옵션그룹 연결 모달 취소 버튼 이벤트 */
$(document).on("click", "#optionGroupConnectModalCancel", function () {
    if (confirm(cancelMessage)) {
        const optionGroupConnectModal = $("#optionGroupConnectModal");
        optionGroupConnectModal.modal("hide")
        optionGroupConnectModal.remove();
    }
})

/* 옵션그룹 연결 모달 저장 버튼 이벤트 */
$(document).on("click", "#optionGroupConnectModalSubmit", function () {
    const optionGroupConnectFrm = $("#optionGroupConnectFrm");
    let optionGroupIdList = [];
    $(".menu-option-connect-sortable").each((index, data) => {
        optionGroupIdList.push(parseInt($(data).attr("data-id")));
    })
    $("#optionGroupIdList").val(optionGroupIdList);
    if (confirm(saveMessage)) {
        optionGroupConnectFrm.attr("action", "/owner/menu/info/menu/connectOptionGroup");
        optionGroupConnectFrm.submit();
    }
})

function removeGrayClass(button) {
    button.addClass("pointer");
    button.addClass("bg-white");
    button.removeClass("bg-gray");
    button.removeClass("text-gray");
}

function addGrayClass(button) {
    button.removeClass("pointer");
    button.removeClass("bg-white");
    button.addClass("bg-gray");
    button.addClass("text-gray");
}

function optionContent() {
    let content = `<div class="row g-3 align-items-center mb-1 menu-option-content clear">
    <input class="menuOptionOrder" type="hidden">
    <div class="col-7 position-relative">
        <label class="d-none"
               for="menuOptionTitle"></label>
        <div class="position-absolute text-secondary count-text-limit end-0 top-50 translate-middle-y me-3"
             id="menuOptionLength">
            0/20
        </div>
        <input
            class="form-control menu-option pe-5"
            id="menuOptionTitle"
            type="text">
    </div>
    <div class="col-4 position-relative">
        <label class="position-absolute end-0 top-50 translate-middle-y me-3 text-secondary"
               for="menuOptionPrice"
               style="font-size: 0.8rem">원</label>
        <input
            class="form-control menu-option-price"
            id="menuOptionPrice"
            type="text"
        >
    </div>`

    if ($("#changeOrderBtn").hasClass("btn-outline-primary")) {
        content +=
            `<div class="icon col-1 d-flex align-items-center ps-0 pointer menu-option-delete">
                <i class="bx bx-x fs-2"></i>
            </div>
        </div>`
    } else {
        content +=
            `<div class="icon col-1 d-flex align-items-center ps-0 pointer menu-option-reorder">
                <i class=\"bx bx-list-ul fs-2\"></i>
            </div>
      </div>`
    }
    return content;
}

const optionDisconnectNode =
    `<div class="d-flex align-items-center">
        <div class="icon pointer menu-option-disconnect-btn d-flex align-items-center me-2">
            <i class="bx bxs-minus-circle fs-3"></i>
        </div>
        <div class="icon pointer menu-option-connect-reorder-btn d-flex align-items-center">
            <i class="bx bx-list-ul fs-2"></i>
        </div>
    </div>`

const optionConnectNode =
    `<div class="icon pointer menu-option-connect-btn">
        <i class="bx bxs-plus-circle fs-3"></i>
    </div>`
