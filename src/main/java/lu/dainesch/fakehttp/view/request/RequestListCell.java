package lu.dainesch.fakehttp.view.request;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import lu.dainesch.fakehttp.server.data.RecordedRequest;
import lu.dainesch.fakehttp.view.filter.create.CreateFilterView;


public class RequestListCell extends ListCell<RecordedRequest> {

    private RequestCellPresenter pres;
    private ContextMenu menu;

    @Override
    protected void updateItem(RecordedRequest item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty && item != null) {
            RequestCellView view = new RequestCellView();
            pres = (RequestCellPresenter) view.getPresenter();
            pres.setRequest(item);
            setGraphic(view.getView());

            MenuItem createItem = new MenuItem("Create Filter...");
            createItem.setOnAction((event) -> {
                showCreateFilterView(item);
            });

            menu = new ContextMenu(createItem);

            setContextMenu(menu);
        } else {
            setGraphic(null);
        }
    }

    @Override
    public void updateSelected(boolean selected) {
        super.updateSelected(selected);
    }

    private void showCreateFilterView(RecordedRequest item) {
        CreateFilterView view = new CreateFilterView();
        view.getPresenter().setFilterPath(item.getPath());
        view.show();

    }

}
