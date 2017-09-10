package org.hdj.tocamtmini.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Tocamt服务器
 */
public class MyTocamt {
    private int port=8080;
    private Map<String,String> urlServletMap=new HashMap<>();

    public MyTocamt(int port) {
        this.port = port;
    }

    //启动tocamt
    public void start(){
        //初始化url与servlet关系
        intiServletMapping();
        ServerSocket serverSocket=null;
        try {
            /*创建服务端的socket*/
            serverSocket=new ServerSocket(port);
            System.out.println("Tocamt 启动");

                while (true){
                    /*不断循环接收请求*/
                    Socket socket = serverSocket.accept();
                    InputStream inputStream=socket.getInputStream();
                    OutputStream outputStream=socket.getOutputStream();

                    MyResponse response=new MyResponse(outputStream);
                    MyRequest request=new MyRequest(inputStream);
                    //请求分发
                    dispatch(request,response);

                    //关闭socket
                    socket.close();
                }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * 初始化请求映射关系
     */
    public void intiServletMapping(){
        for(ServletMapping servletMapping:ServletMappingConfig.servletMappingList){
            urlServletMap.put(servletMapping.getUrl(),servletMapping.getClazz());
        }
    }

    /**
     * 分发
     * @param request
     * @param response
     */
    private void dispatch(MyRequest request,MyResponse response){
        //通过请求的url；找到对相应的Servlet来处理请求
        String clazz=urlServletMap.get(request.getUrl());

        try {
            //通过反射 实例化Servlet
            Class<MyServlet> myServletClass= (Class<MyServlet>) Class.forName(clazz);
            MyServlet myServlet = myServletClass.newInstance();
            //响应服务
            myServlet.service(request,response);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
