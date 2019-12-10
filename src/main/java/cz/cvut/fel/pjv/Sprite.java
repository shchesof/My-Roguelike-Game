package cz.cvut.fel.pjv;
import java.io.Serializable;
import javafx.scene.image.Image;

/**
 * Graphic objects in 2D.
 * @author shchesof
 */
public class Sprite implements Serializable {
    transient private Image image; // not need to serialize

    /**
     * x position of Sprite object on the map
     */
    public int x,

    /**
     * y position of Sprite object on the map
     */
    y;
    private int health;

    /**
     *
     * @param path of object's image
     * @param health number of health at the beginning of the game
     */
    public Sprite(String path, int health) {
        if (path != null) image = new Image(path);
        this.health = health;
    }
    
    /**
     *
     * @return image of this Sprite object
     */
    public Image getImage() {
        return image;
    }

    /**
     *
     * @param newPath - path of object's image to redraw
     */
    public void redraw(String newPath) {
        image = new Image(newPath);
    }
    
    /**
     *
     * @param x
     * @param y
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @param x
     */
    public void increaseX(int x) {
      this.x += x;
    }

    /**
     *
     * @param y
     */
    public void increaseY(int y) {
      this.y += y;
    }

    /**
     *
     * @return x position
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return y position
     */
    public int getY() {
        return y;
    }
    
    /**
     *
     * @param health - current health of the object
     */
    public void increaseHealth(int health) {
        this.health += 1;
    }
    
    /**
     *
     * @param health - current health of the object
     */
    public void decreaseHealth(int health) {
        this.health -= 1;
    }
    
    /**
     *
     * @return current health of the object
     */
    public int getHealth() {
        return health;
    }

    /**
     *
     * @param health - current health of the object
     */
    public void setHealth(int health) {
        this.health = health;
    }
    
}
