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
     * 保存普通关注用户信息至数据库
     *
     * @param openId     用户的OpenID
     * @param createTime 消息的创建时间（如果无法获取用户详细信息，这个时间就是关注时间）
     */
    void saveUserBySubscribe2DB(String openId, Long createTime);

    void saveUserByUnsubscribe2DB(String openId, Long createTime);

    /**
     * 保存关注+登录的用户信息至数据库和Redis
     *
     * @param sceneStr   关注的场景值
     * @param openId     用户的OpenID
     * @param createTime 消息的创建时间（如果无法获取用户详细信息，这个时间就是关注时间）
     */
    void saveUserBySubscribeLogin(String sceneStr, String openId, Long createTime);

    /**
     * 保存登录的用户信息至Redis
     *
     * @param sceneStr 场景值，即为Redis的KEY值
     * @param openId   登录用户的OpenID
     */
    void saveUserByScanLogin2Redis(String sceneStr, String openId);

    /**
     * 通过验证码登录用户信息保存
     *
     * @param verifyCode 验证码
     * @param openId     用户的OpenID
     * @param createTime 消息的创建时间（如果无法获取用户详细信息，这个时间就是关注时间）
     */
    void saveUserByVerifyCodeLogin(String verifyCode, String openId, Long createTime);

}
