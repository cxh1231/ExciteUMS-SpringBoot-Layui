package com.zxdmy.excite.payment.vo;

import com.zxdmy.excite.common.enums.PaymentEnums;
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
 * @since 2022/7/11 11:16
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class BaseResponseVo extends BasePaymentVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 返回码
     */
    private Integer code = PaymentEnums.SUCCESS.getCode();

    /**
     * 返回信息
     */
    private String msg = PaymentEnums.SUCCESS.getMessage();

    public static final String CODE = "code";

    public static final String MSG = "msg";
}
