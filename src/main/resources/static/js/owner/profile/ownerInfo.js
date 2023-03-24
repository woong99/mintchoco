const koreanExpect = /^[ㄱ-ㅎ|가-힣]+$/;
const telExpect = /^[0-9]{3}-[0-9]{4}-[0-9]{4}$/;
const emailExpect = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;
const businessNumberExpect = /^[0-9]{3}-[0-9]{2}-[0-9]{5}$/;

/**
 * 수정 버튼 클릭 시 유효성 검사 후 submit
 * @returns {boolean}
 */
function fn_submit() {
    const fullName = $("#fullName");
    const tel = $("#tel");
    const email = $("#email");
    const businessNumber = $("#businessNumber");
    const frm = $("#frm");

    if (isEmpty(fullName.val())) {
        alert("이름을 입력해주세요.");
        fullName.focus();
        return false;
    }
    if (fullName.val().length > 4 || !koreanExpect.test(fullName.val())) {
        alert("올바른 이름을 입력해주세요.");
        fullName.focus();
        return false;
    }
    if (isEmpty(tel.val())) {
        alert("휴대폰 번호를 입력해주세요.");
        tel.focus();
        return false;
    }
    if (!telExpect.test(tel.val())) {
        alert("올바른 휴대폰 번호를 입력해주세요.");
        tel.focus();
        return false;
    }
    if (isEmpty(email.val())) {
        alert("이메일을 입력해주세요.");
        email.focus();
        return false;
    }
    if (!emailExpect.test(email.val())) {
        alert("이메일 형식이 올바르지 않습니다.");
        email.focus();
        return false;
    }
    if (isEmpty(businessNumber.val())) {
        alert("사업자등록번호를 입력해주세요.");
        businessNumber.focus();
        return false;
    }
    if (!businessNumberExpect.test(businessNumber.val())) {
        alert("올바른 사업자등록번호를 입력해주세요.");
        businessNumber.focus();
        return false;
    }

    if (confirm("정말 수정하시겠습니까?")) {
        frm.attr("action", "/owner/profile/info/update.do");
        frm.submit();
    }
}

/**
 * 휴대폰 번호 입력창에 자동으로 하이픈(-) 추가
 * Ex) 010-0000-0000
 * @param target
 */
function telAutoHyphen(target) {
    target.value = target.value
        .replace(/[^0-9]/g, '')
        .replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/(\-{1,2})$/g, "");
}

/**
 * 사업자등록번호 입력창에 자동으로 하이픈(-) 추가
 * Ex) XXX-XX-XXXXX
 * @param target
 */
function businessNumberAutoHyphen(target) {
    target.value = target.value
        .replace(/[^0-9]/g, '')
        .replace(/(\d{3})(\d{2})(\d{5})/, "$1-$2-$3")
        .replace(/(-{1,2})$/g, "");
}

$(function () {

    /**
     * 이름 입력창에 한글만 입력 가능하게 하는 함수
     */
    $("#fullName").keyup(function (event) {
        const regexp = /[a-z0-9]|[ \[\]{}()<>?|`~!@#$%^&*-_+=,.;:\"'\\]/g;
        const v = $(this).val();
        if (regexp.test(v)) {
            $(this).val(v.replace(regexp, ''));
        }
    });
})