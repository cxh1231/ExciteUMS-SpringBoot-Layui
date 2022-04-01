package com.zxdmy.excite.admin.controller.api;

import com.zxdmy.excite.common.base.BaseController;
import com.zxdmy.excite.common.base.BaseResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 授权登录开放接口
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/3/30 11:26
 */
@Controller
@AllArgsConstructor
@RequestMapping("/api/oauth")
public class ApiOauthController extends BaseController {


    /**
     * 访问的登录页面
     *
     * @param type        登录类型，暂时只支持：QQ、MP、weibo（不区分大小写）
     * @param appid       发起请求的应用的appid
     * @param appSecret   发起请求的应用的秘钥
     * @param callbackUrl 登录成功后，跳转的页面
     * @return JSON数据包
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult login(String type, String appid, String appSecret, String callbackUrl) {
        // 处理信息

        // 保存信息至redis

        //
        return success("success");
    }

    /**
     * 用户登录成功后，首先跳转至此页面
     *
     * @return
     */
    @RequestMapping(value = "callback", method = RequestMethod.GET)
    public String callback() {

        // 处理完信息，然后跳转至请求的页面，并带上参数token
        return "";
    }

    /**
     * 第四方应用通过Token获取登录的用户的信息
     *
     * @param appid
     * @param token
     * @return
     */
    @RequestMapping(value = "/getUserInfo", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseResult getUserInfo(String appid, String token) {
        // 获取用户信息，并返回JSON格式
        return success("用户信息");
    }


}
