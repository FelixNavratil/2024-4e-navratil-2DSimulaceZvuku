package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.time.LocalDateTime;
import java.time.Duration;

/**
 * The Pixel class represents an individual pixel in a 2D grid with a specific position
 * and the ability to be lit with a specified color.
 */
public class Pixel extends Rectangle {
    private int gridX;       // The x-coordinate in the grid
    private int gridY;       // The y-coordinate in the grid
    private double realX;    // The real-world x-coordinate on the scene
    private double realY;    // The real-world y-coordinate on the scene
    private int celkovaVychylka;
    private Rectangle rectangle;   // The visual representation of the pixel as a rectangle
    private static int PIXELSIZE;

    public void setPixelSize(int pixelSize) {
        this.PIXELSIZE = pixelSize;
    }

    // Constructor for the pixel
    public Pixel(int gridX, int gridY, double realX, double realY) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.realX = realX;
        this.realY = realY;
        celkovaVychylka = 0;

        // Initialize the rectangle to represent this pixel, with default properties
        rectangle = new Rectangle();
        rectangle.setX(realX);       // Set the X position of the rectangle
        rectangle.setY(realY);       // Set the Y position of the rectangle
        rectangle.setWidth(PIXELSIZE);     // Set the rectangle width (1 pixel)
        rectangle.setHeight(PIXELSIZE);    // Set the rectangle height (1 pixel)
        setDefault();

    }

    public static int getGridX(double realX, double xMin) {
        return (int) Math.floor((realX - xMin) / PIXELSIZE);
    }

    /**
     * Get the GridY index from real-world coordinates, considering room position.
     */
    public static int getGridY(double realY, double yMIn) {
        return (int) Math.floor((realY - yMIn) / PIXELSIZE);
    }


    public Rectangle getRectangle() {
        return rectangle;
    }

    //Updates the pixel's color based on the celkovaVychylka.
    private void setColor(int celkovaVychylka) {
        // Get the JavaFX color from the method
        Color color = createColor(celkovaVychylka);
        // Set the fill of the rectangle using JavaFX Color
        rectangle.setFill(color);
    }

    public void setDefault() {
        celkovaVychylka = 0;
        setColor(celkovaVychylka);
        // Note: Do NOT update lastUpdatedTime during default initialization
    }

    //Adds to celkovaVychylka
    public void addVychylka(int vychylka) {
        celkovaVychylka += vychylka;
        setColor(celkovaVychylka);
    }


    //creates color based on the celkova vychylka
    private Color createColor(int celkovaVychylka) {


        if (celkovaVychylka > 400) {
            return javafx.scene.paint.Color.web("#8B0000"); // DarkRed
        } else if (celkovaVychylka > 380) {
            return javafx.scene.paint.Color.web("#A00000");
        } else if (celkovaVychylka > 360) {
            return javafx.scene.paint.Color.web("#BB0000");
        } else if (celkovaVychylka > 340) {
            return javafx.scene.paint.Color.web("#DD0000");
        } else if (celkovaVychylka > 320) {
            return javafx.scene.paint.Color.web("#EE0000");
        } else if (celkovaVychylka > 300) {
            return javafx.scene.paint.Color.web("#FF0808");
        } else if (celkovaVychylka > 280) {
            return javafx.scene.paint.Color.web("#FF1919");
        } else if (celkovaVychylka > 260) {
            return javafx.scene.paint.Color.web("#FF2A2A");
        } else if (celkovaVychylka > 240) {
            return javafx.scene.paint.Color.web("#FF3C3C");
        } else if (celkovaVychylka > 220) {
            return javafx.scene.paint.Color.web("#FF4E4E");
        } else if (celkovaVychylka > 200) {
            return javafx.scene.paint.Color.web("#FF5F5F");
        } else if (celkovaVychylka > 180) {
            return javafx.scene.paint.Color.web("#FF7171");
        } else if (celkovaVychylka > 160) {
            return javafx.scene.paint.Color.web("#FF8282");
        } else if (celkovaVychylka > 140) {
            return javafx.scene.paint.Color.web("#FF9494");
        } else if (celkovaVychylka > 120) {
            return javafx.scene.paint.Color.web("#FFA6A6");
        } else if (celkovaVychylka > 100) {
            return javafx.scene.paint.Color.web("#FFB8B8");
        } else if (celkovaVychylka > 80) {
            return javafx.scene.paint.Color.web("#FFCACA");
        } else if (celkovaVychylka > 60) {
            return javafx.scene.paint.Color.web("#FFDBDB");
        } else if (celkovaVychylka > 40) {
            return javafx.scene.paint.Color.web("#FFEDED");
        } else if (celkovaVychylka > 20) {
            return javafx.scene.paint.Color.web("#FFEDED"); // White
        } else if (celkovaVychylka >= -20) { // From -20 to 20 is White
            return javafx.scene.paint.Color.web("#FFFFFF"); // White
        } else if (celkovaVychylka > -40) {
            return javafx.scene.paint.Color.web("#E9F3FC");
        } else if (celkovaVychylka > -60) {
            return javafx.scene.paint.Color.web("#D8ECF8");
        } else if (celkovaVychylka > -80) {
            return javafx.scene.paint.Color.web("#C6E5F0");
        } else if (celkovaVychylka > -100) {
            return javafx.scene.paint.Color.web("#ADD8E6"); // LightBlue
        } else if (celkovaVychylka > -120) {
            return javafx.scene.paint.Color.web("#9FDDED");
        } else if (celkovaVychylka > -140) {
            return javafx.scene.paint.Color.web("#75E3E8");
        } else if (celkovaVychylka > -160) {
            return javafx.scene.paint.Color.web("#3BCFE2");
        } else if (celkovaVychylka > -180) {
            return javafx.scene.paint.Color.web("#00BBDC");
        } else if (celkovaVychylka > -200) {
            return javafx.scene.paint.Color.web("#00A7D3");
        } else if (celkovaVychylka > -220) {
            return javafx.scene.paint.Color.web("#0094CB");
        } else if (celkovaVychylka > -240) {
            return javafx.scene.paint.Color.web("#0081C3");
        } else if (celkovaVychylka > -260) {
            return javafx.scene.paint.Color.web("#006EBB");
        } else if (celkovaVychylka > -280) {
            return javafx.scene.paint.Color.web("#005BB3");
        } else if (celkovaVychylka > -300) {
            return javafx.scene.paint.Color.web("#0048AB");
        } else if (celkovaVychylka > -320) {
            return javafx.scene.paint.Color.web("#0035A3");
        } else if (celkovaVychylka > -340) {
            return javafx.scene.paint.Color.web("#00229B");
        } else if (celkovaVychylka > -360) {
            return javafx.scene.paint.Color.web("#001093");
        } else if (celkovaVychylka > -380) {
            return javafx.scene.paint.Color.web("#00008B"); // DarkBlue
        } else if (celkovaVychylka > -400) {
            return javafx.scene.paint.Color.web("#00008B"); // Same DarkBlue
        } else {
            return javafx.scene.paint.Color.web("#00008B"); // DarkBlue for <= -400

        }
    }

    public PixelCoordinate getCoordinates() {
        return new PixelCoordinate(gridX, gridY);
    }
}