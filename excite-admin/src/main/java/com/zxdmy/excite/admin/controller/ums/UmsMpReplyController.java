package com.zxdmy.excite.admin.controller.ums;

import cn.hutool.core.util.RandomUtil;
import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.common.consts.OffiaccountConsts;
import com.zxdmy.excite.ums.entity.UmsMpReply;
import com.zxdmy.excite.ums.service.IUmsMpReplyService;
import lombok.AllArgsConstructor;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import com.zxdmy.excite.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 微信自动回复表 前端控制器
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-06-29
 */
@Controller
@AllArgsConstructor
@RequestMapping("/ums/mp/reply")
public class UmsMpReplyController extends BaseController {

    private IUmsMpReplyService replyService;

    /**
     * 自动回复 前端页面
     *
     * @return
     */
    @RequestMapping("index")
    public String configIndex() {
        return "ums/mp/reply";
    }


    @RequestMapping("goAdd")
    public String goAdd(String type, ModelMap map) {
        map.put("type", type);
        return "ums/mp/replyAdd";
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    @ResponseBody
    public BaseResult add(UmsMpReply mpReply) {
        if (null == mpReply) {
            return error("保存失败");
        }
        mpReply.setStatus(OffiaccountConsts.ReplyStatus.ENABLE)
                .setMenuKey(RandomUtil.randomStringUpper(8));
        if (replyService.save(mpReply)) {
            return success("保存成功");
        } else {
            return error("保存失败");
        }
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public BaseResult getReply(String type) {
        if (type == null) {
            return error("请求参数错误！");
        }
        try {
            int replyType = Integer.parseInt(type);
            return success("自动回复获取成功！", replyService.getReplyListByType(replyType));
        } catch (Exception e) {
            return error("请求参数错误！");
        }
    }
}
