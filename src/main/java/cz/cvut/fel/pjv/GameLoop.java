package cz.cvut.fel.pjv;

import static cz.cvut.fel.pjv.Data.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shchesof
 */
public class GameLoop {
    
    private Map map;
    private char[][] mapArray;
    private Canvas draw; // drawing map
    private ButtonsMaker buttonsMaker;
    private final Stage stage;
    private Hero hero;
    private boolean newGame; // if it is new or just loaded game
    private List<Monster> monsters;
    private int numberOfSavedMonsers;

    /**
     *
     * @param stage
     */
    public GameLoop(Stage stage) {
        this.stage = stage;
        newGame = true;
    }

    /**
     *
     * @param newGame - true if new game must be created, false if game
     * must be loaded
     */
    public void setNewGame(boolean newGame) {
       this.newGame = newGame;
    }
    
    /**
     * Initialize Map for the new game.
     * @throws IOException
     */
    public void initMap() throws IOException {
        map = new Map(true);
        reloadMap();
    }
    
    // map on the start of the game
    private void reloadMap() throws IOException {
        mapArray = new char[MAP_HEIGHT][MAP_WIDTH];
        mapArray = map.load_map(NEWGAME_MAP_PATH);
        map.redrawMap(mapArray);
        draw = map.drawMap();
    }
    
    /**
     * Reloads Map to the initial state, clears root, draws GameOver
     * or YouWin image, creates PlayAgain button.
     * @param root - current layout root
     * @param scene - current scene
     * @param win - true if hero won, false if he died
     * @param map - Map object to reload
     */
    public void endGame(AnchorPane root,  Scene scene, boolean win, Map map) {
        // reload map to new game map
        mapArray = new char[MAP_HEIGHT][MAP_WIDTH];
        try {
            mapArray = map.load_map(NEWGAME_MAP_PATH);
            map.redrawMap(mapArray);
        } catch (IOException ex) {
            Logger.getLogger(GameLoop.class.getName()).log(Level.SEVERE, null, ex);
        }
        root.getChildren().clear();
        // if hero loses
        if (win == false)
            root.getChildren().add(CanvasDrawer.getGameOver());
        // if hero wins
        else root.getChildren().add(CanvasDrawer.getYouWin());
        // add 'play again' button
        buttonsMaker = new ButtonsMaker(stage, this);
        root.getChildren().add(buttonsMaker.getPlayAgain());
    }
    
    /**
     * Saves serialized Hero and monsters from the list in the files.
     * @param hero - current Hero object to save
     * @param monsters - list of all Monster objects which are currently in the game
     * @throws IOException
     */
    public void saveGame(Hero hero, List<Monster> monsters) throws IOException {
        // save hero as an object in the file
        FileOutputStream fos = new FileOutputStream(HERO_INFO_PATH);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(hero);
        // save monsters as objects
        numberOfSavedMonsers = 0;
        for (Monster m : monsters) {
            (new ObjectOutputStream
        (new FileOutputStream(MONSTERS_INFO_PATH+numberOfSavedMonsers+".ser"))).writeObject(m);
            numberOfSavedMonsers++;
        }
    }
    
    /**
     * If there is no saved game, initializes Map and Hero as new objects.
     * Otherwise loads serialized Hero and monsters from files. Sets the Map.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void loadGame() throws IOException, ClassNotFoundException {
        mapArray = new char[MAP_HEIGHT][MAP_WIDTH];
        map = new Map(newGame);
        if (newGame) {
            mapArray = map.load_map(NEWGAME_MAP_PATH);
            hero = map.getHero();
        }
        else {
            // load the last version of the map
            mapArray = map.load_map(MAP_PATH);
            readMonsterInfo();
            // upload monsters in the Map object to draw them correctly
            map.setMonsters(monsters);
            readHeroInfo();
        }
        map.redrawMap(mapArray);
        // upload hero in the Map object to draw him correctly
        map.setHero(hero);
        draw = map.drawMap();
    }
    
    // load hero from the file he is saved in
    private void readHeroInfo() throws IOException, ClassNotFoundException {
        FileInputStream fin = new FileInputStream(HERO_INFO_PATH);
        ObjectInputStream ois = new ObjectInputStream(fin);
        hero = new Hero(null, 0);
        hero = (Hero)ois.readObject();
        // set hero's Image
        if (hero.hasSword()) hero.redraw(HERO_SWORD_PATH);
        else hero.redraw(HERO_PATH);
    }
    
    // load monsters from the file they are saved in
    private void readMonsterInfo() throws IOException, ClassNotFoundException {
        monsters = new ArrayList<>();
        for (int i = 0; i < numberOfSavedMonsers; i++) {
            FileInputStream fin = new FileInputStream(MONSTERS_INFO_PATH+i+".ser");
            ObjectInputStream ois = new ObjectInputStream(fin);
            Monster monster = (Monster)ois.readObject();
            if (monster.getHealth()<MONSTER_LIVES) monster.redraw(HURTED_MONSTER_PATH);
            else monster.redraw(MONSTER_PATH);
            monsters.add(monster);
        }
    }

    /**
     *
     * @return drawing of the map
     */
    public Canvas getDraw() {
        return draw;
    }

    /**
     *
     * @return current Map instance
     */
    public Map getMap() {
        return map;
    }

    /**
     *
     * @return current Hero instance
     */
    public Hero getHero() {
        return hero;
    }

}

