package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.awt.*;

/**
 * The Pixel class represents an individual pixel in a 2D grid with a specific position
 * and the ability to be lit with a specified color.
 */
public class Pixel extends Rectangle{
    private int gridX;       // The x-coordinate in the grid
    private int gridY;       // The y-coordinate in the grid
    private double realX;    // The real-world x-coordinate on the scene
    private double realY;    // The real-world y-coordinate on the scene
    private int celkovaVychylka;
    private Rectangle rectangle;   // The visual representation of the pixel as a rectangle
    private static final double PIXELSIZE = 2;

    // Constructor for the pixel
    public Pixel(int gridX, int gridY, double realX, double realY) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.realX = realX;
        this.realY = realY;
        celkovaVychylka = 50;

        // Initialize the rectangle to represent this pixel, with default properties
        rectangle = new Rectangle();
        rectangle.setX(realX);       // Set the X position of the rectangle
        rectangle.setY(realY);       // Set the Y position of the rectangle
        rectangle.setWidth(PIXELSIZE);     // Set the rectangle width (1 pixel)
        rectangle.setHeight(PIXELSIZE);    // Set the rectangle height (1 pixel)
        setColor(celkovaVychylka);

    }

    public int getCelkovaVychylka() {
        return celkovaVychylka;
    }

    public void setCelkovaVychylka(int celkovaVychylka) {
        this.celkovaVychylka = celkovaVychylka;
        setColor(celkovaVychylka);
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public double getRealX() {
        return realX;
    }

    public double getRealY() {
        return realY;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }


    private void setColor(int celkovaVychylka){
        // Get the JavaFX color from the method
        Color color = createColor(celkovaVychylka);
        // Set the fill of the rectangle using JavaFX Color
        rectangle.setFill(color);

    }

    public void setDefault(){
        celkovaVychylka = 10;
        setColor(celkovaVychylka);
    }


    @Override
    public String toString() {
        return "Pixel{" +
                ", realX=" + realX +
                ", realY=" + realY +
                '}';
    }



    private Color createColor(int celkovaVychylka) {

        if (celkovaVychylka >= 90 && celkovaVychylka <= 100) {
            // Assign color for interval 90 <= CV <= 100
            return javafx.scene.paint.Color.web("#0100FF"); // Fixed hex code for JavaFX Color

        } else if (celkovaVychylka >= 80 && celkovaVychylka < 90) {
            // Assign color for interval 80 <= CV < 90
            return javafx.scene.paint.Color.web("#2A1BFF");

        } else if (celkovaVychylka >= 70 && celkovaVychylka < 80) {
            // Assign color for interval 70 <= CV < 80
            return javafx.scene.paint.Color.web("#483BFF");

        } else if (celkovaVychylka >= 60 && celkovaVychylka < 70) {
            // Assign color for interval 60 <= CV < 70
            return javafx.scene.paint.Color.web("#5F54FF");

        } else if (celkovaVychylka >= 50 && celkovaVychylka < 60) {
            // Assign color for interval 50 <= CV < 60
            return javafx.scene.paint.Color.web("#756CFF");

        } else if (celkovaVychylka >= 40 && celkovaVychylka < 50) {
            // Assign color for interval 40 <= CV < 50
            return javafx.scene.paint.Color.web("#877FFF");

        } else if (celkovaVychylka >= 30 && celkovaVychylka < 40) {
            // Assign color for interval 30 <= CV < 40
            return javafx.scene.paint.Color.web("#A39DFF");

        } else if (celkovaVychylka >= 20 && celkovaVychylka < 30) {
            // Assign color for interval 20 <= CV < 30
            return javafx.scene.paint.Color.web("#B9B4FF");

        } else if (celkovaVychylka >= 10 && celkovaVychylka < 20) {
            // Assign color for interval 10 <= CV < 20
            return javafx.scene.paint.Color.web("#CFCBFF");

        } else if (celkovaVychylka >= 5 && celkovaVychylka < 10) {
            // Assign color for interval 5 <= CV < 10
            return javafx.scene.paint.Color.web("#E6E5FF");

        } else if (celkovaVychylka >= -5 && celkovaVychylka < 5) {
            // Assign color for interval -5 <= CV < 5
            return javafx.scene.paint.Color.web("#FFFFFF");

        } else if (celkovaVychylka >= -10 && celkovaVychylka < -5) {
            // Assign color for interval -10 <= CV < -5
            return javafx.scene.paint.Color.web("#FFE5E5");

        } else if (celkovaVychylka >= -20 && celkovaVychylka < -10) {
            // Assign color for interval -20 <= CV < -10
            return javafx.scene.paint.Color.web("#FFCDCD");

        } else if (celkovaVychylka >= -30 && celkovaVychylka < -20) {
            // Assign color for interval -30 <= CV < -20
            return javafx.scene.paint.Color.web("#FFB2B2");

        } else if (celkovaVychylka >= -40 && celkovaVychylka < -30) {
            // Assign color for interval -40 <= CV < -30
            return javafx.scene.paint.Color.web("#FF9A9A");

        } else if (celkovaVychylka >= -50 && celkovaVychylka < -40) {
            // Assign color for interval -50 <= CV < -40
            return javafx.scene.paint.Color.web("#FF8484");

        } else if (celkovaVychylka >= -60 && celkovaVychylka < -50) {
            // Assign color for interval -60 <= CV < -50
            return javafx.scene.paint.Color.web("#FF6B6B");

        } else if (celkovaVychylka >= -70 && celkovaVychylka < -60) {
            // Assign color for interval -70 <= CV < -60
            return javafx.scene.paint.Color.web("#FF5151");

        } else if (celkovaVychylka >= -80 && celkovaVychylka < -70) {
            // Assign color for interval -80 <= CV < -70
            return javafx.scene.paint.Color.web("#FF3737");

        } else if (celkovaVychylka >= -90 && celkovaVychylka < -80) {
            // Assign color for interval -90 <= CV < -80
            return javafx.scene.paint.Color.web("#FF1C1C");

        } else if (celkovaVychylka >= -100 && celkovaVychylka < -90) {
            // Assign color for interval -100 <= CV < -90
            return javafx.scene.paint.Color.web("#FF0000");

        } else {
            System.err.println("-----------------Error FinalPicture.setColor() celkova vychylka je neplatna-----------------");
            return null;
        }
    }
}
