package com.example;

import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;
import java.util.ArrayList;

public class BackgroundManager {
    private static final double SCROLL_SPEED = 1.0;
    private final List<ImageView> backgrounds;
    private final Pane gamePane;
    
    public BackgroundManager(Pane gamePane) {
        this.gamePane = gamePane;
        this.backgrounds = new ArrayList<>();
        setupBackground();
    }
    
    private void setupBackground() {
        try {
            Image bgImage = new Image(getClass().getResourceAsStream("/background.png"));
            if (bgImage.isError()) {
                throw new IllegalStateException("Arkaplan resmi yüklenemedi");
            }
            
            double paneWidth = gamePane.getPrefWidth() > 0 ? gamePane.getPrefWidth() : 800;
            double paneHeight = gamePane.getPrefHeight() > 0 ? gamePane.getPrefHeight() : 600;
            
            for (int i = 0; i < 2; i++) {
                ImageView background = new ImageView(bgImage);
                background.setFitHeight(paneHeight);
                background.setFitWidth(paneWidth);
                background.setX(i * paneWidth);
                background.setY(0);
                background.setPreserveRatio(false);
                
                backgrounds.add(background);
                gamePane.getChildren().add(0, background);
            }
        } catch (Exception e) {
            throw new RuntimeException("Arkaplan kurulumu başarısız oldu", e);
        }
    }
    
    public void update() {
        if (backgrounds.isEmpty()) return;
        
        for (ImageView bg : backgrounds) {
            bg.setX(bg.getX() - SCROLL_SPEED);
            
            if (bg.getX() + bg.getFitWidth() <= 0) {
                bg.setX(backgrounds.stream()
                        .mapToDouble(ImageView::getX)
                        .max()
                        .orElse(0) + bg.getFitWidth());
            }
        }
    }
    
    public void reset() {
        for (int i = 0; i < backgrounds.size(); i++) {
            backgrounds.get(i).setX(i * backgrounds.get(0).getFitWidth());
        }
    }
} 