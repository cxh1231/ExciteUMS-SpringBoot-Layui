package com.zxdmy.excite.payment.service.impl;

import com.zxdmy.excite.payment.entity.UmsOrder;
import com.zxdmy.excite.payment.mapper.UmsOrderMapper;
import com.zxdmy.excite.payment.service.IUmsOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * UMS系统的交易平台的订单信息 服务实现类
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-04-01
 */
@Service
public class UmsOrderServiceImpl extends ServiceImpl<UmsOrderMapper, UmsOrder> implements IUmsOrderService {

}
