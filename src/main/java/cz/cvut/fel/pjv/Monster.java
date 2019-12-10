package cz.cvut.fel.pjv;

import static cz.cvut.fel.pjv.Data.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Monster character.
 * @author shchesof
 */
public class Monster extends Sprite {

    private List<Integer> keyCoords; // at the beginning of the game
                                     // each monster guards his own key
    private int[][] movingCoords; // moving around the key
    private int newX, newY, monsterIdx, counter;
    private int[] newMovingCoords;

    /**
     *
     * @param path of Monster's image
     * @param health number of health at the beginning of the game
     */
    public Monster(String path, int health) {
        super(path, health);
        keyCoords = new ArrayList<>();
        movingCoords = DIRECTIONS;
        counter = 0; // it's needed to change monster's moving directions to random
                     // only once
        newMovingCoords = new int[2]; 
    }

    /**
     *
     * @return list of coordinations of monster's key
     */
    public List<Integer> getKeyCoords() {
        return keyCoords;
    }

    /**
     *
     * @param keyCoords
     */
    public void setKeyCoords(List<Integer> keyCoords) {
        this.keyCoords = keyCoords;
    }

    /**
     * Changes position of the Monster on the map. Reloads map.
     * Checks if Monster must be deleted because of his 'death'. 
     * @param monsters
     * @param mapArray
     * @param explosionCoords
     * @param userInputHandler
     */
    public void move(List<Monster> monsters, char[][] mapArray, int[][] explosionCoords,
            UserInputHandler userInputHandler) {
        monsterIdx = 0;
        if (keyCoords != null) {
            keyDependingMove(mapArray); // around the key
        } else {
            randomMove(mapArray); // from wall to wall
        }
        // if it's not unit test
        if (monsters != null) {
            // if here is explosion, black cell is drawn
            if (mapArray[newY][newX] == 'E') {
                for (int j = 0; j < explosionCoords.length; j++) {
                    if ((explosionCoords[j][0] == x)
                            && (explosionCoords[j][1] == y)) {
                        mapArray[y][x] = 'E';
                        break;
                    } // monster just disappears
                    else {
                        mapArray[y][x] = '.';
                    }
                }
                // monster dies
                userInputHandler.addDeadMonster(this);
                return;
            }
        }
        // monster disappears from the previous place 
        if ((mapArray[y][x] != 'E')
                && (mapArray[y][x] != 'H')) {
            mapArray[y][x] = '.';
        }
        mapArray[newY][newX] = 'M';
        setPosition(newX, newY);
    }

    // monster changes his direction if it is wall or door
    // in front of him
    private void changeDirection() {
        int[][] tmp = new int[8][2]; // new directions
        // reverse old directions
        for (int j = 0; j < DIRECTIONS.length; j++) {
            tmp[j][0] = movingCoords[DIRECTIONS.length - 1 - j][0];
            tmp[j][1] = movingCoords[DIRECTIONS.length - 1 - j][1];
        }
        movingCoords = tmp;

        // if wall is on the right
        if (((newX - x) == 1) && ((newY - y) == 0)) {
            for (int j = 0; j < DIRECTIONS.length; j++) {
                if ((movingCoords[j][0] == -1)
                        && (movingCoords[j][1] == 0)) {
                    monsterIdx = j - 1;
                    if (monsterIdx < 0) {
                        monsterIdx = DIRECTIONS.length - 1;
                    }
                }
            }
        } // if wall is on the left
        else if (((newX - x) == -1) && ((newY - y) == 0)) {
            for (int j = 0; j < DIRECTIONS.length; j++) {
                if ((movingCoords[j][0] == 1)
                        && (movingCoords[j][1] == 0)) {
                    monsterIdx = j - 1;
                    if (monsterIdx < 0) {
                        monsterIdx = DIRECTIONS.length - 1;
                    }
                }
            }
        } // if wall is down
        else if (((newX - x) == 0) && ((newY - y) == 1)) {
            for (int j = 0; j < DIRECTIONS.length; j++) {
                if ((movingCoords[j][0] == 0)
                        && (movingCoords[j][1] == -1)) {
                    monsterIdx = j - 1;
                    if (monsterIdx < 0) {
                        monsterIdx = DIRECTIONS.length - 1;
                    }
                }
            }
        } // if wall is up
        else if (((newX - x) == 0) && ((newY - y) == -1)) {
            for (int j = 0; j < DIRECTIONS.length; j++) {
                if ((movingCoords[j][0] == 0)
                        && (movingCoords[j][1] == 1)) {
                    monsterIdx = j - 1;
                    if (monsterIdx < 0) {
                        monsterIdx = DIRECTIONS.length - 1;
                    }
                }
            }
        }
        newX = keyCoords.get(0)
                + movingCoords[monsterIdx][0];
        newY = keyCoords.get(1)
                + movingCoords[monsterIdx][1];
    }

    // moving from wall to wall
    private void randomMove(char[][] mapArray) {
        // check counter to not randomly change direction each move
        if (counter == 0) {
            Random rnd = new Random();
            int n = rnd.nextInt(2); // 50% possibility of choice
            if (n == 0) {
                // move horizontally
                newMovingCoords[0] = 1;
                newMovingCoords[1] = 0;
            } else {
                // move vertically
                newMovingCoords[0] = 0;
                newMovingCoords[1] = 1;
            }
            counter++; // update counter
        }
        newX = x + newMovingCoords[0];
        newY = y + newMovingCoords[1];
        // reverse direction if there is a wall or a door
        if ((mapArray[newY][newX] == 'X') || (mapArray[newY][newX] == 'D')) {
            newMovingCoords[0] = -newMovingCoords[0];
            newMovingCoords[1] = -newMovingCoords[1];
        }
        // update newX and newY
        newX = x + newMovingCoords[0];
        newY = y + newMovingCoords[1];
    }

    // moving depends on the key which monster moves around
    private void keyDependingMove(char[][] mapArray) {
        int[] relativeCoords = new int[2]; 
        relativeCoords[0] = this.x
                - keyCoords.get(0);
        relativeCoords[1] = this.y
                - keyCoords.get(1);
        for (int j = 0; j < DIRECTIONS.length; j++) {
            if (Arrays.equals(relativeCoords, movingCoords[j])) {
                monsterIdx = j + 1;
            }
        }
        if (monsterIdx == DIRECTIONS.length) {
            monsterIdx = 0;
        }
        newX = keyCoords.get(0)
                + movingCoords[monsterIdx][0];
        newY = keyCoords.get(1)
                + movingCoords[monsterIdx][1];
        // if there is a wall or a door monster must move in reverse
        if ((mapArray[newY][newX] == 'X') || (mapArray[newY][newX] == 'D')) {
            changeDirection();
        }
    }

}
