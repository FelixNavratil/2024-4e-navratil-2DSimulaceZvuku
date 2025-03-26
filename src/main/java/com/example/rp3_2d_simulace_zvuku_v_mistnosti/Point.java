package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

public class Point {
    private double x;
    private double y;
    private int okamzitaVychylka;
    private Calculator calculator = new Calculator();

    public Point(double x, double y) {
        setX(x);
        setY(y);
    }

    public Point(Line line1, Line line2){
        setX(calculator.calculateIntersection(line1, line2).getX());
        setY(calculator.calculateIntersection(line1, line2).getY());
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public double distance(Point other) {
        return Math.sqrt(Math.pow(other.x - this.x, 2) + Math.pow(other.y - this.y, 2));
    }


}
