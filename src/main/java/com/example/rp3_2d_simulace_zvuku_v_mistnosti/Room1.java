package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Room1 {

    // Constants defining the height and width of the Mistnost1 scene
    private final int mistnost1ScreenHeight = 720;
    private final int mistnost1ScreenWidth = 1080;

    // Sets up the scene for the Mistnost1 (a specific room or environment)
    public void setScene(Stage stage) throws IOException {
        // Load the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(MainMenu.class.getResource("mistnost1.fxml"));

        // Load the mistnost1Scene and create the Scene object
        Scene mistnost1Scene = new Scene(fxmlLoader.load());

        // Get the controller instance from the FXMLLoader
        Room1Controller room1Controller = fxmlLoader.getController();
        // Pass the scene dimensions and stage to the controller
        room1Controller.setStage(stage);
        stage.setTitle("Mistnost1");
        stage.setScene(mistnost1Scene);
        stage.show();
    }

    // Getter for Scene Height
    public int getMistnost1ScreenHeight() {
        return mistnost1ScreenHeight;
    }

    // Getter for Scene Width
    public int getMistnost1ScreenWidth() {
        return mistnost1ScreenWidth;
    }
}