package lu.dainesch.fakehttp.server.filters;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


class CustomServlet extends PathServlet {

    private final CustomFilter filter;

    CustomServlet(CustomFilter filter) {
        this.filter = filter;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(filter.getStatus());
        resp.setContentType(filter.getContentType());

        try (OutputStream out = resp.getOutputStream();
                PrintWriter w = new PrintWriter(out)) {
            w.write(filter.getBody());
        }
    }

}
