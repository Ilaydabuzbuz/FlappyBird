package com.example;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

public class UIManager {
    private final Text scoreText;
    private final VBox gameOverScreen;
    private final Text gameOverScoreText;
    private final Text highScoreText;  // En yüksek skor için sabit referans
    private ScoreManager scoreManager;
    
    public UIManager(Pane gamePane) {
        this.scoreText = createScoreText();
        this.gameOverScreen = createGameOverScreen(gamePane);
        this.gameOverScoreText = (Text) gameOverScreen.getChildren().get(1);
        this.highScoreText = new Text("Highest Score: 0");
        this.highScoreText.setStyle("-fx-font-size: 24; -fx-fill: white;");
        
        gameOverScreen.getChildren().add(2, highScoreText);  // Butonlardan önce ekle
        gamePane.getChildren().addAll(scoreText, gameOverScreen);
    }
    
    private Text createScoreText() {
        Text text = new Text("Score: 0");
        text.setStyle("-fx-font-size: 36; -fx-fill: white; -fx-font-weight: bold; -fx-stroke: black; -fx-stroke-width: 2;");
        text.setX(20);
        text.setY(50);
        return text;
    }
    
    public Text getScoreText() {
        return scoreText;
    }
    
    private VBox createGameOverScreen(Pane gamePane) {
        VBox screen = new VBox(10);
        screen.setAlignment(Pos.CENTER);
        screen.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
        
        Text gameOverText = new Text("GAME OVER");
        gameOverText.setStyle("-fx-font-size: 48; -fx-fill: white;");
        
        Text scoreText = new Text("Score: 0");
        scoreText.setStyle("-fx-font-size: 32; -fx-fill: white;");
        
        Button restartButton = createStyledButton("Play Again", "#FF69B4"); // Pembe
        Button mainMenuButton = createStyledButton("Main Menu", "#9370DB");   // Mor
        
        screen.getChildren().addAll(gameOverText, scoreText, restartButton, mainMenuButton);
        screen.setVisible(false);
        screen.prefWidthProperty().bind(gamePane.widthProperty());
        screen.prefHeightProperty().bind(gamePane.heightProperty());
        
        // Her elemanı öne getir
        gameOverText.toFront();
        scoreText.toFront();
        restartButton.toFront();
        mainMenuButton.toFront();
        screen.toFront();
        
        return screen;
    }
    
    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(String.format("-fx-font-size: 20; -fx-background-color: %s; -fx-text-fill: white; -fx-cursor: hand;", color));
        
        // Hover efektleri
        button.setOnMouseEntered(e -> {
            button.setScaleX(1.1);
            button.setScaleY(1.1);
        });
        button.setOnMouseExited(e -> {
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });
        
        return button;
    }
    
    public void updateScore(int score) {
        scoreText.setText("Score: " + score);
    }
    
    public void showGameOver(int finalScore) {
        // Skorları güncelle
        gameOverScoreText.setText("Score: " + finalScore);
        highScoreText.setText("Highest Score: " + scoreManager.getHighScore());
        
        // Öne getir
        gameOverScreen.setVisible(true);
        gameOverScreen.toFront();
        
        // Butonları öne getir
        Button restartButton = (Button) gameOverScreen.getChildren().get(3);  // İndeksler değişti
        Button mainMenuButton = (Button) gameOverScreen.getChildren().get(4);
        restartButton.toFront();
        mainMenuButton.toFront();
    }
    
    public void hideGameOver() {
        gameOverScreen.setVisible(false);
        // Skor metnini sıfırla
        gameOverScoreText.setText("Score: 0");
    }
    
    public void setRestartButtonAction(Runnable action) {
        Button restartButton = (Button) gameOverScreen.getChildren().get(3);  // İndeks güncellendi
        restartButton.setOnAction(e -> action.run());
    }
    
    public void setMainMenuButtonAction(Runnable action) {
        Button mainMenuButton = (Button) gameOverScreen.getChildren().get(4);  // İndeks güncellendi
        mainMenuButton.setOnAction(e -> action.run());
    }
    
    public void setScoreManager(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
        scoreManager.setScoreText(scoreText);
    }
    
    // UI element oluşturma metodları
}
