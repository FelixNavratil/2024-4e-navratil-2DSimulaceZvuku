package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu {
    // Constants representing the dimensions of the main menu scene
    private final int hlavniMenuSceeneHeight = 500;
    private final int hlavniMenuSceneWidth = 700;


    // This method sets up the scene for the stage (possibly the main menu UI)
    public void setScene(Stage stage) throws IOException {

        // Load the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(MainMenu.class.getResource("hlavni-menu.fxml"));

        // Load the hlavniMenuScene and create the Scene object
        Scene hlavniMenuScene = new Scene(fxmlLoader.load(), hlavniMenuSceneWidth, hlavniMenuSceeneHeight);

        // Get the controller instance from the FXMLLoader
        MainMenuController hlavniMenuController = fxmlLoader.getController();

        // Pass the hlavniMenuScene dimensions to the controller
        hlavniMenuController.setSceneDimensions(hlavniMenuSceeneHeight, hlavniMenuSceneWidth);

        // Pass the stage to the controller so it can switch scenes
        hlavniMenuController.setStage(stage);


        stage.setTitle("Hlavni Menu");
        stage.setScene(hlavniMenuScene);
        stage.show();
    }

    public int getHlavniMenuSceeneHeight() {
        return hlavniMenuSceeneHeight;
    }

    public int getHlavniMenuSceneWidth() {
        return hlavniMenuSceneWidth;
    }
}