package lu.dainesch.fakehttp.view.filter.create;

import java.util.Optional;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javax.inject.Inject;
import lu.dainesch.fakehttp.server.CommListener;
import lu.dainesch.fakehttp.server.filters.FilterType;
import lu.dainesch.fakehttp.server.filters.PathFilter;
import lu.dainesch.fakehttp.view.common.UIUtils;
import lu.dainesch.fakehttp.view.filter.FilterFactory;
import lu.dainesch.fakehttp.view.filter.FilterHandler;


public class CreateFilterPresenter {

    private static final ObservableList<FilterType> FILTERS = FXCollections.observableArrayList(FilterType.FixedStatus, FilterType.Custom,
            FilterType.FileDownload, FilterType.Forward);

    @FXML
    private BorderPane borderPane;
    @FXML
    private ListView<FilterType> filterList;
    @FXML
    private TextField filterPath;
    @FXML
    private TextArea filterDesc;

    @Inject
    private CommListener comm;

    private Dialog dialog;
    private final BooleanProperty allValid = new SimpleBooleanProperty();
    private final BooleanProperty pathExists = new SimpleBooleanProperty();

    private FilterHandler<?> selectedHandler = null;

    @FXML
    public void initialize() {

        dialog = new Dialog();
        dialog.getDialogPane().setContent(borderPane);
        dialog.setResizable(true);
        UIUtils.setIcon(dialog);

        ButtonType okButton = new ButtonType("Create", ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(cancel, okButton);
        dialog.getDialogPane().lookupButton(okButton).disableProperty().bind(allValid.not());
        dialog.getDialogPane().lookupButton(okButton).addEventFilter(ActionEvent.ACTION, event -> {
            if (!allValid.get()) {
                event.consume();
            }
        });

        filterList.setItems(FILTERS);
        filterList.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            onFilterSelect(newVal);
        });
        filterList.getSelectionModel().selectFirst();

        filterPath.textProperty().addListener((obs, old, newVal) -> {
            pathExists.set(comm.isPathRegistered(newVal));
            if (selectedHandler != null) {
                selectedHandler.setCurrentPath(newVal);
            }
        });

        dialog.setTitle("Create Filter");

    }

    public void showDialog() {
        Platform.runLater(() -> filterPath.requestFocus());

        Optional<ButtonType> opt = dialog.showAndWait();
        if (opt.isPresent() && opt.get().getButtonData() == ButtonData.OK_DONE) {
            onOk();
        }
    }

    private void onFilterSelect(FilterType filter) {
        borderPane.setCenter(FilterFactory.getFilterView(filter).getView());
        selectedHandler = FilterFactory.getFilterHandler(filter);
        pathExists.set(comm.isPathRegistered(filterPath.getText()));
        allValid.bind(selectedHandler.getValidPath().and(selectedHandler.getValidSettings()).and(pathExists.not()));
        filterDesc.setText(FilterFactory.getDescription(filter));
        selectedHandler.setCurrentPath(filterPath.getText());
    }

    private void onOk() {
        PathFilter<?> filter = selectedHandler.createFilter();
        filter.setPath(filterPath.getText());
        comm.getFilters().add(filter);
        allValid.unbind();

    }

    public void setFilterPath(String path) {
        filterPath.setText(path);
    }

}
