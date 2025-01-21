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
    private int currentRadius = 1;   // The current radius of the wave
    private long creationTime; // Timestamp when the wave was created
    private long elapsedPausedTime = 0; // Time we've spent paused
    private long pauseStartTime; // When the wave was paused
    private boolean isPaused = false; // Is the wave currently paused?



    private Calculator calculator = new Calculator();
    private Room0Controller controller = new Room0Controller();



    private List<Line> waveLines;
    private List<Line> roomWalls;
    private Point center;
    private Point[] intersections;
    private Point topLeft;
    private Point topRight;
    private Point bottomLeft;
    private Point bottomRight;
    
    private void initializeLines(double x, double y){
        Line vertical = new Line(1,0,-x);
        Line horizontal = new Line(0,1,-y);
        waveLines = List.of(vertical, horizontal);
        System.out.println("Vertical Line: " + vertical.toString());
        System.out.println("Horizontal Line: " + horizontal.toString());
    }

    //zjisteni kdy vlna dosahne rohu
    public int[] getCornerDistances () {
        int[] distances = new int[4];


        // Calculate distances using the Point class's distance method
        distances[0] = (int)center.distance(topLeft);      // Distance to top-left corner
        distances[1] = (int)center.distance(bottomLeft);   // Distance to bottom-left corner
        distances[2] = (int)center.distance(bottomRight);  // Distance to bottom-right corner
        distances[3] = (int)center.distance(topRight);     // Distance to top-right corner

        return distances;
    }
    
    public Point[] getSymetricPoints(){
        
        if (isInRectangle(x, y)) {
            
            Point[] symetricPoints = new Point[4];
            symetricPoints[0] = calculator.calculateIntersection(waveLines.get(0), roomWalls.get(0));
            symetricPoints[1] = calculator.calculateIntersection(waveLines.get(0), roomWalls.get(1));
            symetricPoints[2] = calculator.calculateIntersection(waveLines.get(1), roomWalls.get(2));
            symetricPoints[3] = calculator.calculateIntersection(waveLines.get(1), roomWalls.get(3));
            
            for (Point point : symetricPoints) {
                if (point != null) {
                    System.out.println("Symmetric point coordinates: " + point.toString());
                }
            }
            return symetricPoints;
            
        } else if (isAboveRectangle(x, y)) {

            Point[] symetricPoints = new Point[1];
            symetricPoints[0] = calculator.calculateIntersection(waveLines.get(0), roomWalls.get(1));

            if (symetricPoints[0] != null) {
                System.out.println("Symmetric point coordinates: " + symetricPoints[0].toString());
            }
            return symetricPoints;
            
        } else if (isBellowRectangle(x, y)) {
            
            Point[] symetricPoints = new Point[1];
            symetricPoints[0] = calculator.calculateIntersection(waveLines.get(0), roomWalls.get(0));

            if (symetricPoints[0] != null) {
                System.out.println("Symmetric point coordinates: " + symetricPoints[0].toString());
            }
            return symetricPoints;

        } else if (isRightOfRectangle(x, y)) {
            
            Point[] symetricPoints = new Point[1];
            symetricPoints[0] = calculator.calculateIntersection(waveLines.get(1), roomWalls.get(2));

            if (symetricPoints[0] != null) {
                System.out.println("Symmetric point coordinates: " + symetricPoints[0].toString());
            }
            return symetricPoints;

        } else if (isLeftOfRectangle(x, y)) {
            
            Point[] symetricPoints = new Point[1];
            symetricPoints[0] = calculator.calculateIntersection(waveLines.get(1), roomWalls.get(3));
            if (symetricPoints[0] != null) {
                System.out.println("Symmetric point coordinates: " + symetricPoints[0].toString());
            }
            return symetricPoints;
            
        } else {
            return null;
        }
    }
    
    public int[] getReflectionDistances(){
        //jestli je to v mistnosti
        if (isInRectangle(x,y) ) {
            
            int[] distances = new int[4];
            distances[0] = (int) center.distance(getIntersectionsWithWalls(x,y)[0]);
            distances[1] = (int) center.distance(getIntersectionsWithWalls(x,y)[1]);
            distances[2] = (int) center.distance(getIntersectionsWithWalls(x,y)[2]);
            distances[3] = (int) center.distance(getIntersectionsWithWalls(x,y)[3]);


           /* System.out.println("Distance to top intersection: " + distances[0]);
            System.out.println("Distance to bottom intersection: " + distances[1]);
            System.out.println("Distance to left intersection: " + distances[2]);
            System.out.println("Distance to right intersection: " + distances[3]);*/
            return distances;

            //mezi lefou a pravou primkou ale nad horni primnkou
        } else if ( isAboveRectangle(x,y)){
            int[] distances = new int[1];
            distances[0] = (int) center.distance(getIntersectionsWithWalls(x,y)[0]);
            return distances;

            //mezi levou a pravou primkou ale pod spodni primkou
        } else if (isBellowRectangle(x,y)) {
            int[] distances = new int[1];
            distances[0] = (int) center.distance(getIntersectionsWithWalls(x,y)[0]);
            return distances;

            //nalevo od leve primky ale mezi horni a spodni primkou
        } else if ( isLeftOfRectangle(x,y)) {
            int[] distances = new int[1];
            distances[0] = (int) center.distance(getIntersectionsWithWalls(x,y)[0]);
            return distances;

            //napravo od prave primky ale mezi horni a spodni primkou
        } else if ( isRightOfRectangle(x,y)) {
            int[] distances = new int[1];
           distances[0] = (int) center.distance(getIntersectionsWithWalls(x,y)[0]);
           return distances;
        }else{

            System.err.println("Error: The x and y coordinates are not valid.");
            System.out.println("x coordinate: " + x);
            System.out.println("y coordinate: " + y);
            System.err.println("Error: The x and y coordinates are not valid.");
            return null;
        }
    }

    //ziskani pruseciku
    private Point[] getIntersectionsWithWalls(double x, double y){


        /*System.out.println("x: " + x);
        System.out.println("y: " + y);
        System.out.println("xMax: " + controller.getXMax());
        System.out.println("xMin: " + controller.getXMin());
        System.out.println("yMax: " + controller.getYMax());
        System.out.println("yMin: " + controller.getYMin());*/

        //jestli souradnice stredu jsou v mistnosti
        if (isInRectangle(x,y)) {

            Point topIntersection = calculator.calculateIntersection(waveLines.get(0), roomWalls.get(0));
            Point bottomIntersection = calculator.calculateIntersection(waveLines.get(0), roomWalls.get(1));
            Point leftIntersection = calculator.calculateIntersection(waveLines.get(1), roomWalls.get(2));
            Point rightIntersection = calculator.calculateIntersection(waveLines.get(1), roomWalls.get(3));

            return new Point[]{topIntersection, bottomIntersection, leftIntersection, rightIntersection};

            //mezi lefou a pravou primkou ale nad horni primnkou
        } else if ( isAboveRectangle(x,y)){

            Point bottomIntersection = calculator.calculateIntersection(waveLines.get(0), roomWalls.get(1));

            return new Point[]{bottomIntersection};

            //mezi levou a pravou primkou ale pod spodni primkou
        } else if ( isBellowRectangle(x,y)) {

            Point topIntersection = calculator.calculateIntersection(waveLines.get(0), roomWalls.get(0));
            return new Point[]{topIntersection};

            //nalevo od leve primky ale mezi horni a spodni primkou
        } else if ( isLeftOfRectangle(x,y)) {

            Point rightIntersection = calculator.calculateIntersection(waveLines.get(1), roomWalls.get(3));
            return new Point[]{rightIntersection};

            //napravo od prave primky ale mezi horni a spodni primkou
        } else if ( isRightOfRectangle(x,y)) {

            Point leftIntersection = calculator.calculateIntersection(waveLines.get(1), roomWalls.get(2));
            return new Point[]{leftIntersection};
        }else {
            return null;
        }
    }
    
    
    public Point getCenter() {
        return center;
    }
    
    public Point[] getIntersections() {
        return intersections;
    }

    public List<Line> getWaveLines() {
        return waveLines;
    }

    public SoundWave(double x, double y, Room0Controller controller, int radius) {
        super(x, y, 0); // Initialize circle with position (x, y) and radius 0
        this.x = x;
        this.y = y;
        this.currentRadius = radius;
        //zjistí jaký kontroller používá (V jaké místnosti je)
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
        
        topLeft = controller.getRoomCorners().get(0);
        bottomLeft = controller.getRoomCorners().get(1);
        bottomRight = controller.getRoomCorners().get(2);
        topRight = controller.getRoomCorners().get(3);
        
        
        //vypocitani pruseciků
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

    public int getCurrentRadius() {
        return currentRadius;
    }

    public void pause() {
        if (!isPaused) {
            pauseStartTime = System.currentTimeMillis();
            isPaused = true;
        }
    }

    public void resume() {
        if (isPaused) {
            long now = System.currentTimeMillis();
            elapsedPausedTime += (now - pauseStartTime); // Add the time spent paused
            isPaused = false;
        }
    }
    
    public boolean isInRectangle(double x, double y){
        return x > controller.getXMin() && x < controller.getXMax() && y > controller.getYMin() && y < controller.getYMax();
    }
    
    public boolean isBellowRectangle(double x, double y){
        return x > controller.getXMin() && x < controller.getXMax() && y > controller.getYMax();
    }
    
    public boolean isAboveRectangle(double x, double y){
        return x > controller.getXMin() && x < controller.getXMax() && y < controller.getYMin();
    }
    
    public boolean isLeftOfRectangle(double x, double y){
        return x < controller.getXMin() && y > controller.getYMin() && y < controller.getYMax();
    }
    
    public boolean isRightOfRectangle(double x, double y){
        return x > controller.getXMax() && y > controller.getYMin() && y < controller.getYMax();
    }



    /**
     * Checks if the wave is older than a specified lifetime (in milliseconds).
     * @param maxAge The lifetime in milliseconds (e.g., 5000 for 5 seconds).
     * @return True if the wave is older than the given lifespan, false otherwise.
     */
    public boolean isOlderThan(long maxAge) {
        long now = System.currentTimeMillis();
        long effectiveAge;
        if (isPaused) {
            effectiveAge = (pauseStartTime - creationTime) - elapsedPausedTime; // Paused state
        } else {
            effectiveAge = (now - creationTime) - elapsedPausedTime; // Running state
        }
        return effectiveAge > maxAge;
    }
}