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
 * 微信消息接收与发送记录表
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-06-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("ums_mp_message")
public class UmsMpMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 官方：消息id，64位整型
     */
    private Long msgId;

    /**
     * 官方：开发者微信号（即公众号的原始ID）
     */
    private String wechatId;

    /**
     * 官方：消息发送方账号（OpenID）
     */
    private String openId;

    /**
     * 消息类型：文本为text，图片为image，语音为voice，视频为video，小视频为shortvideo，消息类型，地理位置为location，链接为link
     */
    private String msgType;

    /**
     * 文本：文本消息内容
     */
    private String msgContent;

    /**
     * 图片：图片链接（由微信官方系统生成）
     */
    private String msgPicUrl;

    /**
     * 语音：语音格式
     */
    private String msgFormat;

    /**
     * 视频：（小）视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
     */
    private String msgThumbMediaId;

    /**
     * 媒体：图片、语音、视频、小视频的媒体id，可以调用获取临时素材接口拉取数据
     */
    private String msgMediaId;

    /**
     * 地理位置：维度
     */
    private Double msgLocationX;

    /**
     * 地理位置：经度
     */
    private Double msgLocationY;

    /**
     * 地理位置：地图缩放大小
     */
    private Double msgScale;

    /**
     * 地理位置：地理位置信息
     */
    private String msgLabel;

    /**
     * 链接：收到的消息标题
     */
    private String msgTitle;

    /**
     * 链接：消息描述
     */
    private String msgDescription;

    /**
     * 链接：消息链接
     */
    private String msgUrl;

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
     * 接收到的消息创建时间（整形）
     */
    private Long msgCreateTime;

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

    public static final String MSG_ID = "msg_id";

    public static final String WECHAT_ID = "wechat_id";

    public static final String OPEN_ID = "open_id";

    public static final String MSG_TYPE = "msg_type";

    public static final String MSG_CONTENT = "msg_content";

    public static final String MSG_PIC_URL = "msg_pic_url";

    public static final String MSG_FORMAT = "msg_format";

    public static final String MSG_THUMB_MEDIA_ID = "msg_thumb_media_id";

    public static final String MSG_MEDIA_ID = "msg_media_id";

    public static final String MSG_LOCATION_X = "msg_location_x";

    public static final String MSG_LOCATION_Y = "msg_location_y";

    public static final String MSG_SCALE = "msg_scale";

    public static final String MSG_LABEL = "msg_label";

    public static final String MSG_TITLE = "msg_title";

    public static final String MSG_DESCRIPTION = "msg_description";

    public static final String MSG_URL = "msg_url";

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

    public static final String MSG_CREATE_TIME = "msg_create_time";

    public static final String REP_CREATE_TIME = "rep_create_time";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String DELETE_TIME = "delete_time";

}
