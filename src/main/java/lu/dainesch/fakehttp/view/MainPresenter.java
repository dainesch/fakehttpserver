package lu.dainesch.fakehttp.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javax.inject.Inject;
import lu.dainesch.fakehttp.FakeHttpConfig;
import lu.dainesch.fakehttp.server.CommListener;
import lu.dainesch.fakehttp.server.FakeHttp;
import lu.dainesch.fakehttp.view.about.AboutDialogView;
import lu.dainesch.fakehttp.view.detail.DetailsView;
import lu.dainesch.fakehttp.view.filter.FilterListView;
import lu.dainesch.fakehttp.view.filter.create.CreateFilterView;
import lu.dainesch.fakehttp.view.request.RequestView;
import lu.dainesch.fakehttp.view.settings.SettingsView;


public class MainPresenter {

    private DetailsView detailsView;
    private RequestView requestView;
    private FilterListView filterView;

    @Inject
    private FakeHttp http;
    @Inject
    private CommListener listener;
    @Inject
    private FakeHttpConfig config;

    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private BorderPane leftPane;
    @FXML
    private Tab detailsTab;
    @FXML
    private Tab filterTab;
    @FXML
    private ToggleButton recordToggle;
    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        detailsView = new DetailsView();
        requestView = new RequestView();
        filterView = new FilterListView();

        // add views
        detailsTab.setContent(detailsView.getView());
        leftPane.setCenter(requestView.getView());
        filterTab.setContent(filterView.getView());

        // register
        startButton.disableProperty().bind(config.getStarted());
        stopButton.disableProperty().bind(config.getStarted().not());
        recordToggle.selectedProperty().set(listener.getRecordRequests().get());

        config.getStarted().addListener((b, o, n) -> {
            updateStatus();
        });
        updateStatus();
    }

    @FXML
    void onStartClicked(ActionEvent event) {
        http.startServer();
    }

    @FXML
    void onStopClicked(ActionEvent event) {
        http.stopServer();
    }

    @FXML
    void onClearClicked(ActionEvent event) {
        listener.getRequests().clear();
        listener.getSelectedRequest().set(null);
    }

    @FXML
    void onRecordClicked(ActionEvent event) {
        listener.getRecordRequests().setValue(listener.getRecordRequests().not().get());
    }

    @FXML
    void onCreateFilter(ActionEvent event) {

        CreateFilterView view = new CreateFilterView();
        view.show();
    }

    @FXML
    void onMenuAbout(ActionEvent event) {
        AboutDialogView about = new AboutDialogView();
        about.getView();
    }

    @FXML
    void onMenuExit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void onMenuSettings(ActionEvent event) {
        SettingsView settings = new SettingsView();
        settings.getView();
    }

    private void updateStatus() {
        if (config.getStarted().get()) {
            statusLabel.setText("Server running at port " + config.getPort().get());
        } else {
            statusLabel.setText("Server stopped");
        }
    }

}
