package lu.dainesch.fakehttp.server.filters;

public class CustomFilter extends PathFilter<CustomServlet> {

    private int status;
    private String contentType;
    private String body;

    public CustomFilter() {
        super(FilterType.Custom);
    }

    public synchronized int getStatus() {
        return status;
    }

    public synchronized void setStatus(int status) {
        this.status = status;
    }

    public synchronized String getContentType() {
        return contentType;
    }

    public synchronized void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public synchronized String getBody() {
        return body;
    }

    public synchronized void setBody(String body) {
        this.body = body;
    }

    @Override
    void initServlet() {
        this.servlet = new CustomServlet(this);
    }

}
