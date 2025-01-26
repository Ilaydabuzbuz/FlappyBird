package com.example;

import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class StartScreen extends StackPane {
    private static final double BUTTON_WIDTH = 300;
    private static final double BUTTON_HEIGHT = 100;
    
    private final Runnable onStartGame;
    private final Text highScoreText;
    
    public StartScreen(double width, double height, Runnable onStartGame) {
        this.onStartGame = onStartGame;
        this.highScoreText = createHighScoreText();
        setupScreen(width, height);
    }
    
    private void setupScreen(double width, double height) {
        // Arkaplan
        ImageView background = createBackground("/background.png", width, height);
        
        // Logo
        ImageView titleLogo = createLogo("/title.png", width * 0.6);
        titleLogo.setTranslateY(-100);  // Logoyu yukarı kaydır
        
        // Başlat butonu
        Button startButton = createStartButton("/button.png");
        startButton.setTranslateY(50);  // Butonu aşağı kaydır
        
        // En yüksek skor yazısı
        highScoreText.setTranslateY(150); // Start butonunun altına
        
        getChildren().addAll(background, titleLogo, startButton, highScoreText);
    }
    
    private ImageView createBackground(String imagePath, double width, double height) {
        Image bgImage = new Image(getClass().getResourceAsStream(imagePath));
        ImageView background = new ImageView(bgImage);
        background.setFitWidth(width);
        background.setFitHeight(height);
        return background;
    }
    
    private ImageView createLogo(String imagePath, double width) {
        Image logoImage = new Image(getClass().getResourceAsStream(imagePath));
        ImageView logo = new ImageView(logoImage);
        logo.setFitWidth(width);
        logo.setPreserveRatio(true);
        return logo;
    }
    
    private Button createStartButton(String imagePath) {
        Image buttonImage = new Image(getClass().getResourceAsStream(imagePath));
        ImageView buttonGraphic = new ImageView(buttonImage);
        buttonGraphic.setFitWidth(BUTTON_WIDTH);
        buttonGraphic.setFitHeight(BUTTON_HEIGHT);
        
        Button button = new Button();
        button.setGraphic(buttonGraphic);
        button.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        button.setOnAction(e -> onStartGame.run());
        
        button.setOnMouseEntered(e -> button.setScaleX(1.1));
        button.setOnMouseEntered(e -> button.setScaleY(1.1));
        button.setOnMouseExited(e -> button.setScaleX(1.0));
        button.setOnMouseExited(e -> button.setScaleY(1.0));
        
        return button;
    }
    
    private Text createHighScoreText() {
        Text text = new Text("Highest Score: 0");
        text.setStyle("-fx-font-size: 24; -fx-fill: white; -fx-font-weight: bold; " +
                     "-fx-stroke: black; -fx-stroke-width: 2;");
        return text;
    }
    
    public void updateHighScore(int highScore) {
        highScoreText.setText("Highest Score: " + highScore);
    }
} 