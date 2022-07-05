package com.zxdmy.excite.offiaccount.api;

import lombok.AllArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 公众号消息事件服务类
 *
 * @author 拾年之璐
 * @since 2022/7/5 14:38
 */
@Component
@AllArgsConstructor
public class OffiaccountEventService {

    private final WxMpService wxService;

    private final WxMpMessageRouter messageRouter;

    final Logger log = LoggerFactory.getLogger(this.getClass());


    /**
     * 检查签名
     *
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param signature 签名
     * @return 是否通过
     */
    public Boolean checkSignature(String timestamp, String nonce, String signature) {
        return wxService.checkSignature(timestamp, nonce, signature);
    }


    /**
     * 处理微信服务器发来的消息
     *
     * @param requestBody  微信服务器发来的消息体
     * @param timestamp    时间戳
     * @param nonce        随机数
     * @param encType      加密类型
     * @param msgSignature 消息签名
     * @return 处理结果
     */
    public String eventMessageService(String requestBody, String timestamp, String nonce, String encType, String msgSignature) {
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
            try {
                WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxService.getWxMpConfigStorage(), timestamp, nonce, msgSignature);
                // 将消息交给route处理
                WxMpXmlOutMessage outMessage = this.route(inMessage);
                if (outMessage == null) {
                    return "";
                }
                // 返回的消息，同样加墨
                out = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
            } catch (Exception e) {
                this.log.error("解密出错，可能来自非官方的请求。错误原因：{}", e.getMessage());
            }
        }
        return out;
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
