package lu.dainesch.fakehttp;

import com.airhacks.afterburner.injection.Injector;
import java.util.prefs.Preferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class FakeHttpConfig {

    private final Preferences preferences;
    private final BooleanProperty started = new SimpleBooleanProperty(false);
    private final IntegerProperty port = new SimpleIntegerProperty(9000);
    private final IntegerProperty previewSize = new SimpleIntegerProperty(1024);

    public FakeHttpConfig() {
        preferences = Preferences.userNodeForPackage(FakeHttpConfig.class);
        readConfig();
        Injector.setModelOrService(FakeHttpConfig.class, this);
    }

    public Object getInjectionValue(Object key) {
        return preferences.get((String) key, null);
    }

    public void putDouble(String key, double val) {
        preferences.putDouble(key, val);
    }

    public double getDouble(String key) {
        return preferences.getDouble(key, -1);
    }

    public void putInt(String key, int value) {
        preferences.putInt(key, value);
    }

    public int getInt(String key) {
        return preferences.getInt(key, -1);
    }

    public void putBoolean(String key, boolean val) {
        preferences.putBoolean(key, val);
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public void putString(String key, String val) {
        preferences.put(key, val);
    }

    public String getString(String key) {
        return preferences.get(key, null);
    }

    public BooleanProperty getStarted() {
        return started;
    }

    public IntegerProperty getPort() {
        return port;
    }

    public IntegerProperty getPreviewSize() {
        return previewSize;
    }

    private void readConfig() {
        started.set(getBoolean(Constants.SERVERSTARTED));
        if (getInt(Constants.SERVERPORT) > 0) {
            port.set(getInt(Constants.SERVERPORT));
        }
        if (getInt(Constants.PREVIEWSIZE) > 0) {
            previewSize.set(getInt(Constants.PREVIEWSIZE));
        }
    }

    public void saveConfig() {
        putBoolean(Constants.SERVERSTARTED, started.get());
        putInt(Constants.SERVERPORT, port.get());
        putInt(Constants.PREVIEWSIZE, previewSize.get());
    }

}
