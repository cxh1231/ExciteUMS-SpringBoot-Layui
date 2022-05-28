package com.zxdmy.excite.admin.controller.ums;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.common.utils.OrderUtils;
import com.zxdmy.excite.ums.entity.UmsOrder;
import com.zxdmy.excite.ums.service.IUmsOrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import com.zxdmy.excite.common.base.BaseController;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * <p>
 * UMS系统的交易平台的订单信息 前端控制器
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-05-26
 */
@Controller
@AllArgsConstructor
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
    public BaseResult saveOrder() throws InterruptedException {
        System.out.println("收到信息，开始分线程处理");
        UmsOrder order = new UmsOrder();
//        order.setOutTradeNo(OrderUtils.createOrderCode());

        long startTime = System.currentTimeMillis();

        CountDownLatch countDownLatch = new CountDownLatch(10000000);

        Set set = Collections.synchronizedSet(new HashSet());
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(() -> {
                for (int i1 = 0; i1 < 100000; i1++) {
                    String id = OrderUtils.createOrderCode();
                    System.out.println("id: " + id);
                    set.add(id);
                    countDownLatch.countDown();
                }
            });
            thread.start();
        }
        countDownLatch.await();

        long endTime = System.currentTimeMillis();
        System.out.println("set.size():" + set.size());
        System.out.println("endTime-startTime:" + (endTime - startTime));

        // 更新数据库
        orderService.updateOrderByNotifyReceive(order);
        // 向下游推送
        orderService.updateOrderByNotifySend(order);
        // 返回结果
        return success("请实现接口！", order);
    }

    // 读取订单列表
    @GetMapping(value = "/list")
    @ResponseBody
    public BaseResult getOrderList() {
        QueryWrapper<UmsOrder> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc(UmsOrder.ID);
        List<UmsOrder> orderList = orderService.list(wrapper);
        return success("查询成功", orderList);
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
