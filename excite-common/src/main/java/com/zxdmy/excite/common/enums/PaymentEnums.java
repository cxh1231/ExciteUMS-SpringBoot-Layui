package com.zxdmy.excite.common.enums;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/7/7 19:53
 */
public enum PaymentEnums {

    SUCCESS(1000, "SUCCESS"),

    ERROR(4000, "ERROR"),

    ERROR_PARAM(4001, "参数错误"),

    ERROR_APP_ID(4002, "APPID无效，请检查APPID参数，或APPID已被禁用"),

    ERROR_SIGN(4003, "签名错误，请检查签名参数和签名算法"),

    ERROR_SYSTEM(5000, "系统错误");


    private final Integer code;

    private final String message;

    PaymentEnums(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
