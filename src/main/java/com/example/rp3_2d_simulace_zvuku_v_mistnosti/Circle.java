package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

public class Circle {

    // Fields to represent the circle's center and radius
    private double centerX; // x-coordinate of the center
    private double centerY; // y-coordinate of the center
    private double radius;  // Radius of the circle

    /**
     * Constructor to initialize the circle.
     *
     * @param centerX The x-coordinate of the center.
     * @param centerY The y-coordinate of the center.
     * @param radius  The radius of the circle.
     */
    public Circle(double centerX, double centerY, double radius) {
        this.centerX = centerX;
        this.centerY = centerY;

        // Ensure the radius is positive
        if (radius < 0) {
            throw new IllegalArgumentException("Radius cannot be negative");
        }
        this.radius = radius;
    }

    // Getters and setters for x, y, and radius
    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("Radius cannot be negative");
        }
        this.radius = radius;
    }

    /**
     * Returns the mathematical representation of the circle
     * in the form: (x - a)² + (y - b)² = r²
     *
     * @return A string representing the circle's equation.
     */
    public String getEquation() {
        return String.format("(x - %.1f)² + (y - %.1f)² = %.1f²", centerX, centerY, radius);
    }

    /**
     * Calculates and returns the circle's area.
     *
     * @return The area of the circle.
     */
    public double getArea() {
        return Math.PI * Math.pow(radius, 2);
    }

    /**
     * Calculates and returns the circle's circumference.
     *
     * @return The circumference of the circle.
     */
    public double getCircumference() {
        return 2 * Math.PI * radius;
    }

    /**
     * Checks if a given point (x, y) lies inside the circle.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     * @return True if the point lies inside or on the circle, false otherwise.
     */
    public boolean isPointInside(double x, double y) {
        double distanceSquared = Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2);
        return distanceSquared <= Math.pow(radius, 2);
    }

    /**
     * Returns the values of the circle (center and radius) in a user-friendly format.
     *
     * @return A string representation of the circle's values.
     */
    @Override
    public String toString() {
        return String.format("Circle(center: (%.1f, %.1f), radius: %.1f)", centerX, centerY, radius);
    }
}
