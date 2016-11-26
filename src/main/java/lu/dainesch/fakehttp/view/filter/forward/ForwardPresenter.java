package lu.dainesch.fakehttp.view.filter.forward;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import lu.dainesch.fakehttp.Constants;
import lu.dainesch.fakehttp.server.filters.ForwardFilter;
import lu.dainesch.fakehttp.view.filter.FilterHandler;


public class ForwardPresenter extends FilterHandler<ForwardFilter> {

    @FXML
    private TextField forwardField;

    @FXML
    public void initialize() {
        onForwardTyped(null);
        
    }

    @FXML
    void onForwardTyped(KeyEvent event) {
        try {
            URL url = new URL(forwardField.getText());
            url.toURI();
            if (Constants.HTTP.equalsIgnoreCase(url.getProtocol()) || Constants.HTTPS.equalsIgnoreCase(url.getProtocol())) {
                settingsValid.set(true);
            } else {
                settingsValid.set(false);
            }

        } catch (MalformedURLException | URISyntaxException ex) {
            settingsValid.set(false);
        }
    }

    @Override
    public void readValuesForm(ForwardFilter filter) {
        forwardField.setText(filter.getForwardURL());
    }

    @Override
    public void saveValuesTo(ForwardFilter filter) {
        filter.setForwardURL(forwardField.getText());
    }

    @Override
    public ForwardFilter createFilter() {
        ForwardFilter filter = new ForwardFilter();
        saveValuesTo(filter);
        return filter;
    }

}
