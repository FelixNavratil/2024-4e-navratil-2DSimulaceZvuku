package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.HexFormat;

public class SoundWave extends Circle {
    private double x;          // Starting X position
    private double y;          // Starting Y position
    private double currentRadius;   // The current radius of the wave
    private long creationTime; // Timestamp when the wave was created




    public SoundWave(double x, double y) {
        super(x, y, 0); // Initialize circle with position (x, y) and radius 0
        this.creationTime = System.currentTimeMillis(); // Store creation time

        // Set the stroke (outline) color and make the fill transparent
        this.setStrokeWidth(5);           // Outline thickness
        this.setFill(Color.TRANSPARENT);  // Transparent inside
        updateColor(1);
        this.setMouseTransparent(true);// Optional: Ignore mouse events on the wave
    }

    /**
     * Grows the wave's radius by a fixed amount (can be dynamic based on time).
     */
    public void grow() {
        currentRadius += 1;  // Example growth logic (adjust increment as needed)
        this.setRadius(currentRadius);
        //updateColor();
    }

    private void updateColor(double perioda){
        Timeline colorTransition = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(this.strokeProperty(), Color.LIGHTGRAY)
                ),
                new KeyFrame(Duration.seconds(perioda/2),
                        new KeyValue(this.strokeProperty(), Color.BLACK)
                ),
                new KeyFrame(Duration.seconds(perioda),
                        new KeyValue(this.strokeProperty(), Color.LIGHTGRAY)
                )
        );
        colorTransition.setCycleCount(Timeline.INDEFINITE); // Keep repeating
        colorTransition.setAutoReverse(true); // Reverse the transition after completing
        colorTransition.play();
    }



    // Getters for wave properties
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getCurrentRadius() {
        return currentRadius;
    }


    /**
     * Checks if the wave is older than a specified lifetime (in milliseconds).
     * @param lifespan The lifetime in milliseconds (e.g., 5000 for 5 seconds).
     * @return True if the wave is older than the given lifespan, false otherwise.
     */
    public boolean isOlderThan(long lifespan) {
        long currentTime = System.currentTimeMillis();
        return (currentTime - creationTime) > lifespan; // Check if the wave is older than the lifespan
    }

}