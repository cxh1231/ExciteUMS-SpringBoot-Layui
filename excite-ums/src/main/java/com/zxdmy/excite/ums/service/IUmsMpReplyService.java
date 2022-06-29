package com.zxdmy.excite.ums.service;

import com.zxdmy.excite.ums.entity.UmsMpReply;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 微信自动回复表 服务类
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-06-29
 */
public interface IUmsMpReplyService extends IService<UmsMpReply> {

    /**
     * 根据消息类型获取一条最新的自动回复消息
     *
     * @param type 枚举：消息类型
     * @param key  关键词 | 菜单的KEY
     * @return 一条自动回复
     */
    UmsMpReply getReplyByType(Integer type, String key);

}
