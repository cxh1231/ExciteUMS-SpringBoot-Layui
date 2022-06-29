package com.zxdmy.excite.offiaccount.handler;

import com.zxdmy.excite.common.consts.OffiaccountConsts;
import com.zxdmy.excite.offiaccount.builder.MsgBuilder;
import com.zxdmy.excite.offiaccount.service.IOffiaccountCommonService;
import com.zxdmy.excite.ums.entity.UmsMpReply;
import com.zxdmy.excite.ums.service.IUmsMpEventService;
import com.zxdmy.excite.ums.service.IUmsMpMessageService;
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
 * 接收文本消息事件
 *
 * @author 拾年之璐
 * @since 2022/6/28 17:20
 */
@Component
@AllArgsConstructor
public class MsgTextHandler extends AbstractHandler {

    private IUmsMpEventService mpEventService;

    private IUmsMpMessageService mpMessageService;

    private IUmsMpReplyService mpReplyService;

    private IOffiaccountCommonService commonService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        // 获取用户发送的消息
        String content = wxMessage.getContent();
        // 从[关键词]数据库检索需要回复的信息（如果有多条，选择最近的一条）
        UmsMpReply msgReply = mpReplyService.getReplyByType(OffiaccountConsts.ReplyType.KEYWORD_REPLY, content);
        // 如果[关键词]为空，则返回默认的信息
        if (null == msgReply) {
            msgReply = mpReplyService.getReplyByType(OffiaccountConsts.ReplyType.DEFAULT_REPLY, null);
        }
        // 根据回复详情，构造消息
        WxMpXmlOutMessage outMessage = new MsgBuilder().build(msgReply, wxMessage, wxMpService);
        // 异步：写入数据库，持久化
        commonService.saveMessage2DB(wxMessage, msgReply, outMessage);
        // 返回消息
        return outMessage;
    }
}
