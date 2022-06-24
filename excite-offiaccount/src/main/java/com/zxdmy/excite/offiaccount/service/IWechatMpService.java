package com.zxdmy.excite.offiaccount.service;

import com.zxdmy.excite.offiaccount.bo.OffiaccountBO;

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

    boolean saveWechatMpConfig(OffiaccountBO wechatMpBo);

    OffiaccountBO getWechatMpConfig(String key);

    OffiaccountBO getWechatMpConfig();

    List<OffiaccountBO> getWechatMpConfigList();
}
