const allowedImageExt = ".jpg,.jpeg,.svg,.png" // 허용된 이미지 파일 확장자
const allowedFileMaxSize = 5242880;            // 허용된 최대 파일 크기
const hasWhiteSpace = /\s/;                    // 공백 포함 여부
const passwordRule = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/; // 하나 이상의 문자, 숫자, 특수 문자

/**
 * 값이 존재하는지 검사(단일)
 * @param data
 * @returns {boolean}
 */
function isEmpty(data) {
    return data === "" || data === undefined;
}


/**
 * 값이 존재하는지 검사(다수)
 * @param datas
 * @param message
 * @returns {boolean}
 */
function areEmpty(datas, message) {
    let pass = false;
    datas.each((index, data) => {
        if (isEmpty($(data).val())) {
            alert(message);
            $(data).focus();
            pass = false;
            return false;
        }
        pass = true;
    })
    return !pass;
}
