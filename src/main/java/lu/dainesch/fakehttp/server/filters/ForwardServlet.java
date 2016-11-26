package lu.dainesch.fakehttp.server.filters;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lu.dainesch.fakehttp.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class ForwardServlet extends PathServlet {

    private static final Logger LOG = LoggerFactory.getLogger(ForwardServlet.class);

    private final ForwardFilter filter;

    ForwardServlet(ForwardFilter filter) {
        this.filter = filter;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final boolean hasoutbody = (req.getMethod().equals("POST") || req.getMethod().equals("PUT"));

        String path = filter.getForwardURL();
        if (req.getPathInfo() != null) {
            path += req.getPathInfo();
        }

        try {
            final URL url = new URL(path
                    + (req.getQueryString() != null ? "?" + req.getQueryString() : ""));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(req.getMethod());

            final Enumeration<String> headers = req.getHeaderNames();
            while (headers.hasMoreElements()) {
                String header = headers.nextElement();
                if (Constants.HOST.equalsIgnoreCase(header)) {
                    continue;
                }
                final Enumeration<String> values = req.getHeaders(header);
                while (values.hasMoreElements()) {
                    String value = values.nextElement();
                    conn.addRequestProperty(header, value);
                }
            }

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(hasoutbody);
            conn.connect();

            byte[] buffer = new byte[8 * 1024];
            while (hasoutbody) {
                int read = req.getInputStream().read(buffer);
                if (read <= 0) {
                    break;
                }
                conn.getOutputStream().write(buffer, 0, read);
            }

            // status
            resp.setStatus(conn.getResponseCode());

            boolean gzip = false;

            for (int i = 0;; ++i) {
                String header = conn.getHeaderFieldKey(i);
                if (header == null) {
                    if (i == 0) {
                        continue;
                    }
                    break;
                } else {
                    String value = conn.getHeaderField(i);
                    if (header.equalsIgnoreCase(Constants.CONTENT_ENCODING) && value.toLowerCase().contains(Constants.GZIP)) {
                        gzip = true;
                    } else {
                        resp.setHeader(header, value);
                    }

                }

            }

            InputStream in;
            if (resp.getStatus() >= 400) {
                in = conn.getErrorStream();
            } else {
                in = conn.getInputStream();
            }
            if (gzip) {
                in = new GZIPInputStream(in);
            }

            while (true) {
                int read = in.read(buffer);
                if (read <= 0) {
                    break;
                }
                resp.getOutputStream().write(buffer, 0, read);
            }
        } catch (Exception ex) {
            LOG.error("Error during forward", ex);
            // pass
        }
    }

}
