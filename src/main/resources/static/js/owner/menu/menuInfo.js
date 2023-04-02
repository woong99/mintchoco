$(function () {
    const sortable = $("#sortable");

    /* 메뉴그룹 추가/수정 이름 글자수 제한 */
    $("#menuGroupTitle").keyup(function (e) {
        countContentLength($(this), $("#menuGroupTitleLength"), 20);
    })

    /* 메뉴그룹 추가/수정 설명 글자수 제한 */
    $("#menuGroupExplanation").keyup(function (e) {
        countContentLength($(this), $("#menuGroupExplanationLength"), 70);
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
        }
    });


    /* 메뉴그룹 수정 버튼 이벤트 */
    $(".menu-group-edit").click(function (e) {
        console.log($(this).data("id"))
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
})

/* 최대 글자수 체크 */
function countContentLength(content, lengthText, maxLength) {
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
