package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import javafx.stage.Stage;

import java.util.List;

public interface BaseRoomControllerInterface {
    void setStage(Stage stage);
    void initialize();
    int getXMin();
    int getXMax();
    int getYMin();
    int getYMax();
    void overlayRectangles();
    void updateLayout();
    List<Line> getRoomWalls();
    List<Point> getRoomCorners();
    int getStroke();

}
