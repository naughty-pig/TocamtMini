package org.hdj.tocamtmini.core;

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

    public String getServletName() {
        return servletName;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }
}
