package lu.dainesch.fakehttp.view.filter.fileserve;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import lu.dainesch.fakehttp.server.filters.FileDownloadFilter;
import lu.dainesch.fakehttp.view.filter.FilterHandler;


public class FileServePresenter extends FilterHandler<FileDownloadFilter> {

    @FXML
    private TextField pathField;


    @FXML
    public void initialize() {
        pathField.textProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                Path p = Paths.get(newVal);
                settingsValid.set(Files.exists(p) && Files.isDirectory(p));
            } else {
                settingsValid.set(false);
            }
        });
    }

    @FXML
    void onPathSelect(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        if (pathField.getText() != null && !pathField.getText().isEmpty()) {
            directoryChooser.setInitialDirectory(new File(pathField.getText()));
        }
        File selectedDirectory = directoryChooser.showDialog(pathField.getScene().getWindow());
        if (selectedDirectory != null) {
            pathField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @Override
    public void readValuesForm(FileDownloadFilter filter) {
        pathField.setText(filter.getBasePath().toString());
    }

    @Override
    public void saveValuesTo(FileDownloadFilter filter) {
        filter.setBasePath(Paths.get(pathField.getText()));
    }

    @Override
    public FileDownloadFilter createFilter() {
        FileDownloadFilter filter = new FileDownloadFilter();
        saveValuesTo(filter);
        return filter;
    }

    @Override
    protected boolean validatePath(String path) {
        return super.validatePath(path) && !path.contains("{") && path.endsWith("/*");
    }
    
    

}
