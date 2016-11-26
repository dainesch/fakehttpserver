package lu.dainesch.fakehttp.server;

import com.airhacks.afterburner.injection.Injector;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import lu.dainesch.fakehttp.FakeHttpConfig;
import lu.dainesch.fakehttp.server.data.RecordedRequest;
import lu.dainesch.fakehttp.server.filters.PathFilter;
import org.glassfish.grizzly.http.server.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CommListener {

    private static final Logger LOG = LoggerFactory.getLogger(CommListener.class);

    private final FakeHttpConfig config;
    private final CustomWebAppContext ctx;

    private final ObservableList<RecordedRequest> requests = FXCollections.observableArrayList();
    private final BooleanProperty recordRequests = new SimpleBooleanProperty(true);
    private final ObjectProperty<RecordedRequest> selectedRequest = new SimpleObjectProperty<>();

    private final ObservableList<PathFilter> filters = FXCollections.observableArrayList();

    CommListener(FakeHttpConfig config) {
        this.config = config;
        this.ctx = new CustomWebAppContext(this.config, this);
        
    }

    void init() {

        filters.addListener((ListChangeListener.Change<? extends PathFilter> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    onFilterAdd(c.getAddedSubList());
                }
                if (c.wasRemoved()) {
                    onFilterRemove(c.getRemoved());
                }
            }

        });

        Injector.setModelOrService(CommListener.class, this);
    }

    void setServer(HttpServer server) {
        ctx.setServer(server);

    }

    private void onFilterAdd(List<? extends PathFilter> list) {

        list.forEach(pf -> {
            ctx.addFilter(pf);
        });

    }

    private void onFilterRemove(List<? extends PathFilter> list) {
        list.forEach(pf -> {
            ctx.removeFilter(pf);
        });

    }

    public ObservableList<RecordedRequest> getRequests() {
        return requests;
    }

    public BooleanProperty getRecordRequests() {
        return recordRequests;
    }

    public ObjectProperty<RecordedRequest> getSelectedRequest() {
        return selectedRequest;
    }

    public ObservableList<PathFilter> getFilters() {
        return filters;
    }
    
    public boolean isPathRegistered(String path) {
        if (path==null) {
            return true;
        }
        return ctx.isPathRegistered(path);
    }

}
