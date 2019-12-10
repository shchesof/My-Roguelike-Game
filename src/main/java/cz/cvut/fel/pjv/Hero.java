package cz.cvut.fel.pjv;

import java.util.ArrayList;
import java.util.List;
import static cz.cvut.fel.pjv.Data.*;
import java.io.IOException;

/**
 * Main hero of the game.
 * @author shchesof
 */
public class Hero extends Sprite {
    
    private List<Character> inventory;
    private boolean openedDoor, hasSword, mustDie, bombIsPut;
    private int bombCount; // count moves within which bomb is on the map
    private int[] bombCoords; // position on which hero has put bomb
  
    /**
     *
     * @param path of Hero's image
     * @param health - number of health at the beginning of the game
     */
    public Hero(String path, int health) {
        super(path, health);
        inventory = new ArrayList<>();
        bombCoords= new int[2];
    }
    
    /**
     * Returns list of hero's inventory.
     * @return list of hero's inventory
     */
    public List<Character> getInventory() {
        return inventory;
    }
    
    /**
     *
     * @param inventory - list of current hero's inventory
     */
    public void setInvetory(List<Character> inventory) {
        this.inventory = inventory;
    } 
    
    /**
     * Changes hero's position in mapArray: x by shiftX and y by shiftY.
     * @param mapArray - current map array
     * @param shiftX - how much to increase X position
     * @param shiftY - how much to increase Y position
     * @param userInputHandler - to synchronize data with UserInputHandler class
     */
    public void move(char[][] mapArray, int shiftX, int shiftY, 
            UserInputHandler userInputHandler) {
        openedDoor = false;
        if (mapArray[y+shiftY][x+shiftX] == 'X') {
            return;
        }
        if (mapArray[y+shiftY][x+shiftX] == 'M') {
            return;
        }
        if (mapArray[y+shiftY][x+shiftX] == 'D') {
            if (!inventory.contains('K')) {
                return;
            }
            else openedDoor = true;
        }
        // F is for hero who has put the bomb
        if (!(mapArray[y][x] == 'F'))
            mapArray[y][x] = '.';
        else {
            // hero left, bomb is still on its cell
            mapArray[y][x] = 'B';
        }
        setPosition(x+shiftX, y+shiftY);
        if (mapArray[y][x] == 'S') {
            // if it is not unit test
            if (userInputHandler != null) redraw(HERO_SWORD_PATH);
            hasSword = true;
        }
        if (mapArray[y][x] == 'K') {
            inventory.add('K');
            if (userInputHandler != null) userInputHandler.removeKeysFromMap();
        }
        if (mapArray[y][x] == 'B') {
            inventory.add('B');
        }
        if (mapArray[y][x] == 'L') {
            increaseHealth(1);
        }
        if (mapArray[y][x] == 'G') {
            if (userInputHandler != null) userInputHandler.setEndOfTheGame(true);
        }
        if (openedDoor) removeKey();
    }
    
    /**
     * Put bomb on the map if Hero has it in his inventory.
     * @param map - Map object to update
     * @return true if bomb was successfully put on the map, false otherwise
     * @throws IOException
     */
    public boolean putBomb(Map map) throws IOException {
        char[][] mapArray = { {0}, {0} };
        if (map != null) mapArray = map.load_map(MAP_PATH);
        if (!inventory.contains('B')) {
            System.out.println("No bombs :(");
            bombIsPut = false;
        }
        else {
            // draw hero with bomb on the same cell
            mapArray[y][x] = 'F';
            bombCoords[0] = x;
            bombCoords[1] = y;
            bombCount = 0;
            bombIsPut = true;
            // remove bomb from the inventory
            for (Character c : inventory) {
                if (c == 'B') {
                    inventory.remove(c);
                    break;
                }
            }
            if (map != null) map.redrawMap(mapArray);
        }  
        return bombIsPut;
    }
    
    /**
     * Removes one key from Hero's inventory, if he has it.
     */
    public void removeKey() {
        for (Character c : inventory) {
            if (c == 'K') {
                inventory.remove(c);
                break;
            }
        }
    }

    /**
     *
     * @return true if door was successfully opened, false otherwise
     */
    public boolean isOpenedDoor() {
        return openedDoor;
    }

    /**
     *
     * @return true if hero has sword, false otherwise
     */
    public boolean hasSword() {
        return hasSword;
    }

    /**
     *
     * @return true if hero must die, false otherwise
     */
    public boolean isMustDie() {
        return mustDie;
    }

    /**
     *
     * @param mustDie
     */
    public void setMustDie(boolean mustDie) {
        this.mustDie = mustDie;
    }

    /**
     *
     * @return number of moves within bomb is on the map
     */
    public int getBombCount() {
        return bombCount;
    }

    /**
     * Increase bombCount.
     */
    public void increaseBombCount() {
        bombCount++;
    }

    /**
     *
     * @param bombCount - number of moves within bomb is on the map
     */
    public void setBombCount(int bombCount) {
        this.bombCount = bombCount;
    }

    /**
     *
     * @return true if bomb is on the map, false otherwise
     */
    public boolean isBombIsPut() {
        return bombIsPut;
    }

    /**
     *
     * @param bombIsPut - true if bomb is on the map
     */
    public void setBombIsPut(boolean bombIsPut) {
        this.bombIsPut = bombIsPut;
    }

    /**
     *
     * @return array of coordinations of put bomb
     */
    public int[] getBombCoords() {
        return bombCoords;
    }
    
}
