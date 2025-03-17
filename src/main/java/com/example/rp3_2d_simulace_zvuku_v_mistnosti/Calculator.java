package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

public class Calculator {
    /**
     * Calculate the intersection point of two lines in general form (Ax + By + C = 0).
     *
     * @param line1 First line
     * @param line2 Second line
     * @return A double array [x, y] representing the intersection point, or null if no unique intersection exists.
     */
    public Point calculateIntersection(Line line1, Line line2) {
        // Extract coefficients from each line
        double A1 = line1.getA();
        double B1 = line1.getB();
        double C1 = line1.getC();

        double A2 = line2.getA();
        double B2 = line2.getB();
        double C2 = line2.getC();

        // Calculate determinant
        double determinant = A1 * B2 - A2 * B1;

        // If determinant is 0, the lines are parallel or coincident
        if (determinant == 0) {
            return null;
        }

        // Calculate the intersection point
        double x = (B1 * C2 - B2 * C1) / determinant;
        double y = (A2 * C1 - A1 * C2) / determinant;


        return new Point(x, y);
    }

    public Point calculateSymetricPoint(Point point, Line line){
        // Extract line coefficients
        double A = line.getA();
        double B = line.getB();
        double C = line.getC();

        // Extract point coordinates
        double x = point.getX();
        double y = point.getY();

        // Calculate projection point (foot of perpendicular)
        double denominator = A * A + B * B;
        if (denominator == 0) {
            throw new IllegalArgumentException("Invalid line coefficients: A and B cannot both be zero.");
        }

        double x_proj = (B * (B * x - A * y) - A * C) / denominator;
        double y_proj = (A * (-B * x + A * y) - B * C) / denominator;

        // Calculate symmetric point
        double x_sym = 2 * x_proj - x;
        double y_sym = 2 * y_proj - y;

        // Return the symmetric point
        return new Point(x_sym, y_sym);
    }


    /**
     * Calculate the distance between two points given their coordinates as integers.
     *
     * @param x1 x-coordinate of the first point
     * @param y1 y-coordinate of the first point
     * @param x2 x-coordinate of the second point
     * @param y2 y-coordinate of the second point
     * @return The distance between the two points as a double
     */
    public double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
