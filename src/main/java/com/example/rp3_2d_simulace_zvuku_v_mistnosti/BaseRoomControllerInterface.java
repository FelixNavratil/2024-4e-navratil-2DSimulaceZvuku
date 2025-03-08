package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import javafx.stage.Stage;

import java.util.List;

public interface BaseRoomControllerInterface {
    void setStage(Stage stage);
    void initialize();
    private void initializeRectangle(double x, double y) {}
    int getXMin();
    int getXMax();
    int getYMin();
    int getYMax();
    private void createOverlay(){}
    void overlayRectangles();
    void updateLayout();
    private void initializeLines(double xMin, double xMax, double yMin, double yMax){}
    public List<Line> getRoomWalls();
    List<Point> getRoomCorners();
    public void setSoundWaveVisualiser(double width, double height);

}
