package com.zxdmy.excite.admin.controller.offiaccount;

import com.zxdmy.excite.common.base.BaseController;
import com.zxdmy.excite.common.base.BaseResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.guide.WxMpGuideAcctConfig;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.WxMpHostConfig;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/3/30 12:01
 */
@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/mp/event")
public class EventController extends BaseController {
    // 微信服务
//    private final WxMpService wxService;
    // 微信消息/事件消息路由
//    private final WxMpMessageRouter messageRouter;

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
    @GetMapping(produces = "text/plain;charset=utf-8")
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
        else if (getWxMpService().checkSignature(timestamp, nonce, signature)) {
            // 验证成功，返回随机字符串，接入成功
            return echostr;
        }
        // 签名验证失败，返回错误信息
        else
            return "非法请求";
    }


    /**
     * @param requestBody
     * @param signature
     * @param timestamp
     * @param nonce
     * @param openid
     * @param msgSignature
     * @return
     */
    @ResponseBody
    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@RequestBody String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam("openid") String openid,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {

        log.info("\n接收微信请求：[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}],"
                + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ", openid, signature, encType, msgSignature, timestamp, nonce, requestBody);


        if (!getWxMpService().checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        String out = null;
        if (encType == null) {
            // 明文传输的消息
            log.info("明文消息");
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            inMessage.setContent("777");
//            WxMpXmlOutMessage outMessage = this.route(inMessage);
            WxMpXmlOutMessage outMessage = WxMpXmlOutMessage
                    .TEXT()
                    .content("嘎嘎嘎").fromUser(inMessage.getToUser()).toUser(inMessage.getFromUser())
                    .build();
//            if (outMessage == null) {
//                return "";
//            }
            out = outMessage.toXml();

        } else if ("aes".equalsIgnoreCase(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, getWxMpService().getWxMpConfigStorage(), timestamp, nonce, msgSignature);
            log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toEncryptedXml(getWxMpService().getWxMpConfigStorage());
        }

        log.debug("\n组装回复信息：{}", out);
        return out;
    }


    private WxMpService getWxMpService() {

        // 实例化配置类
        WxMpDefaultConfigImpl wxMpDefaultConfig = new WxMpDefaultConfigImpl();
        // 配置信息
        wxMpDefaultConfig.setAppId("wxf33dd15c03a05edd");
        wxMpDefaultConfig.setSecret("6865f84a88d51320c2f897678a7142eb");
        // 接口配置里的Token值
        wxMpDefaultConfig.setToken("chenxiuhao");
        // 接口配置里的EncodingAESKey值
        wxMpDefaultConfig.setAesKey("111");

        // 实例化服务类接口
        WxMpService wxMpService = new WxMpServiceImpl();
        // 添加配置类
        wxMpService.setWxMpConfigStorage(wxMpDefaultConfig);
        // 返回信息
        return wxMpService;
    }

    private WxMpMessageRouter getMessageRouter() {
        return new WxMpMessageRouter(this.getWxMpService());
    }

    /**
     * 进行路由操作
     *
     * @param message 接收到微信推送的XML序列化消息
     * @return 返回给微信服务器的XML消息
     */
    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return this.getMessageRouter().route(message);
        } catch (Exception e) {
            log.error("路由消息时出现异常！", e);
        }
        return null;
    }
}
