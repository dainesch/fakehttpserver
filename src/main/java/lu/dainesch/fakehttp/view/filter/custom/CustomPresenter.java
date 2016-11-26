package lu.dainesch.fakehttp.view.filter.custom;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lu.dainesch.fakehttp.server.filters.CustomFilter;
import lu.dainesch.fakehttp.view.ResponseCode;
import lu.dainesch.fakehttp.view.ResponseType;
import lu.dainesch.fakehttp.view.filter.FilterHandler;


public class CustomPresenter extends FilterHandler<CustomFilter> {

    private static final ObservableList<ResponseCode> CODES = FXCollections.observableArrayList(
            ResponseCode.OK, ResponseCode.NO_CONTENT,
            ResponseCode.BAD_REQUEST, ResponseCode.UNAUTHORIZED, ResponseCode.FORBIDDEN, ResponseCode.SERVER_ERROR);

    private static final ObservableList<ResponseType> TYPES = FXCollections.observableArrayList(ResponseType.HTML, ResponseType.JAVASCRIPT, ResponseType.XML, ResponseType.JSON,
            ResponseType.TEXT, ResponseType.CUSTOM);

    @FXML
    private ChoiceBox<ResponseCode> statusChoice;
    @FXML
    private TextArea descArea;
    @FXML
    private ChoiceBox<ResponseType> contentChoice;
    @FXML
    private TextField contentField;
    @FXML
    private TextArea bodyArea;

    @FXML
    public void initialize() {
        contentField.setVisible(false);

        statusChoice.setItems(CODES);
        statusChoice.getSelectionModel().selectedItemProperty().addListener((obs, old, newValue) -> {
            chooseCode(newValue);
        });
        statusChoice.getSelectionModel().selectFirst();

        contentChoice.setItems(TYPES);
        contentChoice.getSelectionModel().selectedItemProperty().addListener((obs, old, newValue) -> {
            chooseType(newValue);
        });
        contentChoice.getSelectionModel().selectFirst();

    }

    private void chooseType(ResponseType type) {
        contentField.setVisible(type == ResponseType.CUSTOM);
    }

    private void chooseCode(ResponseCode code) {
        descArea.setText(code.getDesc());
    }

    @Override
    public void readValuesForm(CustomFilter filter) {
        statusChoice.getSelectionModel().select(ResponseCode.fromCode(filter.getStatus()));
        contentChoice.getSelectionModel().select(ResponseType.fromType(filter.getContentType()));
        
        if (ResponseType.fromType(filter.getContentType()) ==ResponseType.CUSTOM) {
            contentField.setText(filter.getContentType());
        } else {
            contentField.setText("");
        }
    }

    @Override
    public void saveValuesTo(CustomFilter filter) {
        filter.setStatus(statusChoice.getSelectionModel().getSelectedItem().getCode());
        if (contentChoice.getSelectionModel().getSelectedItem()==ResponseType.CUSTOM) {
            filter.setContentType(contentField.getText());
        } else {
            filter.setContentType(contentChoice.getSelectionModel().getSelectedItem().getValue());
        }
        filter.setBody(bodyArea.getText());
    }

    @Override
    public CustomFilter createFilter() {
        CustomFilter filter = new CustomFilter();
        saveValuesTo(filter);
        return filter;
    }

}
