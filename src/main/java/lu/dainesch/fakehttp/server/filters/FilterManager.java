package lu.dainesch.fakehttp.server.filters;

import javax.servlet.http.HttpServlet;


public class FilterManager {
    
    public FilterManager() {
    
    }
    
    public HttpServlet getServlet(PathFilter<?> filter) {
        filter.initServlet();
        return filter.getServlet();
    }
    
}
