package lu.dainesch.fakehttp.server.filters;

public class FixedStatusFilter extends PathFilter<FixedStatusServlet> {

    private int status;
    private String redirPath;

    public FixedStatusFilter() {
        super(FilterType.FixedStatus);
    }

    public synchronized int getStatus() {
        return status;
    }

    public synchronized void setStatus(int status) {
        this.status = status;
    }

    public synchronized String getRedirPath() {
        return redirPath;
    }

    public synchronized void setRedirPath(String redirPath) {
        this.redirPath = redirPath;
    }

    @Override
    void initServlet() {
        this.servlet = new FixedStatusServlet(this);
    }

}
