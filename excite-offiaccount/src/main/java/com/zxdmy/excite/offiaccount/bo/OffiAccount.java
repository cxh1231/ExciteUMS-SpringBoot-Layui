package com.zxdmy.excite.offiaccount.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 公众号账号
 */
@Data
@Accessors(chain = true)
public class OffiAccount implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 公众号名称
     */
    private String name;

    /**
     * 账号类型
     */
    private int type;

    /**
     * 认证状态
     */
    private boolean verified;

    /**
     * id
     */
    private String appid;

    /**
     * appSecret
     */
    private String appSecret;

    /**
     * token
     */
    private String token;

    /**
     * aesKey
     */
    private String aesKey;

}
