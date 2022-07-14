package com.zxdmy.excite.ums.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SignUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxdmy.excite.common.consts.PaymentConsts;
import com.zxdmy.excite.common.utils.HttpRequestUtils;
import com.zxdmy.excite.common.utils.SignUtils;
import com.zxdmy.excite.ums.entity.UmsApp;
import com.zxdmy.excite.ums.entity.UmsOrder;
import com.zxdmy.excite.ums.mapper.UmsOrderMapper;
import com.zxdmy.excite.ums.service.IUmsOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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

    private UmsAppServiceImpl appService;

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
        queryWrapper.orderByDesc(UmsApp.ID);
        // 分页查询并返回
        return orderMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
    }

    /**
     * 创建订单
     * 同时将订单信息放入缓存中
     *
     * @param order 订单信息
     */
    @Async
    @Override
    @CachePut(value = "umsOrder", key = "#order.outTradeNo")
    public UmsOrder createOrder(UmsOrder order) {
        orderMapper.insert(order);
        return order;
    }

    /**
     * 根据 商户单号 查询订单信息
     * 如果命中缓存，则直接返回缓存中的数据
     *
     * @param outTradeNo 商户单号
     * @return 订单信息
     */
    @Override
    @Cacheable(value = "umsOrder", key = "#outTradeNo")
    public UmsOrder getOrderByOutTradeNo(String outTradeNo) {
        QueryWrapper<UmsOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(UmsOrder.OUT_TRADE_NO, outTradeNo);
        return orderMapper.selectOne(queryWrapper);
    }

    /**
     * 异步执行：根据回调信息更新订单信息（包含官方的回调，以及下游的回调信息）
     *
     * @param order 官方回调信息
     * @return 更新后的订单信息
     */
    @Async
    @Override
    @CachePut(value = "umsOrder", key = "#order.outTradeNo")
    public UmsOrder updateOrderByNotify(UmsOrder order) {
        // 先更新官方回调信息
        QueryWrapper<UmsOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(UmsOrder.OUT_TRADE_NO, order.getOutTradeNo())
                // 订单状态为 WAIT 的才需要更新
                .eq(UmsOrder.STATUS, PaymentConsts.Status.WAIT);
        // 商户单号 字段无需更新
//        order.setOutTradeNo(null);
        // 更新订单信息
        int result = orderMapper.update(order, wrapper);
        // 获取更新后的订单信息
        order = orderMapper.selectOne(new QueryWrapper<UmsOrder>().eq(UmsOrder.OUT_TRADE_NO, order.getOutTradeNo()));
        // 订单更新成功
        if (result > 0) {
            // 获取该应用的秘钥
            String secret = appService.getByAppId(order.getAppId()).getAppSecret();
            // 组装回调参数列表
            Map<String, String> params = new HashMap<>();
            params.put("appid", order.getAppId());
            params.put("trade_no", order.getTradeNo());
            params.put("out_trade_no", order.getOutTradeNo());
            params.put("title", order.getTitle());
            params.put("amount", order.getAmount());
            params.put("status", order.getStatus());
            params.put("attach", order.getAttach());
            params.put("time", String.valueOf((int) (System.currentTimeMillis() / 1000)));
            params.put("nonce", RandomUtil.randomString(16));
            // 生成签名
            String hash = SignUtils.sign(new TreeMap<>(params), secret);
            params.put("hash", hash);

            // 发送回调请求
            String notifyResult = HttpRequestUtils.PostForm(order.getNotifyUrl(), params);
            // 回调信息为success
            if (notifyResult != null && notifyResult.equalsIgnoreCase(PaymentConsts.Status.SUCCESS)) {
                // 构造更新条件
                QueryWrapper<UmsOrder> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq(UmsOrder.OUT_TRADE_NO, order.getOutTradeNo());
                // 定义更新内容
                UmsOrder umsOrder = new UmsOrder();
                umsOrder.setNotifyResult(PaymentConsts.Status.SUCCESS)
                        .setNotifyTime(LocalDateTime.now());
                // 更新订单信息
                if (orderMapper.update(umsOrder, queryWrapper) > 0) {
                    order.setNotifyResult(PaymentConsts.Status.SUCCESS)
                            .setNotifyTime(umsOrder.getNotifyTime());
                }
            }
        }
        // @CachePut 注解，对于更新来说，用到的是返回值，所以这里返回的是最新的订单信息，即再查询一次数据库
        return order;
    }

    /**
     * 根据【官方的异步通知】更新系统订单信息，正常情况下是支付成功后的回调通知
     * 同时将最新的订单信息放入缓存中
     *
     * @param order 订单信息
     */
    @Async
    @Override
    @CachePut(value = "umsOrder", key = "#order.outTradeNo")
    public UmsOrder updateOrderByNotifyReceive(UmsOrder order) {
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
        // @CachePut 注解，对于更新来说，用到的是返回值，所以这里返回的是最新的订单信息，即再查询一次数据库
        return orderMapper.selectOne(new QueryWrapper<UmsOrder>().eq(UmsOrder.OUT_TRADE_NO, order.getOutTradeNo()));
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
