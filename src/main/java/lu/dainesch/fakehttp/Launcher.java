package lu.dainesch.fakehttp;

import com.airhacks.afterburner.injection.Injector;
import java.util.Locale;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lu.dainesch.fakehttp.server.FakeHttp;
import lu.dainesch.fakehttp.view.MainView;
import lu.dainesch.fakehttp.view.common.UIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(FakeHttp.class);

    private final FakeHttpConfig config;
    private final FakeHttp http;
    private Stage stage;

    public Launcher() {
        config = new FakeHttpConfig();
        http = new FakeHttp(config);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Locale.setDefault(Locale.ENGLISH);
        
        Injector.setConfigurationSource(config::getInjectionValue);
        Injector.setLogger(s -> LOG.info(s));
        Injector.setModelOrService(Logger.class, LOG);
        Injector.setModelOrService(FakeHttp.class, http);
        Injector.setModelOrService(FakeHttpConfig.class, config);

        MainView appView = new MainView();
        Scene scene = new Scene(appView.getView(), 1000, 800);
        stage.setTitle("Fake Http Server");
        final String uri = getClass().getResource("global.css").toExternalForm();
        scene.getStylesheets().add(uri);
        stage.setScene(scene);
        UIUtils.setIcon(stage);
        
        if (config.getDouble(Constants.SCENEX)>=0) {
            stage.setX(config.getDouble(Constants.SCENEX));
            stage.setY(config.getDouble(Constants.SCENEY));
            stage.setWidth(config.getDouble(Constants.SCENEW));
            stage.setHeight(config.getDouble(Constants.SCENEH));
        }
        
        stage.show();
        this.stage = stage;
    }

    @Override
    public void stop() throws Exception {
        if (stage!=null) {
            config.putDouble(Constants.SCENEX,stage.getX());
            config.putDouble(Constants.SCENEY,stage.getY());
            config.putDouble(Constants.SCENEW,stage.getWidth());
            config.putDouble(Constants.SCENEH,stage.getHeight());
        }
        http.exit();
        Injector.forgetAll();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
