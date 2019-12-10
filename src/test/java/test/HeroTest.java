package test;

import cz.cvut.fel.pjv.Hero;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 * Testing of Hero object main functionality.
 * @author shchesof
 */
public class HeroTest {
    
    final int health = 5;
    
    public HeroTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    /**
     * Test if hero successfully picks some loot and
     * save it in his inventory.
     * @throws IOException
     */
    @org.junit.Test
    public void testPickUpInventory() throws IOException {
        Hero instance = new Hero(null, health);
        final char[][] mapArray = { {'H', 'K'},
                                    {'.', '.'} };
        List<Character> inventory = new ArrayList<>();
        inventory.add('B');
        instance.setInvetory(inventory);
        instance.setPosition(0, 0);
        instance.move(mapArray, 1, 0, null);
        List<Character> expResult = new ArrayList<>();
        expResult.add('B');
        expResult.add('K');
        assertEquals(expResult, instance.getInventory());
    }

    /**
     * Test if hero changes his position correctly when he moves.
     */
    @org.junit.Test
    public void testMove() {
        Hero instance = new Hero(null, health);
        final char[][] mapArray = { {'H', '.'},
                                    {'.', '.'} };
        instance.setPosition(0, 0);
        instance.move(mapArray, 1, 1, null);
        int expX = 1;
        int expY = 1;
        assertEquals(expX, instance.getX());
        assertEquals(expY, instance.getY());
    }

    /**
     * Test if hero successfully opens door if he has key in
     * his inventory.
     */
    @org.junit.Test
    public void testOpenDoor() {
        Hero instance = new Hero(null, health);
        final char[][] mapArray = { {'H', '.'},
                                    {'D', '.'} };
        List<Character> inventory = new ArrayList<>();
        inventory.add('K');
        instance.setPosition(0, 0);
        instance.setInvetory(inventory);
        instance.move(mapArray, 0, 1, null);
        assertEquals(true, instance.isOpenedDoor());
    }

    /**
     * Test if hero can put bomb on the map only if he has it
     * in his inventory.
     * @throws IOException
     */
    @org.junit.Test
    public void testPutBomb() throws IOException {
        Hero instance = new Hero(null, health);
        List<Character> inventory = new ArrayList<>();
        inventory.add('B');
        instance.setInvetory(inventory);
        assertEquals(true, instance.putBomb(null));
    }

    /**
     * Test of 'removeKey' method.
     */
    @org.junit.Test
    public void testRemoveKey() {
        Hero instance = new Hero(null, health);
        List<Character> inventory = new ArrayList<>();
        inventory.add('K');
        instance.setInvetory(inventory);
        List<Character> expResult = new ArrayList<>();
        instance.removeKey();
        assertEquals(expResult, instance.getInventory());
    }

    /**
     * Test of picking sword from the map.
     */
    @org.junit.Test
    public void testHasSword() {
        Hero instance = new Hero(null, health);
        final char[][] mapArray = { {'H', 'S'},
                                    {'.', '.'} };
        instance.setPosition(0, 0);
        instance.move(mapArray, 1, 0, null);
        assertEquals(true, instance.hasSword());
    }
    
}
