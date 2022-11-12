package com.zxdmy.excite.admin.controller.api;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.dfa.WordTree;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxdmy.excite.common.consts.OffiaccountConsts;
import com.zxdmy.excite.ums.entity.UmsMpReply;
import com.zxdmy.excite.ums.mapper.UmsMpReplyMapper;
import com.zxdmy.excite.ums.service.IUmsMpReplyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 开放接口API控制器
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/3/30 11:27
 */
@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/api")
public class ApiController {

    private UmsMpReplyMapper replyMapper;

    public static WordTree wordTree;

    private IUmsMpReplyService replyService;

    @ResponseBody
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test1() {

//        long startTime = System.currentTimeMillis();
//        // 插入1万条数据
//        for (int i = 1192790; i < 100000000; i++) {
//            System.out.println("第" + i + "条数据");
//            UmsMpReply mpReply = new UmsMpReply();
//            // 添加必须信息
//            mpReply.setType(1)
//                    .setMenuKey(RandomUtil.randomStringUpper(8))
//                    .setMate("2")
//                    .setRepType("text")
//                    .setStatus(OffiaccountConsts.ReplyStatus.ENABLE)
//                    .setKeyword(RandomUtil.randomStringUpper(4) + "关键词" + i)
//                    .setRepContent("回复内容" + i);
//            replyService.save(mpReply);
//        }
//        System.out.println("耗时：" + (System.currentTimeMillis() - startTime));
        return "success的点点滴滴";
    }

    @ResponseBody
    @RequestMapping(value = "/test3", method = RequestMethod.GET)
    public String test3() {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        map.put(4, "4");
        map.put(5, "5");
        System.out.println(map.get(3));
        // 遍历map
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            System.out.println(entry.getValue());
        }

        long startTime1 = System.currentTimeMillis();
        List<UmsMpReply> replyList = replyService.list();
        wordTree = new WordTree();
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
        return matchAll + "\n" + replies.size() + "\n耗时：" + (System.currentTimeMillis() - startTime);
    }
}
