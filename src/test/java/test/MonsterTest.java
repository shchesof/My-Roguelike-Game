package test;

import cz.cvut.fel.pjv.Monster;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/** 
 * Testing of Monster object main functionality.
 * @author shchesof
 */
public class MonsterTest {
    
    final int health = 2;

    public MonsterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test if monster moves around the key he guards.
     */
    @Test
    public void testKeyDependingMove() {
        Monster instance = new Monster(null, health);
        final char[][] mapArray = { {'M', '.', '.'},
                                    {'.', 'K', '.'},
                                    {'.', '.', '.'} };
        List<Integer> keyCoords = new ArrayList<>();
        keyCoords.add(1);
        keyCoords.add(1);
        instance.setKeyCoords(keyCoords);
        instance.setPosition(0, 0);
        instance.move(null, mapArray, null, null);
        int expX = 1;
        int expY = 0;
        assertEquals(expX, instance.getX());
        assertEquals(expY, instance.getY());
    }
    
    /**
     * Test if monster changes his moving directions when there is
     * a wall on his way.
     */
    @Test
    public void testChangeDirections() {
        Monster instance = new Monster(null, health);
        final char[][] mapArray = { {'.', '.', '.'},
                                    {'.', 'K', 'M'},
                                    {'X', 'X', 'X'} };
        List<Integer> keyCoords = new ArrayList<>();
        keyCoords.add(1);
        keyCoords.add(1);
        instance.setKeyCoords(keyCoords);
        instance.setPosition(2, 1);
        instance.move(null, mapArray, null, null);
        int expX = 2;
        int expY = 0;
        assertEquals(expX, instance.getX());
        assertEquals(expY, instance.getY());
    }
    
}
