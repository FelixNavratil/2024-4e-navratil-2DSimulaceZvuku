package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.launch;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        MainMenu hlavniMenu = new MainMenu();
        hlavniMenu.setScene(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
