package com.zxdmy.excite.admin.ums;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.dfa.WordTree;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxdmy.excite.common.consts.OffiaccountConsts;
import com.zxdmy.excite.common.utils.HttpRequestUtils;
import com.zxdmy.excite.system.service.ISysMenuService;
import com.zxdmy.excite.ums.entity.UmsMpReply;
import com.zxdmy.excite.ums.mapper.UmsMpReplyMapper;
import com.zxdmy.excite.ums.service.IUmsMpReplyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/7/22 9:47
 */
@SpringBootTest
public class MpTest {

    @Autowired
    private UmsMpReplyMapper replyMapper;

    @Autowired
    private IUmsMpReplyService replyService;

    @Test
    void test() {
        long startTime = System.currentTimeMillis();
        // 插入1万条数据
        for (int i = 1132190; i < 100000000; i++) {
            System.out.println("第" + i + "条数据");
            UmsMpReply mpReply = new UmsMpReply();
            // 添加必须信息
            mpReply.setType(1)
                    .setMenuKey(RandomUtil.randomStringUpper(8))
                    .setMate("2")
                    .setRepType("text")
                    .setStatus(OffiaccountConsts.ReplyStatus.ENABLE)
                    .setKeyword(RandomUtil.randomStringUpper(4) + "关键词" + i)
                    .setRepContent("回复内容" + i);
            replyService.save(mpReply);
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - startTime));
    }

    @Test
    void test2() {
        long startTime = System.currentTimeMillis();
        List<UmsMpReply> replies = replyMapper.getReplyListByKeywordHalfMate("hello,我叫陈煜恒,我要查询关键词;;TM5M关键词45003;;");
        System.out.println(replies);
        System.out.println("耗时：" + (System.currentTimeMillis() - startTime));
    }

    @Test
    void test3() {
        long startTime1 = System.currentTimeMillis();
        List<UmsMpReply> replyList = replyService.list(new QueryWrapper<UmsMpReply>().select(UmsMpReply.ID, UmsMpReply.KEYWORD));
        WordTree wordTree = new WordTree();
        for (UmsMpReply reply : replyList) {
            wordTree.addWord(reply.getKeyword());
        }
        System.out.println("100W数据构建树，耗时：" + (System.currentTimeMillis() - startTime1));

        long startTime = System.currentTimeMillis();
        List<UmsMpReply> replies = new ArrayList<>();
        String text = "hello,我叫陈煜恒,我要查询关键词;;TM5M关键词;;";
        // 根据构造的树，查询出匹配的关键词
        List<String> matchAll = wordTree.matchAll(text, -1, true, true);
        System.out.println(matchAll);
        // 输出：[hello, ……]
        // 根据关键词，查询出匹配的回复
        for (String s : matchAll) {
            replies.addAll(replyService.list(new QueryWrapper<UmsMpReply>().eq(UmsMpReply.KEYWORD, s)));
        }
        System.out.println(replies.size());
        System.out.println("耗时：" + (System.currentTimeMillis() - startTime));
    }
}
