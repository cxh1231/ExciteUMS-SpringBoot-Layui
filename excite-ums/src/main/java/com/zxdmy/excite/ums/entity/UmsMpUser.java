package com.zxdmy.excite.ums.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 微信用户
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-06-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("ums_mp_user")
public class UmsMpUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 公众号用户唯一标识
     */
    private String openId;

    /**
     * UnionID机制下用户的ID
     */
    private String unionId;

    /**
     * 头像
     */
    private String headimgUrl;

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
     * 公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
     */
    private String remark;

    /**
     * 用户所在的分组ID
     */
    private String groupId;

    /**
     * 用户被打上的标签 ID 列表
     */
    private String tagidList;

    /**
     * 是否订阅公众号（1：是；0：否；2：网页授权用户）
     */
    private Integer subscribe;

    /**
     * 用户关注的渠道来源：ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENEPROFILE LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_OTHERS 其他
     */
    private String subscribeScene;

    /**
     * 关注时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime subscribeTime;

    /**
     * 关注次数
     */
    private Integer subscribeNum;

    /**
     * 取消关注时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime unsubscribeTime;

    /**
     * 二维码扫码场景描述（开发者自定义）
     */
    private String qrSceneStr;

    /**
     * 二维码扫码场景（开发者自定义）
     */
    private String qrScene;

    /**
     * 地理位置纬度
     */
    private Double latitude;

    /**
     * 地理位置经度
     */
    private Double longitude;

    /**
     * 系统的备注信息
     */
    private String umsRemark;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除时间
     */
    private LocalDateTime deleteTime;


    public static final String ID = "id";

    public static final String OPEN_ID = "open_id";

    public static final String UNION_ID = "union_id";

    public static final String HEADIMG_URL = "headimg_url";

    public static final String NICKNAME = "nickname";

    public static final String SEX = "sex";

    public static final String COUNTRY = "country";

    public static final String CITY = "city";

    public static final String PROVINCE = "province";

    public static final String LANGUAGE = "language";

    public static final String REMARK = "remark";

    public static final String GROUP_ID = "group_id";

    public static final String TAGID_LIST = "tagid_list";

    public static final String SUBSCRIBE = "subscribe";

    public static final String SUBSCRIBE_SCENE = "subscribe_scene";

    public static final String SUBSCRIBE_TIME = "subscribe_time";

    public static final String SUBSCRIBE_NUM = "subscribe_num";

    public static final String UNSUBSCRIBE_TIME = "unsubscribe_time";

    public static final String QR_SCENE_STR = "qr_scene_str";

    public static final String QR_SCENE = "qr_scene";

    public static final String LATITUDE = "latitude";

    public static final String LONGITUDE = "longitude";

    public static final String PRECISION = "precision";

    public static final String UMS_REMARK = "ums_remark";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String DELETE_TIME = "delete_time";

}
