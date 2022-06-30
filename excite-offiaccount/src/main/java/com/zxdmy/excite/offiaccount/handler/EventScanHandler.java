package com.zxdmy.excite.offiaccount.handler;

import com.zxdmy.excite.offiaccount.builder.TextBuilder;
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
 * @since 2022/6/29 17:01
 */
@Component
@AllArgsConstructor
public class EventScanHandler extends AbstractHandler {

    IOffiaccountCommonService commonService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        // 处理登录逻辑
        commonService.saveUserByScanLogin2Redis(wxMessage.getEventKey(), wxMessage.getFromUser());
        // 返回登录成功的信息
        return new TextBuilder().build("登录成功！", wxMessage, wxMpService);
    }
}
