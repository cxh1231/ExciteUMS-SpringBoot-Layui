package com.zxdmy.excite.payment.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.zxdmy.excite.common.annotations.ValidationEnum;
import com.zxdmy.excite.common.utils.SignUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.TreeMap;

/**
 * 创建订单接口请求数据
 *
 * @author 拾年之璐
 * @since 2022/4/2 10:37
 */
@Getter
@Setter
@Accessors(chain = true)
public class PaymentCreateRequestVo extends BasePaymentVo implements Serializable {

    /**
     * 公众号等微信平台的用户id，微信内支付时必传
     */
//    private String openid;

    /**
     * 订单标题
     */
    @NotBlank(message = "缺少订单标题参数")
    @Length(min = 1, max = 127, message = "订单标题格式错误")
    private String title;

    /**
     * 订单金额
     */
    @NotBlank(message = "缺少订单金额参数")
    @Length(min = 1, max = 12, message = "订单金额格式错误")
    @Pattern(regexp = "\\d+(\\.\\d{1,2})?", message = "订单金额格式错误")
    private String amount;

    /**
     * 支付成功回调地址
     */
    @SerializedName(value = "notifyUrl", alternate = {"notify_url"})
    @NotBlank(message = "缺少回调网址参数")
    @Pattern(regexp = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~/])+$", message = "回调网址格式错误")
    @JsonProperty(value = "notify_url")
    private String notifyUrl;

    /**
     * 支付成功返回网址
     */
    @SerializedName(value = "returnUrl", alternate = {"return_url"})
    @Pattern(regexp = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~/])+$", message = "返回网址格式错误")
    @JsonProperty(value = "return_url")
    private String returnUrl;

    /**
     * 支付取消返回网址
     */
    @SerializedName(value = "cancelUrl", alternate = {"cancel_url"})
    @Pattern(regexp = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~/])+$", message = "取消网址格式错误")
    @JsonProperty(value = "cancel_url")
    private String cancelUrl;

    /**
     * 支付渠道：wechat | alipay
     */
    @SerializedName(value = "payChannel", alternate = {"pay_channel"})
    @NotBlank(message = "缺少支付渠道参数")
    @ValidationEnum(strValues = {"alipay", "wechat"}, message = "支付渠道参数错误")
    @JsonProperty(value = "pay_channel")
    private String payChannel;

    /**
     * 支付场景：qrcode | page | wechat
     */
    @SerializedName(value = "payScene", alternate = {"pay_scene"})
    @NotBlank(message = "缺少支付场景参数")
    @ValidationEnum(strValues = {"qrcode", "page", "wechat", "wap"}, message = "支付场景参数错误")
    @JsonProperty(value = "pay_scene")
    private String payScene;

    /**
     * 附加信息
     */
    @Length(max = 64, message = "附加信息格式错误")
    private String attach;


    public static final String OPENID = "openid";

    public static final String TITLE = "title";

    public static final String AMOUNT = "amount";

    public static final String PAY_CHANNEL = "pay_channel";

    public static final String PAY_SCENE = "pay_scene";

    public static final String NOTIFY_URL = "notify_url";

    public static final String RETURN_URL = "return_url";

    public static final String CANCEL_URL = "cancel_url";

    public static final String ATTACH = "attach";


    /**
     * 生成字典序Map
     *
     * @return 字典序map
     */
    public TreeMap<String, Object> getTreeMap() {
        TreeMap<String, Object> treeMap = new TreeMap<>();
//        treeMap.put(OPENID, this.getOpenid());
        treeMap.put(TITLE, this.getTitle());
        treeMap.put(AMOUNT, this.getAmount());
        treeMap.put(NOTIFY_URL, this.getNotifyUrl());
        treeMap.put(RETURN_URL, this.getReturnUrl());
        treeMap.put(CANCEL_URL, this.getCancelUrl());
        treeMap.put(PAY_CHANNEL, this.getPayChannel());
        treeMap.put(PAY_SCENE, this.getPayScene());
        treeMap.put(ATTACH, this.getAttach());

        treeMap.put(APPID, this.getAppid());
        treeMap.put(TIME, this.getTime());
        treeMap.put(NONCE, this.getNonce());
        return treeMap;
    }
}
