package lu.dainesch.fakehttp.view.detail;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javax.inject.Inject;
import lu.dainesch.fakehttp.server.CommListener;
import lu.dainesch.fakehttp.server.data.RecordedRequest;


public class DetailsPresenter {

    @Inject
    private CommListener listener;

    @FXML
    private TableView<KVTableModel> requestHeaders;

    @FXML
    private TableView<KVTableModel> responseHeaders;

    @FXML
    private TableView<KVTableModel> requestParams;

    @FXML
    private TextArea requestView;

    @FXML
    private TextArea responseView;

    @FXML
    public void initialize() {

        TableColumn nameCol1 = new TableColumn("Name");
        nameCol1.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol1.setCellFactory(TextFieldTableCell.forTableColumn());
        
        TableColumn valCol1 = new TableColumn("Value");
        valCol1.setCellValueFactory(new PropertyValueFactory<>("value"));
        valCol1.setCellFactory(TextFieldTableCell.forTableColumn());
        
        TableColumn nameCol2 = new TableColumn("Name");
        nameCol2.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol2.setCellFactory(TextFieldTableCell.forTableColumn());
        
        TableColumn valCol2 = new TableColumn("Value");
        valCol2.setCellValueFactory(new PropertyValueFactory<>("value"));
        valCol2.setCellFactory(TextFieldTableCell.forTableColumn());
        
        TableColumn nameCol3 = new TableColumn("Name");
        nameCol3.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol3.setCellFactory(TextFieldTableCell.forTableColumn());
        
        TableColumn valCol3 = new TableColumn("Value");
        valCol3.setCellValueFactory(new PropertyValueFactory<>("value"));
        valCol3.setCellFactory(TextFieldTableCell.forTableColumn());

        requestHeaders.getColumns().addAll(nameCol1, valCol1);
        responseHeaders.getColumns().addAll(nameCol2, valCol2);
        requestParams.getColumns().addAll(nameCol3, valCol3);

        listener.getSelectedRequest().addListener((obs, old, newValue) -> {
            setTableData(newValue);
        });

        requestHeaders.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        responseHeaders.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        requestParams.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setTableData(RecordedRequest req) {

        requestHeaders.getItems().clear();
        responseHeaders.getItems().clear();
        requestParams.getItems().clear();
        requestView.setText("");
        responseView.setText("");

        if (req != null) {
            req.getRequestHeaders().entrySet().stream().forEach((e) -> {
                requestHeaders.getItems().add(new KVTableModel(e.getKey(), e.getValue()));
            });
            req.getResponseHeaders().entrySet().stream().forEach((e) -> {
                responseHeaders.getItems().add(new KVTableModel(e.getKey(), e.getValue()));
            });
            req.getRequestParams().entrySet().stream().forEach((e) -> {
                requestParams.getItems().add(new KVTableModel(e.getKey(), e.getValue()));
            });
            requestView.setText(req.getRequestBody());
            responseView.setText(req.getResponseBody());
        }

    }

}
