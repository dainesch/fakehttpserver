package lu.dainesch.fakehttp.server.filters;

import java.util.Objects;
import javax.servlet.http.HttpServlet;

public abstract class PathFilter<S extends HttpServlet> {

    protected S servlet;
    protected String path;
    private final FilterType type;

    public PathFilter(FilterType type) {
        this.type = type;
    }

    public FilterType getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    S getServlet() {
        return servlet;
    }

    abstract void initServlet();

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.path);
        hash = 59 * hash + Objects.hashCode(this.type);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PathFilter<?> other = (PathFilter<?>) obj;
        if (!Objects.equals(this.path, other.path)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }
    
    

}
