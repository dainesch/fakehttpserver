package lu.dainesch.fakehttp.server;

import java.io.IOException;
import javafx.application.Platform;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lu.dainesch.fakehttp.Constants;
import lu.dainesch.fakehttp.FakeHttpConfig;
import lu.dainesch.fakehttp.server.data.RecordedRequest;
import lu.dainesch.fakehttp.server.filters.FilterManager;
import lu.dainesch.fakehttp.server.filters.PathFilter;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.FilterRegistration;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class CustomWebAppContext extends WebappContext implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(CustomWebAppContext.class);

    private final FakeHttpConfig config;
    private final CommListener listener;
    private final FilterManager filterMan;

    private HttpServer server;

    CustomWebAppContext(FakeHttpConfig config, CommListener listener) {
        super(CustomWebAppContext.class.getSimpleName());
        this.listener = listener;
        this.filterMan = new FilterManager();
        this.config = config;
        initContext();
    }

    private void initContext() {
        FilterRegistration cfg = addFilter(CustomWebAppContext.class.getSimpleName(), this);
        cfg.addMappingForUrlPatterns(null, Constants.MAPPING_ALL);
        ServletRegistration reg = addServlet(DefaultServlet.class.getSimpleName(), new DefaultServlet());
        reg.addMapping(Constants.MAPPING_ALL);
    }

    @Override
    protected void destroyFilters() {
        // do not kill filter mapping
    }

    void setServer(HttpServer server) {
        if (deployed) {
            undeploy();
        }
        this.server = server;
        deploy(server);
    }

    void addFilter(PathFilter filter) {
        if (deployed) {
            undeploy();
        }
        ServletRegistration reg = addServlet(filter.getPath(), filterMan.getServlet(filter));
        if (filter.getPath().endsWith("*")) {
            reg.addMapping(filter.getPath().substring(0, filter.getPath().length() - 2));
        }
        reg.addMapping(filter.getPath());
        deploy(server);
    }

    void removeFilter(PathFilter filter) {
        if (deployed) {
            undeploy();
        }
        if (filter.getPath().endsWith("*")) {
            servletRegistrations.remove(filter.getPath().substring(0, filter.getPath().length() - 2));
        }
        servletRegistrations.remove(filter.getPath());
        deploy(server);
    }

    //
    // FILTER
    //
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, javax.servlet.FilterChain chain) throws IOException, ServletException {
        WrappedRequest request = new WrappedRequest((HttpServletRequest) req);
        WrappedResponse response = new WrappedResponse((HttpServletResponse) resp);

        //TODO check thread
        if (listener.getRecordRequests().get()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Got: " + request.getPathInfo());
            }
            RecordedRequest fakeReq = new RecordedRequest();
            
            fakeReq.readRequest(request, request.getInput(), config.getPreviewSize().get(), request.getCharacterEncoding());

            // pass to servlets
            chain.doFilter(request, response);

            // write output
            response.writeToResponse();
            response.flushBuffer();

            fakeReq.readResponse(response, response.getOutput(), config.getPreviewSize().get(), response.getCharacterEncoding());

            // send to ui
            Platform.runLater(() -> {
                listener.getRequests().add(fakeReq);
            });

            if (LOG.isDebugEnabled()) {
                LOG.debug("End: " + request.getPathInfo());
            }
        } else {
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void destroy() {

    }

    public boolean isPathRegistered(String path) {
        if (path.endsWith("*")) {
            return servletRegistrations.containsKey(path.substring(0, path.length() - 2))
                    || servletRegistrations.containsKey(path);
        }
        return servletRegistrations.containsKey(path);
    }

}
