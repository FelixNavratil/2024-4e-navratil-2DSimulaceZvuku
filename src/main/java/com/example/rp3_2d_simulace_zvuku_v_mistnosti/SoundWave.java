package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.List;

public class SoundWave extends Circle {
    private double x;          // Starting X position
    private double y;          // Starting Y position
    private double currentRadius;   // The current radius of the wave
    private long creationTime; // Timestamp when the wave was created
    private IntersectionCalculator calculator = new IntersectionCalculator();
    private Room0Controller controller = new Room0Controller();
    private List<Line> waveLines;
    private List<Line> roomWalls;
    private Point center;
    private Point[] intersections;

    
    private void initializeLines(double x, double y){
        Line vertical = new Line(1,0,-x);
        Line horizontal = new Line(0,1,-y);
        waveLines = List.of(vertical, horizontal);
        System.out.println("Vertical Line: " + vertical.toString());
        System.out.println("Horizontal Line: " + horizontal.toString());
    }

    //ziskani pruseciku
    private Point[] getIntersectionsWithWalls(double x, double y){

        System.out.println("x: " + x);
        System.out.println("y: " + y);
        System.out.println("xMax: " + controller.getXMax());
        System.out.println("xMin: " + controller.getXMin());
        System.out.println("yMax: " + controller.getYMax());
        System.out.println("yMin: " + controller.getYMin());

        //jestli souradnice stredu jsou v mistnosti
        if (    x > controller.getXMin() &&
                x < controller.getXMax() &&
                y > controller.getYMin() &&
                y < controller.getYMax() ) {

            Point topIntersection = calculator.calculateIntersection(waveLines.get(0), roomWalls.get(0));
            Point bottomIntersection = calculator.calculateIntersection(waveLines.get(0), roomWalls.get(1));
            Point leftIntersection = calculator.calculateIntersection(waveLines.get(1), roomWalls.get(2));
            Point rightIntersection = calculator.calculateIntersection(waveLines.get(1), roomWalls.get(3));

            return new Point[]{topIntersection, bottomIntersection, leftIntersection, rightIntersection};

            //mezi lefou a pravou primkou ale nad horni primnkou
        } else if ( x > controller.getXMin() &&
                    x < controller.getXMax() &&
                    y < controller.getYMin()){

            Point bottomIntersection = calculator.calculateIntersection(waveLines.get(0), roomWalls.get(1));

            return new Point[]{bottomIntersection};

            //mezi levou a pravou primkou ale pod spodni primkou
        } else if ( x > controller.getXMin() &&
                    x < controller.getXMax() &&
                    y > controller.getYMax()) {

            Point topIntersection = calculator.calculateIntersection(waveLines.get(0), roomWalls.get(0));
            return new Point[]{topIntersection};

            //nalevo od leve primky ale mezi horni a spodni primkou
        } else if ( x < controller.getXMin() &&
                    y > controller.getYMin() &&
                    y < controller.getYMax()) {

            Point rightIntersection = calculator.calculateIntersection(waveLines.get(1), roomWalls.get(3));
            return new Point[]{rightIntersection};

            //napravo od prave primky ale mezi horni a spodni primkou
        } else if ( x > controller.getXMax() &&
                    y > controller.getYMin() &&
                    y < controller.getYMax()) {

            Point topIntersection = calculator.calculateIntersection(waveLines.get(0), roomWalls.get(0));
            Point leftIntersection = calculator.calculateIntersection(waveLines.get(1), roomWalls.get(2));
            Point bottomIntersection = calculator.calculateIntersection(waveLines.get(0), roomWalls.get(1));
            return new Point[]{topIntersection, leftIntersection, bottomIntersection};
        }else {
            return null;
        }
    }


    public List<Line> getWaveLines() {
        return waveLines;
    }

    public SoundWave(double x, double y, Room0Controller controller) {
        super(x, y, 0); // Initialize circle with position (x, y) and radius 0
        this.x = x;
        this.y = y;
        this.controller = controller;
        roomWalls = controller.getRoomWalls();
        //iniccializace stredu
        center = new Point(x,y);
        this.creationTime = System.currentTimeMillis(); // Store creation time
        initializeLines(x,y);
        // Set the stroke (outline) color and make the fill transparent
        this.setStrokeWidth(5);           // Outline thickness
        this.setFill(Color.TRANSPARENT);  // Transparent inside
        updateColor(1);
        this.setMouseTransparent(true);// Optional: Ignore mouse events on the wave
        intersections =  getIntersectionsWithWalls(x,y);
        if (intersections != null) {
            for (Point intersection : intersections) {
                System.out.println(intersection.toString());
            }
        }

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