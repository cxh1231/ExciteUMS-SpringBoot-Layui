package com.zxdmy.excite.geek.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GeekUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户微信端的openid
     */
    private String uuid;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户积分
     */
    private Integer points;

    /**
     * 不知道
     */
    private Integer num;

    /**
     * 会员到期时间（秒）
     */
    private Integer vipTime;

    /**
     * 时间什么？估计和签到相关
     */
    private String times;

    /**
     * 签到什么？
     */
    private String qiandao;

    /**
     * 登录次数
     */
    private Integer loginCount;

    /**
     * 最后登录时间
     */
    private Integer lastLoginTime;

    /**
     * 用户状态 1-正常 0-禁用
     */
    private Integer status;

    /**
     * 注册时间
     */
    private Integer createTime;

    /**
     * 更新时间
     */
    private Integer updateTime;

    /**
     * 删除时间
     */
    private Integer deleteTime;


}
