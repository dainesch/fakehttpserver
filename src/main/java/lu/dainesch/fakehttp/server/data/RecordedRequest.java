package lu.dainesch.fakehttp.server.data;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Enumeration;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lu.dainesch.fakehttp.Constants;


public class RecordedRequest implements Serializable {

    private int status;
    private String path;
    private final SortedMap<String, String> requestHeaders = new TreeMap<>();
    private final SortedMap<String, String> responseHeaders = new TreeMap<>();
    private final SortedMap<String, String> requestParams = new TreeMap<>();
    private String requestBody;
    private String responseBody;

    public void readRequest(HttpServletRequest req, byte[] in, int previewSize, String charset) throws IOException {

        path = req.getServletPath();
        if (req.getPathInfo() != null) {
            path += req.getPathInfo();
        }

        // headers
        Enumeration<String> headers = req.getHeaderNames();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            requestHeaders.put(header, req.getHeader(header));
        }

        Enumeration<String> params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String name = params.nextElement();
            requestParams.put(name, req.getParameter(name));
        }

        if (in != null) {
            Charset ch = StandardCharsets.UTF_8;
            if (charset != null) {
                ch = Charset.forName(charset);
            }

            int l = in.length > previewSize ? previewSize : in.length;
            requestBody = new String(in, 0, l, ch);
        } else {
            requestBody = Constants.NO_BODY;
        }

    }

    public void readResponse(HttpServletResponse resp, byte[] out, int previewSize, String charset) {

        status = resp.getStatus();

        //headers
        Collection<String> headers = resp.getHeaderNames();
        headers.stream().forEach((header) -> {
            responseHeaders.put(header, resp.getHeader(header));
        });

        if (out != null) {
            Charset ch = StandardCharsets.UTF_8;
            if (charset != null) {
                ch = Charset.forName(charset);
            }

            int l = out.length > previewSize ? previewSize : out.length;
            responseBody = new String(out, 0, l, ch);
        } else {
            responseBody = Constants.NO_BODY;
        }

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public SortedMap<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public SortedMap<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public SortedMap<String, String> getRequestParams() {
        return requestParams;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getContentType() {
        return responseHeaders.get(Constants.CONTENT_TYPE);
    }

}
