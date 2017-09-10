package org.hdj.tocamtmini.core;

/**
 * servlet
 *
 */
public abstract class MyServlet {

    private static final String POST = "POST";
    private static final String GET = "GET";

    /**
     * 响应get请求
     * @param request
     * @param response
     */
    public abstract void doGet(MyRequest request, MyResponse response);

    /**
     * 响应post请求
     * @param request
     * @param response
     */
    public abstract void doPost(MyRequest request,MyResponse response);

    /**
     * 解析，响应相应的请求
     * @param request
     * @param response
     */
    public void service(MyRequest request,MyResponse response){
        if(request.getMethod().equalsIgnoreCase(POST)){
            doPost(request, response);
        }else if (request.getMethod().equalsIgnoreCase(GET)){
            doGet(request, response);
        }
    }

}
