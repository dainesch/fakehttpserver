package lu.dainesch.fakehttp.view.filter;

import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lu.dainesch.fakehttp.server.filters.PathFilter;


public class FilterTableModel {

    private final PathFilter<?> filter;
    private final StringProperty path = new SimpleStringProperty();
    private final StringProperty type = new SimpleStringProperty();

    public FilterTableModel(PathFilter filter) {
        this.type.setValue(FilterFactory.getName(filter.getType()));
        this.path.setValue(filter.getPath());
        this.filter = filter;
    }

    public PathFilter<?> getFilter() {
        return filter;
    }

    public String getType() {
        return type.getValue();
    }

    public void setType(String n) {
        type.setValue(n);
    }

    public String getPath() {
        return path.getValue();
    }

    public void setPath(String v) {
        path.set(v);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.filter);
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
        final FilterTableModel other = (FilterTableModel) obj;
        if (!Objects.equals(this.filter, other.filter)) {
            return false;
        }
        return true;
    }

    

}
