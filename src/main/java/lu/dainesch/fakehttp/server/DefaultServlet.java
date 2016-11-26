package lu.dainesch.fakehttp.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lu.dainesch.fakehttp.Constants;


class DefaultServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       resp.sendError(HttpServletResponse.SC_NOT_FOUND, Constants.DEFAULT_RESPONSE);
    }
    
}
