package lu.dainesch.fakehttp.view.request;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import lu.dainesch.fakehttp.server.data.RecordedRequest;
import lu.dainesch.fakehttp.view.ResponseType;


public class RequestCellPresenter implements Initializable {

    @FXML
    private Label typeLabel;

    @FXML
    private Label statusLabel;
    @FXML
    private Label pathLabel;

    private RecordedRequest request;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setRequest(RecordedRequest request) {
        this.request = request;
        statusLabel.setText(String.valueOf(request.getStatus()));
        pathLabel.setText(request.getPath());
        typeLabel.setText(ResponseType.fromType(request.getContentType()).getDisplay());
        setColor();
    }

    private void setColor() {
        switch (request.getStatus()) {
            case 200:
                statusLabel.setBackground(new Background(new BackgroundFill(Color.DODGERBLUE, new CornerRadii(5), Insets.EMPTY)));
                break;
            case 404:
                statusLabel.setBackground(new Background(new BackgroundFill(Color.BURLYWOOD, new CornerRadii(5), Insets.EMPTY)));
                break;
            case 500:
                statusLabel.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(5), Insets.EMPTY)));
                break;
            default:
                statusLabel.setBackground(new Background(new BackgroundFill(Color.GREY, new CornerRadii(5), Insets.EMPTY)));
                break;
        }
    }

}
