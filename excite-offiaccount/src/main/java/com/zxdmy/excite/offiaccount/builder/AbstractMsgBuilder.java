package com.zxdmy.excite.offiaccount.builder;

import com.zxdmy.excite.ums.entity.UmsMpReply;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 微信回复消息构造器
 *
 * @author 拾年之璐
 * @since 2022/6/29 16:32
 */
public abstract class AbstractMsgBuilder {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 构造消息
     *
     * @param mpReply   回复消息实体
     * @param wxMessage 接收到的消息
     * @param service   微信服务类
     * @return 发送给微信服务器的XML消息
     */
    public abstract WxMpXmlOutMessage build(
            UmsMpReply mpReply,
            WxMpXmlMessage wxMessage,
            WxMpService service
    );

}
