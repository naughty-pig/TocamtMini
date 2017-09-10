package org.hdj.tocamtmini.test;

import org.hdj.tocamtmini.core.MyRequest;
import org.hdj.tocamtmini.core.MyResponse;
import org.hdj.tocamtmini.core.MyServlet;

import java.io.IOException;

public class HelloWordServlet extends MyServlet {

    @Override
    public void doGet(MyRequest request, MyResponse response) {
       System.out.println("HelloWordServlet: get world");
        try {
            response.write("测试");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(MyRequest request, MyResponse response) {
        System.out.println("HelloWordServlet: post world");
    }
}
