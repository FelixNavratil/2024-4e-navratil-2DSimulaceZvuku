package com.example.rp3_2d_simulace_zvuku_v_mistnosti;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.util.List;

public class Room1Controller implements BaseRoomControllerInterface {

    // Room-specific variables
    Room1 room1 = new Room1();
    private int room1Height = room1.getMistnost1ScreenHeight();
    private int room1Width = room1.getMistnost1ScreenWidth();
    private double rectangleHeight = (double) room1Height / 4;
    private double rectangleWidth = (double) room1Width / 4;
    private static boolean vlnaExistuje = false;
    private boolean isRunning = true;
    private double currentRadius = 0;
    private double x;
    private double y;
    private final int roomID = 1;

    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;
    private List<Line> roomWalls;
    private List<Point> roomCorners;

    public List<Line> getRoomWalls() {
        return roomWalls;
    }

    public List<Point> getRoomCorners() {
        return roomCorners;
    }

    Rectangle topRectangle;
    Rectangle bottomRectangle;
    Rectangle leftRectangle;
    Rectangle rightRectangle;

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
            initializeRectangle(stage.getWidth()/2, stage.getHeight()/2);
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

    public void initializeRectangle(double x, double y) {
        if (rectangle != null) {

            rectangle.setWidth(rectangleWidth);
            rectangle.setHeight(rectangleHeight);
            rectangle.setX(x- rectangleWidth / 2);
            rectangle.setY(y- rectangleWidth);



            xMin = rectangle.getX();
            xMax = rectangle.getX() + rectangle.getWidth();
            yMin = rectangle.getY();
            yMax = rectangle.getY() + rectangle.getHeight();

            initializeLines(xMin, xMax, yMin, yMax);

            // Print the boundaries after initializing the rectangle


            createOverlay();


        } else {
            System.err.println("Error: Stage or Rectangle is null. Check initialization.");
        }
    }

    public int getXMin() {
        return (int) xMin;
    }

    public int getXMax() {
        return (int) xMax;
    }

    public int getYMin() {
        return (int) yMin;
    }

    public int getYMax() {
        return (int) yMax;
    }


    private void initializeLines(double xMin, double xMax, double yMin, double yMax) {
        Line top = new Line(0, 1, -yMin);
        Line bottom = new Line(0, 1, -yMax);
        Line left = new Line(1, 0, -xMin);
        Line right = new Line(1, 0, -xMax);

        Point topRight = new Point(top, right);
        Point topLeft = new Point(top, left);
        Point bottomLeft = new Point(bottom, left);
        Point bottomRight = new Point(bottom, right);

        roomCorners = List.of(topLeft, bottomLeft, bottomRight, topRight);
        roomWalls = List.of(top, bottom, left, right);
    }

    public void setSoundWaveVisualiser(double width, double height){
        SoundWaveVisualiser soundWaveVisualiser = new SoundWaveVisualiser((int)width, (int) height, this);
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

    private void createOverlay() {
        topRectangle = new Rectangle(0, 0, stage.getWidth(), yMin);
        bottomRectangle = new Rectangle(0, yMax, stage.getWidth(), stage.getHeight() - yMax);
        leftRectangle = new Rectangle(0, 0, xMin, stage.getHeight());
        rightRectangle = new Rectangle(xMax, 0, stage.getWidth() - xMax, stage.getHeight());

        topRectangle.setFill(Color.WHITE);
        bottomRectangle.setFill(Color.WHITE);
        leftRectangle.setFill(Color.WHITE);
        rightRectangle.setFill(Color.WHITE);

        centerPane.getChildren().addAll(topRectangle, bottomRectangle, leftRectangle, rightRectangle);
    }

    public void overlayRectangles() {
        topRectangle.toFront();
        bottomRectangle.toFront();
        leftRectangle.toFront();
        rightRectangle.toFront();
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

    //Stop tlacitko
    @FXML
    public void handleButtonStopClick() throws IOException {
        // Stop the main timer
        timer.stop();

        // Stop the wave timeline
        if (waveTimeline != null) {
            waveTimeline.pause(); // Pause wave updates
            waveManager.pauseWaves();
        }

        // Update the button states
        buttonStop.setDisable(true);
        buttonResume.setDisable(false);
        buttonReset.setDisable(false);
        isRunning = false;

    }

    //Resume tlacitko
    @FXML
    public void handleButtonResumeClick() {
        // Resume the main timer
        timer.play();

        // Resume the wave updates
        if (waveTimeline != null) {
            waveTimeline.play(); // Resume wave expansion and updates
            waveManager.resumeWaves();
        }

        // Update the button states
        buttonStop.setDisable(false);
        buttonResume.setDisable(true);
        buttonReset.setDisable(false);
        isRunning = true;

    }

    //Restart tlacitko
    @FXML
    public void handleButtonResetClick() {
        timer.stop();  // Stop the timer, if running
        millisecondsElapsed = 0;  // Reset time to 0
        updateTimerLabel();  // Update the timer label to show 0
        buttonStop.setDisable(true);  // Disable the stop button
        buttonResume.setDisable(true);  // Disable the resume button, as there's nothing to resume
        buttonReset.setDisable(true);
        isRunning = false;

        // Stop the wave timeline and clear the waves
        if (waveTimeline != null) {
            waveTimeline.stop(); // Stop the periodic updates
            waveTimeline = null; // Reset the timeline instance
        }

        // Reset the WaveManager
        waveManager.resetWaves(); // Clear all active waves

        // Remove all children from centerPane except the original rectangles
        centerPane.getChildren().removeIf(node ->
                node != rectangle &&
                        node != topRectangle &&
                        node != bottomRectangle &&
                        node != leftRectangle &&
                        node != rightRectangle
        );
    }

    @FXML
    public void handlePaneClick(MouseEvent event) {
// Get the click coordinates
        double x = event.getX();
        double y = event.getY();

        // Check if the click was inside the rectangle using the built-in contains() method
        if (rectangle.contains(x, y)) {
            // Save the coordinates if the click was on the rectangle
            waveManager.createWave(x,y,this, 0, 0, 100, 1);


            if (!isRunning && !buttonReset.isDisabled()){
                buttonStop.setDisable(true);
                buttonResume.setDisable(false);
                timer.stop();
            } else{
                buttonResume.setDisable(true);
                buttonStop.setDisable(false);
                timer.play();
                isRunning = true;
            }

            buttonReset.setDisable(false);
        } else {
            System.out.println("Click was not on the rectangle.");
        }

        if (waveTimeline == null) {
            // Set up a Timeline for periodic updates
            waveTimeline = new Timeline(new KeyFrame(Duration.millis(16), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    waveManager.updateWaves(Room1Controller.this);
                }
            }));

            waveTimeline.setCycleCount(Timeline.INDEFINITE); // Run forever
            waveTimeline.play(); // Start the timeline
        }


    }
}