package com.zxdmy.excite.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxdmy.excite.common.consts.OffiaccountConsts;
import com.zxdmy.excite.common.enums.SystemCode;
import com.zxdmy.excite.ums.entity.UmsMpReply;
import com.zxdmy.excite.ums.mapper.UmsMpReplyMapper;
import com.zxdmy.excite.ums.service.IUmsMpReplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 微信自动回复表 服务实现类
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-06-29
 */
@Service
@AllArgsConstructor
public class UmsMpReplyServiceImpl extends ServiceImpl<UmsMpReplyMapper, UmsMpReply> implements IUmsMpReplyService {

    private UmsMpReplyMapper mpReplyMapper;

    /**
     * 分页查询
     *
     * @param search   检索关键词：关键词、文本消息、标题、描述、备注。
     * @param type     类型
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    @Override
    public Page<UmsMpReply> getPage(String search, Integer type, Integer pageNum, Integer pageSize) {
        // 页码为空，默认为1
        if (pageNum == null) {
            pageNum = 1;
        }
        // 每页数量为空，默认为10
        if (pageSize == null) {
            pageSize = 10;
        }
        // 构造查询条件
        QueryWrapper<UmsMpReply> queryWrapper = new QueryWrapper<>();
        // 检索关键词
        if (null != search && !"".equals(search)) {
            queryWrapper.like(UmsMpReply.KEYWORD, search)
                    .or().like(UmsMpReply.REP_CONTENT, search)
                    .or().like(UmsMpReply.REP_TITLE, search)
                    .or().like(UmsMpReply.REP_DESCRIPTION, search)
                    .or().like(UmsMpReply.REMARK, search);
        }
        // 类型
        queryWrapper.eq(null != search, UmsMpReply.TYPE, type)
                // 最后修改时间倒序
                .orderByDesc(UmsMpReply.UPDATE_TIME);
        // 分页查询并返回
        return mpReplyMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
    }

    /**
     * 根据消息类型获取一条最新的自动回复消息
     *
     * @param type 枚举：消息类型
     * @param key  关键词 | 或菜单的KEY
     * @return 一条自动回复
     */
    @Override
    public UmsMpReply getOneReplyByTypeOrKey(Integer type, String key) {
        QueryWrapper<UmsMpReply> wrapper = new QueryWrapper<>();
        // 构造检索条件并检索：关键词回复
        if (Objects.equals(type, OffiaccountConsts.ReplyType.KEYWORD_REPLY)) {
            // 构造检索条件
            wrapper.eq(UmsMpReply.TYPE, type)
                    .eq(UmsMpReply.STATUS, SystemCode.STATUS_Y.getCode())
                    // 全匹配
                    .and(wrapper1 -> wrapper1
                            .eq(UmsMpReply.MATE, OffiaccountConsts.ReplyMate.FULL_MATE)
                            .eq(UmsMpReply.KEYWORD, key))
                    // 半匹配
                    .or(wrapper2 -> wrapper2
                            .eq(UmsMpReply.MATE, OffiaccountConsts.ReplyMate.HALF_MATE)
                            .like(UmsMpReply.KEYWORD, key))
                    .orderByDesc(UmsMpReply.ID);
        }
        // 其他的回复，直接检索即可
        else {
            wrapper.eq(null != type, UmsMpReply.TYPE, type)
                    .eq(UmsMpReply.STATUS, SystemCode.STATUS_Y.getCode())
                    // 当 KEY 非空，并且非关键词时，是菜单点击的回复
                    .eq(null != key, UmsMpReply.MENU_KEY, key)
                    .orderByDesc(UmsMpReply.ID);
        }
        // 执行检索
        List<UmsMpReply> mpReplyList = mpReplyMapper.selectList(wrapper);
        // 如果回复内容不为空，则返回第 0 条
        if (mpReplyList.size() > 0) {
            return mpReplyList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据消息类型获取自动回复消息列表
     *
     * @param type 枚举：消息类型
     * @return 一条自动回复
     */
    @Override
    public List<UmsMpReply> getReplyListByType(Integer type) {
        QueryWrapper<UmsMpReply> wrapper = new QueryWrapper<>();
        wrapper.eq(UmsMpReply.TYPE, type)
                .orderByDesc(UmsMpReply.ID);
        return mpReplyMapper.selectList(wrapper);
    }
}
