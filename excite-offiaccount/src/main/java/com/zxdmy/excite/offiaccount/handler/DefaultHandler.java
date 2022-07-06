package com.zxdmy.excite.offiaccount.handler;

import com.zxdmy.excite.common.consts.OffiaccountConsts;
import com.zxdmy.excite.offiaccount.builder.MsgBuilder;
import com.zxdmy.excite.offiaccount.service.IOffiaccountCommonService;
import com.zxdmy.excite.ums.entity.UmsMpReply;
import com.zxdmy.excite.ums.service.IUmsMpReplyService;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 默认的消息路由
 *
 * @author 拾年之璐
 * @since 2022/6/28 17:19
 */
@Component
@AllArgsConstructor
public class DefaultHandler extends AbstractHandler {

    private IUmsMpReplyService mpReplyService;

    private IOffiaccountCommonService commonService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        // 从数据库读取默认的消息
        UmsMpReply mpReply = mpReplyService.getOneReplyByTypeOrKey(OffiaccountConsts.ReplyType.DEFAULT_REPLY, null);
        // 构造消息
        WxMpXmlOutMessage outMessage = new MsgBuilder().build(mpReply, wxMessage, wxMpService);
        // 消息异步持久化
        commonService.saveMessage2DB(wxMessage, mpReply, outMessage);
        // 返回消息
        return outMessage;
    }
}
