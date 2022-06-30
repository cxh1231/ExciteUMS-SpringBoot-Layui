package com.zxdmy.excite.offiaccount.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxdmy.excite.common.consts.OffiaccountConsts;
import com.zxdmy.excite.common.service.RedisService;
import com.zxdmy.excite.offiaccount.service.IOffiaccountCommonService;
import com.zxdmy.excite.offiaccount.vo.OffiaccountUserVo;
import com.zxdmy.excite.ums.entity.UmsMpEvent;
import com.zxdmy.excite.ums.entity.UmsMpMessage;
import com.zxdmy.excite.ums.entity.UmsMpReply;
import com.zxdmy.excite.ums.entity.UmsMpUser;
import com.zxdmy.excite.ums.service.IUmsMpEventService;
import com.zxdmy.excite.ums.service.IUmsMpMessageService;
import com.zxdmy.excite.ums.service.IUmsMpUserService;
import com.zxdmy.excite.ums.vo.OauthUserVo;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * 公众号的公共服务类
 *
 * @author 拾年之璐
 * @since 2022/6/29 19:44
 */
@Service
@AllArgsConstructor
public class OffiaccountCommonServiceImpl implements IOffiaccountCommonService {

    private final WxMpService wxService;
    private IUmsMpMessageService mpMessageService;

    private IUmsMpEventService mpEventService;

    private IUmsMpUserService mpUserService;

    private RedisService redisService;


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
                    .setRepThumbMediaId(mpReply.getRepThumbMediaId())
                    .setRepMusicUrl(mpReply.getRepMusicUrl())
                    .setRepHqMusicUrl(mpReply.getRepHqMusicUrl())
                    .setRepCreateTime(outMessage.getCreateTime());
        }
        // 保存至数据库
        mpEventService.save(event);
    }

    /**
     * 保存普通关注用户信息至数据库
     *
     * @param openId     用户的OpenID
     * @param createTime 消息的创建时间（如果无法获取用户详细信息，这个时间就是关注时间）
     */
    @Async
    @Override
    public void saveUserBySubscribe2DB(String openId, Long createTime) {
        // 如果该用户已关注过，则直接关注次数自增
        UmsMpUser user = mpUserService.getOne(new QueryWrapper<UmsMpUser>().eq(UmsMpUser.OPEN_ID, openId));
        if (user != null) {
            user.setSubscribeNum(user.getSubscribeNum() == null ? 1 : user.getSubscribeNum() + 1);
            mpUserService.updateById(user);
        }
        // 该用户未关注过，全新入库onx426wOuWK2fFNu1PU3pvLc9ESo
        else {
            // 将基本信息赋值给用户
            user = new UmsMpUser();
            user.setSubscribe(OffiaccountConsts.Subscribe.YES)
                    .setOpenId(openId)
                    .setSubscribeNum(1)
                    .setSubscribeTime(createTime);
            // 尝试获取用户更加详细的信息
            try {
                WxMpUser userWxInfo = wxService.getUserService().userInfo(openId, null);
                // 如果可以获取用户更加详细的信息（主要针对认证的公众号）
                if (null != userWxInfo) {
                    // 将用户信息保存至数据库
                    System.out.println(userWxInfo);
                    user.setLanguage(userWxInfo.getLanguage())
                            // 微信官方已经不返回下面这两项信息了
                            // .setNickname(userWxInfo.getNickname())
                            // .setHeadimgUrl(userWxInfo.getHeadImgUrl())
                            .setSubscribeTime(userWxInfo.getSubscribeTime())
                            .setRemark(userWxInfo.getRemark())
                            .setGroupId(userWxInfo.getGroupId())
                            .setTagidList(Arrays.toString(userWxInfo.getTagIds()))
                            .setSubscribeScene(userWxInfo.getSubscribeScene())
                            .setQrScene(userWxInfo.getQrScene())
                            .setQrSceneStr(userWxInfo.getQrSceneStr());
                }
            } catch (WxErrorException e) {
                System.out.println(e.getMessage());
            }
            // 保存用户信息
            mpUserService.saveOrUpdate(user, new QueryWrapper<UmsMpUser>().eq(UmsMpUser.OPEN_ID, openId));
        }
    }

    /**
     * 保存关注+登录的用户信息至数据库和Redis
     *
     * @param sceneStr   关注的场景值
     * @param openId     用户的OpenID
     * @param createTime 消息的创建时间（如果无法获取用户详细信息，这个时间就是关注时间）
     */
    @Async
    @Override
    public void saveUserBySubscribeLogin(String sceneStr, String openId, Long createTime) {
        // 保存用户信息
        this.saveUserBySubscribe2DB(openId, createTime);
        // 保存登录用户的信息至Redis
        // 这里的场景值需要裁剪
        this.saveUserByScanLogin2Redis(sceneStr.substring(8), openId);
    }

    /**
     * 保存登录的用户信息至Redis
     *
     * @param sceneStr 场景值，即为Redis的KEY值
     * @param openId   登录用户的OpenID
     */
    @Override
    public void saveUserByScanLogin2Redis(String sceneStr, String openId) {
        // 先尝试从Redis获取用户 VO，主要是获取用户的过期时间
        OauthUserVo userVo = (OauthUserVo) redisService.get(OffiaccountConsts.User.REDIS_KEY_PREFIX + sceneStr);
        // 如果没有，则使用默认的过期时间
        Long expire = OffiaccountConsts.User.REDIS_DEFAULT_EXPIRE;
        if (null != userVo) {
            expire = userVo.getExpire();
        } else {
            userVo = new OauthUserVo();
        }
        userVo.setUserid(openId).setToken(sceneStr);
        // 先从数据库查找该用户的详细信息
        UmsMpUser user = mpUserService.getOne(new QueryWrapper<UmsMpUser>().eq(UmsMpUser.OPEN_ID, openId));
        // 如果数据库中有该记录，获取更加详细的信息
        if (user != null) {
            userVo.setAvatar(user.getHeadimgUrl())
                    .setNickname(user.getNickname())
                    .setSex(user.getSex());
        }
        // 将用户信息保存至redis
        redisService.set(OffiaccountConsts.User.REDIS_KEY_PREFIX + sceneStr, userVo, expire);
    }
}
