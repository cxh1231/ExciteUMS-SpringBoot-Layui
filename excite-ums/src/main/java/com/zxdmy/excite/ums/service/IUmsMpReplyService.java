package com.zxdmy.excite.ums.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
     * 分页查询
     *
     * @param search   检索关键词：关键词、文本消息、标题、描述、备注。
     * @param type     类型
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<UmsMpReply> getPage(String search, Integer type, Integer pageNum, Integer pageSize);


    /**
     * 根据消息类型获取一条最新的自动回复消息
     *
     * @param type 枚举：消息类型
     * @param key  关键词 | 菜单的KEY
     * @return 一条自动回复
     */
    UmsMpReply getOneReplyByTypeOrKey(Integer type, String key);

    List<UmsMpReply> getReplyListByType(Integer type);
}
