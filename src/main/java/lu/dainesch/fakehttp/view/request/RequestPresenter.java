
package lu.dainesch.fakehttp.view.request;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javax.inject.Inject;
import lu.dainesch.fakehttp.server.CommListener;
import lu.dainesch.fakehttp.server.data.RecordedRequest;
import lu.dainesch.fakehttp.view.common.UIUtils;


public class RequestPresenter {

    @Inject
    private CommListener listener;

    @FXML
    private ListView<RecordedRequest> requestList;

    @FXML
    public void initialize() {

        UIUtils.addAutoScroll(requestList);
        requestList.setItems(listener.getRequests());
        requestList.setCellFactory((param) -> {
            return new RequestListCell();
        });
        requestList.getSelectionModel().selectedItemProperty().addListener((obs, old, newValue) -> {
            listener.getSelectedRequest().set(newValue);
        });

    }

}
