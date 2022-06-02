package com.zxdmy.excite.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxdmy.excite.common.consts.PaymentConsts;
import com.zxdmy.excite.ums.entity.UmsOrder;
import com.zxdmy.excite.ums.mapper.UmsOrderMapper;
import com.zxdmy.excite.ums.service.IUmsOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * <p>
 * UMS系统的交易平台的订单信息 服务实现类
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-05-26
 */
@Service
@AllArgsConstructor
public class UmsOrderServiceImpl extends ServiceImpl<UmsOrderMapper, UmsOrder> implements IUmsOrderService {

    private UmsOrderMapper orderMapper;

    /**
     * 创建订单
     *
     * @param order 订单信息
     */
    @Async
    @Override
    public void createOrder(UmsOrder order) {
        orderMapper.insert(order);
    }

    /**
     * 根据异步通知更新系统订单信息，正常情况下是支付成功后的回调通知
     *
     * @param order 订单信息
     */
    @Async
    @Override
    public void updateOrderByNotifyReceive(UmsOrder order) {
        // 如果该订单已经根据异步通知更新了，则无需操作。
        // 判断是否已更新，采用 status 字段判断：已经成功了，则无需操作；否则更新订单信息
        QueryWrapper<UmsOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(UmsOrder.OUT_TRADE_NO, order.getOutTradeNo())
                // 订单状态为 WAIT 的才需要更新
                .eq(UmsOrder.STATUS, PaymentConsts.Status.WAIT);
        // 商户单号 字段无需更新
        order.setOutTradeNo(null);
        // 执行更新
        orderMapper.update(order, wrapper);
    }

    /**
     * 根据订单通知的推送结果，更新订单信息
     *
     * @param order 订单信息（商户单号、回调信息、回调时间）
     */
    @Async
    @Override
    public void updateOrderByNotifySend(UmsOrder order) {
        QueryWrapper<UmsOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(UmsOrder.OUT_TRADE_NO, order.getOutTradeNo());
        // 商户单号 字段无需更新
        order.setOutTradeNo(null);
        orderMapper.update(order, wrapper);
    }
}
