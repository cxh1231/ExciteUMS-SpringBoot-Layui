package com.zxdmy.excite.admin.controller.ums;

import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.ums.service.IUmsOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import com.zxdmy.excite.common.base.BaseController;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * UMS系统的交易平台的订单信息 前端控制器
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-05-26
 */
@Controller
@RequestMapping("/ums/order")
public class UmsOrderController extends BaseController {

    private IUmsOrderService orderService;

    /**
     * 访问页面：统一管理系统 - 订单管理
     *
     * @return 跳转至列表页面
     */
    @RequestMapping("index")
    public String index() {
        return "ums/order/index";
    }

    // 保存订单
    @GetMapping(value = "/save")
    @ResponseBody
    public BaseResult saveOrder() {
        return success("请实现接口！");
    }

    // 读取订单列表
    public BaseResult getOrderList() {
        return success("请实现接口！");
    }

    // 读取订单详情
    public BaseResult getOrder() {
        return success("请实现接口！");
    }

    // 更新订单
    public BaseResult updateOrder() {
        return success("请实现接口！");
    }

    // 删除订单
    public BaseResult deleteOrder() {
        return success("请实现接口！");
    }
}
