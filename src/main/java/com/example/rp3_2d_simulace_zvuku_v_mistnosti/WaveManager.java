package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class WaveManager {
    // List to track all active waves in the room
    private List<SoundWave> activeWaves;

    // Reference to a WaveFactory to create new waves
    private WaveFactory waveFactory;

    // Flag to track whether waves are currently running
    private boolean isRunning = false;

    @FXML
    private Pane centerPane = new Pane();

    public WaveManager(WaveFactory waveFactory, Pane centerPane) {
        this.waveFactory = waveFactory;
        this.activeWaves = new ArrayList<>();
        this.isRunning = false;
        this.centerPane = centerPane;
    }

    public void createWave(double x, double y, Room0Controller controller, int radius) {
        if (centerPane == null) {
            System.err.println("Error: centerPane is null. Cannot add wave.");
            return;
        }
        SoundWave wave = waveFactory.createWave(x, y, controller, radius);
        activeWaves.add(wave);
        centerPane.getChildren().add(wave);
    }

    public void updateWaves(Room0Controller controller) {
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
        checkWavesForCorners(controller);
        checkWavesForReflections(controller);
        activeWaves.removeAll(wavesToRemove);
    }

    public void checkWavesForCorners(Room0Controller controller) {
        // Iterate over a copy of the activeWaves to avoid modification issues
        for (SoundWave wave : new ArrayList<>(activeWaves)) {
            // Get distances to all four corners
            int[] cornerDistances = wave.getCornerDistances();
            int currentRadius = wave.getCurrentRadius();

            // Check if the wave has reached any corner
            if (currentRadius == cornerDistances[0]) { // Top-Left Corner
                //System.out.println("Wave reached the Top-Left Corner.");
                createWave(controller.getXMin(), controller.getYMin(), controller, 0); // Create new wave
            }
            if (currentRadius == cornerDistances[1]) { // Bottom-Left Corner
                //System.out.println("Wave reached the Bottom-Left Corner.");
                createWave(controller.getXMin(), controller.getYMax(), controller,0); // Create new wave
            }
            if (currentRadius == cornerDistances[2]) { // Bottom-Right Corner
                //System.out.println("Wave reached the Bottom-Right Corner.");
                createWave(controller.getXMax(), controller.getYMax(), controller,0); // Create new wave
            }
            if (currentRadius == cornerDistances[3]) { // Top-Right Corner
                //System.out.println("Wave reached the Top-Right Corner.");
                createWave(controller.getXMax(), controller.getYMin(), controller,0); // Create new wave
            }
        }
    }

    public void checkWavesForReflections(Room0Controller controller) {
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

    private void handleOutOfRectangleWave(SoundWave wave, Point center, Room0Controller controller) {
        int[] reflectionDistances = wave.getReflectionDistances();

        // Fix: Check if reflectionDistances is null
        if (reflectionDistances == null) {
            System.err.println("Warning: reflectionDistances is null for out-of-rectangle wave. Skipping reflection check.");
            return;
        }

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

    private void reflectWave(SoundWave wave, Room0Controller controller, int wallIndex) {
        Point center = wave.getCenter();
        Line reflectingWall = controller.getRoomWalls().get(wallIndex);
        Point symmetricPoint = new Calculator().calculateSymetricPoint(center, reflectingWall);
        createWave(symmetricPoint.getX(), symmetricPoint.getY(), controller, wave.getCurrentRadius());
    }

    public void resumeWaves() {
        isRunning = true;
        for (SoundWave wave : activeWaves) {
            wave.resume();
        }
        System.out.println("Wave propagation started.");
    }

    public void pauseWaves() {
        isRunning = false;
        for (SoundWave wave : activeWaves) {
            wave.pause();
        }
        System.out.println("Wave propagation stopped.");
    }

    public void resetWaves() {
        activeWaves.clear();
        System.out.println("All waves have been reset.");
    }

    public boolean isRunning() {
        return isRunning;
    }

    public List<SoundWave> getActiveWaves() {
        return activeWaves;
    }
}