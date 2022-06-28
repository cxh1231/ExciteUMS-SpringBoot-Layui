package com.zxdmy.excite.offiaccount.handler;

import com.google.gson.Gson;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理微信推送消息的处理器接口.
 *
 * @author 拾年之璐
 * @since 2022/3/31 19:05
 */
public abstract class AbstractHandler implements WxMpMessageHandler {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected final Gson gson = new Gson();
}
