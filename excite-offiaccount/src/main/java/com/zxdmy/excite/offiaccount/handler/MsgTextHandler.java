package com.zxdmy.excite.offiaccount.handler;

import com.zxdmy.excite.offiaccount.builder.TextBuilder;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 接收文本消息事件
 *
 * @author 拾年之璐
 * @since 2022/6/28 17:20
 */
@Component
public class MsgTextHandler extends AbstractHandler {
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        // 获取用户发送的消息
        String content = wxMessage.getContent();
        // TODO 根据用户的消息进行处理

        // 组装返回的消息
        String replyContent = "收到消息：" + content;

        // 返回消息
        return new TextBuilder().build(replyContent, wxMessage, wxMpService);


        //当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
//        try {
//            if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
//                    && wxMpService.getKefuService().kfOnlineList()
//                    .getKfOnlineList().size() > 0) {
//                return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE()
//                        .fromUser(wxMessage.getToUser())
//                        .toUser(wxMessage.getFromUser()).build();
//            }
//        } catch (WxErrorException e) {
//            e.printStackTrace();
//        }


    }
}
