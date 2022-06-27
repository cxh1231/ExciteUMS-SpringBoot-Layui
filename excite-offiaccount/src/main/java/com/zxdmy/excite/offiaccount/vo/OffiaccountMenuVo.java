package com.zxdmy.excite.offiaccount.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/6/27 16:06
 */
@Data
@Accessors(chain = true)
public class OffiaccountMenuVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单标题，不超过16个字节，子菜单不超过60个字节.
     */
    private String name;

    /**
     * 菜单的响应动作类型.
     * view表示网页类型，
     * click表示点击类型，
     * miniprogram表示小程序类型
     */
    private String type;

    /**
     * 菜单KEY值，用于消息接口推送，不超过128字节.
     * click等点击类型必须
     */
    private String key;

    /**
     * 网页链接.
     * 用户点击菜单可打开链接，不超过1024字节。type为miniprogram时，不支持小程序的老版本客户端将打开本url。
     * view、miniprogram类型必须
     */
    private String url;

    /**
     * <pre>
     * 小程序的appid.
     * miniprogram类型必须
     * </pre>
     */
    @JsonProperty(value = "appid")
    @SerializedName(value = "appId", alternate = {"appid"})
    private String appId;

    /**
     * <pre>
     * 小程序的页面路径.
     * miniprogram类型必须
     * </pre>
     */
    @SerializedName(value = "pagePath", alternate = {"pagepath"})
    @JsonProperty(value = "pagepath")
    private String pagePath;

    /**
     * 二级菜单
     */
    @JsonProperty(value = "sub_button")
    @SerializedName(value = "subButtons", alternate = {"sub_button"})
    private List<OffiaccountMenuVo> subButtons = new ArrayList<>();

}
