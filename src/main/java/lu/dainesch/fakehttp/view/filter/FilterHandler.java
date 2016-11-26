package lu.dainesch.fakehttp.view.filter;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lu.dainesch.fakehttp.Constants;
import lu.dainesch.fakehttp.server.filters.PathFilter;


public abstract class FilterHandler<F extends PathFilter> {

    protected final BooleanProperty pathValid = new SimpleBooleanProperty(false);
    protected final BooleanProperty settingsValid = new SimpleBooleanProperty(true);

    public abstract void readValuesForm(F filter);

    public abstract void saveValuesTo(F filter);

    public abstract F createFilter();

    public BooleanProperty getValidSettings() {
        return settingsValid;
    }

    public BooleanProperty getValidPath() {
        return pathValid;
    }

    public void setCurrentPath(String path) {
        pathValid.set(validatePath(path));
    }

    protected boolean validatePath(String path) {
        return Constants.VALID_PATH.matcher(path).matches();
    }

}
