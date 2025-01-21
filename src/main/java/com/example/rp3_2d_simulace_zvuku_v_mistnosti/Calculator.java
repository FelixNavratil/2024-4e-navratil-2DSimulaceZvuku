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
}
