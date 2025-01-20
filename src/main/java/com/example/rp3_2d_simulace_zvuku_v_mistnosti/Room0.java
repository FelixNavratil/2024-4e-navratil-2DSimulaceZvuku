package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Room0 {

    // Constants defining the height and width of the Mistnost0 scene
    private final int mistnost0ScreenHeight = 720;
    private final int mistnost0ScreenWidth = 1080;

    // Sets up the scene for the Mistnost0 (a specific room or environment)
    public void setScene(Stage stage) throws IOException {

        // Load the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(MainMenu.class.getResource("mistnost0.fxml"));
        System.out.println("nastavovani full streed Room0");
        // Load the mistnost0Scene and create the Scene object
        Scene mistnost0Scene = new Scene(fxmlLoader.load());

        // Get the controller instance from the FXMLLoader - metoda initialize
        Room0Controller room0Controller = fxmlLoader.getController();
        //vyska a sirka sceny se dostane do controlleru a pot0 do FXML
        room0Controller.setStage(stage);
        stage.setTitle("Mistnost0");
        stage.setScene(mistnost0Scene);
        stage.show();
    }


    // Getter for Scene Height
    public int getMistnost0ScreenHeight(){
        return mistnost0ScreenHeight;
    }


    // Getter for Scene Width
    public int getMistnost0ScreenWidth(){
        return mistnost0ScreenWidth;
    }

}
