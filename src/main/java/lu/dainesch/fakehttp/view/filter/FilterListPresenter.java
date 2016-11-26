package lu.dainesch.fakehttp.view.filter;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.inject.Inject;
import lu.dainesch.fakehttp.server.CommListener;
import lu.dainesch.fakehttp.server.filters.PathFilter;


public class FilterListPresenter {

    @FXML
    private Button delFilterBut;
    @FXML
    private TableView<FilterTableModel> filterTable;

    @Inject
    private CommListener comm;

    @FXML
    public void initialize() {
        TableColumn nameCol = new TableColumn("Path");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("path"));
        TableColumn typeCol = new TableColumn("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        filterTable.getColumns().addAll(nameCol, typeCol);
        filterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        comm.getFilters().addListener((ListChangeListener.Change<? extends PathFilter> c) -> {
            if (c.next()) {
                filterTable.getItems().clear();
                this.comm.getFilters().forEach(pf -> {
                    filterTable.getItems().add(new FilterTableModel(pf));
                });

            }
        });
    }

    @FXML
    void onDeleteClicked(ActionEvent event) {
        FilterTableModel sel = filterTable.getSelectionModel().getSelectedItem();
        if (sel != null) {
            this.comm.getFilters().remove(sel.getFilter());
        }
    }

}
