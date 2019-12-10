package cz.cvut.fel.pjv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import static cz.cvut.fel.pjv.Data.*;

/**
 * Secondary class to create a SubScene to show user rules of the game.
 * @author shchesof
 */
public class HelpSubscene extends SubScene {
    
    private Canvas draw;
    private final AnchorPane root2;
    private int START_Y = 30;
    private final int START_X = 15, FONT_SIZE = 17, LINES_SPACE = 25;
    
    /**
     * Creates SubScene and puts there rules of the game from the
     * text file.
     */
    public HelpSubscene() {
        super(new AnchorPane(), 600, 400);
        BackgroundImage image = new BackgroundImage(new Image("/styles/background1.jpg", 600, 400, false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        root2 = (AnchorPane)this.getRoot();
        root2.setBackground(new Background(image));
        setText();
    }
    
    // initializes Canvas and GraphicsContext to put there rules 
    private void setText() {
        draw = new Canvas(600, 400);
        draw.setLayoutX(0);
        draw.setLayoutY(0);
        GraphicsContext gc = draw.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD, FONT_SIZE));
        readRules(gc);
        root2.getChildren().add(draw);
    }
    
    // reads rules from the text file and 'draws' them by passed GraphicsContext
    private void readRules(GraphicsContext gc) {
        try (FileInputStream file = new FileInputStream(RULES_PATH)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(file));
            String line;
            while ((line = br.readLine()) != null){
                gc.fillText(line, START_X, START_Y); 
                START_Y += LINES_SPACE;
            } 
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        
    }
    
}
