package lu.dainesch.fakehttp.server.filters;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


class FixedStatusServlet extends PathServlet {

    private final FixedStatusFilter filter;

    FixedStatusServlet(FixedStatusFilter filter) {
        this.filter = filter;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(filter.getStatus());
        if (filter.getRedirPath() != null) {
            switch (filter.getStatus()) {
                case 301:
                case 302:
                case 303:
                    resp.setHeader("Location", filter.getRedirPath());
            }
        }
    }

}
