package com.zxdmy.excite.admin.payment;

import cn.hutool.core.util.IdUtil;
import com.zxdmy.excite.payment.bo.AlipayBO;
//import com.zxdmy.excite.payment.service.IAlipayService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/1/11 11:42
 */
@SpringBootTest
public class AlipayTest {


//    @Autowired
//    private IAlipayService alipayService;

    @Test
    void saveTest() {
        AlipayBO alipayBO = new AlipayBO()
                // 基本信息
                .setAppId("2021000119622008")
                .setAppName("沙箱测试应用")
                .setMerchantPrivateKey("MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCEePZ4imjrk6i8J6T4kgQlpzh3zSKbjVIOK7P/6FJnxFTShWltPtYrRyevlhf5JRNf4rNG5rP3YeBqhBl/4Zgok6YmFOqcYBepu0/d3cbYB6y7mvHC9y+ZDfhJtvXFwtxYWsImm5UC8TxgPX2PcVyTi09pHPWZp1hexGoJEVXQqX1tE9ckLu8Hp2QVeWUV2yVWnfJ42TKTPtsdevyoBg9cxNK1PcgqXp2Xd33Z66MH/ZClXgSYugxjukmEoIkKH++Rs1vGmoblv5m0UPZKZoZzK0xZHlXBiKlTyKJX+GcYbjJKyyjurrOUGJpLvmJhKjew5zWVf+BjKFd5vRyUh2rNAgMBAAECggEALqsuqWiZbeSnHSIaTDeknl861Xm/J22x6qwazZza6xOPjlrRuVrZuIVoxcLqzyrwmGcVIXVFEXG5zcyb7yFaqXDap6/WtyBjNbh3eoIW+yQ5Bh6f7T+H1TPGtLFwBCuR1M6kd1V4OYEi9AS1p0MYiiIZ5738CBWcXEZ11Jv2z3tfVjI4OCNhHAJp5B9hipeva6DklJ3W8VzrXrH6sjr1Az/bykOMtSqF53HpPfc0lGSekMMvUIb0aKVdfBUAPuQczUEQzWjRjZgS1J8o9+0SskVDsD4/2Jh+elpzy2OX9YQfD6LVaQZ/VHRdlFdFilaUzY0Po0fc5tizfSaetUt7QQKBgQD03J8PNl1UvzClxnjDdkOtiitHAWTzOL+7t9xJdZA8R7CrYKFFTB6LdgwcleXDn/KX8/EURYNxyA3dpV+j+2HF2bLT+nvRs0bahntFFJ8J07b0TixEXOTjzsAt/8AkcueXsiBfXrAaaonI0/TkiKhqagPw090RD8vrDjOQfQR3JwKBgQCKf5XL5P9FfR1xEEZKOl3gbsXfNW5xNstxw1IiB9HRrtZtHfoa/NyZeJ4cbsX6Pm1mcvVdY9H1cg034Bzr7vA9ZVp/ed+P3s5AuUFn3KdCFd/28H2CgO68niA/aYYa+8olSbMR0EL8NkpQ7YhF8b3z5/QnTYch1bGkaonFQzfm6wKBgQCOBZjMPgAMM4iH3oIU/PdsJIPNf2WKHRQ4UQAYK3QpQnuaK0Xe57OfuCx2OAs4WAFhWUAvPdJroFlKgazjc97V3tr2UIaYrp8eCHpfKwLDtGSddun+DNNFvpmBFKHhgjQJrGtSZZ1G9RxNu4KDOzBT6IG4oWnLh5oDEA2gdKKuaQKBgG2y0q5V6k9a4yf7sQD4LAUpGDdMtfacZRXtlC96LBm+Nv7koIYhxIUWwX1ZzBwCuFpOMcRP3SxgstGQBLhnYAfxMHYJzSbCXnFrir++SQiSFPwdzfvBLddeE8LGTT8wMhPIxToV0Ai+46woCCkGaeM31O4QfTkybmsBK7CAJlYTAoGBAISpvM3JrSbEAc8vNu11R50SWnfgmvOP+suYoH4AvphwCGrwUWN+eUwIKKW4M5C6BwZvci6raYbRX+E6rByjIa7Wh2VZ3Hp7QM7mtLjQyMZmUSOHZjCmKx+Ab1h462a6B7nOzUkPmSTne7p261lyhEUcJQgZCkWUPuFCnLXAdEC4")
                // 证书模式
//                .setMerchantCertPath("E:\\支付宝支付\\appCertPublicKey_2021000117696058.crt")
//                .setAlipayCertPath("E:\\支付宝支付\\alipayCertPublicKey_RSA2.crt")
//                .setAlipayRootCertPath("E:\\支付宝支付\\alipayRootCert.crt")
                // 公钥模式
                .setAlipayPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv5tMRTrs4NbHsp4iNpTjT/HkldndK1SQAbb7ini3EqMInh+/RDAbJ2WNvVk5/CnZprOMFJg4ENMtbh5DXEei+d6iP2I7T5ApALhc4VX0AJeJmMQM+vzJEsCkIPlQTAy3tICh5qKgDqNz3St8LFoB/XCR7HjYJGkxZpAKwl6Dq04rn+eg86kj9qAg5n0NqbO7BSSymhUuW+EdZIDPNcniNts8ABihxff4uZaZWOkqIK6lY5LIrh0tVZF+I3dJrxoYNbX/vMVXUcfe8D0Trln/hxX6iisoVNTx4gONOOfmadFltNugveWSvGyvsigHNJXtXNfxjJmHIaLQm6JhiQ+bVQIDAQAB")
                // 接口内容加密方式
                .setEncryptKey("cAikHtKWeTYvCvw==")
                // 注意：沙箱环境下，请配置此网关！正式环境可忽略！
                .setGatewayHost("openapi.alipaydev.com");
//        alipayService.saveConfig(alipayBO);
    }

    /**
     * 支付测试
     */
    @Test
    void payTest() {
        String outTradeNo = IdUtil.simpleUUID();
        System.out.println(outTradeNo);
//        System.out.println(alipayService.pay(null, "facetoface", "测试商品名称", outTradeNo, "66666", "https://www.zxdmy.com", "https://www.zxdmy.com"));
    }
//
//    /**
//     * 查询测试
//     */
//    @Test
//    void queryTest() {
//        System.out.println(Arrays.toString(alipayService.queryPay(null, "2022011122001428220502263620", "f9bd5856549b47b684284816ac324f8c")));
//        // [Y, 2022011122001428220502263620, f9bd5856549b47b684284816ac324f8c, TRADE_SUCCESS, 666.00, 2088622956328227, aev***@sandbox.com]
//    }
//
//    /**
//     * 退款测试
//     */
//    @Test
//    void refundTest() {
//        System.out.println(
//                Arrays.toString(alipayService
//                        .refund(null, "2022011122001428220502263620", null, "20.00", "不想要了")));
//        // [Y, 2022011122001428220502263620, f9bd5856549b47b684284816ac324f8c, da7486bdbfe644c48c815af84af423cd, 10.00]
//        // [Y, 2022011122001428220502263620, f9bd5856549b47b684284816ac324f8c, 1dde059885134947948197f3b3268cf0, 30.00]
//    }
//
//    /**
//     * 查询退款测试
//     */
//    @Test
//    void queryRefundTest() {
//        System.out.println(Arrays.toString(alipayService.queryRefund(
//                null,
//                "f9bd5856549b47b684284816ac324f8c0",
//                null)));
//        // [Y, 2022011122001428220502263620, f9bd5856549b47b684284816ac324f8c, da7486bdbfe644c48c815af84af423cd, 666.00, 10.00, null]
//        // [Y, 2022011122001428220502263620, f9bd5856549b47b684284816ac324f8c, 1dde059885134947948197f3b3268cf0, 666.00, 20.00, null]
//    }
//
//    @Test
//    void downLoadTest() {
//        System.out.println(Arrays.toString(alipayService.downloadBill(null, "2021-12")));
//    }
}
