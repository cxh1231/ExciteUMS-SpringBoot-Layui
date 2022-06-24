package com.zxdmy.excite.offiaccount.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 微信公众号账号配置信息
 */
@Data
@Accessors(chain = true)
public class OffiaccountBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 保存配置用到的KEY
     */
    private String key;

    // 微信公众号基本信息

    /**
     * 公众号名称，如：测试号
     */
    private String name;

    /**
     * 公众号原始ID，将用来获取公众号二维码。如：gh_8e5f0f438f2e
     */
    private String originalId;

    /**
     * 账号类型：订阅号、服务号
     */
    private String type;

    /**
     * 认证状态：1 - 已认证，0 - 未认证
     */
    private boolean verified;

    // 公众号开发者信息

    /**
     * 开发者ID(AppID)
     */
    private String appid;

    /**
     * 开发者密码(AppSecret)
     */
    private String appSecret;

    // 服务器配置

    /**
     * 令牌(Token)
     */
    private String token = "GeekLabUmsMPCenter";

    /**
     * 消息加解密密钥(EncodingAESKey)
     */
    private String aesKey;

    /**
     * 消息加解密方式：0 - 明文（plaintext） 1 - 兼容（compatible） 2 - 密文（ciphertext）
     */
    private int encryptionType;

}
