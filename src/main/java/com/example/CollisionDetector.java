package com.example;

import java.util.List;

public class CollisionDetector {
    private static final double COLLISION_TOLERANCE = 15.0;
    
    public boolean checkCollisions(Bird bird, List<Obstacle> obstacles, double paneHeight) {
        return checkObstacleCollisions(bird, obstacles) || checkBoundaryCollisions(bird, paneHeight);
    }
    
    private boolean checkObstacleCollisions(Bird bird, List<Obstacle> obstacles) {
        for (Obstacle obstacle : obstacles) {
            javafx.geometry.Bounds birdBounds = bird.getBounds();
            javafx.geometry.Bounds topPipeBounds = obstacle.getTopPipe().getBoundsInLocal();
            javafx.geometry.Bounds bottomPipeBounds = obstacle.getBottomPipe().getBoundsInLocal();
            
            boolean topCollision = birdBounds.getMaxX() - COLLISION_TOLERANCE > obstacle.getX() &&
                                 birdBounds.getMinX() + COLLISION_TOLERANCE < obstacle.getX() + obstacle.getTopPipe().getWidth() &&
                                 birdBounds.getMinY() + COLLISION_TOLERANCE < topPipeBounds.getMaxY();
            
            boolean bottomCollision = birdBounds.getMaxX() - COLLISION_TOLERANCE > obstacle.getX() &&
                                    birdBounds.getMinX() + COLLISION_TOLERANCE < obstacle.getX() + obstacle.getBottomPipe().getWidth() &&
                                    birdBounds.getMaxY() - COLLISION_TOLERANCE > bottomPipeBounds.getMinY();
            
            if (topCollision || bottomCollision) return true;
        }
        return false;
    }
    
    private boolean checkBoundaryCollisions(Bird bird, double paneHeight) {
        return bird.getY() <= 0 || bird.getY() >= paneHeight - 20;
    }
}
