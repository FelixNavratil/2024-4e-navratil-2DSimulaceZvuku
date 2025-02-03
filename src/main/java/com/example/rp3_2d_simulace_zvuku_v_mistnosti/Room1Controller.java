package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Room1Controller {

    // Room-specific variables
    Room1 room1 = new Room1();
    private int room1Height = room1.getMistnost1ScreenHeight();
    private int room1Width = room1.getMistnost1ScreenWidth();
    private double rectangleSize = (double) room1Width / 4;
    private boolean isRunning = true;

    @FXML
    BorderPane root;
    @FXML
    private Button buttonHlavniMenu;
    @FXML
    private Button buttonStop;
    @FXML
    private Label labelInstrukce;
    @FXML
    private Button buttonResume;
    @FXML
    private Button buttonReset;
    @FXML
    private Pane centerPane;
    @FXML
    private HBox mistnost1HBox;
    @FXML
    private Label bottomText;
    @FXML
    private Rectangle rectangle;

    private Stage stage;

    // Timer variables
    private Timeline timer;
    private int millisecondsElapsed = 0;

    // Wave-related variables
    private WaveFactory waveFactory = new WaveFactory();
    private WaveManager waveManager = new WaveManager(waveFactory, centerPane);
    private Timeline waveTimeline;

    public void setSceneDimensions(Scene scene, int height, int width) {
        stage.setScene(scene);
        stage.setHeight(height);
        stage.setWidth(width);
        updateLayout();
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        boolean isFullScreen = stage.isFullScreen();
        if (!isFullScreen) {
            double width = Screen.getPrimary().getBounds().getWidth();
            double height = Screen.getPrimary().getBounds().getHeight();
            stage.setWidth(width);
            stage.setHeight(height);
            stage.setMaximized(true);
            stage.setResizable(true);
        } else {
            System.out.println("Fullscreen mode is enabled. Dimensions managed by JavaFX.");
        }
    }

    @FXML
    public void initialize() {
        waveFactory = new WaveFactory();
        waveManager = new WaveManager(waveFactory, centerPane);
        updateLayout();

        createTimeline();
        buttonResume.setDisable(true);
        buttonStop.setDisable(true);
        buttonReset.setDisable(true);

        centerPane.setPrefHeight(room1Height);
        centerPane.setPrefWidth(room1Width);
    }

    private void createTimeline() {
        timer = new Timeline(new KeyFrame(Duration.millis(1), event -> {
            millisecondsElapsed++;
            updateTimerLabel();
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
    }

    // Dynamically update button sizes based on `room1Height` and `room1Width`
    public void updateLayout() {
        System.out.println("update layout");
        if (root != null){
            //Velikost buttonHlavniMenu

            buttonHlavniMenu.setPrefWidth(room1Width /8);
            buttonHlavniMenu.setPrefHeight(room1Height /8);

            //velikost buttonStop a start
            labelInstrukce.setPrefSize(room1Width /8, room1Height /8);
            buttonStop.setPrefSize(room1Width /8, room1Height /8);
            buttonResume.setPrefSize(room1Width /8, room1Height /8);
            buttonReset.setPrefSize(room1Width /8, room1Height /8);

            //velikost bottom textu
            bottomText.setPrefSize(room1Width /8, room1Height /8);
            bottomText.setFont(new Font(20));

        }
    }

    private void updateTimerLabel() {
        int hours = millisecondsElapsed / (3600 * 1000);
        int minutes = (millisecondsElapsed % (3600 * 1000)) / (60 * 1000);
        int seconds = (millisecondsElapsed % (60 * 1000)) / 1000;
        int milliseconds = millisecondsElapsed % 1000;

        bottomText.setText(String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds));
    }

    @FXML
    public void handleButtonHlavniMenuClick() throws IOException {
        MainMenu hlavniMenu = new MainMenu();
        hlavniMenu.setScene(stage);
    }

    @FXML
    public void handleButtonStopClick() throws IOException {
        timer.stop();

        if (waveTimeline != null) {
            waveTimeline.pause();
            waveManager.pauseWaves();
        }

        buttonStop.setDisable(true);
        buttonResume.setDisable(false);
        buttonReset.setDisable(false);

        System.out.println("Timer stopped.");
    }

    @FXML
    public void handleButtonResumeClick() {
        timer.play();

        if (waveTimeline != null) {
            waveTimeline.play();
            waveManager.resumeWaves();
        }

        buttonStop.setDisable(false);
        buttonResume.setDisable(true);
        buttonReset.setDisable(false);

        System.out.println("Timer resumed.");
    }

    @FXML
    public void handleButtonResetClick() {
        timer.stop();
        millisecondsElapsed = 0;
        updateTimerLabel();
        buttonStop.setDisable(true);
        buttonResume.setDisable(true);
        buttonReset.setDisable(true);

        if (waveTimeline != null) {
            waveTimeline.stop();
            waveTimeline = null;
        }

        waveManager.resetWaves();
        centerPane.getChildren().clear();
    }

    @FXML
    public void handlePaneClick(MouseEvent event) {
        /*
        double x = event.getX();
        double y = event.getY();

        if (rectangle.contains(x, y)) {
            waveManager.createWave(x, y, this, 1);
            timer.play();
            buttonStop.setDisable(false);
            buttonResume.setDisable(true);
            buttonReset.setDisable(false);
        } else {
            System.out.println("Click was not on the rectangle.");
        }

        if (waveTimeline == null) {
            waveTimeline = new Timeline(new KeyFrame(Duration.millis(16), e -> waveManager.updateWaves(Room1Controller.this)));
            waveTimeline.setCycleCount(Timeline.INDEFINITE);
            waveTimeline.play();
        }

         */
    }
}