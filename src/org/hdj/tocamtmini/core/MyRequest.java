package org.hdj.tocamtmini.core;

import java.io.IOException;
import java.io.InputStream;

/**
 * 封装请求
 *
 * 通过输入流，对HTTP协议进行解析，拿到了HTTP请求头的方法以及URL。
 */
public class MyRequest {
    /**
     * 请求的url
     */
    private String url;
    /**
     * 请求方法
     */
    private String method;

    public MyRequest(InputStream inputStream) throws IOException {
        /**
         * 请求属性
         */
        String httpRequest = "";
        /**
         * 读取请求缓存
         */
        byte[] httpRequestBytes = new byte[1024];
        int length = 0;
        if ((length = inputStream.read(httpRequestBytes)) > 0) {
            httpRequest = new String(httpRequestBytes, 0, length);
        }
//
//        HTTP请求协议
////      GET /SpringMVC_MyBatis/ItemsInfo/queryItems.action HTTP/1.1
//        Host	localhost:8080
//        Cache-Control	max-age=0
//        Upgrade-Insecure-Requests	1
//        User-Agent	Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36
//        Accept	text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
//        Accept-Encoding	gzip, deflate, br
//        Accept-Language	zh-CN,zh;q=0.8
//        Cookie	JSESSIONID=880F78E8D4AC7C0CF2581E0B71A992A2; Idea-c1507386=a1900b02-591d-4dc9-8728-c02acd39c0e92.113 Safari/537.36
        String httpHead = httpRequest.split("\\n")[0];
        url = httpHead.split("\\s")[1];
        method = httpHead.split("\\s")[0];
        System.out.println(this);
        System.out.println("请求：method:"+method+" ; url: "+url);
    }


    public String getMethod(){
        return method;
    }

    public String getUrl() {
        return url;
    }
}
