package com.zxdmy.excite.admin.controller.api;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxdmy.excite.common.base.BaseController;
import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.offiaccount.api.OffiaccountApiService;
import com.zxdmy.excite.offiaccount.api.OffiaccountEventService;
import com.zxdmy.excite.ums.entity.UmsApp;
import com.zxdmy.excite.ums.service.IUmsAppService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 微信公众号开发接口
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/3/30 11:25
 */
@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/api/mp")
public class ApiMpController extends BaseController {

    private OffiaccountApiService offiaccountApiService;

    private OffiaccountEventService eventService;

    private IUmsAppService appService;

    /**
     * 首次进行【服务器配置】时，接入认证使用
     *
     * @param signature 微信加密签名，
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @return 认证成功：返回随机字符串echostr | 认证失败：返回其他信息
     */
    @ResponseBody
    @GetMapping(value = "event", produces = "text/plain;charset=utf-8")
    public String authGet(
            @RequestParam(name = "signature", required = false) String signature,
            @RequestParam(name = "timestamp", required = false) String timestamp,
            @RequestParam(name = "nonce", required = false) String nonce,
            @RequestParam(name = "echostr", required = false) String echostr
    ) {
        // 打印认证日志
        log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);
        // 请求参数不可或缺
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }
        // 进行签名验证
        else if (eventService.checkSignature(timestamp, nonce, signature)) {
            // 验证成功，返回随机字符串，接入成功
            return echostr;
        }
        // 签名验证失败，返回错误信息
        else
            return "非法请求";
    }


    /**
     * 处理微信收到的消息与事件
     *
     * @param requestBody  请求的xml
     * @param signature    签名
     * @param timestamp    时间戳
     * @param nonce        随机字符串
     * @param openid       用户的开放ID
     * @param msgSignature 消息签名
     * @return 返回的消息
     */
    @ResponseBody
    @PostMapping(value = "event", produces = "application/xml; charset=UTF-8")
    public String post(
            @RequestBody String requestBody,
            @RequestParam(name = "signature") String signature,
            @RequestParam(name = "timestamp") String timestamp,
            @RequestParam(name = "nonce") String nonce,
            @RequestParam(name = "openid") String openid,
            @RequestParam(name = "encrypt_type", required = false) String encType,
            @RequestParam(name = "msg_signature", required = false) String msgSignature
    ) {
        // 打印日志
        // log.info("\n接收微信请求：\n[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}],timestamp=[{}], nonce=[{}],\nrequestBody=[\n{}\n] ", openid, signature, encType, msgSignature, timestamp, nonce, requestBody);
        // 校验时间戳等签名信息
        if (!eventService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }
        return eventService.eventMessageService(requestBody, timestamp, nonce, encType, msgSignature);
    }

    /**
     * 获取公众号的AccessToken
     *
     * @param appid   应用的appid
     * @param refresh 是否强制刷新AccessToken：1-是，0-否
     * @param nonce   随机字符串
     * @param hash    签名
     * @return 返回的json数据
     */
    @GetMapping(value = "get_access_token/{appid}")
    @ResponseBody
    public BaseResult getAccessToken(@PathVariable String appid, String refresh, String nonce, String hash) {
        if (null == appid || null == nonce || null == hash) {
            return error("必填参数不能为空");
        }
        // 从数据库读取该 APPID 信息
        UmsApp umsApp = appService.getOne(new QueryWrapper<UmsApp>().eq(UmsApp.APP_ID, appid));
        if (null == umsApp) {
            return error("应用不存在");
        }
        // 校验签名
        String signStr = "nonce=" + nonce + "&refresh=" + refresh + "&" + umsApp.getAppSecret();
        // 签名正确
        if (hash.equalsIgnoreCase(MD5.create().digestHex(signStr))) {
            String accessToken;
            if (refresh.equals("1")) {
                accessToken = offiaccountApiService.getAccessToken(true);
            } else {
                accessToken = offiaccountApiService.getAccessToken(false);
            }
            if (null == accessToken) {
                return error("获取AccessToken失败");
            }
            return success("获取AccessToken成功！", accessToken);
        } else {
            return error("签名错误");
        }
    }

}
