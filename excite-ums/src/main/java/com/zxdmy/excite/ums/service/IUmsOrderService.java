package com.zxdmy.excite.ums.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxdmy.excite.ums.entity.UmsOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * UMS系统的交易平台的订单信息 服务类
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-05-26
 */
public interface IUmsOrderService extends IService<UmsOrder> {


    /**
     * 分页查询订单信息
     *
     * @param search     搜索关键字：应用信息，订单标题，商户号，交易号。
     * @param payChannel 交易方式：支付宝，微信。
     * @param status     交易状态
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @param pageNum    页码
     * @param pageSize   每页数量
     * @return 分页结果
     */
    Page<UmsOrder> getPage(String search, String payChannel, String status, String startDate, String endDate, Integer pageNum, Integer pageSize);

    /**
     * 创建订单
     *
     * @param order 订单信息
     */
    UmsOrder createOrder(UmsOrder order);


    UmsOrder getOrderByOutTradeNo(String outTradeNo);

    /**
     * 异步执行：根据回调信息更新订单信息（包含官方的回调，以及下游的回调信息）
     *
     * @param order 官方回调信息
     * @return 更新后的订单信息
     */
    UmsOrder updateOrderByNotify(UmsOrder order);

    /**
     * 根据异步通知更新系统订单信息，正常情况下是支付成功后的回调通知
     *
     * @param order 订单信息
     */
    UmsOrder updateOrderByNotifyReceive(UmsOrder order);

    /**
     * 根据订单通知的推送结果，更新订单信息
     *
     * @param order 订单信息
     */
    void updateOrderByNotifySend(UmsOrder order);


}
