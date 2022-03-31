package com.zxdmy.excite.offiaccount.service;

import com.zxdmy.excite.offiaccount.bo.WechatMpBo;

import java.util.List;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/3/30 19:31
 */
public interface IWechatMpService {

    boolean saveWechatMpConfig(WechatMpBo wechatMpBo);

    WechatMpBo getWechatMpConfig(String key);

    WechatMpBo getWechatMpConfig();

    List<WechatMpBo> getWechatMpConfigList();
}
