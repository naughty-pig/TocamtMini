package org.hdj.tocamtmini.core;

import java.util.ArrayList;
import java.util.List;

public class ServletMappingConfig {

    public static List<ServletMapping> servletMappingList=new ArrayList<>();

    static {
        servletMappingList.add(new ServletMapping("helloWord","/world","org.hdj.tocamtmini.test.HelloWordServlet"));
    }
}
