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
    private boolean isRunning;



    //pripojeni centerpane
    @FXML
    private Pane centerPane = new Pane();

    public  WaveManager(WaveFactory waveFactory, Pane centerPane) {
        // Initialize fields
        this.waveFactory = waveFactory;
        this.activeWaves = new ArrayList<>();
        this.isRunning = false;
        this.centerPane = centerPane;
    }

    /**
     * Handles creation of a new wave at the given position.
     * @param x The starting X position of the wave.
     * @param y The starting Y position of the wave.
     * @param maxRadius The maximum radius of the wave.
     */
    public void createWave(double x, double y, double maxRadius) {
        if (centerPane == null) {
            System.err.println("Error: centerpane is null. Cannot add wave.");
            return;
        }
        // Create a new wave using the WaveFactory
        SoundWave wave = waveFactory.createWave(x, y, maxRadius);
        // Add the newly created wave to the active waves list
        activeWaves.add(wave);
        centerPane.getChildren().add(wave);
    }

    /**
     * Updates all active waves (e.g., grows their size) and removes completed or expired waves.
     */
    public void updateWaves() {
        // Use an iterator to safely modify the list during iteration
        activeWaves.removeIf(wave -> {
            // Update wave radius
            wave.grow();

            // Remove the wave if it has reached its max radius or if it's older than 5 seconds
            if (wave.isOlderThan(10000)) {
                centerPane.getChildren().remove(wave); // Remove from the UI
                return true; // Remove the wave from the list
            }
            return false;
        });
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
     * @return True if waves are running, false otherwise.
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Returns the list of active waves.
     * @return List of active ZvukovaVlna objects.
     */
    public List<SoundWave> getActiveWaves() {
        return activeWaves;
    }
}
