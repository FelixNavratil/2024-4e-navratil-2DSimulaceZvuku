package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * TODO udelej hledani bodu vice efektivni
 */
public class SoundWave extends Circle {
    private double x;          // Starting X position
    private double y;          // Starting Y position
    private int currentRadius = 1;   // The current radius of the wave
    private int amplitude;
    Map<Point, Integer> pointMap = new HashMap<>();

    // **New field to store the mathematical representation of the circle**
    private String mathRepresentation;

    // Getter for the field mathRepresentation
    public String getMathRepresentation() {
        return mathRepresentation;
    }

    /**
     * Calculates the mathematical representation of the sound wave circle in the form:
     *      (x − a)² + (y − b)² = r²
     * where (a, b) is the center of the circle and r is the current radius.
     *
     * @return The mathematical representation of the circle as a String.
     */
    private String calculateMathRepresentation() {
        double a = this.x;             // X-coordinate of the center
        double b = this.y;             // Y-coordinate of the center
        double r = this.currentRadius; // Radius of the circle

        // Format the representation using the circle equation
        return String.format("(x - %.1f)^2 + (y - %.1f)^2 = %.1f^2", a, b, r);
    }

    public int getAmplitude() {
        return amplitude;
    }

    private final int PERIODA = 1;
    private int okamzitaVychylka;

    // New field to control the direction (1 for normal, -1 for opposite/progressing backwards)
    private int direction;

    public int getDirection() {
        return direction;
    }

    // promene na cas
    private long creationTime; // Timestamp when the wave was created
    private long elapsedPausedTime = 0; // Time we've spent paused
    private long pauseStartTime; // When the wave was paused
    private boolean isPaused = false; // Is the wave currently paused?


    // Getter to calculate elapsedTime dynamically
    public double getElapsedTime() {
        if (isPaused) {
            // If paused, return the time until the pause started
            return (pauseStartTime - creationTime - elapsedPausedTime) / 1000.0; // Convert ms to seconds
        } else {
            // If not paused, return total elapsed time
            return (System.currentTimeMillis() - creationTime - elapsedPausedTime) / 1000.0; // Convert ms to seconds
        }
    }


    private Calculator calculator = new Calculator();
    private BaseRoomControllerInterface controller;



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


    public int[] getReflectionDistances(){

        Point topLeft = controller.getRoomCorners().get(0);
        Point bottomLeft = controller.getRoomCorners().get(1);
        Point bottomRight = controller.getRoomCorners().get(2);
        Point topRight = controller.getRoomCorners().get(3);

        //jestli je to v mistnosti
        if (isInRectangle(x,y) ) {

            int[] distances = new int[4];
            distances[0] = (int) center.distance(getIntersectionsWithWalls(x,y)[0]);
            distances[1] = (int) center.distance(getIntersectionsWithWalls(x,y)[1]);
            distances[2] = (int) center.distance(getIntersectionsWithWalls(x,y)[2]);
            distances[3] = (int) center.distance(getIntersectionsWithWalls(x,y)[3]);

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

            //napravo nahore
        } else if (isAboveOnTheRightOfRectangle(x,y)){
            int[] distances = new int[3];
            distances[0] = (int) center.distance(topLeft);
            distances[1] = (int) center.distance(bottomLeft);
            distances[2] = (int) center.distance(bottomRight);
            return distances;

            //napravo dole
        }else if (isBellowOnTheRightOfRectangle(x,y)){
            int[] distances = new int[3];
            distances[0] = (int) center.distance(topRight);
            distances[1] = (int) center.distance(topLeft);
            distances[2] = (int) center.distance(bottomLeft);
            return distances;

            //nalevo nahore
        }else if (isAboveOnTheLeftOfRectangle(x,y)){
            int[] distances = new int[3];
            distances[0] = (int) center.distance(bottomLeft);
            distances[1] = (int) center.distance(bottomRight);
            distances[2] = (int) center.distance(topRight);
            return distances;

            //nalevo dole
        }else if (isBellowOnTheLeftOfRectangle(x,y)){
            int[] distances = new int[3];
            distances[0] = (int) center.distance(bottomRight);
            distances[1] = (int) center.distance(topRight);
            distances[2] = (int) center.distance(topLeft);
            return distances;

        } else{

            System.err.println("Error: The x and y coordinates are not valid.");
            System.out.println("x coordinate: " + x);
            System.out.println("y coordinate: " + y);
            System.err.println("Error: The x and y coordinates are not valid.");
            return null;
        }
    }

    //ziskani pruseciku
    private Point[] getIntersectionsWithWalls(double x, double y){

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

    /**
     * Calculates the instantaneous displacement (okamžitá výchylka) of the sound wave
     * based on its current phase within the oscillation cycle.
     * The calculation depends on the amplitude, oscillation direction, and elapsed time.
     *
     * @return The instantaneous displacement of the wave. Positive, negative, or zero values indicate
     *         the wave's position relative to its equilibrium point during its oscillation cycle.
     *
     *
     */
    public int getokamzitaVychylka() {
        if (direction == 1) {
            // Ensure amplitude is positive
            amplitude = Math.max(amplitude, 0);

            // Cycle duration (perioda) is fixed to 1 second
            double cycleDuration =  PERIODA;

            // Total number of steps (for full cycle)
            int stepsToAmplitude = amplitude;             // 0 to +amplitude
            int stepsToNegativeAmplitude = 2 * amplitude; // +amplitude to -amplitude
            int stepsToZero = amplitude;                  // -amplitude back to 0
            int totalSteps = stepsToAmplitude + stepsToNegativeAmplitude + stepsToZero;

            // Derive time per step dynamically
            double timePerStep = cycleDuration / totalSteps;

            // Normalize elapsedTime to the cycle (loop every cycleDuration)
            double timeInCycle = getElapsedTime() % cycleDuration;

            // Calculate which step we are in based on elapsed time
            int currentStep = (int) (timeInCycle / timePerStep);

            // Determine which phase of the cycle we're in
            if (currentStep < stepsToAmplitude) {
                // Phase 1: 0 to +amplitude
                return currentStep;
            } else if (currentStep < stepsToAmplitude + stepsToNegativeAmplitude) {
                // Phase 2: +amplitude to -amplitude
                return amplitude - (currentStep - stepsToAmplitude);
            } else {
                // Phase 3: -amplitude back to 0
                return -amplitude + (currentStep - (stepsToAmplitude + stepsToNegativeAmplitude));
            }

        }else{
            // Ensure amplitude is positive
            amplitude = Math.max(amplitude, 0);

            // Cycle duration (perioda) is fixed to 1 second
            double cycleDuration =  PERIODA;

            // Total number of steps (for full cycle)
            int stepsToAmplitude = amplitude;             // 0 to -amplitude
            int stepsToPositiveAmplitude = 2 * amplitude; // -amplitude to +amplitude
            int stepsToZero = amplitude;                  // +amplitude back to 0
            int totalSteps = stepsToAmplitude + stepsToPositiveAmplitude + stepsToZero;

            // Derive time per step dynamically
            double timePerStep = cycleDuration / totalSteps;

            // Normalize elapsedTime to the cycle (loop every cycleDuration)
            double timeInCycle = getElapsedTime() % cycleDuration;

            // Calculate which step we are in based on elapsed time
            int currentStep = (int) (timeInCycle / timePerStep);

            // Determine which phase of the cycle we're in
            if (currentStep < stepsToAmplitude) {
                // Phase 1: 0 to -amplitude
                return -currentStep;
            } else if (currentStep < stepsToAmplitude + stepsToPositiveAmplitude) {
                // Phase 2: -amplitude to +amplitude
                return -amplitude + (currentStep - stepsToAmplitude);
            } else {
                // Phase 3: +amplitude back to 0
                return amplitude - (currentStep - (stepsToAmplitude + stepsToPositiveAmplitude));
            }
        }

    }


    public Point getCenter() {
        return center;
    }







    /**
     * Constructor initializes the SoundWave object, its position, and relevant fields.
     * It also generates the mathematical representation of the sound wave circle.
     */
    public SoundWave(double x, double y, BaseRoomControllerInterface controller, int radius, int okamzitaVychylka, int amplitude, int direction) {
        super(x, y, 0); // Initialize circle with position (x, y) and radius 0
        this.x = x;
        this.y = y;
        this.currentRadius = radius;
        this.amplitude = amplitude;
        this.okamzitaVychylka = okamzitaVychylka;
        this.direction = direction;

        // Initialize the room and geometric properties
        this.controller = controller;
        roomWalls = controller.getRoomWalls();
        center = new Point(x, y);
        this.creationTime = System.currentTimeMillis(); // Store creation time
        initializeLines(x, y);

        // Generate the mathematical representation
        this.mathRepresentation = calculateMathRepresentation();

        // Initialize other properties
        this.setStrokeWidth(1);          // Outline thickness
        this.setFill(Color.TRANSPARENT); // Transparent fill
        this.setMouseTransparent(true);

        topLeft = controller.getRoomCorners().get(0);
        bottomLeft = controller.getRoomCorners().get(1);
        bottomRight = controller.getRoomCorners().get(2);
        topRight = controller.getRoomCorners().get(3);
        intersections = getIntersectionsWithWalls(x, y);

        updateColor(PERIODA);
    }

    /**
     * Grows the wave's radius by a fixed amount (can be dynamic based on time).
     */
    public void grow() {

        currentRadius += 1;  // Example growth logic (adjust increment as needed)
        this.setRadius(currentRadius);

        // Update the mathematical representation when the wave grows
        this.mathRepresentation = calculateMathRepresentation();

        listPointsInCircleOptimizedFilteredEarly();
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

    public boolean isInRectangle(int x, int y){
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

    public boolean isAboveOnTheRightOfRectangle(double x, double y){
        return x > controller.getXMax() && y < controller.getYMin();
    }

    public boolean isAboveOnTheLeftOfRectangle(double x, double y){
        return x < controller.getXMin() && y < controller.getYMin();
    }

    public boolean isBellowOnTheRightOfRectangle(double x, double y){
        return x > controller.getXMax() && y > controller.getYMax();
    }

    public boolean isBellowOnTheLeftOfRectangle(double x, double y){
        return x < controller.getXMin() && y > controller.getYMax();
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

    /**
     * Returns a mathematical representation of the sound wave circle in the form:
     *      (x − a)² + (y − b)² = r²
     * where (a, b) is the center of the circle and r is the current radius.
     * This represents the circle as a set of all points that satisfy the equation.
     *
     * @return A String representing the mathematical representation of the circle.
     */
    public String mathematicalRepresentation() {
        // Circle center coordinates
        double a = this.getCenterX(); // X-coordinate of the center
        double b = this.getCenterY(); // Y-coordinate of the center
        double r = this.getRadius();  // Current radius of the sound wave

        // Format the mathematical representation
        return String.format("(x - %.1f)^2 + (y - %.1f)^2 = %.1f^2", a, b, r);
    }


    public void listPointsInCircleOptimizedFilteredEarly() {
        double a = this.x;                  // Circle center X-coordinate
        double b = this.y;                  // Circle center Y-coordinate
        int r = this.currentRadius;         // Radius of the circle
        int okamzitaVychylkaValue = getokamzitaVychylka();

        // Midpoint Circle Algorithm
        int x = 0;
        int y = r;
        int d = 1 - r; // Decision variable

        // Add only points that may fall in the rectangle
        addCirclePointsFilteredEarly(x, y, a, b, okamzitaVychylkaValue);

        while (x <= y) {
            if (d < 0) {
                d = d + 2 * x + 3; // Move horizontally
            } else {
                d = d + 2 * (x - y) + 5; // Move diagonally
                y--;
            }
            x++;

            // Add only points that may be in the rectangle
            addCirclePointsFilteredEarly(x, y, a, b, okamzitaVychylkaValue);
        }

        // Confirm total points added
        //System.out.println("Total points added (early filtered): " + pointMap.size());
    }

    /**
     * Adds only points that are within the rectangle after basic symmetry checks.
     */
    private void addCirclePointsFilteredEarly(int x, int y, double a, double b, int okamzitaVychylkaValue) {
        // Early filtering: Check if at least one symmetric point is in the rectangle
        if (isAnySymmetricPointInRectangle(x, y, a, b)) {
            // Add only valid symmetric points
            addPointToMapIfInRectangle((int) (a + x), (int) (b + y), okamzitaVychylkaValue); // Quadrant 1
            addPointToMapIfInRectangle((int) (a - x), (int) (b + y), okamzitaVychylkaValue); // Quadrant 2
            addPointToMapIfInRectangle((int) (a + x), (int) (b - y), okamzitaVychylkaValue); // Quadrant 4
            addPointToMapIfInRectangle((int) (a - x), (int) (b - y), okamzitaVychylkaValue); // Quadrant 3
            addPointToMapIfInRectangle((int) (a + y), (int) (b + x), okamzitaVychylkaValue); // Transposed 1
            addPointToMapIfInRectangle((int) (a - y), (int) (b + x), okamzitaVychylkaValue); // Transposed 2
            addPointToMapIfInRectangle((int) (a + y), (int) (b - x), okamzitaVychylkaValue); // Transposed 3
            addPointToMapIfInRectangle((int) (a - y), (int) (b - x), okamzitaVychylkaValue); // Transposed 4
        }
    }

    /**
     * Adds a point to the map if it lies within the allowed rectangle.
     *
     * @param x                     The x-coordinate of the point.
     * @param y                     The y-coordinate of the point.
     * @param okamzitaVychylkaValue The value to associate with the point in the map.
     */
    private void addPointToMapIfInRectangle(int x, int y, int okamzitaVychylkaValue) {
        // Check if the point is within the specified rectangle
        if (isInRectangle(x, y)) {
            // If the point is inside the rectangle, add it to the map
            Point point = new Point(x, y);
            if (!pointMap.containsKey(point)) { // Avoid adding duplicate points
                pointMap.put(point, okamzitaVychylkaValue);

                // Optional: Print for debugging purposes
                //System.out.println("Point (" + x + ", " + y + ") added with okamzitaVychylka = " + okamzitaVychylkaValue);
            }
        }
    }

    /**
     * Checks if any of the 8 symmetric points for (x, y) are inside the rectangle.
     */
    private boolean isAnySymmetricPointInRectangle(int x, int y, double a, double b) {
        return isInRectangle((int) (a + x), (int) (b + y)) ||
                isInRectangle((int) (a - x), (int) (b + y)) ||
                isInRectangle((int) (a + x), (int) (b - y)) ||
                isInRectangle((int) (a - x), (int) (b - y)) ||
                isInRectangle((int) (a + y), (int) (b + x)) ||
                isInRectangle((int) (a - y), (int) (b + x)) ||
                isInRectangle((int) (a + y), (int) (b - x)) ||
                isInRectangle((int) (a - y), (int) (b - x));
    }




}