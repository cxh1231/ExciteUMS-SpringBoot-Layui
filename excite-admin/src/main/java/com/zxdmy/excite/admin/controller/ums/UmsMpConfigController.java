package com.zxdmy.excite.admin.controller.ums;

import com.zxdmy.excite.common.base.BaseController;
import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.offiaccount.bo.OffiaccountBO;
import com.zxdmy.excite.offiaccount.service.IOffiaccountConfigService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/6/24 10:54
 */
@Controller
@AllArgsConstructor
@RequestMapping("/ums/mp/config")
public class UmsMpConfigController extends BaseController {

    private IOffiaccountConfigService configService;

    /**
     * 访问页面：统一管理系统 - 公众平台管理 - 公众号配置
     *
     * @return 跳转至配置页面
     */
    @RequestMapping("index")
    public String configIndex(ModelMap map) {
        // 获取当前的公众号配置信息
        OffiaccountBO offiaccountBO = configService.getConfig();
        if (offiaccountBO == null) {
            offiaccountBO = new OffiaccountBO();
        }
        map.put("mpConfig", offiaccountBO);
        return "ums/mp/config";
    }

    /**
     * 保存公众号配置信息
     *
     * @param offiaccountBO 公众号配置信息
     * @return 保存结果
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult saveConfig(OffiaccountBO offiaccountBO) {
        if (configService.saveConfig(offiaccountBO)) {
            return success("配置信息保存成功");
        } else {
            return error("配置信息保存失败，请重试！");
        }
    }
}
