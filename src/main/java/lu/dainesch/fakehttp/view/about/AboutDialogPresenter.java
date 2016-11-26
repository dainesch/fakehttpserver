package lu.dainesch.fakehttp.view.about;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import lu.dainesch.fakehttp.view.common.UIUtils;


public class AboutDialogPresenter {

    @FXML
    private BorderPane borderPane;
    @FXML
    private ImageView logoView;

    @FXML
    public void initialize() {
        
        logoView.setImage(new Image("/icon.png"));
        
        Dialog diag = new Dialog();
        diag.getDialogPane().setContent(borderPane);
        diag.setResizable(false);
        UIUtils.setIcon(diag);

        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);

        diag.getDialogPane().getButtonTypes().addAll(closeButton);

        diag.setTitle("About");

        diag.show();

    }

}
