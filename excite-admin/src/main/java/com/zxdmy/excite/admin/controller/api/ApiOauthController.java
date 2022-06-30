package com.zxdmy.excite.admin.controller.api;

import cn.hutool.core.lang.Validator;
import com.zxdmy.excite.common.base.BaseController;
import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.common.consts.OauthConsts;
import com.zxdmy.excite.common.consts.OffiaccountConsts;
import com.zxdmy.excite.common.service.RedisService;
import com.zxdmy.excite.common.utils.OrderUtils;
import com.zxdmy.excite.offiaccount.api.OffiaccountApiService;
import com.zxdmy.excite.ums.vo.OauthUserVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 授权登录开放接口
 *
 * @author 拾年之璐
 * @since 2022/3/30 11:26
 */
@Controller
@AllArgsConstructor
@RequestMapping("/api/oauth")
public class ApiOauthController extends BaseController {

    private OffiaccountApiService offiaccountApiService;

    private RedisService redisService;

    /**
     * 访问的登录页面
     *
     * @param type     登录类型
     * @param appid    发起请求的应用的appid
     * @param callback 登录成功后，跳转的页面
     * @return 跳转网页
     */
    @RequestMapping(value = "/login/{appid}/{type}", method = RequestMethod.GET)
    public String login(@PathVariable String appid, @PathVariable String type, String callback, ModelMap map) {
        // 回调链接 不是URL，则跳转至 404 页面
        if (!Validator.isUrl(callback)) {
            return "/error/404";
        }
        // 随机生成 token 值
        String token = OrderUtils.createOrderCode();
        // 公众号登录
        if (type.equalsIgnoreCase(OauthConsts.LoginType.MP)) {
            // 根据 KEY 生成带参数的二维码
            String qrcode = offiaccountApiService.getQrCodeUrlWithScene(token, Math.toIntExact(OauthConsts.User.REDIS_DEFAULT_EXPIRE));
            // 构造用户信息
            OauthUserVo userVo = new OauthUserVo();
            userVo.setType(type)
                    .setAppid(appid)
                    .setToken(token)
                    .setExpire(OauthConsts.User.REDIS_DEFAULT_EXPIRE);
            // 将 KEY 值保存至 Redis
            redisService.set(OauthConsts.User.REDIS_KEY_PREFIX + token, userVo, OauthConsts.User.REDIS_DEFAULT_EXPIRE);
            // 跳转至登录页面
            map.put("qrcode", qrcode);
            map.put("callback", callback + "?token=" + token);
            map.put("token", token);
            return "api/oauth/mpLogin";
        }
        // 微信普通登录
        else if (type.equalsIgnoreCase(OauthConsts.LoginType.WECHAT)) {

        }
        // 处理信息

        // 保存信息至redis

        return "abc";
    }

    /**
     * 用户登录成功后，首先跳转至此页面
     *
     * @return
     */
    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    @ResponseBody
    public String callback(String code) {
        System.out.println(code);
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
    @RequestMapping(value = "/get_user_info", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseResult getUserInfo(String appid, String token) {
        OauthUserVo userVo = (OauthUserVo) redisService.get(OauthConsts.User.REDIS_KEY_PREFIX + token);
        if (null != userVo && userVo.getUserid() != null) {
            // 获取用户信息，并返回JSON格式
            return success("获取用户信息成功", userVo);
        } else {
            return error("获取用户信息失败，请核实Token");
        }
    }


    /**
     * 检查登录成功与否
     *
     * @param key 登录记录的KEY值
     * @return 登录成功，返回状态码200，否则400
     */
    @RequestMapping(value = "/check/{key}", method = {RequestMethod.POST})
    @ResponseBody
    public BaseResult check(@PathVariable String key) {
        OauthUserVo userVo = (OauthUserVo) redisService.get(OffiaccountConsts.User.REDIS_KEY_PREFIX + key);
        if (null != userVo) {
            if (userVo.getUserid() != null) {
                return success("登录成功");
            } else {
                return error("未登录");
            }
        } else {
            return error("未登录");
        }
    }
}
