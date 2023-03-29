$(function () {
    /* 클립보드에 주소 복사 후 토스트 메세지 노출 */
    const clipboard = new ClipboardJS("#copyAddress");
    const toastLiveExample = document.getElementById("toast");
    const toast = new bootstrap.Toast(toastLiveExample, {delay: 1800});
    clipboard.on('success', function () {
        toast.show();
    })
})

/**
 * 지도보기로 넘어가는 메소드
 * @param latitude
 * @param longitude
 * @param name
 */
function fn_goMap(latitude, longitude, name) {
    window.open(`/owner/store/info/map?name=${name}&latitude=${latitude}&longitude=${longitude}`);
}