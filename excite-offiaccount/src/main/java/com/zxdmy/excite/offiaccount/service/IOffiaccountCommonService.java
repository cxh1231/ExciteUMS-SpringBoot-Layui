package com.zxdmy.excite.offiaccount.service;

import com.zxdmy.excite.ums.entity.UmsMpReply;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * 公众号的公共服务类
 *
 * @author 拾年之璐
 * @since 2022/6/29 19:42
 */
public interface IOffiaccountCommonService {

    /**
     * 保存普通消息至数据库
     *
     * @param wxMessage 接收用户的消息
     * @param mpReply   返回给用户的消息
     */
    void saveMessage2DB(WxMpXmlMessage wxMessage, UmsMpReply mpReply, WxMpXmlOutMessage outMessage);

    /**
     * 保存事件消息至数据库
     *
     * @param wxMessage 接收用户的消息
     * @param mpReply   返回给用户的消息
     */
    void saveEvent2DB(WxMpXmlMessage wxMessage, UmsMpReply mpReply, WxMpXmlOutMessage outMessage);

    /**
     * 保存用户信息至数据库，同时进行其他操作
     *
     * @param wxMessage 保存用户信息至数据库
     */
    void saveUser2DB(WxMpXmlMessage wxMessage);
}
