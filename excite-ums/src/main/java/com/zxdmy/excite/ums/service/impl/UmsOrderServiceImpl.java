package com.zxdmy.excite.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
     * 分页查询订单信息
     *
     * @param search     搜索关键字：应用信息，订单标题，商户号，交易号。
     * @param payChannel 交易方式：支付宝，微信。
     * @param status     交易状态
     * @param pageNum    页码
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @param pageSize   每页数量
     * @return 分页结果
     */
    @Override
    public Page<UmsOrder> getPage(String search, String payChannel, String status, String startDate, String endDate, Integer pageNum, Integer pageSize) {
        // 如果页码为空，默认为 1
        if (pageNum == null) {
            pageNum = 1;
        }
        // 如果每页数量为空，默认为 10
        if (pageSize == null) {
            pageSize = 10;
        }
        // 创建查询条件
        QueryWrapper<UmsOrder> queryWrapper = new QueryWrapper<>();
        // 搜索关键字不为空，则添加搜索条件
        if (search != null && !"".equals(search)) {
            queryWrapper.like(UmsOrder.APP_ID, search)
                    .or().like(UmsOrder.TITLE, search)
                    .or().like(UmsOrder.TRADE_NO, search)
                    .or().like(UmsOrder.OUT_TRADE_NO, search);
        }
        // 交易方式不为空，则添加搜索条件
        queryWrapper.eq(payChannel != null && !"".equals(payChannel), UmsOrder.PAY_CHANNEL, payChannel)
                .eq(status != null && !"".equals(status), UmsOrder.STATUS, status)
                // 检索起始时间
                .ge(startDate != null && !"".equals(startDate), UmsOrder.CREATE_TIME, startDate)
                // 检索终止时间
                .le(endDate != null && !"".equals(endDate), UmsOrder.CREATE_TIME, endDate);
        // 时间排序，降序
        queryWrapper.orderByDesc(UmsOrder.CREATE_TIME);
        // 分页查询并返回
        return orderMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
    }

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
