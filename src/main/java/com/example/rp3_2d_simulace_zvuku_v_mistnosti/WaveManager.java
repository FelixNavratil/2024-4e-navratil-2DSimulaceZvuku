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

    // pripojeni centerpane
    @FXML
    private Pane centerPane = new Pane();

    public WaveManager(WaveFactory waveFactory, Pane centerPane) {
        // Initialize fields
        this.waveFactory = waveFactory;
        this.activeWaves = new ArrayList<>();
        this.isRunning = false;
        this.centerPane = centerPane;
    }

    /**
     * Handles creation of a new wave at the given position.
     *
     * @param x The starting X position of the wave.
     * @param y The starting Y position of the wave.
     */
    public void createWave(double x, double y, Room0Controller controller) {
        if (centerPane == null) {
            System.err.println("Error: centerpane is null. Cannot add wave.");
            return;
        }
        // Create a new wave using the WaveFactory
        SoundWave wave = waveFactory.createWave(x, y, controller);
        // Add the newly created wave to the active waves list
        activeWaves.add(wave);
        centerPane.getChildren().add(wave);
    }

    /**
     * Updates all active waves (e.g., grows their size) and removes completed or
     * expired waves.
     */
    public void updateWaves(Room0Controller controller) {
        List<SoundWave> wavesToRemove = new ArrayList<>(); // Temporary list for waves to remove

        // Iterate over all active waves and update their state
        for (SoundWave wave : activeWaves) {
            wave.grow(); // Grow the wave's radius

            // Check and remove wave if its age goes beyond 7 seconds
            if (wave.isOlderThan(7000)) {
                wavesToRemove.add(wave); // Mark wave for removal
                if (centerPane != null) {
                    centerPane.getChildren().remove(wave); // Remove from UI
                }
            }
        }

        // Check for corner-related events
        checkWavesForCorners(controller);

        // Remove all marked waves
        activeWaves.removeAll(wavesToRemove);
    }

    /**
     * Checks if any wave has reached a corner and handles the event.
     */
    public void checkWavesForCorners(Room0Controller controller) {
        // Iterate over a copy of the activeWaves to avoid modification issues
        for (SoundWave wave : new ArrayList<>(activeWaves)) {
            // Get distances to all four corners
            int[] cornerDistances = wave.getCornerDistances();
            int currentRadius = wave.getCurrentRadius();

            // Check if the wave has reached any corner
            if (currentRadius == cornerDistances[0]) { // Top-Left Corner
                System.out.println("Wave reached the Top-Left Corner.");
                createWave(controller.getXMin(), controller.getYMin(), controller); // Create new wave
            }
            if (currentRadius == cornerDistances[1]) { // Bottom-Left Corner
                System.out.println("Wave reached the Bottom-Left Corner.");
                createWave(controller.getXMin(), controller.getYMax(), controller); // Create new wave
            }
            if (currentRadius == cornerDistances[2]) { // Bottom-Right Corner
                System.out.println("Wave reached the Bottom-Right Corner.");
                createWave(controller.getXMax(), controller.getYMax(), controller); // Create new wave
            }
            if (currentRadius == cornerDistances[3]) { // Top-Right Corner
                System.out.println("Wave reached the Top-Right Corner.");
                createWave(controller.getXMax(), controller.getYMin(), controller); // Create new wave
            }
        }
    }

    /**
     * Starts wave propagation (could involve setting up timers, etc.).
     */
    public void startWaves() {
        isRunning = true;
        System.out.println("Wave propagation started.");
    }

    /**
     * Stops all wave updates.
     */
    public void stopWaves() {
        isRunning = false;
        System.out.println("Wave propagation stopped.");
    }

    /**
     * Resets all active waves (clears the list).
     */
    public void resetWaves() {
        activeWaves.clear();
        System.out.println("All waves have been reset.");
    }

    /**
     * Returns whether waves are currently running.
     *
     * @return True if waves are running, false otherwise.
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Returns the list of active waves.
     *
     * @return List of active ZvukovaVlna objects.
     */
    public List<SoundWave> getActiveWaves() {
        return activeWaves;
    }


}