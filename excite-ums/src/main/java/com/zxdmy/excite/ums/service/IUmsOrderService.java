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


    // 生成订单
    void createOrder(UmsOrder order);

    // 根据官方回调更新订单
    void updateOrderByNotifyReceive(UmsOrder order);

    // 根据下游回调更新订单
    void updateOrderByNotifySend(UmsOrder order);


}
