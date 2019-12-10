package cz.cvut.fel.pjv;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;

// ИСПРАВЬ ПРОЦЕСС ПОЛОЖЕННОЙ БОМБЫ ПОСЛЕ ЗАГРУЗКИ ИГРЫ

/**
 *
 * @author shchesof
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage = new Stage();
        GameLoop gameLoop = new GameLoop(stage);
        ViewManager vw = new ViewManager(stage, true, gameLoop);
        stage.show();
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
