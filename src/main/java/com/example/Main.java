package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final String GAME_TITLE = "Flappy Bird";
    
    private Stage stage;
    private Scene menuScene;
    private Scene gameScene;
    private StartScreen startScreen;
    private Pane gamePane;
    private Bird bird;
    private GameManager gameManager;
    private ScoreManager scoreManager;
    
    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        initializeScenes();
        configureStage();
        showMainMenu();
    }
    
    private void initializeScenes() {
        // Ana menü ekranını oluştur
        startScreen = new StartScreen(WIDTH, HEIGHT, this::startNewGame);
        menuScene = new Scene(startScreen, WIDTH, HEIGHT);
        
        // Oyun ekranını oluştur
        gamePane = new Pane();
        gameScene = new Scene(gamePane, WIDTH, HEIGHT);
        
        // ScoreManager'ı başlat
        scoreManager = new ScoreManager(null); // null yerine UIManager'dan alınacak Text
        startScreen.updateHighScore(scoreManager.getHighScore());
    }
    
    private void configureStage() {
        stage.setTitle(GAME_TITLE);
        stage.setResizable(false);
        stage.setMaximized(false);
        stage.show();
    }
    
    private void showMainMenu() {
        cleanupGame();
        // Ana menüye dönerken en yüksek skoru güncelle
        startScreen.updateHighScore(scoreManager.getHighScore());
        stage.setScene(menuScene);
    }
    
    private void startNewGame() {
        cleanupGame();
        initializeGameComponents();
        stage.setScene(gameScene);
        gameManager.startGame();
    }
    
    private void cleanupGame() {
        if (gamePane != null) {
            gamePane.getChildren().clear();
        }
        gameManager = null;
        bird = null;
    }
    
    private void initializeGameComponents() {
        Obstacle.setScreenHeight(HEIGHT);
        createBird();
        createGameManager();
    }
    
    private void createBird() {
        bird = new Bird(WIDTH / 3, HEIGHT / 2);
        gamePane.getChildren().add(bird.getView());
    }
    
    private void createGameManager() {
        gameManager = new GameManager(gamePane, bird);
        // ScoreManager'ı UIManager'a ver
        gameManager.getUiManager().setScoreManager(scoreManager);
        gameManager.getUiManager().setMainMenuButtonAction(this::showMainMenu);
    }

    public static void main(String[] args) {
        launch(args);
    }
}