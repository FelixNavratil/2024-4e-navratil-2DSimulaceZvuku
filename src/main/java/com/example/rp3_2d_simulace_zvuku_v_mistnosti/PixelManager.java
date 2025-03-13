package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO momentalne se pixely vepisuji i na okraj obdelnika a to nechci
 * sjistit jake souradnice ma soundwave
 */
public class PixelManager {

    private Pixel[][] pixelGrid; // 2D grid of all pixels in the room
    private int width;  // Width of the room
    private int height; // Height of the room
    private Map<String, Double> pixelDisplacements = new HashMap<>();

    public PixelManager(BaseRoomControllerInterface roomController) {
        // Empty constructor; initialization happens in initializePixelGrid
    }

    /**
     * Initializes the 2D array of Pixels based on the rectangle's width, height, and its position in the scene.
     *
     * @param width     Width of the rectangle (number of columns in the grid).
     * @param height    Height of the rectangle (number of rows in the grid).
     * @param rectX     The starting X-coordinate of the rectangle in the scene.
     * @param rectY     The starting Y-coordinate of the rectangle in the scene.
     */
    public void initializePixelGrid(int width, int height, double rectX, double rectY) {
        this.width = width;
        this.height = height;

        // Create a 2D array matching the dimensions
        pixelGrid = new Pixel[width][height];

        // Initialize each pixel with its real-world scene coordinates
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Calculate the real X and Y on the scene
                double realX = rectX + x; // Add the x-offset of the rectangle
                double realY = rectY + y; // Add the y-offset of the rectangle

                pixelGrid[x][y] = new Pixel(x, y, realX, realY); // Add real coordinates
            }
        }

        // Debugging: Print the width and height of the grid
        System.out.println("Width: " + pixelGrid.length);
        System.out.println("Height: " + (pixelGrid.length > 0 ? pixelGrid[0].length : 0));
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

    // Additional methods omitted for brevity, unchanged...
}