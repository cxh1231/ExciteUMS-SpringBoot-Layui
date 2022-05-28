package com.zxdmy.excite.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    @Async
    @Override
    public void createOrder(UmsOrder order) {
        orderMapper.insert(order);
    }

    @Async
    @Override
    public void updateOrderByNotifyReceive(UmsOrder order) {
        QueryWrapper<UmsOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(UmsOrder.OUT_TRADE_NO, order.getOutTradeNo());
        orderMapper.update(order, wrapper);
    }

    /**
     * 发送推送并根据推送结果更新订单信息
     *
     * @param order
     */
    @Async
    @Override
    public void updateOrderByNotifySend(UmsOrder order) {
        try {
            System.out.println("向下游发送Notify");
            Thread.sleep(10000L);
            System.out.println("向下游发送Notify，收到反馈！有");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
