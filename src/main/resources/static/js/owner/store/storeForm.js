/**
 * 카카오 API를 이용해 주소를 조회하는 함수
 */
function findAddress() {
    new daum.Postcode({
        oncomplete: function (data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if (data.userSelectedType === 'R') {
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if (extraAddr !== '') {
                    extraAddr = ' (' + extraAddr + ')';
                }
            }
            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            $("#zipCode").val(data.zonecode);
            $("#address").val(addr);
            $("#detailAddress").focus();
        }
    }).open();
}


/**
 * command가 insert이면 가게 정보를 등록,
 * command가 update이면 가게 정보를 수정한다.
 *
 * @param command
 * @returns {boolean}
 */
function fn_submit(command) {
    const name = $("#name");
    const intro = $("#intro");
    const tel = $("#tel");
    const closedDays = $("#closedDays");
    const zipCode = $("#zipCode");
    const address = $("#address");
    const weekdayIsOpen = $("#weekdayIsOpen");
    const weekdayStart = $("#weekdayStart");
    const weekdayEnd = $("#weekdayEnd");
    const saturdayIsOpen = $("#saturdayIsOpen");
    const saturdayStart = $("#saturdayStart");
    const saturdayEnd = $("#saturdayEnd");
    const sundayIsOpen = $("#sundayIsOpen");
    const sundayStart = $("#sundayStart");
    const sundayEnd = $("#sundayEnd");
    const frm = $("#frm");

    if (isEmpty(name.val())) {
        alert("상호명을 입력해주세요.");
        name.focus();
        return false;
    }

    if (isEmpty(intro.val())) {
        alert("소개를 입력해주세요.");
        intro.focus();
        return false;
    }

    if (isEmpty(tel.val())) {
        alert("전화번호를 입력해주세요.");
        tel.focus();
        return false;
    }

    if (isEmpty(closedDays.val())) {
        alert("휴무일을 입력해주세요.");
        closedDays.focus();
        return false;
    }

    if (isEmpty(zipCode.val())) {
        alert("우편번호를 입력해주세요.");
        zipCode.focus();
        return false;
    }

    if (isEmpty(address.val())) {
        alert("주소를 입력해주세요.");
        address.focus();
        return false;
    }

    if (weekdayIsOpen.is(":checked")) {
        weekdayIsOpen.val("Y");
        if (isEmpty(weekdayStart.val())) {
            alert("평일 영업 시작 시간을 입력해주세요.");
            weekdayStart.focus();
            return false;
        }

        if (isEmpty(weekdayEnd.val())) {
            alert("평일 영업 종료 시간을 입력해주세요.");
            weekdayEnd.focus();
            return false;
        }
    }

    if (saturdayIsOpen.is(":checked")) {
        saturdayIsOpen.val("Y");
        if (isEmpty(saturdayStart.val())) {
            alert("토요일 영업 시작 시간을 입력해주세요.");
            saturdayStart.focus();
            return false;
        }

        if (isEmpty(saturdayEnd.val())) {
            alert("토요일 영업 종료 시간을 입력해주세요.");
            saturdayEnd.focus();
            return false;
        }
    }

    if (sundayIsOpen.is(":checked")) {
        sundayIsOpen.val("Y");
        if (isEmpty(sundayStart.val())) {
            alert("일요일 영업 시작 시간을 입력해주세요.");
            sundayStart.focus();
            return false;
        }

        if (isEmpty(sundayEnd.val())) {
            alert("일요일 영업 종료 시간을 입력해주세요.");
            sundayEnd.focus();
            return false;
        }
    }

    /* 주소를 좌표로 변환 */
    const geocoder = new kakao.maps.services.Geocoder();
    geocoder.addressSearch(address.val(), function (result, status) {
        if (status === kakao.maps.services.Status.OK) {
            const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
            $('#latitude').val(coords.getLat());
            $('#longitude').val(coords.getLng());
            if (command === 'insert') {
                if (confirm("등록하시겠습니까?")) {
                    frm.attr("action", "/owner/store/form/insert.do");
                    frm.submit();
                }
            } else if (command === 'update') {
                if (confirm("수정하시겠습니까?")) {
                    frm.attr("action", "/owner/store/form/update.do");
                    frm.submit();
                }
            } else {
                alert("잘못된 접근입니다.");
                return false;
            }
        } else {
            alert("주소를 변환하는 과정에서 오류가 발생했습니다.\n관리자에게 문의해주세요.");
        }
    })
}


/**
 * 평일 영업 유무에 따라 평일 영업 시작 시간과 평일 영업 종료 시간의 ui 변경
 */
function fn_changeWeekdayIsOpen() {
    const weekdayIsOpen = $("#weekdayIsOpen");
    const weekdayStart = $("#weekdayStart");
    const weekdayEnd = $("#weekdayEnd");
    const weekdayStartLabel = $("label[for='weekdayStart']");
    const weekdayEndLabel = $("label[for='weekdayEnd']");
    const text = ' <em class="text-danger">*</em>';

    if (weekdayIsOpen.is(":checked")) {
        weekdayStartLabel.html(weekdayStartLabel.html() + text);
        weekdayEndLabel.html(weekdayEndLabel.html() + text);
        weekdayStart.removeAttr("disabled");
        weekdayEnd.removeAttr("disabled");
    } else {
        weekdayStartLabel.html(weekdayStartLabel.html().replace(text, ""));
        weekdayEndLabel.html(weekdayEndLabel.html().replace(text, ""));
        weekdayStart.attr("disabled", "true");
        weekdayStart.val("");
        weekdayEnd.attr("disabled", "true");
        weekdayEnd.val("");
    }
}


/**
 * 토요일 영업 유무에 따라 토요일 영업 시작 시간과 토요일 영업 종료 시간의 ui 변경
 */
function fn_changeSaturdayIsOpen() {
    const saturdayIsOpen = $("#saturdayIsOpen");
    const saturdayStart = $("#saturdayStart");
    const saturdayEnd = $("#saturdayEnd");
    const saturdayStartLabel = $("label[for='saturdayStart']");
    const saturdayEndLabel = $("label[for='saturdayEnd']");
    const text = ' <em class="text-danger">*</em>';

    if (saturdayIsOpen.is(":checked")) {
        saturdayStartLabel.html(saturdayStartLabel.html() + text);
        saturdayEndLabel.html(saturdayEndLabel.html() + text);
        saturdayStart.removeAttr("disabled");
        saturdayEnd.removeAttr("disabled");
    } else {
        saturdayStartLabel.html(saturdayStartLabel.html().replace(text, ""));
        saturdayEndLabel.html(saturdayEndLabel.html().replace(text, ""));
        saturdayStart.attr("disabled", "true");
        saturdayStart.val("");
        saturdayEnd.attr("disabled", "true");
        saturdayEnd.val("");
    }
}


/**
 * 평일 영업 유무에 따라 평일 영업 시작 시간과 평일 영업 종료 시간의 ui 변경
 */
function fn_changeSundayIsOpen() {
    const sundayIsOpen = $("#sundayIsOpen");
    const sundayStart = $("#sundayStart");
    const sundayEnd = $("#sundayEnd");
    const sundayStartLabel = $("label[for='sundayStart']");
    const sundayEndLabel = $("label[for='sundayEnd']");
    const text = ' <em class="text-danger">*</em>';

    if (sundayIsOpen.is(":checked")) {
        sundayStartLabel.html(sundayStartLabel.html() + text);
        sundayEndLabel.html(sundayEndLabel.html() + text);
        sundayStart.removeAttr("disabled");
        sundayEnd.removeAttr("disabled");
    } else {
        sundayStartLabel.html(sundayStartLabel.html().replace(text, ""));
        sundayEndLabel.html(sundayEndLabel.html().replace(text, ""));
        sundayStart.attr("disabled", "true");
        sundayStart.val("");
        sundayEnd.attr("disabled", "true");
        sundayEnd.val("");
    }
}

/**
 * 전화번호에 자동으로 하이픈 추가
 * @param target
 */
function telAutoHyphen(target) {
    target.value = target.value
        .replace(/[^0-9]/g, '')
        .replace(/^(\d{0,3})(\d{0,3})(\d{0,4})$/g, "$1-$2-$3").replace(/(\-{1,2})$/g, "");
}