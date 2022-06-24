package com.zxdmy.excite.admin.controller.ums;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 公众号用户管理
 *
 * @author 拾年之璐
 * @since 2022/6/24 11:01
 */
@Controller
@AllArgsConstructor
@RequestMapping("/ums/mp/user")
public class UmsMpUserController {

    @RequestMapping("index")
    public String configIndex() {
        return "ums/mp/user";
    }
}
