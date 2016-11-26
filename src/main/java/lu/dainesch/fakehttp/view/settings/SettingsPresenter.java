package lu.dainesch.fakehttp.view.settings;

import java.util.Optional;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javax.inject.Inject;
import lu.dainesch.fakehttp.FakeHttpConfig;
import lu.dainesch.fakehttp.server.FakeHttp;
import lu.dainesch.fakehttp.view.common.UIUtils;


public class SettingsPresenter {

    private static final ObservableList<SettingsPage> PAGES = FXCollections.observableArrayList(SettingsPage.General);

    @FXML
    private BorderPane borderPane;

    @FXML
    private ListView<SettingsPage> settingsList;

    @FXML
    private TextField serverPortLabel;

    @FXML
    private TextField previewSizeLabel;

    @Inject
    private FakeHttpConfig config;
    @Inject
    private FakeHttp http;

    private final BooleanProperty allValid = new SimpleBooleanProperty(true);

    @FXML
    public void initialize() {
        UIUtils.setTextFieldNumbersOnly(serverPortLabel, 1, 65535);
        UIUtils.setTextFieldNumbersOnly(previewSizeLabel, 10, 2 * 1024 * 1024);
        settingsList.setItems(PAGES);
        
        serverPortLabel.setText(String.valueOf(config.getPort().get()));
        previewSizeLabel.setText(String.valueOf(config.getPreviewSize().get()));

        Dialog diag = new Dialog();
        diag.getDialogPane().setContent(borderPane);
        diag.setResizable(true);
        UIUtils.setIcon(diag);

        ButtonType okButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        diag.getDialogPane().getButtonTypes().addAll(cancel, okButton);
        diag.getDialogPane().lookupButton(okButton).disableProperty().bind(allValid.not());
        diag.getDialogPane().lookupButton(okButton).addEventFilter(ActionEvent.ACTION, event -> {
            if (!allValid.get()) {
                event.consume();
            }
        });

        diag.setTitle("Settings");

        Optional<ButtonType> opt = diag.showAndWait();
        if (opt.isPresent() && opt.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            onOk();
        }

    }

    public void onOk() {
        config.getPort().set(Integer.parseInt(serverPortLabel.getText()));
        config.getPreviewSize().set(Integer.parseInt(previewSizeLabel.getText()));

        http.restartServer();

        config.saveConfig();
    }

}
