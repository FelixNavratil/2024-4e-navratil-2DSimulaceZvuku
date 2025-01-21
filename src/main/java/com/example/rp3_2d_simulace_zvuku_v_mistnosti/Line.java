package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

public class Line {
    private double A; // Coefficient for x
    private double B; // Coefficient for y
    private double C; // Constant term

    public Line(double A, double B, double C) {
        this.A = A;
        this.B = B;
        this.C = C;
    }

    public Line(Point point1, Point point2){
        double x1 = point1.getX();
        double y1 = point1.getY();
        double x2 = point2.getX();
        double y2 = point2.getY();
        A = (y2 - y1) / (x2 - x1);
        B = y1 - A * x1;
        C = 0;
    }

    public double getA() {
        return A;
    }

    public double getB() {
        return B;
    }

    public double getC() {
        return C;
    }

    @Override
    public String toString() {
        return A + "x + " + B + "y + " + C + " = 0";
    }
}