package com.zxdmy.excite.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
     * 根据消息类型获取一条最新的自动回复消息
     *
     * @param type    枚举：消息类型
     * @param keyword 关键词
     * @return 一条自动回复
     */
    @Override
    public UmsMpReply getReplyByType(Integer type, String keyword) {
        QueryWrapper<UmsMpReply> wrapper = new QueryWrapper<>();
        // 构造检索条件并检索：关键词回复
        if (Objects.equals(type, OffiaccountConsts.ReplyType.KEYWORD_REPLY)) {
            // 构造检索条件
            wrapper.eq(UmsMpReply.TYPE, type)
                    .eq(UmsMpReply.STATUS, SystemCode.STATUS_Y.getCode())
                    // 全匹配
                    .and(wrapper1 -> wrapper1
                            .eq(UmsMpReply.MATE, OffiaccountConsts.ReplyMate.FULL_MATE)
                            .eq(UmsMpReply.KEYWORD, keyword))
                    // 半匹配
                    .or(wrapper2 -> wrapper2
                            .eq(UmsMpReply.MATE, OffiaccountConsts.ReplyMate.HALF_MATE)
                            .like(UmsMpReply.KEYWORD, keyword))
                    .orderByDesc(UmsMpReply.ID);
        }
        // 其他的回复，直接检索即可
        else {
            wrapper.eq(UmsMpReply.TYPE, type)
                    .eq(UmsMpReply.STATUS, SystemCode.STATUS_Y.getCode())
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
}
