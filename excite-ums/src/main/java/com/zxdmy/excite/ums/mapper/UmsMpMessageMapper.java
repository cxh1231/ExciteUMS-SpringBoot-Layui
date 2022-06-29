package com.zxdmy.excite.ums.mapper;

import com.zxdmy.excite.ums.entity.UmsMpMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 微信消息接收与发送记录表 Mapper 接口
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-06-29
 */
@Mapper
public interface UmsMpMessageMapper extends BaseMapper<UmsMpMessage> {

}
