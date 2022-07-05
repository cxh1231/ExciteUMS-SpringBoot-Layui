package com.zxdmy.excite.offiaccount.handler;

import com.zxdmy.excite.offiaccount.service.IOffiaccountCommonService;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/3/31 20:03
 */
@Component
@AllArgsConstructor
public class EventSubscribeCancelHandler extends AbstractHandler {

    IOffiaccountCommonService commonService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        // 取关更新用户信息
        commonService.saveUserByUnsubscribe2DB(wxMessage.getFromUser(), wxMessage.getCreateTime());
        return null;
    }
}
