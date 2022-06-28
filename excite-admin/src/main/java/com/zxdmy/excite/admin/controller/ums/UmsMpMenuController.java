package com.zxdmy.excite.admin.controller.ums;

import com.zxdmy.excite.common.base.BaseController;
import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.offiaccount.api.OffiaccountApiService;
import com.zxdmy.excite.offiaccount.vo.OffiaccountMenuVo;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公众号 菜单管理
 *
 * @author 拾年之璐
 * @since 2022/6/24 11:00
 */
@Controller
@AllArgsConstructor
@RequestMapping("/ums/mp/menu")
public class UmsMpMenuController extends BaseController {

    private OffiaccountApiService offiaccountApiService;

    /**
     * 访问页面：统一管理系统 - 公众平台管理 - 菜单配置
     *
     * @return 跳转至菜单配置页面
     */
    @RequestMapping("index")
    public String configIndex() {
        return "ums/mp/menu";
    }

    /**
     * 获取公众号菜单
     *
     * @return 公众号菜单列表
     */
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    @ResponseBody
    public BaseResult getMenu() {
        List<OffiaccountMenuVo> menu = offiaccountApiService.getMenu();
        if (menu == null) {
            return error("公众号菜单获取失败，可能未设置菜单或其他错误，请见系统日志！");
        }
        return success("公众号菜单获取成功！", menu);
    }

    /**
     * 创建微信公众号菜单
     *
     * @return 菜单创建结果
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    @ResponseBody
    public BaseResult createMenu(@RequestBody List<OffiaccountMenuVo> menus) {
        System.out.println(menus);
        if (offiaccountApiService.createMenu(menus)) {
            return success("菜单配置成功，马上生效！");
        } else {
            return error("菜单配置失败，请查看系统日志！");
        }
    }
}
