package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import java.awt.*;

/**
 * TODO
 * - musis spocitat vychylku v kazdem bode na kterem je kruznice
 * - for clkus kteery projde kazdou dvojci kruznic
 * - for cylkus ktery projde kaydy pixel a zjisti zda je tam nejaka kruznice/ vic kruznic
 * - musis tyhle pixely udelat fyzicke aby byli vsechny v mistnosti
 */
public class FinalPicture {
    
    // 2D array that represents the grid of pixels
    private Pixel[][] pixels;
    private int width;  // Width of the rectangle
    private int height; // Height of the rectangle

    // Constructor to initialize the grid with the specified dimensions
    public FinalPicture(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new Pixel[width][height];

        // Initialize each pixel in the grid
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //pixels[x][y] = new Pixel(x, y);
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
    
    private Color setColor(int okamzitaVychylka){

        if (okamzitaVychylka >= 90 && okamzitaVychylka <= 100) {
            // Assign color for interval 90 < OV <= 100
            return Color.decode("#100FF");

        } else if (okamzitaVychylka >= 80 && okamzitaVychylka < 90) {
            // Assign color for interval 80 < OV <= 90
            return Color.decode("#2A1BFF");

        } else if (okamzitaVychylka >= 70 && okamzitaVychylka < 80) {
            // Assign color for interval 70 < OV <= 80
            return Color.decode("#483BFF");

        } else if (okamzitaVychylka >= 60 && okamzitaVychylka < 70) {
            // Assign color for interval 60 < OV <= 70
            return Color.decode("#5F54FF");

        } else if (okamzitaVychylka >= 50 && okamzitaVychylka < 60) {
            // Assign color for interval 50 < OV <= 60
            return Color.decode("#756CFF");

        } else if (okamzitaVychylka >= 40 && okamzitaVychylka < 50) {
            // Assign color for interval 40 < OV <= 50
            return Color.decode("#877FFF");

        } else if (okamzitaVychylka >= 30 && okamzitaVychylka < 40) {
            // Assign color for interval 30 < OV <= 40
            return Color.decode("#A39DFF");

        } else if (okamzitaVychylka >= 20 && okamzitaVychylka < 30) {
            // Assign color for interval 20 < OV <= 30
            return Color.decode("#B9B4FF");

        } else if (okamzitaVychylka >= 10 && okamzitaVychylka < 20) {
            // Assign color for interval 10 < OV <= 20
            return Color.decode("#CFCBFF");

        } else if (okamzitaVychylka >= 5 && okamzitaVychylka < 10) {
            // Assign color for interval 0 < OV <= 10
            return Color.decode("#E6E5FF");

        }else if (okamzitaVychylka >= -5 && okamzitaVychylka < 5) {
            // Assign color for interval 0 < OV <= 10
            return Color.decode("#FFFFFF");

        }else if (okamzitaVychylka >= -10 && okamzitaVychylka < -5) {
            // Assign color for interval -10 <= OV < 0
            return Color.decode("#FFE5E5");

        } else if (okamzitaVychylka >= -20 && okamzitaVychylka < -10) {
            // Assign color for interval -20 <= OV < -10
            return Color.decode("#FFCDCD");

        } else if (okamzitaVychylka >= -30 && okamzitaVychylka < -20) {
            // Assign color for interval -30 <= OV < -20
            return Color.decode("#FFB2B2");

        } else if (okamzitaVychylka >= -40 && okamzitaVychylka < -30) {
            // Assign color for interval -40 <= OV < -30
            return Color.decode("#FF9A9A");

        } else if (okamzitaVychylka >= -50 && okamzitaVychylka < -40) {
            // Assign color for interval -50 <= OV < -40
            return Color.decode("#FF8484");

        } else if (okamzitaVychylka >= -60 && okamzitaVychylka < -50) {
            // Assign color for interval -60 <= OV < -50
            return Color.decode("#FF6B6B");

        } else if (okamzitaVychylka >= -70 && okamzitaVychylka < -60) {
            // Assign color for interval -70 <= OV < -60
            return Color.decode("#FF5151");

        } else if (okamzitaVychylka >= -80 && okamzitaVychylka < -70) {
            // Assign color for interval -80 <= OV < -70
            return Color.decode("#FF3737");

        } else if (okamzitaVychylka >= -90 && okamzitaVychylka < -80) {
            // Assign color for interval -90 <= OV < -80
            return Color.decode("#FF1C1C");

        } else if (okamzitaVychylka >= -100 && okamzitaVychylka < -90) {
            // Assign color for interval -100 <= OV < -90
            return Color.decode("#FF0000");

        }else{
            System.err.println("-----------------error FinalPicture.setColor() okamzita vychylka je neplatna-----------------");
            return null;
        }
    }
   
}
