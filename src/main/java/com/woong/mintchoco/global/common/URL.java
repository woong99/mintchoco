package com.woong.mintchoco.global.common;

public enum URL {

    MESSAGE("/views/common/message"),
    MENU_INFO("/owner/menu/info");

    private final String url;

    URL(String url) {
        this.url = url;
    }

    public String url() {
        return this.url;
    }

}
