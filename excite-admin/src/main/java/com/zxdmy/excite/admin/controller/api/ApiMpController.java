package com.zxdmy.excite.admin.controller.api;

import com.zxdmy.excite.common.base.BaseController;
import com.zxdmy.excite.common.base.BaseResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 微信公众号开发接口
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/3/30 11:25
 */
@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/api/mp")
public class ApiMpController extends BaseController {

    // 微信服务
    private final WxMpService wxService;

    // 微信消息/事件消息路由
    private final WxMpMessageRouter messageRouter;

    /**
     * 首次进行【服务器配置】时，接入认证使用
     *
     * @param signature 微信加密签名，
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @return 认证成功：返回随机字符串echostr | 认证失败：返回其他信息
     */
    @ResponseBody
    @GetMapping(value = "event", produces = "text/plain;charset=utf-8")
    public String authGet(@RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {

        log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);

        // 请求参数不可或缺
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }
        // 进行签名验证
        else if (wxService.checkSignature(timestamp, nonce, signature)) {
            // 验证成功，返回随机字符串，接入成功
            return echostr;
        }
        // 签名验证失败，返回错误信息
        else
            return "非法请求";
    }


    /**
     * 处理微信收到的消息与事件
     *
     * @param requestBody  请求的xml
     * @param signature    签名
     * @param timestamp    时间戳
     * @param nonce        随机字符串
     * @param openid       用户的开放ID
     * @param msgSignature 消息签名
     * @return 返回的消息
     */
    @ResponseBody
    @PostMapping(value = "event", produces = "application/xml; charset=UTF-8")
    public String post(@RequestBody String requestBody,
                       @RequestParam(name = "signature") String signature,
                       @RequestParam(name = "timestamp") String timestamp,
                       @RequestParam(name = "nonce") String nonce,
                       @RequestParam(name = "openid") String openid,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        log.info("\n接收微信请求：[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}],"
                + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ", openid, signature, encType, msgSignature, timestamp, nonce, requestBody);

        // 校验时间戳等签名信息
        if (!wxService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }
        // 输出内容
        String out = null;
        // 明文传输的消息
        if (encType == null) {
            // 获取接收到的数据
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            // 交给route
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }
            out = outMessage.toXml();
        }
        // aes加密的消息
        else if ("aes".equalsIgnoreCase(encType)) {
            // 消息解密
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxService.getWxMpConfigStorage(), timestamp, nonce, msgSignature);
            // 将消息交给route处理
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }
            // 返回的消息，同样加墨
            out = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
        }
        // 返回组装后的消息
        return out;
    }

    @GetMapping(value = "access")
    @ResponseBody
    public BaseResult getAccessToken() throws WxErrorException {
        return success(wxService.getAccessToken());
    }

    /**
     * 进行路由操作
     *
     * @param message 接收到微信推送的XML序列化消息
     * @return 返回给微信服务器的XML消息
     */
    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return this.messageRouter.route(message);
        } catch (Exception e) {
            log.error("路由消息时出现异常！", e);
        }
        return null;
    }
}
