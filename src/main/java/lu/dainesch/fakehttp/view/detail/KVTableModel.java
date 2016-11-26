package lu.dainesch.fakehttp.view.detail;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class KVTableModel {

    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty value = new SimpleStringProperty();

    public KVTableModel(String name, String vlaue) {
        this.name.setValue(name);
        this.value.setValue(vlaue);
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String n) {
        name.setValue(n);
    }

    public String getValue() {
        return value.getValue();
    }

    public void setValue(String v) {
        value.set(v);
    }

}
