package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import java.awt.*;

/**
 * The Pixel class represents an individual pixel in a 2D grid with a specific position
 * and the ability to be lit with a specified color.
 */
public class Pixel {
    private int x;
    private int y;
    private Color light; // Light property (null if not lit)
    private int celkovaVychylka;

    // Constructor for the pixel
    public Pixel(int x, int y, int celkovaVychylka) {
        this.x = x;
        this.y = y;
        this.light = setColor(celkovaVychylka); // By default, the pixel is not lit
        this.celkovaVychylka = celkovaVychylka;
    }

    public int getCelkovaVychylka() {
        return celkovaVychylka;
    }

    public void setCelkovaVychylka(int celkovaVychylka) {
        this.celkovaVychylka = celkovaVychylka;
    }

    // Getters and setters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getLight() {
        return light;
    }

    // Method to light up the pixel with a desired color
    public void lightUp(Color color) {
        this.light = color;
    }

    // Method to turn off the light
    public void turnOff() {
        this.light = null;
    }

    // Check if the pixel is lit
    public boolean isLit() {
        return this.light != null;
    }

    @Override
    public String toString() {
        return "Pixel{" +
                "x=" + x +
                ", y=" + y +
                ", light=" + (light != null ? light.toString() : "OFF") +
                '}';
    }

    private Color setColor(int celkovaVychylka){

        if (celkovaVychylka >= 90 && celkovaVychylka <= 100) {
            // Assign color for interval 90 < CV <= 100
            return Color.decode("#100FF");

        } else if (celkovaVychylka >= 80 && celkovaVychylka < 90) {
            // Assign color for interval 80 < CV <= 90
            return Color.decode("#2A1BFF");

        } else if (celkovaVychylka >= 70 && celkovaVychylka < 80) {
            // Assign color for interval 70 < CV <= 80
            return Color.decode("#483BFF");

        } else if (celkovaVychylka >= 60 && celkovaVychylka < 70) {
            // Assign color for interval 60 < CV <= 70
            return Color.decode("#5F54FF");

        } else if (celkovaVychylka >= 50 && celkovaVychylka < 60) {
            // Assign color for interval 50 < CV <= 60
            return Color.decode("#756CFF");

        } else if (celkovaVychylka >= 40 && celkovaVychylka < 50) {
            // Assign color for interval 40 < CV <= 50
            return Color.decode("#877FFF");

        } else if (celkovaVychylka >= 30 && celkovaVychylka < 40) {
            // Assign color for interval 30 < CV <= 40
            return Color.decode("#A39DFF");

        } else if (celkovaVychylka >= 20 && celkovaVychylka < 30) {
            // Assign color for interval 20 < CV <= 30
            return Color.decode("#B9B4FF");

        } else if (celkovaVychylka >= 10 && celkovaVychylka < 20) {
            // Assign color for interval 10 < CV <= 20
            return Color.decode("#CFCBFF");

        } else if (celkovaVychylka >= 5 && celkovaVychylka < 10) {
            // Assign color for interval 0 < CV <= 10
            return Color.decode("#E6E5FF");

        } else if (celkovaVychylka >= -5 && celkovaVychylka < 5) {
            // Assign color for interval 0 < CV <= 10
            return Color.decode("#FFFFFF");

        } else if (celkovaVychylka >= -10 && celkovaVychylka < -5) {
            // Assign color for interval -10 <= CV < 0
            return Color.decode("#FFE5E5");

        } else if (celkovaVychylka >= -20 && celkovaVychylka < -10) {
            // Assign color for interval -20 <= CV < -10
            return Color.decode("#FFCDCD");

        } else if (celkovaVychylka >= -30 && celkovaVychylka < -20) {
            // Assign color for interval -30 <= CV < -20
            return Color.decode("#FFB2B2");

        } else if (celkovaVychylka >= -40 && celkovaVychylka < -30) {
            // Assign color for interval -40 <= CV < -30
            return Color.decode("#FF9A9A");

        } else if (celkovaVychylka >= -50 && celkovaVychylka < -40) {
            // Assign color for interval -50 <= CV < -40
            return Color.decode("#FF8484");

        } else if (celkovaVychylka >= -60 && celkovaVychylka < -50) {
            // Assign color for interval -60 <= CV < -50
            return Color.decode("#FF6B6B");

        } else if (celkovaVychylka >= -70 && celkovaVychylka < -60) {
            // Assign color for interval -70 <= CV < -60
            return Color.decode("#FF5151");

        } else if (celkovaVychylka >= -80 && celkovaVychylka < -70) {
            // Assign color for interval -80 <= CV < -70
            return Color.decode("#FF3737");

        } else if (celkovaVychylka >= -90 && celkovaVychylka < -80) {
            // Assign color for interval -90 <= CV < -80
            return Color.decode("#FF1C1C");

        } else if (celkovaVychylka >= -100 && celkovaVychylka < -90) {
            // Assign color for interval -100 <= CV < -90
            return Color.decode("#FF0000");

        } else {
            System.err.println("-----------------error FinalPicture.setColor() celkova vychylka je neplatna-----------------");
            return null;
        }
    }
}
