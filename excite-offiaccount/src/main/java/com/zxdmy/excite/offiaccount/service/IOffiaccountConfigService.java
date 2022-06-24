package com.zxdmy.excite.offiaccount.service;

import com.zxdmy.excite.offiaccount.bo.OffiaccountBO;

/**
 * 公众号配置 服务 接口类
 *
 * @author 拾年之璐
 * @since 2022/6/24 10:23
 */
public interface IOffiaccountConfigService {

    /**
     * 保存公众号配置
     *
     * @param offiaccountBO 公众号配置信息
     * @return 保存结果
     */
    boolean saveConfig(OffiaccountBO offiaccountBO);

    /**
     * 获取公众号配置（不脱敏，指定KEY）
     *
     * @param key 配置列表的KEY
     * @return 公众号配置信息
     */
    OffiaccountBO getConfig(String key);

    /**
     * 获取公众号配置（不脱敏，默认KEY）
     *
     * @return 公众号配置信息
     */
    OffiaccountBO getConfig();

    /**
     * 获取公众号配置（脱敏，指定KEY）
     *
     * @param key 配置列表的KEY
     * @return 公众号配置信息
     */
    OffiaccountBO getConfigHide(String key);

    /**
     * 获取公众号配置（脱敏，默认KEY）
     *
     * @return 公众号配置信息
     */
    OffiaccountBO getConfigHide();
}
