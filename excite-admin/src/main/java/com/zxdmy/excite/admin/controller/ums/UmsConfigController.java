package com.zxdmy.excite.admin.controller.ums;

import com.zxdmy.excite.common.base.BaseController;
import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.payment.bo.AlipayBO;
import com.zxdmy.excite.payment.bo.WechatPayBO;
import com.zxdmy.excite.payment.service.IAlipayConfigService;
import com.zxdmy.excite.payment.service.IWechatPayConfigService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 后台各项配置的保存与读取
 *
 * @author 拾年之璐
 * @since 2022/5/26 16:55
 */
@Controller
@AllArgsConstructor
@RequestMapping("/ums/config")
public class UmsConfigController extends BaseController {

    private IAlipayConfigService alipayConfigService;

    private IWechatPayConfigService wechatPayConfigService;

    // 文件上传保存的路径
    private static final String CERT_UPLOAD_PATH = "/cert/ums/config/";

    /**
     * 访问页面：统一管理系统 - 交易管理 - 支付宝配置
     *
     * @return 跳转至列表页面
     */
    @RequestMapping("alipay")
    public String alipayConfigIndex(ModelMap map) {
        // 获取当前系统的配置
        AlipayBO alipayBO = alipayConfigService.getAlipayConfig();
        if (alipayBO == null) {
            alipayBO = new AlipayBO();
        }
        map.put("alipayConfig", alipayBO);
        return "ums/config/alipay";
    }

    /**
     * 访问页面：统一管理系统 - 交易管理 - 微信支付配置
     *
     * @return 跳转至列表页面
     */
    @RequestMapping("wechatPay")
    public String wechatPayConfigIndex(ModelMap map) {
        // 获取当前系统的配置
        WechatPayBO wechatPayBO = wechatPayConfigService.getWechatPayConfig();
        if (wechatPayBO == null) {
            wechatPayBO = new WechatPayBO();
        }
        map.put("wechatPayConfig", wechatPayBO);
        return "ums/config/wechatPay";
    }


    /**
     * 保存支付宝支付配置信息
     *
     * @param alipayBO 支付宝支付配置信息实体
     * @return 配置信息保存结果
     */
    @RequestMapping(value = "/alipay/save", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult saveAlipayConfig(AlipayBO alipayBO) {
        if (alipayConfigService.saveAlipayConfig(alipayBO)) {
            return success("配置信息保存成功！");
        }
        return error("配置信息保存失败！");
    }

    /**
     * 保存支付宝支付配置信息
     *
     * @param wechatPayBO 支付宝支付配置信息实体
     * @return 配置信息保存结果
     */
    @RequestMapping(value = "/wechatPay/save", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult saveWechatPayConfig(WechatPayBO wechatPayBO) {
        if (wechatPayConfigService.saveWechatPayConfig(wechatPayBO)) {
            return success("配置信息保存成功！");
        }
        return error("配置信息保存失败！");
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 返回上传结果
     */
    @PostMapping("upload")
    @ResponseBody
    public BaseResult uploadCert(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return error(400, "上传的文件为空，请检查！");
        }
        String path = CERT_UPLOAD_PATH;
        // 如果系统是Windows，则默认存储在C盘
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            // 默认保存在第一个盘里
            String root = File.listRoots()[0].getPath();
            path = root.substring(0, root.length() - 1) + path;
        }
        // 目录不存在则创建
        File file1 = new File(path);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 组成上传路径
        String localFilePath = path + fileName;
        try {
            file.transferTo(new File(localFilePath));
            return success("上传成功！").put("path", localFilePath);
        } catch (IOException e) {
            return error("上传失败：" + e.getMessage());
        }
    }


}
