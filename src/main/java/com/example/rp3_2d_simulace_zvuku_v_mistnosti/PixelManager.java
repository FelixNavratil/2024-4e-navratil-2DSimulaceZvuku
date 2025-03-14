package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import javafx.scene.layout.Pane;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 */
public class PixelManager {

    private Pixel[][] pixelGrid; // 2D grid of all pixels in the room
    private int width;  // Width of the room
    private int height; // Height of the room
    private Map<String, Double> pixelDisplacements = new HashMap<>();
    private BaseRoomControllerInterface roomController;
    private static final double PIXELSIZE = 2;

    public PixelManager(BaseRoomControllerInterface roomController) {
        this.roomController = roomController;
    }

    /**
     * Initializes the 2D array of Pixels based on the rectangle's width, height, and its position in the scene.
     *
     * @param rectWidth     Width of the rectangle (number of columns in the grid).
     * @param rectHeight    Height of the rectangle (number of rows in the grid).
     * @param rectX     The starting X-coordinate of the rectangle in the scene.
     * @param rectY     The starting Y-coordinate of the rectangle in the scene.
     */
    public void initializePixelGrid(int rectWidth, int rectHeight, double rectX, double rectY) {
        // Get the stroke width from the controller
        double strokeWidth = roomController.getStroke();

        // Usable area inside the rectangle (excluding the stroke on all sides)
        double innerWidth = rectWidth -  strokeWidth;
        double innerHeight = rectHeight -  strokeWidth;

        // Calculate number of pixels that fit inside the usable area
        this.width = (int) Math.floor(innerWidth / PIXELSIZE);
        this.height = (int) Math.floor(innerHeight / PIXELSIZE);

        // Create the 2D pixel grid
        pixelGrid = new Pixel[width][height];

        // Offset the starting position to account for the stroke
        double startX = rectX + strokeWidth/2 + (innerWidth % PIXELSIZE) / 2; // Center-align pixels horizontally
        double startY = rectY + strokeWidth/2 + (innerHeight % PIXELSIZE) / 2; // Center-align pixels vertically

        // Initialize the pixel grid
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Calculate real-world (scene) coordinates for each pixel
                double realX = startX + x * PIXELSIZE;
                double realY = startY + y * PIXELSIZE;

                // Create a new pixel and add it to the grid
                pixelGrid[x][y] = new Pixel(x, y, realX, realY);
                pixelGrid[x][y].setPixelSize(PIXELSIZE);
            }
        }

        // Debugging: Log the grid dimensions and starting positions
        System.out.println("Pixel Grid Width: " + width);
        System.out.println("Pixel Grid Height: " + height);
        System.out.println("Start X: " + startX + ", Start Y: " + startY);
    }

    /**
     * Adds all the pixel rectangles to a JavaFX Pane for visualization.
     *
     * @param pane The Pane where rectangles will be added.
     */
    public void addRectanglesToPane(Pane pane) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Pixel pixel = pixelGrid[x][y];

                pane.getChildren().add(pixel.getRectangle());
            }
        }
    }

    /**
     * Get the 2D pixel grid.
     *
     * @return A 2D array of Pixels.
     */
    public Pixel[][] getPixelGrid() {
        return pixelGrid;
    }

    /**
     *  add okamzitaVychylka to celkovaVychylka in a specific pixel
     * @param gridX
     * @param gridY
     * @param okamzitaVychylka
     */
    public void setPixelColor(int gridX, int gridY, int okamzitaVychylka){
        pixelGrid[gridX][gridY].addVychylka(okamzitaVychylka);
    }

    /**
     * Resets the displacement (celkovaVychylka) of all pixels to a specified value.
     * For the reset functionality, it will set all celkovaVychylka values to 0.
     */
    public void resetPixelGridDisplacement() {
        if (pixelGrid != null) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Pixel pixel = pixelGrid[x][y];
                    if (pixel != null) {
                        pixel.setDefault(); // Reset celkovaVychylka to 0
                    }
                }
            }

            // Debugging: Print confirmation of the reset
            System.out.println("All pixel displacements have been reset.");
        } else {
            System.err.println("Pixel grid is not initialized.");
        }
    }

    /**
     * Prints the details of each Pixel in the grid, including real-world scene coordinates.
     */
    public void printPixelGrid() {
        System.out.println("Pixel Grid:");
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Pixel pixel = pixelGrid[x][y];
                System.out.println(pixel); // toString() in the Pixel class will handle the formatting
            }
        }
    }
    public double getPixelSize(){
        return PIXELSIZE;
    }

    // Additional methods omitted for brevity, unchanged...
}