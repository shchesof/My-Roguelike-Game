package cz.cvut.fel.pjv;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import static cz.cvut.fel.pjv.Data.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main game process is going on in this class. It analyzes user's input
 * (pressed keys) and 'moves' Sprite characters (Hero and Monster) on the map.
 * Program works with this map by using its 2d array representation. 
 * @author shchesof
 */
public class UserInputHandler implements EventHandler<KeyEvent> {
    
    private boolean keyIgnored, explodedBomb, endOfTheGame;
    private final Hero hero;
    private Map map;
    private char[][] mapArray;
    private final int[][] explosionCoords; // for monster to know where is explosion
    private Canvas draw;
    private AnchorPane root;
    private final List<Monster> monsters;
    private final Scene scene;
    private final Stage stage;
    private final List<Monster> toRemove;
    private GameLoop gameLoop;

    /**
     * 
     * @param map - Map object to update
     * @param root - current layout root
     * @param scene
     * @param stage
     * @param savedGame - true if it is needed to load saved game, false if
     * it is a new game
     * @param gameLoop
     * @throws IOException
     */
    public UserInputHandler(Map map, AnchorPane root, Scene scene, Stage stage,
            boolean savedGame, GameLoop gameLoop) throws IOException {
        this.map = map;
        this.root = root;
        this.scene = scene;
        this.stage = stage;
        this.gameLoop = gameLoop;
        try {
            mapArray = map.load_map(MAP_PATH);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        if (savedGame == false) hero = map.getHero(); // if it is new game, hero will
                                                      // be created by Map object
        else hero = gameLoop.getHero(); // if it is loaded game, hero will be created
                                        // by GameLoop object
        monsters = map.getMonsters(); // monsters are created by Map object
        explosionCoords = new int[9][2];
        toRemove = new ArrayList<>(); // list to remove dead monsters
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED && !keyIgnored && !(endOfTheGame)) {
            if (event.getCode() == KeyCode.W) {
                heroMove('W');
            }
            else if (event.getCode() == KeyCode.S) {
                heroMove('S');
            }
            else if (event.getCode() == KeyCode.D) {
                heroMove('D');
            }
            else if (event.getCode() == KeyCode.A) {
                heroMove('A');
            }
            else if (event.getCode() == KeyCode.E) {
                heroMove('B');
            }
        } else if (event.getEventType() == KeyEvent.KEY_RELEASED && keyIgnored) {
            keyIgnored = false; // prevent autorepeat
        }
    }
    
    private void heroMove(char eventChar) {
    	// check if hero is dead
        if (hero.isMustDie()) {
            endOfTheGame = true;
            gameLoop.endGame(root, scene, false, map);
            return;
        }
        if (eventChar == 'W') {
            hero.move(mapArray, 0, -1, this);
            keyIgnored = true;
        }
        else if (eventChar == 'S') {
            hero.move(mapArray, 0, 1, this);
            keyIgnored = true;
        }
        else if (eventChar == 'D') {
            hero.move(mapArray, 1, 0, this);
            keyIgnored = true;
        }
        else if (eventChar == 'A') {
            hero.move(mapArray, -1, 0, this);
            keyIgnored = true;
        }  
        else if (eventChar == 'B') {
            try {
                boolean bombIsPut = hero.putBomb(map); // this variable is not
                // used, its is needed just to call boolean function putBomb()
            } catch (IOException ex) {
                Logger.getLogger(UserInputHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            keyIgnored = true;
        }
        // draw normal map again without explosion
        if (explodedBomb) explodeOff();
        if (hero.isBombIsPut()) {
            // after 3 moves since bomb is put it must go off
            if (hero.getBombCount() == 3) {
                try {
                    bombExplode();
                } catch (IOException ex) {
                    Logger.getLogger(UserInputHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else hero.increaseBombCount();
        }
        // check if it is needed to end the game
        if (endOfTheGame) {
            gameLoop.endGame(root, scene, true, map);
            return;
        }
        // monsters move
        for (Monster m : monsters)
            m.move(monsters, mapArray, explosionCoords, this);
        // hero interacts with monsters
        heroAndMonsterInteraction();
        // draw hero if he changes his position on the map
        if (eventChar != 'B')
            mapArray[hero.getY()][hero.getX()] = 'H';
        // removing dead monsters
        monsters.removeAll(toRemove);
        toRemove.clear();
        
        if (hero.isMustDie()) {
            // explosion is drawn instead of the hero
            mapArray[hero.getY()][hero.getX()] = 'E';
        }
        // check if hero is dead
        if (hero.getHealth() == 0) {
            hero.setMustDie(true);
            // skull is drawn on the place of dead hero
            mapArray[hero.getY()][hero.getX()] = 'Z';
        }
        try {
            map.redrawMap(mapArray);
        } catch (IOException ex) {
            Logger.getLogger(UserInputHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        // update hero in Map object (need to draw him and his inventory and health)
        map.setHero(hero);
        draw = map.drawMap();
        root.getChildren().add(draw);
        // update button 'save and quit'
        ButtonsMaker buttonsMaker = new ButtonsMaker(stage, gameLoop);
        root.getChildren().add(buttonsMaker.getSaveButton(hero, monsters));
        keyIgnored = true;
    }
    
    // activate explosion
    private void bombExplode() throws IOException {
        int x = hero.getBombCoords()[0];
        int y = hero.getBombCoords()[1];
        explosionCoords[0][0] = x;
        explosionCoords[0][1] = y;
        mapArray[y][x] = 'E';
        for (int i = 0; i < DIRECTIONS.length; i++) {
            // bombs don't damage walls and doors
            if ((mapArray[y+DIRECTIONS[i][1]][x+DIRECTIONS[i][0]] != 'X') &&
                    (mapArray[y+DIRECTIONS[i][1]][x+DIRECTIONS[i][0]] != 'D')) {
                // draw black cell as explosion
                mapArray[y+DIRECTIONS[i][1]][x+DIRECTIONS[i][0]] = 'E';
                if ((x+DIRECTIONS[i][0] == hero.getX()) &&
                        (y+DIRECTIONS[i][1] == hero.getY())) {
                    hero.setMustDie(true); // hero can also die because of the bomb
                }
                explosionCoords[i+1][0] = x+DIRECTIONS[i][0];
                explosionCoords[i+1][1] = y+DIRECTIONS[i][1];
            }
        }
        // start bomb's 'timer'
        hero.setBombCount(0);
        hero.setBombIsPut(false);
        explodedBomb = true;
    }
    
    // deactivate explosion
    private void explodeOff() {
        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                // draw empty cells again
                if (mapArray[i][j] == 'E')
                    mapArray[i][j] = '.';
            }
        }
        // stop bomb's 'timer'
        explodedBomb = false;
        // return keys on the map
        for (List<Integer> keys : map.getKeyCoords()) {
            mapArray[keys.get(1)][keys.get(0)] = 'K';
        }
    }
    
    private void heroAndMonsterInteraction() {
        for (Monster m: monsters) {
            // if monster is on adjacent cell to the hero (or on the same)
            if ((Math.abs(m.getX() - hero.getX()) <= 1) &&
                    (Math.abs(m.getY() - hero.getY()) <= 1)) {
                hero.decreaseHealth(1); // hero is hurted
                if (hero.hasSword()) { // if hero has sword, monster is also hurted
                    m.decreaseHealth(1);
                    m.redraw(HURTED_MONSTER_PATH);
                    // if monster is killed
                    if (m.getHealth() == 0) {
                        Random rnd = new Random();
                        int n = rnd.nextInt(5);
                        if (!((m.getX() == hero.getX()) && (m.getY() == hero.getY()))) {
                            // there is 80% possibility that instead of killed monster
                            // there will be a heart to heal hero
                            if ((n == 0) || (n == 1) || (n == 2) || (n == 3))
                                mapArray[m.getY()][m.getX()] = 'L';
                            else mapArray[m.getY()][m.getX()] = '.';
                        }
                        else {
                            // if hero will kill monster and they were
                            // on the same cell, he will get one heart
                            hero.increaseHealth(1);
                        }
                        toRemove.add(m); // monster 'dies'   
                    }
                }
            }
        }  
    }
    
    /**
     * Removes picked keys from the map. Sets removed keys coordinations
     * of each Monster object to null.
     */
    public void removeKeysFromMap() {
        int h = 0;
        while (h < map.getKeyCoords().size()) {
            if ((map.getKeyCoords().get(h).get(0) == hero.getX()) && (map.getKeyCoords().get(h).get(1) == hero.getY()))
                map.removeKeyCoords(map.getKeyCoords().get(h));
            h++;
        }
        for (Monster m : monsters) {
            if (m.getKeyCoords() != null)
                if ((m.getKeyCoords().get(0) == hero.getX()) &&
                        (m.getKeyCoords().get(1) == hero.getY()))
                    // remove picked key from the monster which guards it
                    m.setKeyCoords(null);
        }
    }
    
    /**
     * Adds Monster objects which is needed to delete
     * to the list which will be deleted.
     * @param monster to delete
     */
    public void addDeadMonster(Monster monster) {
        toRemove.add(monster);
    }

    /**
     *
     * @param endOfTheGame - true if game must be ended
     */
    public void setEndOfTheGame(boolean endOfTheGame) {
        this.endOfTheGame = endOfTheGame;
    }
    
}
