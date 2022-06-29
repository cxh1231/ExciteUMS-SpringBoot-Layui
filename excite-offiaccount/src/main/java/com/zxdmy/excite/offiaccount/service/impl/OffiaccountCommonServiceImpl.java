package com.zxdmy.excite.offiaccount.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxdmy.excite.offiaccount.service.IOffiaccountCommonService;
import com.zxdmy.excite.ums.entity.UmsMpEvent;
import com.zxdmy.excite.ums.entity.UmsMpMessage;
import com.zxdmy.excite.ums.entity.UmsMpReply;
import com.zxdmy.excite.ums.service.IUmsMpEventService;
import com.zxdmy.excite.ums.service.IUmsMpMessageService;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 公众号的公共服务类
 *
 * @author 拾年之璐
 * @since 2022/6/29 19:44
 */
@Service
@AllArgsConstructor
public class OffiaccountCommonServiceImpl implements IOffiaccountCommonService {

    private IUmsMpMessageService mpMessageService;

    private IUmsMpEventService mpEventService;

    /**
     * 保存普通消息至数据库
     *
     * @param wxMessage  接收用户的消息
     * @param mpReply    返回给用户的消息
     * @param outMessage 返回的消息
     */
    @Async
    @Override
    public void saveMessage2DB(WxMpXmlMessage wxMessage, UmsMpReply mpReply, WxMpXmlOutMessage outMessage) {
        // 接收的消息为空，直接返回
        if (null == wxMessage) {
            return;
        }
        // 普通消息实体
        UmsMpMessage message = new UmsMpMessage();
        // 依次写入
        message.setMsgId(wxMessage.getMsgId())
                .setWechatId(wxMessage.getToUser())
                .setOpenId(wxMessage.getFromUser())
                .setMsgType(wxMessage.getMsgType())
                .setMsgContent(wxMessage.getContent())
                .setMsgPicUrl(wxMessage.getPicUrl())
                .setMsgFormat(wxMessage.getFormat())
                .setMsgThumbMediaId(wxMessage.getThumbMediaId())
                .setMsgMediaId(wxMessage.getMediaId())
                .setMsgLocationX(wxMessage.getLocationX())
                .setMsgLocationY(wxMessage.getLocationY())
                .setMsgScale(wxMessage.getScale())
                .setMsgLabel(wxMessage.getLabel())
                .setMsgTitle(wxMessage.getTitle())
                .setMsgDescription(wxMessage.getDescription())
                .setMsgUrl(wxMessage.getUrl())
                .setMsgCreateTime(wxMessage.getCreateTime());
        // 写入返回的数据
        if (null != mpReply && outMessage != null) {
            message.setRepType(mpReply.getRepType())
                    .setRepContent(mpReply.getRepContent())
                    .setRepTitle(mpReply.getRepTitle())
                    .setRepDescription(mpReply.getRepDescription())
                    .setRepPicUrl(mpReply.getRepPicUrl())
                    .setRepUrl(mpReply.getRepUrl())
                    .setRepMediaId(mpReply.getRepMediaId())
                    .setRepMusicUrl(mpReply.getRepMusicUrl())
                    .setRepHqMusicUrl(mpReply.getRepHqMusicUrl())
                    .setRepThumbMediaId(mpReply.getRepThumbMediaId())
                    .setRepCreateTime(outMessage.getCreateTime());
        }
        // 保存至数据库（避免消息重复送达，以消息ID为主键，进行去重）
        QueryWrapper<UmsMpMessage> wrapper = new QueryWrapper<>();
        wrapper.eq(UmsMpMessage.MSG_ID, message.getMsgId());
        mpMessageService.saveOrUpdate(message, wrapper);
    }

    /**
     * 保存事件消息至数据库
     *
     * @param wxMessage  接收用户的消息
     * @param mpReply    返回给用户的消息
     * @param outMessage 返回的消息
     */
    @Async
    @Override
    public void saveEvent2DB(WxMpXmlMessage wxMessage, UmsMpReply mpReply, WxMpXmlOutMessage outMessage) {
        // 接收的消息为空，直接返回
        if (null == wxMessage) {
            return;
        }
        UmsMpEvent event = new UmsMpEvent();
        event.setWechatId(wxMessage.getToUser())
                .setOpenId(wxMessage.getFromUser())
                .setEvent(wxMessage.getEvent())
                .setEventKey(wxMessage.getEventKey())
                .setEventTicket(wxMessage.getTicket())
                .setEventLatitude(wxMessage.getLatitude())
                .setEventLongitude(wxMessage.getLongitude())
                .setEventPrecision(wxMessage.getPrecision())
                .setEventMenuId(wxMessage.getMenuId())
                .setEventScanType(wxMessage.getScanCodeInfo().getScanType())
                .setEventScanResult(wxMessage.getScanCodeInfo().getScanResult())
                .setEventCreateTime(wxMessage.getCreateTime());
        if (mpReply != null && outMessage != null) {
            event.setRepType(mpReply.getRepType())
                    .setRepContent(mpReply.getRepContent())
                    .setRepTitle(mpReply.getRepTitle())
                    .setRepDescription(mpReply.getRepDescription())
                    .setRepPicUrl(mpReply.getRepPicUrl())
                    .setRepUrl(mpReply.getRepUrl())
                    .setRepMediaId(mpReply.getRepMediaId())
                    .setRepMusicUrl(mpReply.getRepMusicUrl())
                    .setRepHqMusicUrl(mpReply.getRepHqMusicUrl())
                    .setRepThumbMediaId(mpReply.getRepThumbMediaId())
                    .setRepCreateTime(outMessage.getCreateTime());
        }
        // 保存至数据库
        mpEventService.save(event);
    }

    /**
     * 保存用户信息至数据库，同时进行其他操作
     *
     * @param wxMessage 保存用户信息至数据库
     */
    @Override
    public void saveUser2DB(WxMpXmlMessage wxMessage) {
        // 获取关注的用户的信息
//            WxMpUser userWxInfo = wxMpService.getUserService().userInfo(wxMessage.getFromUser(), null);
//            // 回复信息实体
//            if (userWxInfo != null) {
//
//            }
    }
}
