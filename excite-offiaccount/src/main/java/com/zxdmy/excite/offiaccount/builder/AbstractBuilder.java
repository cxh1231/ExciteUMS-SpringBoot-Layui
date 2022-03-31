package com.zxdmy.excite.offiaccount.builder;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/3/31 19:13
 */
public abstract class AbstractBuilder {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public abstract WxMpXmlOutMessage build(String content, WxMpXmlMessage wxMessage, WxMpService service);
}
