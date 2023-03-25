/**
 * 비밀번호를 변경하기 위해 폼을 전송하는 함수
 * 유효성 검사를 진행한다.
 * @returns {boolean}
 */
function fn_submit() {
    const userPwd = $("#userPwd");
    const newPwd = $("#newPwd");
    const reNewPwd = $("#reNewPwd");
    const frm = $("#frm");

    if (isEmpty(userPwd.val())) {
        alert("현재 비밀번호를 입력해주세요.");
        userPwd.focus();
        return false;
    }

    if (isEmpty(newPwd.val())) {
        alert("새 비밀번호를 입력해주세요.");
        newPwd.focus();
        return false;
    }

    if (newPwd.val().length < 8 || newPwd.val().length > 15) {
        alert("비밀번호는 8자리 이상, 15자리 이하로 입력해주세요.");
        newPwd.focus();
        return false;
    }

    if (newPwd.val().match(hasWhiteSpace)) {
        alert("비밀번호는 공백 없이 입력해주세요.");
        newPwd.focus();
        return false;
    }

    if (!newPwd.val().match(passwordRule)) {
        alert("비밀번호는 하나 이상의 문자, 숫자, 특수 문자가 포함되어야 합니다.")
        newPwd.focus();
        return false;
    }

    if (isEmpty(reNewPwd.val())) {
        alert("새 비밀번호 확인을 입력해주세요.");
        reNewPwd.focus();
        return false;
    }

    if (newPwd.val() !== reNewPwd.val()) {
        alert("새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.");
        return false;
    }

    if (confirm("정말 수정하시겠습니까?")) {
        frm.attr("action", "/owner/profile/password/update.do");
        frm.submit();
    }
}