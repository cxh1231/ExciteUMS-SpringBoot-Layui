package com.zxdmy.excite.ums.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 微信自动回复表
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-06-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("ums_mp_reply")
public class UmsMpReply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 类型（1、关注时默认回复；2、消息默认回复；3、关键词回复；4、登录成功回复）
     */
    private Integer type;

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 关键词匹配类型（1、全匹配，2、半匹配）
     */
    private String mate;

    /**
     * 回复消息类型（text：文本；image：图片；voice：语音；video：视频；music：音乐；news：图文）
     */
    private String repType;

    /**
     * 回复文本消息
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
     * 图文消息：点击图文消息跳转链接
     */
    private String repUrl;

    /**
     * 图文消息：图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
     */
    private String repPicUrl;

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
     * 状态（1-正常 0-禁用）
     */
    private Integer status;

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

    public static final String TYPE = "type";

    public static final String KEYWORD = "keyword";

    public static final String MATE = "mate";

    public static final String REP_TYPE = "rep_type";

    public static final String REP_CONTENT = "rep_content";

    public static final String REP_TITLE = "rep_title";

    public static final String REP_DESCRIPTION = "rep_description";

    public static final String REP_URL = "rep_url";

    public static final String REP_PIC_URL = "rep_pic_url";

    public static final String REP_MEDIA_ID = "rep_media_id";

    public static final String REP_MUSIC_URL = "rep_music_url";

    public static final String REP_HQ_MUSIC_URL = "rep_hq_music_url";

    public static final String REP_THUMB_MEDIA_ID = "rep_thumb_media_id";

    public static final String REMARK = "remark";

    public static final String STATUS = "status";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String DELETE_TIME = "delete_time";

}
