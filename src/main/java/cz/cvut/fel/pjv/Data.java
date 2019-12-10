package cz.cvut.fel.pjv;


public class Data {
    
    static final int STAGE_WIDTH = 900;
    static final int STAGE_HEIGHT = 600;
    
    static final int NUMBER_OF_PIXELS = 30; // size of one game map cell to draw
    
    static final int START_BUTTON_Y = 300;
    static final int START_BUTTON_X = 370;
    
    static final int MAP_WIDTH = STAGE_WIDTH / NUMBER_OF_PIXELS;  //30
    static final int MAP_HEIGHT = STAGE_HEIGHT / NUMBER_OF_PIXELS;  //20
    
    static final int NUMBER_OF_LIVES = 5; // hero's default health
    
    // positions to draw hero's health on the scene
    static final int LIVES_START_X = STAGE_WIDTH/2 + 2*NUMBER_OF_PIXELS;
    static final int LIVES_START_Y = 0;
    
    static final int MONSTER_LIVES = 2; // monsters' defaul health
    
    // positions to draw hero's inventory on the scene
    static final int INVENTORY_START_Y = 0;
    static final int INVENTORY_START_X = STAGE_WIDTH/2 - 3*NUMBER_OF_PIXELS;
    
    // directions for monsters to move around the key
    static final int[][] DIRECTIONS = {{1,0},{1,1},{0,1},{-1,1},{-1,0},
        {-1,-1},{0,-1},{1,-1}};
    
    
    // paths
    static final String MONSTER_PATH = "/characters/monster.png";
    static final String HURTED_MONSTER_PATH = "/characters/hurtedMonster.png";
    static final String HERO_PATH = "/characters/hero.png";
    static final String HERO_SWORD_PATH = "/characters/heroWithSword.png";
    
    static final String MAIN_STYLE_PATH = "/styles/MainStyles.css";
    
    static final String RULES_PATH = "src/main/resources/rules/rules.txt";
    
    static final String DOOR_PATH = "/cave/door.png";
    static final String WALL_PATH = "/cave/wall.png";
    static final String EMPTY_CELL_PATH = "/cave/empty_cell.png";
    static final String EXPLODED_PATH = "/cave/exploded.png";
    
    static final String FULL_HEART_PATH = "/health/full_heart.png";
    //static final String HALF_HEART_PATH = "/health/half_heart.png";
    static final String EMPTY_HEART_PATH = "/health/empty_heart.png";
    static final String GAME_OVER_PATH = "/health/game_over.png";
    static final String SKULL_PATH = "/health/skull.png";
    static final String YOU_WIN_PATH = "/styles/youWin.jpg";
    
    static final String GOLD_PATH = "/loot/gold.png";
    static final String KEY_PATH = "/loot/key.png";
    static final String SWORD_PATH = "/loot/sword.png";
    static final String BOMB_PATH = "/loot/bomb.png";
    
    static final String MAP_PATH = "src/main/resources/map/map.txt";
    static final String NEWGAME_MAP_PATH = "src/main/resources/map/mapNewGame.txt";
    
    static final String BACKGROUND_PATH = "/styles/background.jpg";
    
    // to save and load the game
    static final String HERO_INFO_PATH = "src/main/resources/saved/hero.ser";
    static final String MONSTERS_INFO_PATH = "src/main/resources/saved/monster";
    
}
