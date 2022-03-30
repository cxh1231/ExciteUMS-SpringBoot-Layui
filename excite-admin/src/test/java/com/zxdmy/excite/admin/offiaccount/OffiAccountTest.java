package com.zxdmy.excite.admin.offiaccount;

import com.zxdmy.excite.offiaccount.bo.OffiAccount;
import com.zxdmy.excite.offiaccount.service.IOffiAccountService;
import com.zxdmy.excite.offiaccount.service.IQrCodeService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

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
    private IOffiAccountService accountService;
    @Autowired
    private IQrCodeService qrCodeService;

    @Test
    void saveAccountTest() {
        OffiAccount account = new OffiAccount()
                .setAppid("wxf33dd15c03a05edd")
                .setAppSecret("6865f84a88d51320c2f897678a7142eb")
                .setToken("chenxiuhao")
                .setAesKey("111");
        accountService.saveOffiAccount(account);
    }
    @Test
    void saveAccountTest2() throws WxErrorException {
        System.out.println(qrCodeService.get());
    }

}
