package cz.cvut.fel.pjv;

import static cz.cvut.fel.pjv.Data.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Secondary class to make all buttons of the game. Attention, not
 * all of them are set on action.
 * @author shchesof
 */
public class ButtonsMaker {
    
    private final AnchorPane root;
    private Button button, butExit, butHelp, playAgain, loadGame, saveGame;
    private final HelpSubscene subscene;
    private Stage stage;
    private GameLoop gameLoop;
    // style of the button
    private final String buttonStyle = "-fx-font-weight: bold; -fx-border-width: 2;"
            + "-fx-font-size: 30pt; -fx-background-color: aquamarine;" +
            "-fx-text-fill: black;";
    // style of the button when it's pressed on
    private final String buttonHoverStyle = buttonStyle + "-fx-background-color: green";

    /**
     *
     * @param root - current layout root
     * @param subscene - already created HelpSubscene object
     */
    public ButtonsMaker(AnchorPane root, HelpSubscene subscene) {
        this.subscene = subscene;
        this.root = root;
    }
    
    /**
     *
     * @param stage
     * @param gameLoop
     */
    public ButtonsMaker(Stage stage, GameLoop gameLoop) {
        this.root = null;
        this.subscene = null;
        this.stage = stage;
        this.gameLoop = gameLoop;
    }
    
    private void createExitButton() {
        // exit from the rules
        butExit = new Button("EXIT");
        butExit.setLayoutX(START_BUTTON_X);
        butExit.setLayoutY(START_BUTTON_Y+200);
        butExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                root.getChildren().removeAll(subscene, butExit);
                root.getChildren().add(butHelp);
            }
        });
        root.getChildren().add(butExit);
    }

    /**
     * Returns 'play' button and creates 'help' button.
     * @return 'play' button
     */
    public Button getPlayButton() {
        button = new Button("PLAY");
        createHelpButton();
        button.setLayoutX(START_BUTTON_X);
        button.setLayoutY(START_BUTTON_Y);
        root.getChildren().addAll(button, butHelp);
        return button;
    }
    
    private void createHelpButton() {
        // to open rules of the game
        butHelp = new Button("HELP");
        butHelp.setLayoutX(START_BUTTON_X);
        butHelp.setLayoutY(START_BUTTON_Y+200);
        butHelp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                subscene.setLayoutX(150);
                subscene.setLayoutY(100);
                root.getChildren().add(subscene);
                root.getChildren().remove(butHelp);
                createExitButton();
            }
        });
    }
    
    /**
     *
     * @return 'exit' button
     */
    public Button getButExit() {
        createExitButton();
        return butExit;
    }
    
    /**
     * Creates playAgain button and sets in on action.
     * @return playAgain button
     */
    public Button getPlayAgain() {
        playAgain = new Button("Play again");
        playAgain.setLayoutX(START_BUTTON_X-50);
        playAgain.setLayoutY(START_BUTTON_Y+200);
        playAgain.setStyle(buttonStyle);
        // set style
        playAgain.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playAgain.setStyle(buttonHoverStyle);
            }
        });
        playAgain.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playAgain.setStyle(buttonStyle);
            }
        });
        // set on action
        playAgain.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ViewManager vw = new ViewManager(stage, true, gameLoop);
                stage = vw.getStage();
                stage.show();
            }
        });
        return playAgain;
    }
    
    /**
     * Returns 'load' button.
     * @return 'load' button
     */
    public Button getLoadButton() {
        loadGame = new Button("LOAD");
        loadGame.setLayoutX(START_BUTTON_X);
        loadGame.setLayoutY(START_BUTTON_Y+100);
        root.getChildren().add(loadGame);
        return loadGame;
    }
    
    /**
     * Creates 'save' button and sets it on action.
     * @param hero - current Hero object
     * @param monsters - list of all Monster objects which are currently in the game
     * @return 'save' button
     */
    public Button getSaveButton(final Hero hero, final List<Monster> monsters) {
        saveGame = new Button("SAVE AND QUIT");
        saveGame.setLayoutX(STAGE_WIDTH-120);
        saveGame.setLayoutY(0);
        saveGame.setMaxHeight(30);
        saveGame.setPrefHeight(30);
        saveGame.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    gameLoop.saveGame(hero, monsters);
                    ViewManager vw = new ViewManager(stage, false, gameLoop);
                    stage = vw.getStage();
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(ButtonsMaker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        return saveGame;
    }
}
