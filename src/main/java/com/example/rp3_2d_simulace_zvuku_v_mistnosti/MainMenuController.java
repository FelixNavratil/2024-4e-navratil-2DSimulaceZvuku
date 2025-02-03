package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    // These variables will store the passed scene dimensions
    private int hlavniMenuSceneHeight;
    private int hlavniMenuSceeneWidth;

    // Method to set scene height and width
    public void setSceneDimensions(int height, int width) {
        this.hlavniMenuSceneHeight = height;
        this.hlavniMenuSceeneWidth = width;
        // Example: You can use these values in your FXML components
        updateLayout();
    }



    // Reference to the Stage object to switch scenes
    private Stage stage;

    // Setter to pass the Stage from the main application
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private Button buttonRoom0;// A button in the main menu

    @FXML
    private Button buttonRoom1;

    @FXML
    private VBox hlavniMenuVBox;// A VBox (layout container) in the main menu

    // Method to handle layout updates
    private void updateLayout() {
        // Ensure VBox is resized according to the scene dimensions
        if (hlavniMenuVBox != null) {
            hlavniMenuVBox.setPrefWidth(hlavniMenuSceeneWidth * 0.8);  // Example: 80% of the scene width
            hlavniMenuVBox.setPrefHeight(hlavniMenuSceneHeight * 0.8); // Example: 80% of the scene height
            buttonRoom0.setPrefHeight(hlavniMenuSceneHeight /5);
            buttonRoom0.setPrefWidth(hlavniMenuSceeneWidth /5);
            buttonRoom1.setPrefHeight(hlavniMenuSceneHeight /5);
            buttonRoom1.setPrefWidth(hlavniMenuSceeneWidth /5);
        }
    }

    @FXML
    protected void handleButtonRoom1Click() throws IOException {
        System.out.println(" spustit Room1 ");
        Room1 room1 = new Room1();
        room1.setScene(stage);

    }

    // Method for handling button clicks
    @FXML
    protected void handleButtonRoom0Click() throws IOException {
        System.out.println("spustit Room0");

        // Create an instance of Mistnost0 to switch scenes
        Room0 room0 = new Room0();

        // Switch to Mistnost0 scene by passing the current stage
        room0.setScene(stage);
    }
}