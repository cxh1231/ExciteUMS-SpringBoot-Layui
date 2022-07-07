package com.zxdmy.excite.admin.controller.ums;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.common.utils.OrderUtils;
import com.zxdmy.excite.ums.entity.UmsOrder;
import com.zxdmy.excite.ums.service.IUmsOrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import com.zxdmy.excite.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMethod;
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
public class UmsPaymentOrderController extends BaseController {

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

    /**
     * 接口：分页查询订单信息
     *
     * @param search     搜索关键字：应用信息，订单标题，商户号，交易号。
     * @param payChannel 交易方式：支付宝，微信。
     * @param status     交易状态
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @param page       当前页码
     * @param limit      每页显示条数
     * @return 分页列表数据
     */
    public BaseResult list(String search, String payChannel, String status, String startDate, String endDate, Integer page, Integer limit) {
        // 分页查询结果，输入的数据可以为空，在内部已处理
        Page<UmsOrder> orderPage = orderService.getPage(search, payChannel, status, startDate, endDate, page, limit);
        // 返回结果
        return success("查询成功", orderPage.getRecords(), orderPage.getTotal());
    }

    /**
     * 接口：刷新订单
     *
     * @param id 订单id
     * @return 操作结果
     */
    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult refreshOrder(String id) {
        // 尝试类型转换，如果转换失败，则抛出异常
        long orderId;
        try {
            orderId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return error("订单id不合法");
        }
        // 通过 官方 查询订单

        // 返回结果

        // 创建新的实体

        // 根据返回结果更新数据库

        // 返回结果

        return success("刷新成功");
    }

    /**
     * 接口：删除订单
     *
     * @param id 订单id
     * @return 删除结果
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult deleteOrder(String id) {
        // 尝试类型转换
        long orderId;
        try {
            orderId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return error("参数格式错误");
        }
        // 删除订单并返回结果
        if (orderService.removeById(orderId)) {
            return success("删除成功");
        } else {
            return error("删除失败");
        }
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
//        orderService.updateOrderByNotifyReceive(order);
        // 向下游推送
//        orderService.updateOrderByNotifySend(order);
        // 返回结果
        return success("请实现接口！", order);
    }

    // 读取订单列表
    @GetMapping(value = "/list")
    @ResponseBody
    public BaseResult getOrderList() {
//        String json = HttpUtils.post("http://ip.tool.zxdmy.com/", null);
//        System.out.println(json);

        HttpResponse response = HttpRequest.get("http://ip.tool.zxdmy.com/")
                .timeout(10000)
                .execute();
        System.out.println(response.getStatus());
        System.out.println(response.body());

        QueryWrapper<UmsOrder> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc(UmsOrder.ID);
        List<UmsOrder> orderList = orderService.list(wrapper);
        return success("查询成功", orderList);
    }


}
