package com.zxdmy.excite.offiaccount.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxdmy.excite.common.exception.ServiceException;
import com.zxdmy.excite.common.service.IGlobalConfigService;
import com.zxdmy.excite.offiaccount.bo.OffiAccount;
import com.zxdmy.excite.offiaccount.service.IOffiAccountService;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/3/30 19:31
 */
@Service
@AllArgsConstructor
public class OffiAccountServiceImpl implements IOffiAccountService {

    private static final String DEFAULT_SERVICE = "offiaccount";

    // 默认使用公众号的 appid 作为 key
    // private static final String DEFAULT_KEY = "wx1111111111";

    private IGlobalConfigService configService;

    private WxMpService wxMpService;

    /**
     * 保存公众号配置
     *
     * @param account 公众号信息实体
     * @return 保存结果
     */
    @Override
    public boolean saveOffiAccount(OffiAccount account) {
        // 如果必填信息为空，则返回错误
        if (null == account.getAppid() || null == account.getAppSecret() || null == account.getToken() || null == account.getAesKey()) {
            throw new ServiceException("appid、appSecret、token或AesKey为空，请核实！");
        }
        try {
            if (configService.save(DEFAULT_SERVICE, account.getAppid(), account, false)) {
                this.addAccountToRuntime(account);
                return true;
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        return false;
    }

    /**
     * 通过appid获取公众号配置信息
     *
     * @param appId 公众号的appid
     * @return 公众号配置信息
     */
    @Override
    public OffiAccount getOffiAccount(String appId) {
        OffiAccount account = new OffiAccount();
        return (OffiAccount) configService.get(DEFAULT_SERVICE, appId, account);
    }

    @Override
    public List<OffiAccount> getOffiAccountList() {
        OffiAccount account = new OffiAccount();
        // 加载公众号配置
        List<Object> objectList = configService.getList(DEFAULT_SERVICE, account);
        // 公众号配置不为空
        if (objectList != null && !objectList.isEmpty()) {
            List<OffiAccount> accountList = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            // 遍历
            for (Object o : objectList) {
                // 转换类型并加载公众号配置至系统
                account = objectMapper.convertValue(o, account.getClass());
                accountList.add(account);
            }
            // 返回
            return accountList;
        }
        return null;
    }


    /**
     * 项目启动后，即注入公众号的配置
     */
    @PostConstruct
    public void loadWxMpConfigStorages() {
        OffiAccount account = new OffiAccount();
        // 加载公众号配置
        List<Object> accountList = configService.getList(DEFAULT_SERVICE, account);
        // 公众号配置不为空
        if (accountList != null && !accountList.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            // 遍历
            for (Object o : accountList) {
                // 转换类型并加载公众号配置至系统
                account = objectMapper.convertValue(o, account.getClass());
                this.addAccountToRuntime(account);
            }
        }
    }

    /**
     * 添加账号到当前程序。首次添加需初始化configStorageMap
     *
     * @param account 公众号配置
     */
    private synchronized void addAccountToRuntime(OffiAccount account) {
        // 配置类
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(account.getAppid());
        config.setSecret(account.getAppSecret());
        config.setToken(account.getToken());
        config.setAesKey(account.getAesKey());
        // 尝试动态添加配置类
        try {
            wxMpService.addConfigStorage(account.getAppid(), config);
        } catch (NullPointerException e) {
            // 首次添加需要初始化
            Map<String, WxMpConfigStorage> configStorages = new HashMap<>(4);
            configStorages.put(account.getAppid(), config);
            wxMpService.setMultiConfigStorages(configStorages, account.getAppid());
        }
    }

}
