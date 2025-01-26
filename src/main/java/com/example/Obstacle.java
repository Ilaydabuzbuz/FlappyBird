package com.example;

import javafx.geometry.Bounds;
import javafx.geometry.BoundingBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.effect.InnerShadow;

public class Obstacle {
    // Sabit değerler için anlamlı isimler ve private static final kullanımı
    private static final Color PIPE_COLOR = Color.rgb(145, 40, 190);
    private static final Color PIPE_BORDER_COLOR = Color.rgb(100, 20, 140);
    private static final double PIPE_STROKE_WIDTH = 3;
    private static final double INNER_SHADOW_RADIUS = 15;
    private static final double PIPE_WIDTH_RATIO = 0.0625;
    private static final double PIPE_GAP_RATIO = 0.25;
    private static final double PIPE_MOVEMENT_SPEED = 3;
    private static final double DEFAULT_SCREEN_WIDTH = 800;
    
    private static double screenHeight = 600;
    
    private final Rectangle topPipe;
    private final Rectangle bottomPipe;
    private double xPosition;
    private boolean scored;
    
    public static void setScreenHeight(double height) {
        screenHeight = height;
    }
    
    public Obstacle(double initialX, double topHeight, double gapHeight, double paneHeight) {
        this.xPosition = initialX;
        double pipeWidth = PIPE_WIDTH_RATIO * DEFAULT_SCREEN_WIDTH;
        double gapSize = PIPE_GAP_RATIO * screenHeight;
        
        LinearGradient pipeGradient = createPipeGradient();
        
        topPipe = createPipe(initialX, 0, pipeWidth, topHeight, pipeGradient);
        bottomPipe = createBottomPipe(initialX, topHeight, gapSize, paneHeight, pipeWidth, pipeGradient);
    }
    
    private LinearGradient createPipeGradient() {
        Stop[] gradientStops = {
            new Stop(0, PIPE_COLOR),
            new Stop(0.3, PIPE_COLOR.brighter()),
            new Stop(0.5, PIPE_COLOR.brighter().brighter()),
            new Stop(0.7, PIPE_COLOR.brighter()),
            new Stop(1, PIPE_COLOR)
        };
        
        return new LinearGradient(
            0, 0, 1, 0,
            true,
            CycleMethod.REPEAT,
            gradientStops
        );
    }
    
    private Rectangle createPipe(double x, double y, double width, double height, LinearGradient gradient) {
        Rectangle pipe = new Rectangle(x, y, width, height);
        pipe.setFill(gradient);
        pipe.setStroke(PIPE_BORDER_COLOR);
        pipe.setStrokeWidth(PIPE_STROKE_WIDTH);
        pipe.setEffect(new InnerShadow(INNER_SHADOW_RADIUS, Color.rgb(0, 0, 0, 0.4)));
        return pipe;
    }
    
    private Rectangle createBottomPipe(double x, double topHeight, double gapSize, 
                                     double paneHeight, double width, LinearGradient gradient) {
        double bottomPipeHeight = paneHeight - topHeight - gapSize;
        Rectangle pipe = createPipe(x, paneHeight - bottomPipeHeight, width, bottomPipeHeight, gradient);
        return pipe;
    }
    
    public void update() {
        xPosition -= PIPE_MOVEMENT_SPEED;
        updatePipePositions();
    }
    
    private void updatePipePositions() {
        topPipe.setX(xPosition);
        bottomPipe.setX(xPosition);
    }
    
    public boolean isOffscreen() {
        return xPosition + topPipe.getWidth() < 0;
    }
    
    public Rectangle getTopPipe() {
        return topPipe;
    }
    
    public Rectangle getBottomPipe() {
        return bottomPipe;
    }
    
    public Bounds getBounds() {
        return new BoundingBox(xPosition, 0, topPipe.getWidth(), screenHeight);
    }
    
    public double getX() {
        return xPosition;
    }
    
    public boolean checkCollision(Bird bird) {
        return bird.getBounds().intersects(topPipe.getBoundsInParent()) || 
               bird.getBounds().intersects(bottomPipe.getBoundsInParent());
    }
    
    public boolean isScored() {
        return scored;
    }
    
    public void setScored(boolean scored) {
        this.scored = scored;
    }
}
