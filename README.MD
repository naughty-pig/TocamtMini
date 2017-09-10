### 一、Tocamt迷你版实现

#### 1. 封装处理请求对象

```
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

//省略getter/setter方法
}
```

#### 2. 封装响应

```
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
```

##### 3. Servlet容器

```
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
```

##### 4. 处理映射关系

```$xslt
//1. 映射类
/**
 * 映射关系
 */
public class ServletMapping {
    /**
     * 请求的Servlet的名称
     */
    private String servletName;
    /**
     * 请求url
     */
    private String url;
    /**
     * 请求对应的Servlet的全限定类名
     */
    private String clazz;


    public ServletMapping(String servletName, String url, String clazz) {
        this.servletName = servletName;
        this.url = url;
        this.clazz = clazz;
    }

   //省略getter/setter方法
}


2. 映射配置类（配置请求url与Servlet之间的映射关系）
public class ServletMappingConfig {

    public static List<ServletMapping> servletMappingList=new ArrayList<>();

    static {
        servletMappingList.add(new ServletMapping("helloWord","/world","org.hdj.tocamtmini.test.HelloWordServlet"));
    }
}


```
##### 5. Tocamt处理流程
1. 启动Tocamt
- 会先加载映射配置
```$xslt
private Map<String,String> urlServletMap=new HashMap<>();

/**
 * 初始化请求映射关系
 */
public void intiServletMapping(){
    for(ServletMapping servletMapping:ServletMappingConfig.servletMappingList){
        urlServletMap.put(servletMapping.getUrl(),servletMapping.getClazz());
    }
}
```

- 创建服务端Socket（然后等待接收请求）
```$xslt
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
                    /*不断循环等待接收请求；*/
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
```

- 对接收到的请求进行分发
```$xslt
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
```
- 对请求进行响应
```$xslt
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
```

### 学习
http://www.jianshu.com/p/dce1ee01fb90