package com.zxdmy.excite.common.utils;

import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 发起HTTP请求（POST、GET）工具类
 *
 * @author 拾年之璐
 * @since 2022/5/29 17:21
 */
public class HttpRequestUtils {

    /**
     * 以FORM形式发起POST请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 请求结果
     */
    public static String PostForm(String url, Map<String, String> params) {
        if (url == null || url.length() == 0) {
            return null;
        }
        MultiValueMap<String, String> paramsReq = new LinkedMultiValueMap<>();
        if (null != params) {
            paramsReq.setAll(params);
        }
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 组装请求实体：将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(paramsReq, headers);
        // 请求方法
        HttpMethod method = HttpMethod.POST;
        // 请求工厂
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 连接超时：5秒
        factory.setConnectTimeout(5000);
        // 数据读取超时时间：10秒
        factory.setReadTimeout(10000);
        // 发起请求
        RestTemplate client = new RestTemplate(factory);
        // 执行HTTP请求，将返回的结构使用String类格式化
        ResponseEntity<String> response;
        // 尝试发起请求
        try {
            response = client.exchange(url, method, requestEntity, String.class);
            // 测试：打印返回结果
            System.out.println(response.getBody());
            System.out.println(response.getStatusCodeValue());
            // 如果返回码为2XX，则返回请求实体，否则返回空
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * 以JSON形式发起POST请求
     *
     * @param url    请求地址
     * @param entity 请求实体
     * @return 请求结果
     */
    public static String PostJSON(String url, Object entity) {
        // 发起请求
        RestTemplate client = new RestTemplate();
        // 发送 post请求，并输出结果
        ResponseEntity<String> responseEntity = client.postForEntity(url, entity, String.class);
        String body = responseEntity.getBody(); // 获取响应体
        HttpStatus statusCode = responseEntity.getStatusCode(); // 获取响应码
        int statusCodeValue = responseEntity.getStatusCodeValue(); // 获取响应码值
        HttpHeaders headers = responseEntity.getHeaders(); // 获取响应头
        return responseEntity.getBody();
    }
}
