package com.zxdmy.excite.admin.controller.ums;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 公众号消息管理，包含自动回复、关注回复、默认回复等内容
 *
 * @author 拾年之璐
 * @since 2022/6/24 11:00
 */
@Controller
@AllArgsConstructor
@RequestMapping("/ums/mp/message")
public class UmsMpMessageController {

    @RequestMapping("index")
    public String configIndex() {
        return "ums/mp/message";
    }
}
