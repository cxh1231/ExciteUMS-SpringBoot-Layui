package com.zxdmy.excite.offiaccount.builder;

import com.zxdmy.excite.ums.entity.UmsMpReply;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;

import java.util.List;

/**
 * 微信回复消息构造器
 *
 * @author 拾年之璐
 * @since 2022/6/29 16:33
 */
public class MsgBuilder extends AbstractMsgBuilder {

    /**
     * 构造消息
     *
     * @param mpReply   回复消息实体
     * @param wxMessage 接收到的消息
     * @param service   微信服务类
     * @return 发送给微信服务器的XML消息
     */
    @Override
    public WxMpXmlOutMessage build(UmsMpReply mpReply, WxMpXmlMessage wxMessage, WxMpService service) {
        if (null == mpReply || mpReply.getRepType() == null) {
            return null;
        }
        // text：文本；
        else if (mpReply.getRepType().equals(WxConsts.XmlMsgType.TEXT)) {
            return WxMpXmlOutMessage.TEXT()
                    .content(mpReply.getRepContent())
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser())
                    .build();
        }
        // news：图文
        else if (mpReply.getRepType().equals(WxConsts.XmlMsgType.NEWS)) {
            // 构造图文
            WxMpXmlOutNewsMessage.Item article = new WxMpXmlOutNewsMessage.Item();
            article.setTitle(mpReply.getRepTitle());
            article.setDescription(mpReply.getRepDescription());
            article.setPicUrl(mpReply.getRepPicUrl());
            article.setUrl(mpReply.getRepUrl());
            // 返回图文
            return WxMpXmlOutMessage.NEWS()
                    .addArticle(article)
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser())
                    .build();
        }
        // image：图片；
        else if (mpReply.getRepType().equals(WxConsts.XmlMsgType.IMAGE)) {
            return WxMpXmlOutMessage.IMAGE()
                    .mediaId(mpReply.getRepMediaId())
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser())
                    .build();
        }
        // voice：语音；
        else if (mpReply.getRepType().equals(WxConsts.XmlMsgType.VOICE)) {
            return WxMpXmlOutMessage.VOICE()
                    .mediaId(mpReply.getRepMediaId())
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser())
                    .build();
        }

        // video：视频；
        else if (mpReply.getRepType().equals(WxConsts.XmlMsgType.VIDEO)) {
            return WxMpXmlOutMessage.VIDEO()
                    .mediaId(mpReply.getRepMediaId())
                    .title(mpReply.getRepTitle())
                    .description(mpReply.getRepDescription())
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser())
                    .build();
        }
        // music：音乐；
        else if (mpReply.getRepType().equals(WxConsts.XmlMsgType.MUSIC)) {
            return WxMpXmlOutMessage.MUSIC()
                    .title(mpReply.getRepTitle())
                    .description(mpReply.getRepDescription())
                    .musicUrl(mpReply.getRepMusicUrl())
                    .hqMusicUrl(mpReply.getRepHqMusicUrl())
                    .thumbMediaId(mpReply.getRepThumbMediaId())
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser())
                    .build();
        } else {
            return null;
        }
    }
}
