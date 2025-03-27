package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import javafx.scene.layout.Pane;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * TODO
 */
public class PixelManager {

    private Pixel[][] pixelGrid; // 2D grid of all pixels in the room
    private int width;  // Width of the room
    private int height; // Height of the room
    private BaseRoomControllerInterface roomController;
    private static final int PIXELSIZE = 3;



    public PixelManager(BaseRoomControllerInterface roomController) {
        this.roomController = roomController;
    }


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


    public void addRectanglesToPane(Pane pane) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Pixel pixel = pixelGrid[x][y];

                pane.getChildren().add(pixel.getRectangle());
            }
        }
    }


    public Pixel[][] getPixelGrid() {
        return pixelGrid;
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

    //Resets all the pixels that are not in the set.
    public void resetAllInactivePixels(Set activePixelCoordinates){
// Iterate through every row in the pixelGrid
        for (int row = 0; row < pixelGrid.length; row++) {
            // Iterate through every column in the current row
            for (int col = 0; col < pixelGrid[row].length; col++) {
                Pixel currentPixel = pixelGrid[row][col]; // Access the current pixel
                PixelCoordinate currentPixelCoordinate = currentPixel.getCoordinates(); // Get its coordinates

                // Check if the current pixel's coordinates are NOT in activePixelCoordinates
                if (!activePixelCoordinates.contains(currentPixelCoordinate)) {
                    currentPixel.setDefault(); // Reset the pixel to default state
                }
            }
        }
    }


    public int getPixelSize(){
        return PIXELSIZE;
    }

}