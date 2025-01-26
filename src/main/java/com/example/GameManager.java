package com.example;

import javafx.scene.layout.Pane;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.input.KeyCode;

public class GameManager {
    private static final double OBSTACLE_GAP = 250.0;
    private static final double PIPE_GAP_HEIGHT = 200.0;
    private static final double MIN_PIPE_HEIGHT = 100.0;
    
    private final Pane gamePane;
    private final Bird bird;
    private final List<Obstacle> obstacles;
    private final UIManager uiManager;
    private final BackgroundManager backgroundManager;
    private final CollisionDetector collisionDetector;
    private final ScoreManager scoreManager;
    
    private Timeline gameLoop;
    private boolean gameRunning;
    private javafx.scene.layout.VBox gameOverScreen;
    private List<javafx.scene.image.ImageView> backgrounds;
    
    public GameManager(Pane gamePane, Bird bird) {
        this.gamePane = gamePane;
        this.bird = bird;
        this.obstacles = new ArrayList<>();
        this.gameRunning = false;
        
        this.uiManager = new UIManager(gamePane);
        this.backgroundManager = new BackgroundManager(gamePane);
        this.collisionDetector = new CollisionDetector();
        this.scoreManager = new ScoreManager(uiManager.getScoreText());
        
        setupControls();
        
        // Game over ekranını oluştur
        gameOverScreen = new javafx.scene.layout.VBox(10);
        gameOverScreen.setAlignment(javafx.geometry.Pos.CENTER);
        gameOverScreen.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
        
        javafx.scene.text.Text gameOverText = new javafx.scene.text.Text("GAME OVER");
        gameOverText.setStyle("-fx-font-size: 48; -fx-fill: white;");
        
        javafx.scene.control.Button restartButton = new javafx.scene.control.Button("Tekrar Oyna");
        restartButton.setOnAction(e -> restartGame());
        
        gameOverScreen.getChildren().addAll(gameOverText, new javafx.scene.text.Text(""), restartButton);
        gameOverScreen.setVisible(false);
        gameOverScreen.prefWidthProperty().bind(gamePane.widthProperty());
        gameOverScreen.prefHeightProperty().bind(gamePane.heightProperty());
        
        // Game over ekranını en üstte göstermek için
        gamePane.getChildren().add(gameOverScreen);
        gameOverScreen.toFront();
        
        // Arkaplan listesini başlat
        backgrounds = new ArrayList<>();
        setupBackground();
        
        uiManager.setRestartButtonAction(this::restartGame);
    }
    
    private void setupBackground() {
        // Arkaplan resmini yükle
        javafx.scene.image.Image bgImage = new javafx.scene.image.Image(
            getClass().getResourceAsStream("/background.png")); // Arkaplan dosya yolu
        
        // İki adet arkaplan oluştur (sürekli kaydırma için)
        for (int i = 0; i < 2; i++) {
            javafx.scene.image.ImageView background = new javafx.scene.image.ImageView(bgImage);
            background.setFitHeight(gamePane.getHeight());
            // Genişliği ekran genişliğine göre ayarla
            background.setFitWidth(gamePane.getWidth());
            // İlk resmi 0'a, ikinci resmi hemen yanına yerleştir
            background.setX(i * background.getFitWidth());
            background.setY(0);
            
            backgrounds.add(background);
            // Arkaplanları en arkaya yerleştir
            gamePane.getChildren().add(0, background);
        }
    }
    
    private void setupControls() {
        gamePane.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE && gameRunning) {
                bird.jump();
            } else if (event.getCode() == KeyCode.ENTER && !gameRunning) {
                restartGame();
            }
        });
        gamePane.setFocusTraversable(true);
    }
    
    public void startGame() {
        scoreManager.resetScore();  // Skoru sıfırla
        gameOverScreen.setVisible(false);
        gameRunning = true;
        
        // İlk boruları yerleştir - sabit 250 birim aralıklarla
        for (int i = 0; i < 4; i++) {  // 3 yerine 4 boru ile başlayalım
            double paneHeight = gamePane.getHeight();
            double gapHeight = 200;
            double minHeight = 100;
            double maxHeight = paneHeight - gapHeight - minHeight;
            
            double topPipeHeight = Math.random() * (maxHeight - minHeight) + minHeight;
            // Her boru arasında tam olarak 250 birim mesafe olacak
            Obstacle obstacle = new Obstacle(800 + (i * 250), topPipeHeight, gapHeight, paneHeight);
            obstacles.add(obstacle);
            gamePane.getChildren().addAll(obstacle.getTopPipe(), obstacle.getBottomPipe());
        }
        
        gameLoop = new Timeline(new KeyFrame(Duration.millis(16), e -> {
            updateGame();
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }
    
    private void updateGame() {
        if (!gameRunning) return;
        
        backgroundManager.update();
        bird.update();
        updateObstacles();
        
        if (collisionDetector.checkCollisions(bird, obstacles, gamePane.getHeight())) {
            gameOver();
            return;
        }
        
        scoreManager.checkAndUpdateScore(bird, obstacles);
    }
    
    private void updateObstacles() {
        obstacles.forEach(Obstacle::update);
        
        if (shouldAddNewObstacle()) {
            addNewObstacle();
        }
        
        removeOffscreenObstacles();
    }
    
    private boolean shouldAddNewObstacle() {
        // Son borunun X pozisyonu 550'nin altına düştüğünde yeni boru ekle
        return obstacles.isEmpty() || 
               obstacles.get(obstacles.size() - 1).getX() < 550;
    }
    
    private void addNewObstacle() {
        double paneHeight = gamePane.getHeight();
        double maxHeight = paneHeight - PIPE_GAP_HEIGHT - MIN_PIPE_HEIGHT;
        double topPipeHeight = Math.random() * (maxHeight - MIN_PIPE_HEIGHT) + MIN_PIPE_HEIGHT;
        
        double lastPipeX = obstacles.get(obstacles.size() - 1).getX();
        Obstacle obstacle = new Obstacle(lastPipeX + OBSTACLE_GAP, topPipeHeight, PIPE_GAP_HEIGHT, paneHeight);
        
        obstacles.add(obstacle);
        gamePane.getChildren().addAll(obstacle.getTopPipe(), obstacle.getBottomPipe());
    }
    
    private void removeOffscreenObstacles() {
        obstacles.removeIf(obstacle -> {
            if (obstacle.isOffscreen()) {
                gamePane.getChildren().remove(obstacle.getTopPipe());
                gamePane.getChildren().remove(obstacle.getBottomPipe());
                return true;
            }
            return false;
        });
    }
    
    private void gameOver() {
        if (!gameRunning) return;
        
        gameRunning = false;
        gameLoop.stop();
        uiManager.showGameOver(scoreManager.getScore());
    }
    
    private void restartGame() {
        // Engelleri temizle
        obstacles.forEach(obstacle -> {
            gamePane.getChildren().remove(obstacle.getTopPipe());
            gamePane.getChildren().remove(obstacle.getBottomPipe());
        });
        obstacles.clear();
        
        // Kuşu başlangıç pozisyonuna getir
        bird.reset(); // Bird sınıfına reset metodu eklenmeli
        
        // Arkaplanları başlangıç pozisyonlarına getir
        for (int i = 0; i < backgrounds.size(); i++) {
            backgrounds.get(i).setX(i * backgrounds.get(0).getFitWidth());
        }
        
        uiManager.hideGameOver();
        startGame();
    }
    
    public UIManager getUiManager() {
        return uiManager;
    }
}