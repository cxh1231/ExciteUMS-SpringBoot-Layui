package com.zxdmy.excite.admin.controller.ums;

import com.zxdmy.excite.offiaccount.bo.OffiaccountBO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 公众号 菜单管理
 *
 * @author 拾年之璐
 * @since 2022/6/24 11:00
 */
@Controller
@AllArgsConstructor
@RequestMapping("/ums/mp/menu")
public class UmsMpMenuController {

    /**
     * 访问页面：统一管理系统 - 公众平台管理 - 公众号配置
     *
     * @return 跳转至配置页面
     */
    @RequestMapping("index")
    public String configIndex(ModelMap map) {
        // 获取当前的公众号配置信息
//        OffiaccountBO offiaccountBO = configService.getConfig();
//        if (offiaccountBO == null) {
//            offiaccountBO = new OffiaccountBO();
//        }
//        map.put("mpConfig", offiaccountBO);
        return "ums/mp/menu";
    }
}
