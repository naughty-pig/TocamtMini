package org.hdj.tocamtmini.core;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 封装响应
 *
 * 基于HTTP协议的格式进行输出写入。
 */
public class MyResponse {

    private OutputStream outputStream;

    public MyResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(String content) throws IOException{
//        HTTP响应协议
//        HTTP/1.1 200 OK
//        Server	Apache-Coyote/1.1
//        Set-Cookie	JSESSIONID=0FEF60C71629248265A9FC8E7D74CA90; Path=/SpringMVC_MyBatis/; HttpOnly
//        Content-Type	text/html;charset=UTF-8
//        Content-Length	487
//        Date	Sun, 10 Sep 2017 06:28:46 GMT
        //<html><body></body></html>

        StringBuffer httpResponse=new StringBuffer();
        httpResponse.append("HTTP/1.1 200 OK\n")
                .append("Content-Type: text/html;charset=UTF-8\n")
                .append("\r\n")
                .append("<html><body>")
                .append(content)
                .append("</body></html>");

        outputStream.write(httpResponse.toString().getBytes());
        outputStream.close();

    }
}
