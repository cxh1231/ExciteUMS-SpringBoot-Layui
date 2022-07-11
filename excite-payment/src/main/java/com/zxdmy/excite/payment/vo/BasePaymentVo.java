package com.zxdmy.excite.payment.vo;

import cn.hutool.core.util.RandomUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/4/2 10:40
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class BasePaymentVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * app用户的appid
     */
    @NotBlank(message = "缺少APPID参数")
    @Length(min = 12, max = 12, message = "APPID参数格式错误")
    private String appid;

    /**
     * 时间戳
     */
    @NotBlank(message = "缺少时间戳参数")
    @Length(min = 10, max = 14, message = "时间戳参数格式错误")
    private String time = String.valueOf((int) (System.currentTimeMillis() / 1000));

    /**
     * 随机值
     */
    @NotBlank(message = "缺少随机数参数")
    @Length(min = 8, max = 64, message = "随机数参数格式错误")
    private String nonce = RandomUtil.randomString(16);;

    /**
     * 签名
     */
    @NotBlank(message = "缺少签名参数")
    @Length(min = 32, max = 32, message = "签名参数格式错误")
    private String hash;

    public static final String APPID = "appid";

    public static final String TIME = "time";

    public static final String NONCE = "nonce";

    public static final String HASH = "hash";
}
