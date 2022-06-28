package com.zxdmy.excite.offiaccount.handler;

import lombok.SneakyThrows;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 将所有的消息进行持久化处理，同时可以可选进行消息日志输出
 *
 * @author 拾年之璐
 * @since 2022/3/31 19:08
 */
@Component
public class LogHandler extends AbstractHandler {

    @SneakyThrows
    @Override
    public WxMpXmlOutMessage handle(
            WxMpXmlMessage wxMessage,
            Map<String, Object> context,
            WxMpService wxMpService,
            WxSessionManager sessionManager
    ) {
        // 打印日志
        this.logger.info("\n接收到请求消息，内容：【{}】", wxMessage.toString());
        Thread.sleep(10000L);
        this.logger.info("信息保存成功");
        // 可以选择持久化
        return null;
    }
}
