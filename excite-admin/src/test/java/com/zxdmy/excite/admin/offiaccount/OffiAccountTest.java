package com.zxdmy.excite.admin.offiaccount;

import com.zxdmy.excite.offiaccount.bo.OffiaccountBO;
import com.zxdmy.excite.offiaccount.service.IWechatMpService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/3/30 20:06
 */
@SpringBootTest
public class OffiAccountTest {

    @Autowired
    private IWechatMpService accountService;


    @Test
    void saveAccountTest() {
        OffiaccountBO account = new OffiaccountBO()
                .setAppid("wxf33dd15c03a05edd")
                .setAppSecret("6865f84a88d51320c2f897678a7142eb")
                .setToken("chenxiuhao")
                .setAesKey("111");
        accountService.saveWechatMpConfig(account);
    }
    @Test
    void saveAccountTest2() throws WxErrorException {

    }

}
