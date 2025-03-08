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
        this.light = null; // By default, the pixel is not lit
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
}
