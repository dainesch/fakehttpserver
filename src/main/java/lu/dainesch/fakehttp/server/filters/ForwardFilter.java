package lu.dainesch.fakehttp.server.filters;

public class ForwardFilter extends PathFilter<ForwardServlet> {

    private int status;
    private String forwardURL;
    private boolean includePath;

    public ForwardFilter() {
        super(FilterType.Forward);
    }

    public synchronized int getStatus() {
        return status;
    }

    public synchronized void setStatus(int status) {
        this.status = status;
    }

    public synchronized String getForwardURL() {
        return forwardURL;
    }

    public synchronized void setForwardURL(String forwardURL) {
        this.forwardURL = forwardURL;
    }

    public synchronized boolean isIncludePath() {
        return includePath;
    }

    public synchronized void setIncludePath(boolean includePath) {
        this.includePath = includePath;
    }

    @Override
    void initServlet() {
        this.servlet = new ForwardServlet(this);
    }

}
