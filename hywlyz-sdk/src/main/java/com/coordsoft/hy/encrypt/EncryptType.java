package com.coordsoft.hy.encrypt;

public enum EncryptType {

    NONE, BASE64, AES_CBC_PKCS5Padding, AES_CBC_ZeroPadding;

    public static EncryptType valueOf(int code) {
        switch (code) {
            case 0:
                return NONE;
            case 1:
                return BASE64;
            case 2:
                return AES_CBC_PKCS5Padding;
            case 3:
                return AES_CBC_ZeroPadding;
        }
        return NONE;
    }

}
