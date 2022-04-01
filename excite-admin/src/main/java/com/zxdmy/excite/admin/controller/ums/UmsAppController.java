package com.zxdmy.excite.admin.controller.ums;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.ums.entity.UmsApp;
import com.zxdmy.excite.ums.service.IUmsAppService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import com.zxdmy.excite.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

/**
 * <p>
 * UMS系统的用户体系 前端控制器
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-04-01
 */
@Controller
@AllArgsConstructor
@RequestMapping("/ums/app")
public class UmsAppController extends BaseController {

    private IUmsAppService appService;

    /**
     * 访问页面：用户管理 - 列表页面
     *
     * @return 跳转至列表页面
     */
    @RequestMapping("index")
    public String index() {
        return "ums/app/index";
    }

    /**
     * 接口功能：获取用户列表
     *
     * @param page    当前页面
     * @param limit   每页请求数
     * @param appid   检索用户名
     * @param appName 检索电话或邮箱
     * @return JSON：用户列表 | 错误信息
     */
    @GetMapping(value = "/list")
    @ResponseBody
    public BaseResult getAppList(Integer page, Integer limit, String appid, String appName) {
        QueryWrapper<UmsApp> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        Page<UmsApp> userPage = appService.page(new Page<>(page, limit), wrapper);
        if (null != userPage) {
            return success("查询成功", userPage.getRecords(), (int) userPage.getTotal());
        } else {
            return error("无数据");
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult addApp(String appName) {
        // 生成appid
        String appid = DateUtil.format(DateUtil.date(), "yyyyMMdd") + RandomUtil.randomString("1234567890", 4);
        // 当前生成的appid，不允许存在
        if (appService.getOne(new QueryWrapper<UmsApp>().eq(UmsApp.APP_ID, appid)) == null) {
            // 生成app secret
            String appSecret = RandomUtil.randomString(32);
            // 执行添加
            if (appService.save(new UmsApp().setAppId(appid)
                    .setAppName(appName)
                    .setAppSecret(appSecret)
                    .setAppType("1")
                    .setStatus(1)
                    .setCreateTime(LocalDateTime.now())
            ))
                return success("应用创建成果！");
            return error("添加失败");
        } else {
            return addApp(appName);
        }
    }
}
