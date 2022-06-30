package com.zxdmy.excite.admin.controller.ums;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxdmy.excite.common.base.BaseController;
import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.offiaccount.api.OffiaccountApiService;
import com.zxdmy.excite.ums.entity.UmsMpUser;
import com.zxdmy.excite.ums.service.impl.UmsMpUserServiceImpl;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

/**
 * 公众号用户管理
 *
 * @author 拾年之璐
 * @since 2022/6/24 11:01
 */
@Controller
@AllArgsConstructor
@RequestMapping("/ums/mp/user")
public class UmsMpUserController extends BaseController {

    private UmsMpUserServiceImpl mpUserService;

    private OffiaccountApiService offiaccountApiService;

    @RequestMapping("index")
    public String configIndex() {
        return "ums/mp/user";
    }

    /**
     * 获取数据库的用户列表
     *
     * @return
     */
    @GetMapping(value = "getList")
    @ResponseBody
    public BaseResult getList() {
        return success(mpUserService.list());
    }


    /**
     * 【同步用户】将当前公众号关注的用户信息，导入至数据库（一般是首次部署项目后使用该功能）
     * 该接口执行会很慢
     *
     * @return 同步结果
     */
    @PostMapping(value = "synchronyFromMp")
    @ResponseBody
    public BaseResult addAllUsersFromMp() {
        // 获取全部用户的OpenID
        List<String> userOpenIds = offiaccountApiService.getAllUsersOpenId();
        // 遍历OpenID
        for (String openId : userOpenIds) {
            // 获取用户详情
            WxMpUser mpUser = offiaccountApiService.getUserInfo(openId);
            if (null != mpUser) {
                // 赋值
                UmsMpUser user = new UmsMpUser();
                user.setOpenId(openId)
                        .setLanguage(mpUser.getLanguage())
                        .setSubscribe(mpUser.getSubscribe() ? 1 : 0)
                        .setSubscribeTime(mpUser.getSubscribeTime())
                        .setUnionId(mpUser.getUnionId())
                        .setRemark(mpUser.getRemark())
                        .setGroupId(mpUser.getGroupId())
                        .setTagidList(Arrays.toString(mpUser.getTagIds()))
                        .setSubscribeScene(mpUser.getSubscribeScene())
                        .setQrScene(mpUser.getQrScene())
                        .setQrSceneStr(mpUser.getQrSceneStr());
                // 更新数据库
                QueryWrapper<UmsMpUser> wrapper = new QueryWrapper<>();
                wrapper.eq(UmsMpUser.OPEN_ID, openId);
                mpUserService.saveOrUpdate(user, wrapper);
            }
        }
        return success("公众号用户同步完毕！");
    }


    /**
     * 更新数据库的用户信息
     *
     * @return
     */
    @GetMapping(value = "updateAllUserInfo")
    @ResponseBody
    public BaseResult updateAllUserInfo() {
        return success("更新所有用户信息接口，请实现！");
    }

}
