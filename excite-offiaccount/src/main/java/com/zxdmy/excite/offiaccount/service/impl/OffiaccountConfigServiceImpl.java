package com.zxdmy.excite.offiaccount.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import com.zxdmy.excite.common.exception.ServiceException;
import com.zxdmy.excite.common.service.IGlobalConfigService;
import com.zxdmy.excite.offiaccount.bo.OffiaccountBO;
import com.zxdmy.excite.offiaccount.service.IOffiaccountConfigService;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 公众号配置 服务实现类
 *
 * @author 拾年之璐
 * @since 2022/6/24 10:29
 */
@Service
@AllArgsConstructor
public class OffiaccountConfigServiceImpl implements IOffiaccountConfigService {

    private static final String DEFAULT_SERVICE = "offiaccount";

    /**
     * 后期项目，如果开启了多公众号管理，则可以取消该默认key。使用公众号appid作为key。
     */
    private static final String DEFAULT_KEY = "offiaccount";

    private IGlobalConfigService configService;

    private WxMpService wxMpService;

    /**
     * 保存公众号配置
     *
     * @param offiaccountBO 公众号配置信息
     * @return 保存结果
     */
    @Override
    public boolean saveConfig(OffiaccountBO offiaccountBO) {
        // 必填信息[公众号名称、原始ID、APPID、AppSecret]不能为空
        if (null == offiaccountBO.getName() || null == offiaccountBO.getOriginalId() || null == offiaccountBO.getAppid() || null == offiaccountBO.getAppSecret()) {
            throw new ServiceException("公众号的[名称][原始ID][AppID][AppSecret]均不能为空，请核实！");
        }
        try {
            // 设置保存的KEY
            String key = (null == offiaccountBO.getKey() ? DEFAULT_KEY : offiaccountBO.getKey());
            // 如果保存成功，则添加至运行时
            if (configService.save(DEFAULT_SERVICE, DEFAULT_KEY, offiaccountBO, false)) {
                this.addWechatMpConfigToRuntime(offiaccountBO);
                return true;
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        return false;
    }

    /**
     * 获取公众号配置（不脱敏，指定KEY）
     *
     * @param key 配置列表的KEY
     * @return 公众号配置信息
     */
    @Override
    public OffiaccountBO getConfig(String key) {
        OffiaccountBO offiaccountBO = new OffiaccountBO();
        return (OffiaccountBO) configService.get(DEFAULT_SERVICE, key, offiaccountBO);
    }

    /**
     * 获取公众号配置（不脱敏，默认KEY）
     *
     * @return 公众号配置信息
     */
    @Override
    public OffiaccountBO getConfig() {
        return this.getConfig(DEFAULT_KEY);
    }

    /**
     * 获取公众号配置（脱敏，指定KEY）
     *
     * @param key 配置列表的KEY
     * @return 公众号配置信息
     */
    @Override
    public OffiaccountBO getConfigHide(String key) {
        OffiaccountBO offiaccountBO = new OffiaccountBO();
        offiaccountBO = (OffiaccountBO) configService.get(DEFAULT_SERVICE, key, offiaccountBO);
        // 目前只需要对 AppSecret 脱敏
        offiaccountBO.setAppSecret(DesensitizedUtil.idCardNum(offiaccountBO.getAppSecret(), 6, 6));
        return offiaccountBO;
    }

    /**
     * 获取公众号配置（脱敏，默认KEY）
     *
     * @return 公众号配置信息
     */
    @Override
    public OffiaccountBO getConfigHide() {
        return this.getConfigHide(DEFAULT_KEY);
    }

    /**
     * 项目启动后，即注入公众号的配置（单个注册）
     */
    @PostConstruct
    public void loadWxMpConfigStorages() {
        OffiaccountBO wechatMpBo = this.getConfig();
        // 公众号配置不为空
        if (wechatMpBo != null) {
            this.addWechatMpConfigToRuntime(wechatMpBo);
        }
    }

    /**
     * 添加账号到当前程序。动态配置使用。首次添加需初始化configStorageMap
     *
     * @param account 公众号配置
     */
    private synchronized void addWechatMpConfigToRuntime(OffiaccountBO account) {
        // 配置类
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(account.getAppid());
        config.setSecret(account.getAppSecret());
        config.setToken(account.getToken());
        config.setAesKey(account.getAesKey());
        // 尝试动态添加配置类
        try {
            wxMpService.setWxMpConfigStorage(config);
            System.out.println("公众号配置信息已更新");
        } catch (NullPointerException e) {
            // 首次添加需要初始化
            Map<String, WxMpConfigStorage> configStorages = new HashMap<>(4);
            configStorages.put(DEFAULT_KEY, config);
            wxMpService.setMultiConfigStorages(configStorages, DEFAULT_KEY);
            System.out.println("公众号配置信息已初始化！");
        }
    }
}
