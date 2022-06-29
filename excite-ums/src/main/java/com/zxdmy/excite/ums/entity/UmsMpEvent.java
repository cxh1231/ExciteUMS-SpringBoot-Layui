package com.zxdmy.excite.ums.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 微信事件接收与发送记录表
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-06-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("ums_mp_event")
public class UmsMpEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 官方：开发者微信号（即公众号的原始ID）
     */
    private String wechatId;

    /**
     * 官方：消息发送方账号（OpenID）
     */
    private String openId;

    /**
     * 事件类型：subscribe(订阅，含扫描带参数的二维码)、unsubscribe(取消订阅)、SCAN（扫描带参数的二维码）、LOCATION（上报地理位置）、CLICK（点击菜单）、VIEW（访问菜单链接）、view_miniprogram（点击小程序菜单）
     */
    private String event;

    /**
     * 事件KEY值，如二维码场景值、菜单KEY值、跳转的URL
     */
    private String eventKey;

    /**
     * 二维码的ticket，可用来换取二维码图片
     */
    private String eventTicket;

    /**
     * 地理位置纬度
     */
    private Double eventLatitude;

    /**
     * 地理位置经度
     */
    private Double eventLongitude;

    /**
     * 地理位置精度
     */
    private Double eventPrecision;

    /**
     * 指菜单ID，如果是个性化菜单，则可以通过这个字段，知道是哪个规则的菜单被点击了。	
     */
    private Long eventMenuId;

    /**
     * 菜单扫码：扫描类型，一般是qrcode
     */
    private String eventScanType;

    /**
     * 菜单扫码：扫描结果，即二维码对应的字符串信息
     */
    private String eventScanResult;

    /**
     * 回复消息类型（text：文本；image：图片；voice：语音；video：视频；music：音乐；news：图文）
     */
    private String repType;

    /**
     * 回复文本：回复的文本消息
     */
    private String repContent;

    /**
     * 多媒体消息：图文、视频、音乐消息的标题，后两者非必须
     */
    private String repTitle;

    /**
     * 多媒体消息：图文、视频、音乐消息的描述，后两者非必须
     */
    private String repDescription;

    /**
     * 图文消息：图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
     */
    private String repPicUrl;

    /**
     * 图文消息：点击图文消息跳转链接
     */
    private String repUrl;

    /**
     * 媒体消息ID：回复类型imge、voice、video的mediaID，通过素材管理中的接口上传多媒体文件，得到的id
     */
    private String repMediaId;

    /**
     * 音乐消息：音乐链接
     */
    private String repMusicUrl;

    /**
     * 音乐消息：高质量音乐链接，WIFI环境优先使用该链接播放音乐，非必须
     */
    private String repHqMusicUrl;

    /**
     * 音乐消息：缩略图的媒体id
     */
    private String repThumbMediaId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 接收到的消息创建时间 （整型）
     */
    private Long eventCreateTime;

    /**
     * 回复的消息创建时间（整形）
     */
    private Long repCreateTime;

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

    public static final String WECHAT_ID = "wechat_id";

    public static final String OPEN_ID = "open_id";

    public static final String EVENT = "event";

    public static final String EVENT_KEY = "event_key";

    public static final String EVENT_TICKET = "event_ticket";

    public static final String EVENT_LATITUDE = "event_latitude";

    public static final String EVENT_LONGITUDE = "event_longitude";

    public static final String EVENT_PRECISION = "event_precision";

    public static final String EVENT_MENU_ID = "event_menu_id";

    public static final String EVENT_SCAN_TYPE = "event_scan_type";

    public static final String EVENT_SCAN_RESULT = "event_scan_result";

    public static final String REP_TYPE = "rep_type";

    public static final String REP_CONTENT = "rep_content";

    public static final String REP_TITLE = "rep_title";

    public static final String REP_DESCRIPTION = "rep_description";

    public static final String REP_PIC_URL = "rep_pic_url";

    public static final String REP_URL = "rep_url";

    public static final String REP_MEDIA_ID = "rep_media_id";

    public static final String REP_MUSIC_URL = "rep_music_url";

    public static final String REP_HQ_MUSIC_URL = "rep_hq_music_url";

    public static final String REP_THUMB_MEDIA_ID = "rep_thumb_media_id";

    public static final String REMARK = "remark";

    public static final String EVENT_CREATE_TIME = "event_create_time";

    public static final String REP_CREATE_TIME = "rep_create_time";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String DELETE_TIME = "delete_time";

}
