package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import java.awt.*;

/**
 * TODO
 * - musis spocitat vychylku v kazdem bode na kterem je kruznice
 * - for clkus kteery projde kazdou dvojci kruznic
 * - for cylkus ktery projde kaydy pixel a zjisti zda je tam nejaka kruznice/ vic kruznic
 * - musis tyhle pixely udelat fyzicke aby byli vsechny v mistnosti
 */
public class SoundWaveVisualiser {

    private BaseRoomControllerInterface controller;
    // 2D array that represents the grid of pixels
    private Pixel[][] pixels;
    private int width;  // Width of the rectangle
    private int height; // Height of the rectangle

    // Constructor to initialize the grid with the specified dimensions
    public SoundWaveVisualiser(int width, int height, BaseRoomControllerInterface controller) {
        this.width = width;
        this.height = height;
        this.pixels = new Pixel[width][height];

        // Initialize each pixel in the grid
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixels[x][y] = new Pixel(x, y,0);
            }
        }
    }

    // Method to light up a pixel at (x, y) with a desired color
    public void lightUpPixel(int x, int y, Color color) {
        if (isInBounds(x, y)) {
            pixels[x][y].lightUp(color);
        } else {
            System.out.println("Pixel out of bounds: (" + x + ", " + y + ")");
        }
    }

    // Method to check if a wave is on the same position as a pixel
    public boolean isWaveOnPixel(int x, int y, int waveX, int waveY) {
        if (!isInBounds(x, y)) {
            return false; // Pixel is out of bounds
        }
        return pixels[x][y].getX() == waveX && pixels[x][y].getY() == waveY;
    }

    // Helper method to check bounds
    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    // Method to retrieve the pixel at (x, y)
    public Pixel getPixel(int x, int y) {
        if (isInBounds(x, y)) {
            return pixels[x][y];
        }
        throw new IndexOutOfBoundsException("Pixel out of bounds: (" + x + ", " + y + ")");
    }
    

   
}
