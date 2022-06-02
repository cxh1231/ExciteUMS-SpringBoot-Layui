package com.zxdmy.excite.ums.service;

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
     * 创建订单
     *
     * @param order 订单信息
     */
    void createOrder(UmsOrder order);

    /**
     * 根据异步通知更新系统订单信息，正常情况下是支付成功后的回调通知
     *
     * @param order 订单信息
     */
    void updateOrderByNotifyReceive(UmsOrder order);

    /**
     * 根据订单通知的推送结果，更新订单信息
     *
     * @param order 订单信息
     */
    void updateOrderByNotifySend(UmsOrder order);


}
