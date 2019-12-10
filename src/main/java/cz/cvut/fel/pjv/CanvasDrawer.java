package cz.cvut.fel.pjv;

import static cz.cvut.fel.pjv.Data.*;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Secondary class to draw graphic components of the game.
 * @author shchesof
 */
public class CanvasDrawer {

    private static Canvas background, gameOver, youWin;
    
    private static void drawBackground() {
        // background of the main menu
        background = new Canvas(STAGE_WIDTH, STAGE_HEIGHT);
        Image back = new Image(BACKGROUND_PATH);
        GraphicsContext gc = background.getGraphicsContext2D();
        gc.drawImage(back, 0, 0, STAGE_WIDTH, STAGE_HEIGHT);
        background.setLayoutX(0);
        background.setLayoutY(0);
    }
    
    /**
     * Draws 'GameOver' background.
     */
    public static void drawGameOver() {
        gameOver = new Canvas(STAGE_WIDTH, STAGE_HEIGHT);
        Image go = new Image(GAME_OVER_PATH);
        GraphicsContext gc = gameOver.getGraphicsContext2D();
        gc.drawImage(go, 0, 0, STAGE_WIDTH, STAGE_HEIGHT);
        gameOver.setLayoutX(0);
        gameOver.setLayoutY(0);
    }
    
    /**
     * Draws 'YouWin' background.
     */
    public static void drawYouWin() {
        youWin = new Canvas(STAGE_WIDTH, STAGE_HEIGHT);
        Image go = new Image(YOU_WIN_PATH);
        GraphicsContext gc = youWin.getGraphicsContext2D();
        gc.drawImage(go, 0, 0, STAGE_WIDTH, STAGE_HEIGHT);
        youWin.setLayoutX(0);
        youWin.setLayoutY(0);
    }

    /**
     *
     * @return background for the main menu
     */
    public static Canvas getBackground() {
        drawBackground();
        return background;
    }

    /**
     *
     * @return 'YouWin' drawing
     */
    public static Canvas getYouWin() {
        drawYouWin();
        return youWin;
    }
    
    /**
     *
     * @return 'GameOver' drawing
     */
    public static Canvas getGameOver() {
        drawGameOver();
        return gameOver;
    }

    /**
     * Draws monsters from the list on the map by GraphicsContext.
     * @param graphicsContext - already created in the Map class
     * @param monsters - list of all Monster objects which are currently in the game
     * @param monsterImg - image of Monster object
     * @param empty_cell - image of empty cell
     */
    public static void drawMonsters(GraphicsContext graphicsContext, List<Monster> monsters,
            Image monsterImg, Image empty_cell) {
        for (Monster m : monsters) {
            monsterImg = m.getImage();
            graphicsContext.drawImage(empty_cell, m.getX()*NUMBER_OF_PIXELS, m.getY()*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
            graphicsContext.drawImage(monsterImg, m.getX()*NUMBER_OF_PIXELS, m.getY()*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
        }
    }

    /**
     * Draws hearts on the map by GraphicsContext.
     * @param graphicsContext - already created in the Map class
     * @param health - current Hero's health
     * @param heart - image of heart
     */
    public static void drawHealth(GraphicsContext graphicsContext, int health, Image heart) {
        for (int i = (int)LIVES_START_X; i < LIVES_START_X+health*NUMBER_OF_PIXELS; i+=NUMBER_OF_PIXELS) {
            graphicsContext.drawImage(heart, i, LIVES_START_Y, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
        }
        
    }

    /**
     * Draws elements of Hero's inventory from the list by GraphicsContext.
     * @param graphicsContext - already created in the Map class
     * @param inventory - current Hero's inventory
     * @param key - image of key
     * @param bomb - image of bomb
     */
    public static void drawInventory(GraphicsContext graphicsContext, List<Character> inventory,
            Image key, Image bomb) {
        int invIdx = 0;
        if (inventory.size() > 0) {
            for (int i = (int)INVENTORY_START_X; i < INVENTORY_START_X + inventory.size()*NUMBER_OF_PIXELS; i+= NUMBER_OF_PIXELS) {
                if (inventory.get(invIdx) == 'K')
                    graphicsContext.drawImage(key, i, INVENTORY_START_Y, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                else if (inventory.get(invIdx) == 'B')
                    graphicsContext.drawImage(bomb, i, INVENTORY_START_Y, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                invIdx++;
            }
        }
    }
  
}
