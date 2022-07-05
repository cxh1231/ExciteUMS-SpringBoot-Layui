package com.zxdmy.excite.ums.vo;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/6/30 11:47
 */
@Data
@Accessors(chain = true)
public class OauthUserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登录方式
     */
    private String type;

    /**
     * 发起该登录请求的APPID
     */
    private String appid;

    /**
     * 用户唯一标识
     */
    private String userid;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别（1：男，2：女，0：未知）
     */
    private Integer sex;

    /**
     * 附加信息
     */
    private String text;

    /**
     * 该记录的 Token 值
     */
    private String token;

    /**
     * 过期时间，单位：秒
     */
    private Long expire;
}
