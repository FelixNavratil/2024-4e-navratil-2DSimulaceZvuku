package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.*;


/**
 * TODO udelej hledani bodu vice efektivni zkus Bresenham’s Circle Drawing Algorithm
 */
public class SoundWave extends Circle {
    private double x;          // Starting X position
    private double y;          // Starting Y position
    private int outerRadius;   // The current radius of the wave
    private final int deltaR = 60;   // Fixed difference between radii (donut thickness)
    private int innerRadius = 0;      // Inner circle radius (calculated as currentRadius - deltaR)

    private Circle innerCircle; // Circle object to represent the inner boundary (when it appears)
    private boolean innerCircleIsCreated = false;


    private int amplitude;
    PixelManager pixelManager;
    Pixel[][] pixelGrid;
    private int xMin, xMax, yMin, yMax;

    //nezapomen zmenit periodu i v soundWave
    public  int PERIODA = 3;
    private int okamzitaVychylka;

    /**
     * Constructor initializes the SoundWave object, its position, and relevant fields.
     * It also generates the mathematical representation of the sound wave circle.
     */
    public SoundWave(double x, double y, BaseRoomControllerInterface controller, int radius, int okamzitaVychylka, int amplitude, int direction) {
        super(x, y, 0); // Initialize circle with position (x, y) and radius 0
        this.x = x;
        this.y = y;
        this.outerRadius = radius;
        this.amplitude = amplitude;
        this.okamzitaVychylka = okamzitaVychylka;
        this.direction = direction;

        // Initialize the room and geometric properties
        this.controller = controller;
        roomWalls = controller.getRoomWalls();
        center = new Point(x, y);
        this.creationTime = System.currentTimeMillis(); // Store creation time
        initializeLines(x, y);


        this.xMin = controller.getXMin();
        this.xMax = controller.getXMax();
        this.yMin = controller.getYMin();
        this.yMax = controller.getYMax();


        // Initialize other properties
        this.setStroke( Color.BLACK);
        this.setStrokeWidth(1);          // Outline thickness
        this.setFill(Color.TRANSPARENT); // Transparent fill
        this.setMouseTransparent(true);

        topLeft = controller.getRoomCorners().get(0);
        bottomLeft = controller.getRoomCorners().get(1);
        bottomRight = controller.getRoomCorners().get(2);
        topRight = controller.getRoomCorners().get(3);
        intersections = getIntersectionsWithWalls(x, y);

        // Initialize the inner circle (it will be made visible later)
        /*
        innerCircle = new Circle();
        innerCircle.setCenterX(x);
        innerCircle.setCenterY(y);
        innerCircle.setStrokeWidth(2.0);
        innerCircle.setFill(null); // Ensures it's a "ring"
        innerCircle.setStroke(javafx.scene.paint.Color.BLUE); // Match outer circle stroke color

         */

        //updateColor(PERIODA);
    }

    private void createInnerCircle(double x , double y, int innerRadius) {
        innerCircle = new Circle(x, y, innerRadius);
        innerCircle.setMouseTransparent(true);
        innerCircle.setStroke(Color.RED);
        innerCircle.setStrokeWidth(1);
        innerCircle.setFill(null);
    }

    public Circle getInnerCircle() {
        if (outerRadius >deltaR)return innerCircle;
        else return null;
    }

    public int getDeltaR() {
        return deltaR;
    }

    public void setPixelManager(PixelManager pixelManager){
        this.pixelManager = pixelManager;
        pixelGrid = pixelManager.getPixelGrid();
    }
    

    public int getAmplitude() {
        return amplitude;
    }



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

            // Cycle duration (perioda)
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


/*
    public int getokamzitaVychylka() {
        return 100;
    }*/


    public Point getCenter() {
        return center;
    }


    
/*
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
*/
    public int getOuterRadius() {
        return outerRadius;
    }

    public int getInnerRadius() {
        return innerRadius; // Inner radius
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
        return x > xMin && x < xMax && y > yMin && y < yMax;
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


    public void grow() {
// Increment the outer radius
        outerRadius += 1;  // Example growth increment

        this.setRadius(outerRadius);

        // Handle inner circle creation and growth
        if (outerRadius > deltaR) {
            if (!innerCircleIsCreated) {
                // Create inner circle when threshold is met
                innerRadius = outerRadius - deltaR;
                createInnerCircle(x, y, innerRadius);
                innerCircleIsCreated = true;
            } else {
                // Grow inner circle incrementally as outer radius grows
                innerRadius += 1;
                innerCircle.setRadius(innerRadius);
            }
        }

        // Generate the donut wave dynamically
        generateDonutWithOkamzitaVychylka();

        // Reset visited and duplicate pixel sets for recalculating transitions
        resetVisitedPixels();
    }



    // Use a Set to track pixels added
    private final Set<PixelCoordinate> visitedPixels = new HashSet<>();
    private final Set<PixelCoordinate> duplicatePixels = new HashSet<>(); // To store duplicates for debugging
    private final Set<PixelCoordinate> activePixelCoordinates = new HashSet<>(); // Track coordinates of active pixels

    public Set<PixelCoordinate> getactivePixelCoordinates   () {
        return activePixelCoordinates;
    }

    /**
     * Generates concentric circles for the sound wave and updates the `okamzitaVychylka`
     * for every pixel in the donut (between innerRadius and outerRadius).
     */
    public void generateDonutWithOkamzitaVychylka() {
        // Calculate the wave width
        int waveWidth = outerRadius - innerRadius;

        // Pixel granularity for the wave
        int pixelSize = (int) pixelManager.getPixelSize();

        if (waveWidth > pixelSize) {
            int numberOfCircles = waveWidth / pixelSize;

            // Divide the circles into four equal segments
            int segmentSize = numberOfCircles / 4;

            //System.out.println("Wave Width: " + waveWidth);
            //System.out.println("Pixel Size: " + pixelSize);
            //System.out.println("Number of Circles: " + numberOfCircles);
            //System.out.println("Segment Size: " + segmentSize);

            // Increment for amplitude change in each segment
            int amplitudeIncrement = amplitude / segmentSize;

            // Iterate for all concentric circles (outer to inner)
            for (int i = 0; i < numberOfCircles; i++) {
                // Calculate the radius for the current circle
                int radius = innerRadius + ((numberOfCircles - i - 1) * pixelSize);

                // Determine instantaneous displacement
                double amplitudeForCircle = calculateAmplitudeForCircle(i, segmentSize, amplitudeIncrement);

                // Number circles, outermost is "1"
                int circleNumber = i + 1;

                // Debug output
                //System.out.println("Circle Number: " + circleNumber + ", Radius: " + radius);

                // Draw the circle and update pixels
                drawCircleWithOkamzitaVychylka((int) x, (int) y, radius, (int) amplitudeForCircle);
            }
        }

        // Output separator
        //System.out.println("----------------------------------------------");
    }

    /**
     * Resets the sets tracking visited and duplicate pixels for the next update.
     */
    public void resetVisitedPixels() {
        visitedPixels.clear();
        duplicatePixels.clear();
        activePixelCoordinates.clear(); // Reset active pixels as well
    }

    /**
     * Determines the amplitude (okamzitaVychylka) for the given circle index
     * based on its position in the wave cycle.
     */
    private double calculateAmplitudeForCircle(int circleIndex, int segmentSize, int amplitudeIncrement) {
        if (circleIndex < segmentSize) {
            //System.out.println("Circle Index: " + circleIndex + ", Amplitude Increment: " + amplitudeIncrement);
            // First segment: Increase from 0 to maximum amplitude
            return +amplitudeIncrement;
        } else if (circleIndex < 2 * segmentSize) {
            //System.out.println("Circle Index: " + circleIndex + ", Amplitude Increment: " + amplitudeIncrement);
            // Second segment: Decrease from maximum amplitude back to 0
            return -amplitudeIncrement;
        } else if (circleIndex < 3 * segmentSize) {
            //System.out.println("Circle Index: " + circleIndex + ", Amplitude Increment: " + amplitudeIncrement);
            // Third segment: Decrease from 0 to maximum negative amplitude
            return -amplitudeIncrement;
        } else {
            //System.out.println("Circle Index: " + circleIndex + ", Amplitude Increment: " + amplitudeIncrement);
            // Fourth segment: Increase from maximum negative amplitude back to 0
            return +amplitudeIncrement;
        }
    }

    /**
     * Draws a circle using Bresenham's algorithm and adds `okamzitaVychylka` to each pixel.
     */
    private void drawCircleWithOkamzitaVychylka(int centerX, int centerY, int radius, int okamzitaVychylkaValue) {
        int x = 0;
        int y = radius;
        int d = 3 - 2 * radius;

        // Set initial circle pixels
        setCirclePixelsDisplacement(centerX, centerY, x, y, okamzitaVychylkaValue);
        while (y >= x) {
            x++;
            if (d > 0) {
                y--;
                d = d + 4 * (x - y) + 10;
            } else {
                d = d + 4 * x + 6;
            }

            // Set the pixels for this circle
            setCirclePixelsDisplacement(centerX, centerY, x, y, okamzitaVychylkaValue);
        }
    }


    /**
     * Sets the instantaneous displacement (`okamzitaVychylka`) for all eight symmetric octants of a circle.
     */
    private void setCirclePixelsDisplacement(int centerX, int centerY, int x, int y, int okamzitaVychylkaValue) {

        if (centerY + y > yMin && centerY + y < yMax && centerX + x > xMin && centerX + x < xMax) {
            setPixelDisplacement(centerX + x, centerY + y, okamzitaVychylkaValue);
        }
        if (centerY + y > yMin && centerY + y < yMax && centerX - x > xMin && centerX - x < xMax) {
            setPixelDisplacement(centerX - x, centerY + y, okamzitaVychylkaValue);
        }
        if (centerY - y > yMin && centerY - y < yMax && centerX + x > xMin && centerX + x < xMax) {
            setPixelDisplacement(centerX + x, centerY - y, okamzitaVychylkaValue);
        }
        if (centerY - y > yMin && centerY - y < yMax && centerX - x > xMin && centerX - x < xMax) {
            setPixelDisplacement(centerX - x, centerY - y, okamzitaVychylkaValue);
        }
        if (centerY + x > yMin && centerY + x < yMax && centerX + y > xMin && centerX + y < xMax) {
            setPixelDisplacement(centerX + y, centerY + x, okamzitaVychylkaValue);
        }
        if (centerY + x > yMin && centerY + x < yMax && centerX - y > xMin && centerX - y < xMax) {
            setPixelDisplacement(centerX - y, centerY + x, okamzitaVychylkaValue);
        }
        if (centerY - x > yMin && centerY - x < yMax && centerX + y > xMin && centerX + y < xMax) {
            setPixelDisplacement(centerX + y, centerY - x, okamzitaVychylkaValue);
        }
        if (centerY - x > yMin && centerY - x < yMax && centerX - y > xMin && centerX - y < xMax) {
            setPixelDisplacement(centerX - y, centerY - x, okamzitaVychylkaValue);
        }
    }


    /**
     * Adds the `okamzitaVychylka` displacement to the specified pixel, ensuring boundary constraints are respected
     * and avoiding duplicate updates using `visitedPixels`.
     */
    private void setPixelDisplacement(int x, int y, int okamzitaVychylkaValue) {
        // Check if the pixel is within the grid boundaries
        //if (x >= xMin && x <= xMax && y >= yMin && y <= yMax) {
            int gridX = Pixel.getGridX(x, controller.getXMin());
            int gridY = Pixel.getGridY(y, controller.getYMin());

            if (gridX >= 0 && gridX < pixelGrid.length && gridY >= 0 && gridY < pixelGrid[0].length) {
                // Create a PixelCoordinate object
                PixelCoordinate pixelCoord = new PixelCoordinate(gridX, gridY);

                if (visitedPixels.contains(pixelCoord)) {
                    // If already visited, log as a duplicate
                    duplicatePixels.add(pixelCoord);
                } else {
                    // Mark pixel as visited and update its displacement
                    visitedPixels.add(pixelCoord);
                    pixelGrid[gridX][gridY].addVychylka(okamzitaVychylkaValue);

                    // Activate pixel for periodic checks
                    activatePixelCoordinate(gridX, gridY);
                }
            }
        //}
    }

    public void addCirclesToPane(Pane pane) {
        pane.getChildren().addAll(this, innerCircle);
    }


















































/*

    private void addPointToMapIfInRectangle(int x, int y, int okamzitaVychylkaValue) {
        // Check if the point is within the specified rectangle
        if (x >= xMin && x <= xMax && y >= yMin && y <= yMax) {
            // Calculate the grid coordinates using the static method in Pixel

            int gridX = Pixel.getGridX(x, controller.getXMin());
            int gridY = Pixel.getGridY(y, controller.getYMin());

            // Validate that the calculated grid indices are within bounds
            if (gridX >= 0 && gridX < pixelGrid.length && gridY >= 0 && gridY < pixelGrid[0].length) {
                // Create a PixelCoordinate object
                PixelCoordinate pixelCoord = new PixelCoordinate(gridX, gridY);

                if (visitedPixels.contains(pixelCoord)) {
                    duplicatePixels.add(pixelCoord); // Log duplicate pixel
                } else {
                    visitedPixels.add(pixelCoord);  // Add to the set of visited pixels
                    pixelGrid[gridX][gridY].addVychylka(50);

                    // Activate pixel for periodic checks
                    activatePixelCoordinate(gridX, gridY);
                }

            }

        }
    }

    public void generateOuterCircleUsingBresenhamsFiltered() {
        int a = (int) this.x;  // Circle center X-coordinate
        int b = (int) this.y;  // Circle center Y-coordinate
        int r = this.outerRadius;  // Radius of the circle
        int okamzitaVychylkaValue = getokamzitaVychylka();

        int x = 0;
        int y = r;
        int d = 3 - 2 * r;  // Decision variable

        // Add initial symmetric points if at least one is in the rectangle
        addSymmetricPointsFiltered(x, y, a, b, okamzitaVychylkaValue);

        // Loop through each step while x <= y
        while (x <= y) {
            x++;  // Move to the next horizontal position

            // Update the decision variable and adjust y if needed
            if (d < 0) {
                d += 4 * x + 6; // Move horizontally
            } else {
                y--;           // Move diagonally (horizontal + vertical)
                d += 4 * (x - y) + 10;
            }

            // Add symmetric points for the current (x, y)
            addSymmetricPointsFiltered(x, y, a, b, okamzitaVychylkaValue);
        }
    }

    public void generateInnerCircleUsingBresenhamsFiltered() {
        int a = (int) this.x; // Circle center X-coordinate
        int b = (int) this.y; // Circle center Y-coordinate
        int r = this.innerRadius; // Current inner radius
        int okamzitaVychylkaValue = getokamzitaVychylka();

        int x = 0, y = r;
        int d = 3 - 2 * r; // Decision variable

        // Add initial symmetric points
        addSymmetricPointsFilteredInner(x, y, a, b, okamzitaVychylkaValue);

        // Loop for generating circle points
        while (x <= y) {
            x++;

            // Update decision variable
            if (d < 0) {
                d += 4 * x + 6;
            } else {
                y--;
                d += 4 * (x - y) + 10;
            }

            // Add symmetrical points for the inner circle
            addSymmetricPointsFilteredInner(x, y, a, b, okamzitaVychylkaValue);
        }
    }


    private void addSymmetricPointsFilteredInner(int x, int y, int a, int b, int okamzitaVychylkaValue) {
        if (isAnySymmetricPointInRectangle(x, y, a, b)) {
            removePointFromMapIfInRectangle(a + x, b + y, okamzitaVychylkaValue); // Quadrant 1
            removePointFromMapIfInRectangle(a - x, b + y, okamzitaVychylkaValue); // Quadrant 2
            removePointFromMapIfInRectangle(a + x, b - y, okamzitaVychylkaValue); // Quadrant 4
            removePointFromMapIfInRectangle(a - x, b - y, okamzitaVychylkaValue); // Quadrant 3
            removePointFromMapIfInRectangle(a + y, b + x, okamzitaVychylkaValue); // Transposed 1
            removePointFromMapIfInRectangle(a - y, b + x, okamzitaVychylkaValue); // Transposed 2
            removePointFromMapIfInRectangle(a + y, b - x, okamzitaVychylkaValue); // Transposed 3
            removePointFromMapIfInRectangle(a - y, b - x, okamzitaVychylkaValue); // Transposed 4
        }
    }


    private void removePointFromMapIfInRectangle(int x, int y, int okamzitaVychylkaValue) {
        if (x >= xMin && x <= xMax && y >= yMin && y <= yMax) {
            int gridX = Pixel.getGridX(x, controller.getXMin());
            int gridY = Pixel.getGridY(y, controller.getYMin());

            if (gridX >= 0 && gridX < pixelGrid.length && gridY >= 0 && gridY < pixelGrid[0].length) {
                PixelCoordinate pixelCoord = new PixelCoordinate(gridX, gridY);

                // If the pixel was previously visited, remove any "inner circle" influence
                if (visitedPixels.contains(pixelCoord)) {
                    visitedPixels.remove(pixelCoord); // Stop tracking this pixel
                    duplicatePixels.remove(pixelCoord); // Remove from duplicate list
                    pixelGrid[gridX][gridY].addVychylka(-50); // Reverse previous action

                    deactivatePixelCoordinate(gridX, gridY);
                }
            }
        }
    }

    private void deactivatePixelCoordinate(int gridX, int gridY) {
        PixelCoordinate coord = new PixelCoordinate(gridX, gridY);

        // Remove the coordinate from the active set
        activePixelCoordinates.remove(coord);
    }


    private void addSymmetricPointsFiltered(int x, int y, int a, int b, int okamzitaVychylkaValue) {
        // Perform early filtering: Add points only if at least one symmetric point is within the rectangle
        if (isAnySymmetricPointInRectangle(x, y, a, b)) {
            addPointToMapIfInRectangle(a + x, b + y, okamzitaVychylkaValue); // Quadrant 1
            addPointToMapIfInRectangle(a - x, b + y, okamzitaVychylkaValue); // Quadrant 2
            addPointToMapIfInRectangle(a + x, b - y, okamzitaVychylkaValue); // Quadrant 4
            addPointToMapIfInRectangle(a - x, b - y, okamzitaVychylkaValue); // Quadrant 3
            addPointToMapIfInRectangle(a + y, b + x, okamzitaVychylkaValue); // Transposed 1
            addPointToMapIfInRectangle(a - y, b + x, okamzitaVychylkaValue); // Transposed 2
            addPointToMapIfInRectangle(a + y, b - x, okamzitaVychylkaValue); // Transposed 3
            addPointToMapIfInRectangle(a - y, b - x, okamzitaVychylkaValue); // Transposed 4
        }
    }


    private boolean isAnySymmetricPointInRectangle(int x, int y, int a, int b) {
        return isInRectangle(a + x, b + y) ||
                isInRectangle(a - x, b + y) ||
                isInRectangle(a + x, b - y) ||
                isInRectangle(a - x, b - y) ||
                isInRectangle(a + y, b + x) ||
                isInRectangle(a - y, b + x) ||
                isInRectangle(a + y, b - x) ||
                isInRectangle(a - y, b - x);
    }


*/





    private void activatePixelCoordinate(int gridX, int gridY) {
        PixelCoordinate coord = new PixelCoordinate(gridX, gridY);

        // Add the coordinate to the active set
        activePixelCoordinates.add(coord);
    }


    /**
     * Subtracts the contribution of this inner circle from the pixel map.
     */
    /*
    private void subtractVychylkaIfInRectangle(int x, int y, int okamzitaVychylkaValue) {
        // Check if the pixel is within the grid bounds
        if (x >= xMin && x <= xMax && y >= yMin && y <= yMax) {
            int gridX = Pixel.getGridX(x, controller.getXMin());
            int gridY = Pixel.getGridY(y, controller.getYMin());

            // Ensure the grid indices are within bounds
            if (gridX >= 0 && gridX < pixelGrid.length && gridY >= 0 && gridY < pixelGrid[0].length) {
                // Subtract the okamzitaVychylka value from the pixel
                pixelGrid[gridX][gridY].addVychylka(-okamzitaVychylkaValue);

                // Optionally deactivate the pixel (if no contributions remain)
                deactivatePixelCoordinate(gridX, gridY);

            }
        }
    }

    /*
      Subtracts the okamzitaVychylka contribution of the **inner circle** from the overlapping pixels.

    private void subtractSymmetricPointsFilteredInner(int x, int y, int a, int b, int okamzitaVychylkaValue) {
        if (isAnySymmetricPointInRectangle(x, y, a, b)) {
            // Subtract only at points within the active rectangle
            subtractVychylkaIfInRectangle(a + x, b + y, okamzitaVychylkaValue); // Quadrant 1
            subtractVychylkaIfInRectangle(a - x, b + y, okamzitaVychylkaValue); // Quadrant 2
            subtractVychylkaIfInRectangle(a + x, b - y, okamzitaVychylkaValue); // Quadrant 4
            subtractVychylkaIfInRectangle(a - x, b - y, okamzitaVychylkaValue); // Quadrant 3
            subtractVychylkaIfInRectangle(a + y, b + x, okamzitaVychylkaValue); // Transposed 1
            subtractVychylkaIfInRectangle(a - y, b + x, okamzitaVychylkaValue); // Transposed 2
            subtractVychylkaIfInRectangle(a + y, b - x, okamzitaVychylkaValue); // Transposed 3
            subtractVychylkaIfInRectangle(a - y, b - x, okamzitaVychylkaValue); // Transposed 4
        }
    }
*/


    /**
     * Logs duplicate pixels detected during circle generation.
     */
    /*
    public void logDuplicatePixels() {
        if (!duplicatePixels.isEmpty()) {
            System.out.println("Duplicate Pixels Detected:");
            for (PixelCoordinate pixel : duplicatePixels) {
                System.out.println("Duplicate Pixel: " + pixel);
            }
            System.out.println("Total Duplicate Pixels: " + duplicatePixels.size());
        } else {
            System.out.println("No duplicate pixels detected.");
        }
    }
*/

    /**
     * Generate the donut shape between the outer and inner circles
     * and apply sinusoidal behavior for okamzitaVychylka at each pixel.
     */


    /**
     * Sets the color of a pixel in the grid based on okamzitaVychylka.
     * Maps -100 to 100 range to a color scale (blue for negative, red for positive,
     * and white for neutral (0)).
     */
    /*
    private void setPixelColor(int x, int y, int okamzitaVychylka) {
        // Ensure pixel is within the grid boundary
        if (x >= xMin && x <= xMax && y >= yMin && y <= yMax) {
            int gridX = Pixel.getGridX(x, controller.getXMin());
            int gridY = Pixel.getGridY(y, controller.getYMin());

            if (gridX >= 0 && gridX < pixelGrid.length && gridY >= 0 && gridY < pixelGrid[0].length) {
                pixelGrid[gridX][gridY].addVychylka(okamzitaVychylka);
            }
        }
    }
    */

}
