package com.zxdmy.excite.admin.controller.system;


import cn.dev33.satoken.stp.StpUtil;
import com.zxdmy.excite.common.base.BaseController;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统模块
 *
 * @author 拾年之璐
 * @since 2021-09-30 0030 23:45
 */
@Controller
@AllArgsConstructor
public class SysController extends BaseController {

    /**
     * 默认访问域名，跳转至后台主页
     *
     * @return 页面跳转
     */
    @RequestMapping("login2")
    public String login2() {
        return "/system/login2";
    }
    /**
     * 默认访问域名，跳转至后台主页
     *
     * @return 页面跳转
     */
    @RequestMapping("")
    public String defaultPage() {
        return "redirect:/system/login";
    }

    /**
     * 后台首页
     *
     * @return 后台首页
     */
    @RequestMapping("system/index")
    public String index() {
        return "system/index";
    }

    /**
     * @return 登录页面
     */
    @RequestMapping("system/login")
    public String login() {
        // 已登录就跳转至后台首页
        if (StpUtil.isLogin()) {
            return "redirect:/system/index";
        }
        // 否则跳转至登录页面
        return "system/login";
    }

    /**
     * @return 后台欢迎页面
     */
    @RequestMapping("system/welcome")
    public String welcome() {
        return "system/welcome";
    }


}