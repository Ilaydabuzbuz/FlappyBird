package com.example;

import javafx.scene.image.ImageView;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;

public class Bird {
    // Sabit değerleri daha açıklayıcı isimlerle tanımlayalım
    private static final double GRAVITY_FORCE = 0.5;
    private static final double JUMP_FORCE = -8.0;
    private static final double INITIAL_Y_POSITION = 200.0;
    private static final Size BIRD_SIZE = new Size(80, 80);
    
    private final Position position;
    private double velocity;
    private final ImageView birdImageView;

    public Bird(double initialX, double initialY) {
        this.position = new Position(initialX, initialY);
        this.velocity = 0;
        this.birdImageView = createBirdImageView();
        updateBirdPosition();
    }

    private ImageView createBirdImageView() {
        ImageView view = new ImageView();
        view.setFitWidth(BIRD_SIZE.width());
        view.setFitHeight(BIRD_SIZE.height());
        setBirdImage(view);
        return view;
    }

    private void setBirdImage(ImageView view) {
        var imageStream = getClass().getResourceAsStream("/bird.png");
        if (imageStream == null) {
            throw new IllegalStateException("Kuş resmi yüklenemedi: /bird.png dosyası bulunamadı");
        }
        view.setImage(new Image(imageStream));
    }

    public void update() {
        updateVelocity();
        updatePosition();
        updateBirdPosition();
    }

    private void updateVelocity() {
        velocity += GRAVITY_FORCE;
    }

    private void updatePosition() {
        position.setY(position.getY() + velocity);
    }

    private void updateBirdPosition() {
        birdImageView.setX(position.getX() - BIRD_SIZE.width() / 2);
        birdImageView.setY(position.getY() - BIRD_SIZE.height() / 2);
    }

    public void jump() {
        velocity = JUMP_FORCE;
    }

    public void reset() {
        position.setY(INITIAL_Y_POSITION);
        velocity = 0;
        updateBirdPosition();
    }

    public Node getView() {  // Circle yerine Node döndürüyoruz
        return birdImageView;
    }
    
    public boolean collidesWith(Obstacle obstacle) {
        return birdImageView.getBoundsInParent().intersects(obstacle.getBounds());
    }
    
    public double getY() {
        return position.getY();
    }
    
    public Bounds getBounds() {
        return birdImageView.getBoundsInParent();
    }
    
    public double getX() {
        return position.getX();
    }
}

// Yardımcı sınıflar
class Position {
    private final double x;
    private double y;  // final olmaktan çıkar
    
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void setY(double newY) {
        this.y = newY;
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
}

record Size(double width, double height) {}