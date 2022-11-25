package com.zxdmy.excite.admin.controller.api;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxdmy.excite.common.base.BaseController;
import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.common.consts.OauthConsts;
import com.zxdmy.excite.common.service.RedisService;
import com.zxdmy.excite.common.utils.OrderUtils;
import com.zxdmy.excite.offiaccount.api.OffiaccountApiService;
import com.zxdmy.excite.offiaccount.service.IOffiaccountConfigService;
import com.zxdmy.excite.ums.entity.UmsApp;
import com.zxdmy.excite.ums.service.IUmsAppService;
import com.zxdmy.excite.ums.vo.OauthUserVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    private IUmsAppService appService;

    private OffiaccountApiService offiaccountApiService;

    private IOffiaccountConfigService offiaccountConfigService;

    private RedisService redisService;

    @RequestMapping(value = "/qqlogin", method = RequestMethod.GET)
    @ResponseBody
    public String qqlogin() {
        // 处理完信息，然后跳转至请求的页面，并带上参数token
        return "QQ授权登录配置信息错误";
    }

    /**
     * 访问的登录页面
     *
     * @param type     登录类型
     * @param appid    发起请求的应用的appid
     * @param callback 登录成功后，跳转的页面
     * @return 跳转网页
     */
    @RequestMapping(value = "/login/{appid}/{type}", method = RequestMethod.GET)
    public String login(@PathVariable String appid, @PathVariable String type, String callback, String expire, String text, ModelMap map) {
        // 回调链接 不是URL，则跳转至 404 页面
        if (!Validator.isUrl(callback)) {
            return "/error/500";
        }
        Long expireTime = null;
        // 获取过期时间，转换失败则使用默认的过期时间
        try {
            expireTime = Long.parseLong(expire);
        } catch (Exception e) {
            expireTime = OauthConsts.User.REDIS_DEFAULT_EXPIRE;
        }
        // 附加信息超过长度限制，则不保存
        if (text != null && text.length() > OauthConsts.User.REDIS_TEXT_MAX_LENGTH) {
            text = null;
        }
        // 随机生成 token 值
        String token = OrderUtils.createOrderCode();
        // 构造用户信息
        OauthUserVo userVo = new OauthUserVo();
        userVo.setType(type)
                .setAppid(appid)
                .setToken(token)
                .setText(text)
                .setExpire(expireTime);

        // 公众号登录
        if (type.equalsIgnoreCase(OauthConsts.LoginType.MP)) {
            // 根据 KEY 生成带参数的二维码
            String qrcode = offiaccountApiService.getQrCodeUrlWithScene(token, Math.toIntExact(OauthConsts.User.REDIS_DEFAULT_EXPIRE));
            // 将 KEY 值保存至 Redis（登录阶段的缓存，为默认值5分钟
            redisService.set(OauthConsts.User.REDIS_KEY_PREFIX + token, userVo, OauthConsts.User.REDIS_DEFAULT_EXPIRE);
            // 跳转至登录页面
            map.put("qrcode", qrcode);
            map.put("callback", callback + "?token=" + token);
            map.put("token", token);
            return "api/oauth/mpLogin";
        }
        // 关注回复关键字登录
        else if (type.equalsIgnoreCase(OauthConsts.LoginType.MP1)) {
            // 生成 5 位数关键字
            String code = "L" + RandomUtil.randomStringUpper(4);
            // 如果不为空，则说明已经存在，生成新的 KEY 值
            if (redisService.get(OauthConsts.User.REDIS_KEY_PREFIX + code) != null) {
                return this.login(appid, type, callback, expire, text, map);
            } else {
                String originalId;
                // 获取公众号的 原始ID 或 微信号
                try {
                    originalId = offiaccountConfigService.getConfig().getOriginalId();
                } catch (Exception e) {
                    System.out.println("请先配置公众号信息");
                    return "error/500";
                }
                // 保存至Redis
                redisService.set(OauthConsts.User.REDIS_KEY_PREFIX + code.toLowerCase(), userVo, OauthConsts.User.REDIS_DEFAULT_EXPIRE);
                // 跳转至登录页面，显示关键字
                map.put("qrcode", "https://open.weixin.qq.com/qr/code?username=" + originalId);
                map.put("callback", callback + "?token=" + token);
                map.put("code", code);
                return "api/oauth/mp1Login";
            }
        }
        // 不关注公众号登录
        else if (type.equalsIgnoreCase(OauthConsts.LoginType.MP2)) {

        }
        // 公众号登录（微信内页登录，不关注公众号）
        else if (type.equalsIgnoreCase(OauthConsts.LoginType.MPH5)) {

        }
        // 处理信息

        // 保存信息至redis

        return "error/500";
    }

    /**
     * qq、微博等官方的第三方登录方式，用户登录成功后，首先跳转至此页面
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
    @RequestMapping(value = "/get_user_info/{appid}", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseResult getUserInfo(String token, String nonce, String hash, @PathVariable String appid) {
        if (null == appid || null == token || null == nonce || null == hash) {
            return error("必填参数不能为空");
        }
        // 从数据库读取该APPID信息
        UmsApp umsApp = appService.getOne(new QueryWrapper<UmsApp>().eq(UmsApp.APP_ID, appid));
        if (null == umsApp) {
            return error("应用不存在");
        }
        // 校验签名
        String signStr = "nonce=" + nonce + "&token=" + token + "&" + umsApp.getAppSecret();
        // 签名正确
        if (hash.equalsIgnoreCase(MD5.create().digestHex(signStr))) {
            // 获取用户信息
            OauthUserVo userVo = (OauthUserVo) redisService.get(OauthConsts.User.REDIS_KEY_PREFIX + token);
            if (userVo == null) {
                return error("用户不存在");
            }
            // 如果该应用所属的APPID与请求的APPID不一致，则返回错误
            if (!userVo.getAppid().equals(appid)) {
                return error("请求应用与登录应用不一致");
            }
            if (userVo.getUserid() != null) {
                // 获取用户信息，并返回JSON格式
                return success("获取用户信息成功", userVo);
            } else {
                return error("获取用户信息失败，当前用户未登录");
            }
        } else {
            return error("签名错误");
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
        OauthUserVo userVo = (OauthUserVo) redisService.get(OauthConsts.User.REDIS_KEY_PREFIX + key.toLowerCase());
        if (null != userVo) {
            if (userVo.getUserid() != null) {
                String token = userVo.getToken();
                Long expireTime = userVo.getExpire();
                System.out.println(userVo);
                // 更换 KEY保存
                redisService.set(OauthConsts.User.REDIS_KEY_PREFIX + token, userVo, expireTime);
                return success("登录成功");
            } else {
                return error("未登录");
            }
        } else {
            return error("未登录");
        }
    }
}
