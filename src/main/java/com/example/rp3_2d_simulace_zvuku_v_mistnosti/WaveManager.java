package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class WaveManager {
    // List to track all active waves in the room
    private List<SoundWave> activeWaves;
    BaseRoomControllerInterface controller;

    // Reference to a WaveFactory to create new waves
    private WaveFactory waveFactory;

    // Flag to track whether waves are currently running
    private boolean isRunning = false;

    @FXML
    private Pane centerPane = new Pane();

    private Calculator calculator = new Calculator();

    // Initializes a WaveManager instance.
    public WaveManager(WaveFactory waveFactory, Pane centerPane) {
        this.waveFactory = waveFactory;
        this.activeWaves = new ArrayList<>();
        this.isRunning = false;
        this.centerPane = centerPane;
    }

    //Creates a new sound wave at the given coordinates (x, y) with the specified radius.
    public void createWave(double x, double y, BaseRoomControllerInterface controller, int radius) {
        if (centerPane == null) {
            System.err.println("Error: centerPane is null. Cannot add wave.");
            return;
        }
        SoundWave wave = waveFactory.createWave(x, y, controller, radius);
        activeWaves.add(wave);
        centerPane.getChildren().add(wave);

        //making the rectangles appear above the waves
        controller.overlayRectangles();
    }


    //Updates the current state of all active waves.
    public void updateWaves(BaseRoomControllerInterface controller) {
        List<SoundWave> wavesToRemove = new ArrayList<>();

        for (SoundWave wave : activeWaves) {
            wave.grow();

            if (wave.isOlderThan(7000)) {
                wavesToRemove.add(wave);
                if (centerPane != null) {
                    centerPane.getChildren().remove(wave);
                }
            }
        }
        //checkWavesForCorners(controller);
        checkWavesForReflections(controller);
        activeWaves.removeAll(wavesToRemove);
    }

    //Checks if waves have reached any corner of the room and generates new waves if so (to simulate corner reflections).
    public void checkWavesForCorners(BaseRoomControllerInterface controller) {
        // Iterate over a copy of the activeWaves to avoid modification issues

        for (SoundWave wave : new ArrayList<>(activeWaves)) {
            // Get distances to all four corners
            int[] cornerDistances = wave.getCornerDistances();
            int currentRadius = wave.getCurrentRadius();

            // Check if the wave has reached any corner
            if (currentRadius == cornerDistances[0]) { // Top-Left Corner

                createWave(controller.getXMin(), controller.getYMin(), controller, 0); // Create new wave
            }
            if (currentRadius == cornerDistances[1]) { // Bottom-Left Corner

                createWave(controller.getXMin(), controller.getYMax(), controller,0); // Create new wave
            }
            if (currentRadius == cornerDistances[2]) { // Bottom-Right Corner

                createWave(controller.getXMax(), controller.getYMax(), controller,0); // Create new wave
            }
            if (currentRadius == cornerDistances[3]) { // Top-Right Corner

                createWave(controller.getXMax(), controller.getYMin(), controller,0); // Create new wave
            }
        }
    }

    //Handles wave reflections when they hit the walls of the room.
    public void checkWavesForReflections(BaseRoomControllerInterface controller) {
        for (SoundWave wave : new ArrayList<>(activeWaves)) {
            Point center = wave.getCenter();

            if (wave.isInRectangle(center.getX(), center.getY())) {
                int[] reflectionDistances = wave.getReflectionDistances();

                // Fix: Check if reflectionDistances is null
                if (reflectionDistances == null) {
                    System.err.println("Warning: reflectionDistances is null for wave. Skipping reflection check.");
                    continue;
                }

                int currentRadius = wave.getCurrentRadius();

                for (int i = 0; i < reflectionDistances.length; i++) {
                    if (currentRadius == reflectionDistances[i]) {
                        Line reflectingWall = controller.getRoomWalls().get(i);
                        Point symmetricPoint = new Calculator().calculateSymetricPoint(center, reflectingWall);
                        createWave(symmetricPoint.getX(), symmetricPoint.getY(), controller, wave.getCurrentRadius());
                        break;
                    }
                }
            } else {
                handleOutOfRectangleWave(wave, center, controller);
            }
        }
    }

    //Specialized handling for waves that hit the corners of the rectangle when outside.
    private void handleOutOfRectangleWaveForCorners(SoundWave wave, Point center, BaseRoomControllerInterface controller) {
        Point topLeft = controller.getRoomCorners().get(0);
        Point bottomLeft = controller.getRoomCorners().get(1);
        Point bottomRight = controller.getRoomCorners().get(2);
        Point topRight = controller.getRoomCorners().get(3);
        int currentRadius = wave.getCurrentRadius();

        if (wave.isAboveRectangle(center.getX(), center.getY())) {

            if (currentRadius == (int) wave.getCenter().distance(controller.getRoomCorners().get(0))){
                System.out.println("odrazeno horni");
                reflectWave(wave, controller, 2);
            }

            if (wave.getRadius() ==(int) wave.getCenter().distance(controller.getRoomCorners().get(3))) {
                System.out.println("odrazeno horni1 -------");
                reflectWave(wave, controller, 3);
            }

        } else if (wave.isBellowRectangle(center.getX(), center.getY())) {

            if (currentRadius== (int) wave.getCenter().distance(controller.getRoomCorners().get(1))){
                //System.out.println("odrazeno spodni");
                reflectWave(wave, controller, 2);
            }

            if (wave.getRadius() ==(int) wave.getCenter().distance(controller.getRoomCorners().get(2))) {
                //System.out.println("odrazeno spodni1 -------");
                reflectWave(wave, controller, 3);
            }

        } else if (wave.isLeftOfRectangle(center.getX(), center.getY())) {

            if (currentRadius== (int) wave.getCenter().distance(controller.getRoomCorners().get(1))){
                //System.out.println("odrazeno leva");
                reflectWave(wave, controller, 1);
            }
            if (wave.getRadius() == (int)wave.getCenter().distance(controller.getRoomCorners().get(0))) {
                //System.out.println("odrazeno leva1 ------");
                reflectWave(wave, controller, 0);
            }

        } else if (wave.isRightOfRectangle(center.getX(), center.getY())) {
            System.out.println("current radius = " + currentRadius + "  distance =" + (int) wave.getCenter().distance(controller.getRoomCorners().get(3)));

            if (currentRadius== (int) wave.getCenter().distance(controller.getRoomCorners().get(2))){
                //System.out.println("odrazeno zprava dole");
                reflectWave(wave, controller, 1);
            }
            if (wave.getRadius() == (int)wave.getCenter().distance(controller.getRoomCorners().get(3))) {
                //System.out.println("odrazeno zprava nahore");
                reflectWave(wave, controller, 0);
            }
        }
    }

    private void handleDiagonalWavesForCorners(SoundWave wave, Point center, BaseRoomControllerInterface controller) {
        Point topLeft = controller.getRoomCorners().get(0);
        Point bottomLeft = controller.getRoomCorners().get(1);
        Point bottomRight = controller.getRoomCorners().get(2);
        Point topRight = controller.getRoomCorners().get(3);
        int currentRadius = wave.getCurrentRadius();

        //nahore nalevo
        if (wave.isAboveOnTheLeftOfRectangle(center.getX(), center.getY())) {

            if (currentRadius== (int) wave.getCenter().distance(bottomLeft)){
                reflectWave(wave, controller, 1);
            }
            if (wave.getRadius() == (int)wave.getCenter().distance(bottomRight)) {
                Point pomocnyBod = calculator.calculateSymetricPoint(center, controller.getRoomWalls().get(1));
                reflectWave(pomocnyBod, wave.getCurrentRadius(), controller, 3);
            }
            if (currentRadius== (int) wave.getCenter().distance(topRight)){
                reflectWave(wave, controller, 3);
            }

            //dole nalevo
        }else if (wave.isBellowOnTheLeftOfRectangle(center.getX(), center.getY())) {

            if (currentRadius== (int) wave.getCenter().distance(bottomRight)){
                reflectWave(wave, controller, 3);
            }
            if (wave.getRadius() == (int)wave.getCenter().distance(topRight)) {
                Point pomocnyBod = calculator.calculateSymetricPoint(center, controller.getRoomWalls().get(3));
                reflectWave(pomocnyBod, wave.getCurrentRadius(), controller, 0);
            }
            if (currentRadius== (int) wave.getCenter().distance(topLeft)){
                reflectWave(wave, controller, 0);
            }

            //dole napravo
        }else if (wave.isBellowOnTheRightOfRectangle(center.getX(), center.getY())) {

            if (currentRadius== (int) wave.getCenter().distance(topRight)){
                reflectWave(wave, controller, 0);
            }
            if (wave.getRadius() == (int)wave.getCenter().distance(topLeft)) {
                Point pomocnyBod = calculator.calculateSymetricPoint(center, controller.getRoomWalls().get(0));
                reflectWave(pomocnyBod, wave.getCurrentRadius(), controller, 2);
            }
            if (currentRadius== (int) wave.getCenter().distance(bottomLeft)){
                reflectWave(wave, controller, 2);
            }

            //nahore napravo
        }else if (wave.isAboveOnTheRightOfRectangle(center.getX(), center.getY())) {

            if (currentRadius== (int) wave.getCenter().distance(topLeft)){
                reflectWave(wave, controller, 2);
            }
            if (wave.getRadius() == (int)wave.getCenter().distance(bottomLeft)) {
                Point pomocnyBod = calculator.calculateSymetricPoint(center, controller.getRoomWalls().get(2));
                reflectWave(pomocnyBod, wave.getCurrentRadius(), controller, 1);
            }
            if (currentRadius== (int) wave.getCenter().distance(bottomRight)){
                reflectWave(wave, controller, 1);
            }

        }
    }

    // Handles waves that are outside the boundaries of the rectangle.
    private void handleOutOfRectangleWave(SoundWave wave, Point center, BaseRoomControllerInterface controller) {
        int[] reflectionDistances = wave.getReflectionDistances();

        // Fix: Check if reflectionDistances is null
        if (reflectionDistances == null) {
            System.err.println("Warning: reflectionDistances is null for out-of-rectangle wave. Skipping reflection check.");
            //return;
        }

        handleOutOfRectangleWaveForCorners(wave, center, controller);
        handleDiagonalWavesForCorners(wave, center, controller);
        int currentRadius = wave.getCurrentRadius();

        if (wave.isAboveRectangle(center.getX(), center.getY()) && currentRadius == reflectionDistances[0]) {

            reflectWave(wave, controller, 1);

        } else if (wave.isBellowRectangle(center.getX(), center.getY()) && currentRadius == reflectionDistances[0]) {

            reflectWave(wave, controller, 0);

        } else if (wave.isLeftOfRectangle(center.getX(), center.getY()) && currentRadius == reflectionDistances[0]) {

            reflectWave(wave, controller, 3);

        } else if (wave.isRightOfRectangle(center.getX(), center.getY()) && currentRadius == reflectionDistances[0]) {

            reflectWave(wave, controller, 2);

        }
    }

    //Creates a reflected wave when an existing wave interacts with a wall.
    private void reflectWave(SoundWave wave, BaseRoomControllerInterface controller, int wallIndex) {
        Point center = wave.getCenter();
        Line reflectingWall = controller.getRoomWalls().get(wallIndex);
        Point symmetricPoint = new Calculator().calculateSymetricPoint(center, reflectingWall);
        createWave(symmetricPoint.getX(), symmetricPoint.getY(), controller, wave.getCurrentRadius());
    }

    private void reflectWave(Point center, int currentRadius, BaseRoomControllerInterface controller, int wallIndex) {
        Line reflectingWall = controller.getRoomWalls().get(wallIndex);
        Point symmetricPoint = new Calculator().calculateSymetricPoint(center, reflectingWall);
        createWave(symmetricPoint.getX(), symmetricPoint.getY(), controller, currentRadius);
    }





    public void resumeWaves() {
        isRunning = true;
        for (SoundWave wave : activeWaves) {
            wave.resume();
        }
        //System.out.println("Wave propagation started.");
    }

    public void pauseWaves() {
        isRunning = false;
        for (SoundWave wave : activeWaves) {
            wave.pause();
        }
        //System.out.println("Wave propagation stopped.");
    }

    public void resetWaves() {
        activeWaves.clear();
        //System.out.println("All waves have been reset.");
    }

    public boolean isRunning() {
        return isRunning;
    }

    public List<SoundWave> getActiveWaves() {
        return activeWaves;
    }
}