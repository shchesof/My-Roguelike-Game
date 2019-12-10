package cz.cvut.fel.pjv;

import static cz.cvut.fel.pjv.Data.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * This class draws main process of the game as a map.
 * @author shchesof
 */
public final class Map {
    
    private Hero hero;
    private Image wall, gold, key, sword, door, emptyCell, heroImg, heart,
            monsterImg, bomb, exploded, skull;
    private final List<List<Integer>> keyCoords; // coordinations of all keys
    private int idx;
    private List<Monster> monsters;
    private char[][] mapArray;
    private boolean newGame;

    /**
     *
     * @param newGame - true if new game must be created, false if game
     * must be loaded
     * @throws IOException
     */
    public Map(boolean newGame) throws IOException {
        hero = new Hero(HERO_PATH, NUMBER_OF_LIVES);
        idx = 0; // to create lists of monsters and keys
        this.newGame = newGame; // monsters are needed as objects to create only
                                // if it is new game
        keyCoords = new ArrayList<>();
        monsters = new ArrayList<>();
        mapArray = new char[MAP_HEIGHT][MAP_WIDTH];
    }

    /**
     *
     * @return list of all Monster objects which are currently in the game
     */
    public List<Monster> getMonsters() {
        return monsters;
    }

    /**
     *
     * @param monsters - list of all Monster objects which are currently in the game
     */
    public void setMonsters(List<Monster> monsters) {
        this.monsters = monsters;
    }

    /**
     * Loads map as an array from the text file.
     * @param path of the text file which contains representation of the map
     * @return mapArray - 2d char array which represents the map of the game
     * @throws IOException
     */
    public char[][] load_map(String path) throws IOException {
        int row = 0;
        try (FileInputStream file = new FileInputStream(path)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(file));
            String line;
            while ((line = br.readLine()) != null){
                char[] lineArray = line.toCharArray();
                System.arraycopy(lineArray, 0, mapArray[row], 0, line.length());
                row++;
            } 
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return mapArray;
    }
    
    /**
     * Reloads text file of the map by updated map array. Set updated mapArray.
     * @param newMapArray - - 2d char array which represents current map of the game
     * @throws IOException
     */
    public void redrawMap(char[][] newMapArray) throws IOException {
        try (FileWriter fw = new FileWriter(MAP_PATH)) {
            for (int i = 0; i < MAP_HEIGHT; i++) {
                fw.write(newMapArray[i]);
                fw.write("\n");
            } 
        }
        catch(IOException ex) {  
            System.out.println(ex.getMessage());
        } 
        mapArray = load_map(MAP_PATH);
    }
    
    /**
     * Draws elements of the game on the map. Set position of the Hero.
     * Creates monsters.
     * @return drawing of the map
     */
    public Canvas drawMap() {
        createImages();
        Canvas draw = new Canvas(STAGE_WIDTH, STAGE_HEIGHT);
        GraphicsContext gc = draw.getGraphicsContext2D();
        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                switch (mapArray[i][j]) {
                    // wall
                    case 'X':
                        gc.drawImage(wall, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                        break;
                    // floor (empty cell)
                    case '.':
                        gc.drawImage(emptyCell, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                        break;
                    // hero
                    case 'H':
                        gc.drawImage(emptyCell, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                        gc.drawImage(heroImg, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                        hero.setPosition(j, i);
                        break;
                    // door
                    case 'D':
                        gc.drawImage(emptyCell, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                        gc.drawImage(door, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                        break;
                    // key
                    case 'K':
                        gc.drawImage(emptyCell, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                        gc.drawImage(key, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                        if (newGame)
                            createMonster(j, i); 
                        break;
                    // gold
                    case 'G':
                        gc.drawImage(emptyCell, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                        gc.drawImage(gold, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                        break;
                    // sword
                    case 'S':
                        gc.drawImage(emptyCell, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                        gc.drawImage(sword, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                        break;
                    // bomb
                    case 'B':
                        gc.drawImage(emptyCell, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                        gc.drawImage(bomb, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                        break;
                    // hero with bomb
                    case 'F':
                        gc.drawImage(emptyCell, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                        gc.drawImage(heroImg, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                        gc.drawImage(bomb, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                        break;
                    // black cell (explosion)
                    case 'E':
                        gc.drawImage(exploded, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                        break;
                    // lives (health)
                    case 'L':
                       gc.drawImage(emptyCell, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                       gc.drawImage(heart, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                       break;
                    // skull when hero is dead
                    case 'Z':
                       gc.drawImage(emptyCell, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                       gc.drawImage(skull, j*NUMBER_OF_PIXELS, i*NUMBER_OF_PIXELS, NUMBER_OF_PIXELS, NUMBER_OF_PIXELS);
                       break;
                    default:
                        break;
                }
            }
        }
        newGame = false; // it's not needed to create monsters as objects again
        CanvasDrawer.drawMonsters(gc, monsters, monsterImg, emptyCell);
        CanvasDrawer.drawHealth(gc, hero.getHealth(), heart);
        CanvasDrawer.drawInventory(gc, hero.getInventory(), key, bomb);
        return draw;
    }
    
    /**
     *
     * @return current Hero instance
     */
    public Hero getHero() {
        return hero;
    }
    
    /**
     *
     * @param hero - updated Hero object
     */
    public void setHero(Hero hero) {
        this.hero = hero;
    }

    // creates all images that will be drawn on the map
    private void createImages() {
        wall = new Image(WALL_PATH);
        gold = new Image(GOLD_PATH);
        sword = new Image(SWORD_PATH);
        key = new Image(KEY_PATH);
        emptyCell = new Image(EMPTY_CELL_PATH);
        heroImg = hero.getImage();
        door = new Image(DOOR_PATH);
        heart = new Image(FULL_HEART_PATH);
        bomb = new Image(BOMB_PATH);
        exploded = new Image(EXPLODED_PATH);
        skull = new Image(SKULL_PATH);
    }
    
    // creates new Monster object
    private void createMonster(int j, int i) {
        // at the begining of the game each monster depends on its own key,
        // so list of keys will be created or updated
        keyCoords.add(new ArrayList<Integer>());
        // add key coordinations to the list of all keys
        keyCoords.get(idx).add(j);
        keyCoords.get(idx).add(i);
        // add Monster object to the list of all monsters
        monsters.add(new Monster(MONSTER_PATH, MONSTER_LIVES));
        // each monster has its own key
        monsters.get(idx).setKeyCoords(keyCoords.get(idx));
        // finding own monster for each key and setting its positions
        for (int h = 0; h < DIRECTIONS.length; h++) {
            if (mapArray[keyCoords.get(idx).get(1) + DIRECTIONS[h][1]]
                    [keyCoords.get(idx).get(0)+DIRECTIONS[h][0]] == 'M') {
                monsters.get(idx).setPosition(keyCoords.get(idx).get(0) + DIRECTIONS[h][0], keyCoords.get(idx).get(1)+DIRECTIONS[h][1]);
                break;
            }
        }
        idx++;
    }

    /**
     *
     * @return list of coordinations of all keys on the map
     */
    public List<List<Integer>> getKeyCoords() {
        return keyCoords;
    }
    
    /**
     * Removes given key coordinations from the list of
     * coordinations of all keys on the map.
     * @param coords - list of coordinations of key to remove
     */
    public void removeKeyCoords(List<Integer> coords) {
        keyCoords.remove(coords);
    }

}
