package cz.cvut.fel.pjv;

import static cz.cvut.fel.pjv.Data.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * This class is responsible for making basic components of GUI,
 * main menu of the game, buttons of this menu (by using help
 * ButtonsMaker class) and set some of these buttons on action. 
 * @author shchesof
 */
public class ViewManager {
    
    private final AnchorPane root;
    private final Scene scene;
    private final Stage stage;
    private Map map;
    private UserInputHandler userInputHandler;
    private final HelpSubscene subScene; // for the rules
    private final ButtonsMaker buttonsMaker;
    private final Button playButton, loadButton;
    private Canvas draw; // to draw the map
    private boolean savedGame;
    private final GameLoop gameLoop;

    /**
     * Creates basic objects of the GUI - root, scene, SubScene,
     * buttons of main menu. Sets buttons on action. Sets GameLoop to new game
     * condition if newGame is true. Otherwise sets it to loaded game condition.
     * @param stage
     * @param newGame - true if new game must be created, false if game
     * must be loaded
     * @param gameLoop
     */
    public ViewManager(Stage stage, boolean newGame, GameLoop gameLoop) {
        this.stage = stage;
        root = new AnchorPane();
        subScene = new HelpSubscene();
        scene = new Scene(root, STAGE_WIDTH, STAGE_HEIGHT);
        buttonsMaker = new ButtonsMaker(root, subScene);
        scene.getStylesheets().add(MAIN_STYLE_PATH);
        root.getChildren().add(CanvasDrawer.getBackground());
        playButton = buttonsMaker.getPlayButton();
        loadButton = buttonsMaker.getLoadButton();
        stage.setScene(scene);
        stage.setTitle("GOLD MINER v.1.0");
        stage.setResizable(false);
        this.gameLoop = gameLoop;
        this.gameLoop.setNewGame(newGame);
        setPlayOnAction();
        setLoadOnAction();
    }
    
    private void setPlayOnAction() {
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    savedGame = false;
                    startGame(!savedGame);
                    putOnHandler();
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(ViewManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });
    }
    
    private void setLoadOnAction() {
        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    savedGame = true;
                    startGame(!savedGame);
                    putOnHandler();
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(ViewManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });
    }
    
    // draw map, init all the objects for the game for the new game if
    // newGame is true, if it is false - init objects for the loaded game
    private void startGame(boolean newGame) throws IOException, ClassNotFoundException {
        root.getChildren().clear();
        if (newGame)
            gameLoop.initMap();
        else
            gameLoop.loadGame();
        map = gameLoop.getMap();
        draw = gameLoop.getDraw();
        root.getChildren().add(draw);
        scene.getStylesheets().clear();
    }
    
    // activate keys for user handler
    private void putOnHandler() throws IOException {
        userInputHandler = new UserInputHandler(map, root, scene, stage,
                savedGame, gameLoop);
        scene.setOnKeyPressed(userInputHandler);
        scene.setOnKeyReleased(userInputHandler);
    }
    
    /**
     *
     * @return stage
     */
    public Stage getStage() {
        return stage;
    } 
    
}
