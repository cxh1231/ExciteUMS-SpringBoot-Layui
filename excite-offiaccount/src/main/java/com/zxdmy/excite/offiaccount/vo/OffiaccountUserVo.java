package com.zxdmy.excite.offiaccount.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 开放接口调用
 *
 * @author 拾年之璐
 * @since 2022/6/30 10:34
 */
@Data
@Accessors(chain = true)
public class OffiaccountUserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公众号用户唯一标识
     */
    @JsonProperty(value = "open_id")
    @SerializedName(value = "openId", alternate = {"open_id"})
    private String openId;

    /**
     * UnionID机制下用户的ID
     */
    @JsonProperty(value = "union_id")
    @SerializedName(value = "unionId", alternate = {"union_id"})
    private String unionId;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别（1：男，2：女，0：未知）
     */
    private Integer sex;

    /**
     * 国家
     */
    private String country;

    /**
     * 城市
     */
    private String city;

    /**
     * 省份
     */
    private String province;

    /**
     * 用户语言
     */
    private String language;

    /**
     * 二维码扫码场景描述（开发者自定义）
     */
    @JsonProperty(value = "scene_str")
    @SerializedName(value = "sceneStr", alternate = {"scene_str"})
    private String sceneStr;

    /**
     * 二维码扫码场景（开发者自定义）
     */
    @JsonProperty(value = "scene_num")
    @SerializedName(value = "sceneNum", alternate = {"scene_num"})
    private String sceneNum;

    /**
     * 过期时间，单位：秒
     */
    private Long expire;

}
