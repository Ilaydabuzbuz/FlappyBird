package com.example;

import javafx.scene.text.Text;
import java.util.List;
import java.util.prefs.Preferences;

public class ScoreManager {
    private Text scoreText;
    private int score = 0;
    private final Preferences prefs;
    private static final String HIGH_SCORE_KEY = "highScore";
    
    public ScoreManager(Text scoreText) {
        this.scoreText = scoreText;
        this.prefs = Preferences.userNodeForPackage(ScoreManager.class);
    }
    
    public void setScoreText(Text scoreText) {
        this.scoreText = scoreText;
        updateScoreText();
    }
    
    public void checkAndUpdateScore(Bird bird, List<Obstacle> obstacles) {
        for (Obstacle obstacle : obstacles) {
            if (!obstacle.isScored() && obstacle.getX() + obstacle.getTopPipe().getWidth() < bird.getX()) {
                incrementScore();
                obstacle.setScored(true);
            }
        }
    }
    
    public void incrementScore() {
        score++;
        updateScoreText();
        checkAndUpdateHighScore();
    }
    
    public void resetScore() {
        score = 0;
        updateScoreText();
    }
    
    private void updateScoreText() {
        scoreText.setText("Score: " + score);
    }
    
    public int getScore() {
        return score;
    }
    
    private void checkAndUpdateHighScore() {
        int currentHighScore = getHighScore();
        if (score > currentHighScore) {
            prefs.putInt(HIGH_SCORE_KEY, score);
        }
    }
    
    public int getHighScore() {
        return prefs.getInt(HIGH_SCORE_KEY, 0);
    }
} 