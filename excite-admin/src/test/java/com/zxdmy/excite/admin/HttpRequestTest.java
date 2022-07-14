package com.zxdmy.excite.admin;

import com.zxdmy.excite.common.utils.HttpRequestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/7/14 10:56
 */
@SpringBootTest
public class HttpRequestTest {

    @Test
    void test() {
        Map<String,String> params = new HashMap<>();
        params.put("appid", "12243456456457645");
        params.put("trade_no", "8876756123456");
        params.put("out_trade_no", "123456");
        params.put("amount", "12.34");
        params.put("status", "SUCCESS");
        params.put("attach", "123456");
        params.put("time", "12345656756756");
        params.put("nonce", "dfgfdgdftdf");
        params.put("hash", "dgdfgdfgdfgdfgdfgdf");

        HttpRequestUtils.PostForm("http://localhost:8088/post", params);

    }
}
