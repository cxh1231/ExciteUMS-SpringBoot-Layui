package com.zxdmy.excite.offiaccount.handler;

import com.zxdmy.excite.common.consts.OffiaccountConsts;
import com.zxdmy.excite.offiaccount.builder.MsgBuilder;
import com.zxdmy.excite.offiaccount.builder.TextBuilder;
import com.zxdmy.excite.ums.entity.UmsMpReply;
import com.zxdmy.excite.ums.service.IUmsMpReplyService;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 关注用户事件
 *
 * @author 拾年之璐
 * @since 2022/3/31 19:58
 */
@Component
@AllArgsConstructor
public class EventSubscribeHandler extends AbstractHandler {

    private IUmsMpReplyService mpReplyService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        this.logger.info("新关注用户 OPENID: " + wxMessage.getFromUser());

        try {
            // 获取关注的用户的信息
            WxMpUser userWxInfo = wxMpService.getUserService().userInfo(wxMessage.getFromUser(), null);
            // 回复信息实体
            UmsMpReply mpReply = new UmsMpReply();
            if (userWxInfo != null) {
                // 普通关注用户
                if (wxMessage.getEventKey() == null || wxMessage.getEventKey().equals("")) {
                    // 获取关注后返回的内容
                    mpReply = mpReplyService.getReplyByType(OffiaccountConsts.ReplyType.SUBSCRIBE_REPLY, null);
                    // TODO 入库（异步处理

                }
                // 如果是通过带场景信息的二维码关注，则返回登录成功的提示
                else {
                    // 获取登录成功后返回的内容
                    mpReply = mpReplyService.getReplyByType(OffiaccountConsts.ReplyType.LOGIN_REPLY, null);
                    // TODO 入库 + 将用户信息写入redis，供开放接口调用（异步处理

                }
            }
            return new MsgBuilder().build(mpReply, wxMessage, wxMpService);
        } catch (WxErrorException e) {
            if (e.getError().getErrorCode() == 48001) {
                this.logger.info("该公众号没有获取用户信息权限！");
            }
        }


//        // 获取微信用户基本信息
//        try {
//            WxMpUser userWxInfo = wxMpService.getUserService()
//                    .userInfo(wxMessage.getFromUser(), null);
//            if (userWxInfo != null) {
//
//            }
//        } catch (WxErrorException e) {
//            if (e.getError().getErrorCode() == 48001) {
//                this.logger.info("该公众号没有获取用户信息权限！");
//            }
//        }
//
//
//        WxMpXmlOutMessage responseResult = null;
//        try {
//            responseResult = this.handleSpecial(wxMessage);
//        } catch (Exception e) {
//            this.logger.error(e.getMessage(), e);
//        }
//
//        if (responseResult != null) {
//            return responseResult;
//        }
//
//        try {
//            return new TextBuilder().build("感谢关注", wxMessage, wxMpService);
//        } catch (Exception e) {
//            this.logger.error(e.getMessage(), e);
//        }
        return new TextBuilder().build("感谢关注", wxMessage, wxMpService);
    }

    /**
     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
     */
    private WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage) {
        //TODO
        return null;
    }
}
