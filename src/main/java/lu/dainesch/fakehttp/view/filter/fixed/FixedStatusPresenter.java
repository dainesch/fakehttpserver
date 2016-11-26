package lu.dainesch.fakehttp.view.filter.fixed;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import lu.dainesch.fakehttp.server.filters.FixedStatusFilter;
import lu.dainesch.fakehttp.view.ResponseCode;
import lu.dainesch.fakehttp.view.filter.FilterHandler;


public class FixedStatusPresenter extends FilterHandler<FixedStatusFilter> {

    private static final ObservableList<ResponseCode> CODES = FXCollections.observableArrayList(
            ResponseCode.OK, ResponseCode.NO_CONTENT, ResponseCode.MOVED_PERM, ResponseCode.FOUND, ResponseCode.SEE_OTHER,
            ResponseCode.BAD_REQUEST, ResponseCode.UNAUTHORIZED, ResponseCode.FORBIDDEN, ResponseCode.NOT_FOUND, ResponseCode.SERVER_ERROR);

    @FXML
    private ChoiceBox<ResponseCode> statusChoice;
    @FXML
    private TextArea descArea;
    @FXML
    private BorderPane redirPanel;
    @FXML
    private TextField redirField;
    
    
    @FXML
    public void initialize() {
        redirPanel.visibleProperty().set(false);
        
        statusChoice.setItems(CODES);
        statusChoice.getSelectionModel().selectedItemProperty().addListener((obs, old, newValue) -> {
            chooseCode(newValue);
        });
        statusChoice.getSelectionModel().selectFirst();
        

    }

    private void chooseCode(ResponseCode code) {
        descArea.setText(code.getDesc());

        switch (code) {
            case MOVED_PERM:
            case FOUND:
            case SEE_OTHER:
                redirPanel.visibleProperty().set(true);
                break;
            default:
                redirPanel.visibleProperty().set(false);
                break;
        }
    }

    @Override
    public void readValuesForm(FixedStatusFilter filter) {
        statusChoice.getSelectionModel().select(ResponseCode.fromCode(filter.getStatus()));
        if (filter.getRedirPath() != null) {
            redirField.setText(filter.getRedirPath());
        } else {
            redirField.setText("");
        }
    }

    @Override
    public void saveValuesTo(FixedStatusFilter filter) {
        filter.setStatus(statusChoice.getSelectionModel().getSelectedItem().getCode());
        if (redirField.getText().trim().isEmpty()) {
            filter.setRedirPath(null);
        } else {
            filter.setRedirPath(redirField.getText().trim());
        }
    }

    @Override
    public FixedStatusFilter createFilter() {
        FixedStatusFilter filter = new FixedStatusFilter();
        saveValuesTo(filter);
        return filter;
    }

}
