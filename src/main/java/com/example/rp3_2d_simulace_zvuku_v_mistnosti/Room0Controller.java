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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class Room0Controller implements BaseRoomControllerInterface {

    //inicializace promennych
    Room0 room0 = new Room0();
    private int room0Height = room0.getMistnost0ScreenHeight();
    private int room0Width = room0.getMistnost0ScreenWidth();
    private double rectangleHeight = (double) room0Height /4;
    private double rectangleSize = (double) room0Width /4;
    private static boolean vlnaExistuje = false;
    private boolean isRunning = true;
    private double currentRadius = 0;      // To track the current radius for resuming
    private double x;
    private double y;
    private  final double maxRadius = Math.sqrt(rectangleHeight*rectangleHeight + rectangleSize * rectangleSize);
    private final int roomID = 0;
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;
    private List<Line> roomWalls;
    private List<Point> roomCorners;
    Rectangle topRectangle;
    Rectangle bottomRectangle;
    Rectangle leftRectangle;
    Rectangle rightRectangle;
    private int strokeWidth = 4;

//--------------------------------------------------------------------------------------------------------------------------------------------------------

    //getters
    public int getRoom0Height(){
        return room0Height;
    }
    public int getRoom0Width(){
        return room0Width;
    }
    public Pane getCenterPane(){
        return centerPane;
    }
    public boolean getVlnaExistuje() {
        return vlnaExistuje;
    }
    public boolean getIsRunning(){
        return isRunning;
    }
    public double getCurrentRadius(){
        return currentRadius;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public int getRoomID() {
        return roomID;
    }

//--------------------------------------------------------------------------------------------------------------------------------------------------------
    //inicializace promennych

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
    private HBox mistnost0HBox;
    @FXML
    private HBox buttonHlavniMenuHBox;
    @FXML
    private Label bottomText;
    @FXML
    private Rectangle rectangle;

    private Stage stage;
//--------------------------------------------------------------------------------------------------------------------------------------------------------

    // vytvoreni promennych na cas
    private Timeline timer;
    private int millisecondsElapsed  = 0;  // Track elapsed seconds
    //--------------------------------------------------------------------------------------------------------------------------------------------------------
    // inicializace waveManazera a waveFactory
    private WaveFactory waveFactory = new WaveFactory();
    PixelManager pixelManager;
    private WaveManager waveManager = new WaveManager(waveFactory, centerPane, pixelManager);
    private Timeline waveTimeline;


    public void setSceneDimensions(Scene scene, int height, int width) {

        stage.setScene(scene);
        stage.setHeight(height);
        stage.setWidth(width);
        updateLayout();
    }

    public void setStage(Stage stage) {

        this.stage = stage;

        // Check if fullscreen is enabled
        boolean isFullScreen = stage.isFullScreen();

        if (!isFullScreen) {
            // If not fullscreen, set the stage dimensions to match the screen size
            double width = Screen.getPrimary().getBounds().getWidth();
            double height = Screen.getPrimary().getBounds().getHeight();
            stage.setWidth(width);
            stage.setHeight(height);
            stage.setMaximized(true);
            stage.setResizable(true);
            initializeRectangle(stage.getWidth()/2, stage.getHeight()/2);

            // Debugging output

        } else {
            // If fullscreen, let JavaFX handle the size
            System.out.println("Fullscreen mode is enabled. Dimensions managed by JavaFX.");
        }
    }





    /** inicializace timeru, tlacitek a obdelnika */
    @FXML
    public void initialize() {
        System.out.println("-----------inicializace zacala-------------");
        updateLayout();
        // Initialize the PixelManager with this Room0Controller
        pixelManager = new PixelManager(this);
        waveFactory = new WaveFactory();
        if (centerPane == null) {
            System.err.println("Error: centerPane is null. Check your FXML file.");
        }
        waveManager = new WaveManager(waveFactory, centerPane, pixelManager);

        createTimeline();
        buttonResume.setDisable(true);
        buttonStop.setDisable(true);
        buttonReset.setDisable(true);
        centerPane.setPrefHeight(room0Height);
        centerPane.setPrefWidth(room0Width);



        System.out.println("-----------inicializace skoncila-------------");
    }

    public int getStroke(){
        return (int)rectangle.getStrokeWidth();
    }

    private void initializeRectangle(double x, double y) {
        if (rectangle != null) {
            rectangle.setWidth(rectangleSize);
            rectangle.setHeight(rectangleSize);
            rectangle.setX(x- rectangleSize / 2);
            rectangle.setY(y- rectangleSize);
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(strokeWidth);


            xMin = rectangle.getX();
            xMax = rectangle.getX() + rectangle.getWidth();
            yMin = rectangle.getY();
            yMax = rectangle.getY() + rectangle.getHeight();

            initializeLines(xMin, xMax, yMin, yMax);

            // Initialize the Pixel Grid with the rectangle's dimensions and position
            pixelManager.initializePixelGrid((int) rectangle.getWidth(), (int) rectangle.getHeight(), rectangle.getX(), rectangle.getY());

            // Add all pixels to the pane
            pixelManager.addRectanglesToPane( centerPane);
            System.out.println("pixels added");

            System.out.println("Room Width: " + rectangle.getWidth());
            System.out.println("Room Height: " + rectangle.getHeight());

            createOverlay();


        } else {
            System.err.println("Error: Stage or Rectangle is null. Check initialization.");
        }
    }

    public double getRectangleWidth() {
        return rectangle != null ? rectangle.getWidth() : 0;
    }

    public double getRectangleHeight() {
        return rectangle != null ? rectangle.getHeight() : 0;
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

//--------------------------------------------------------------------------------------------------------------------------------------------------------

    //zapnuti timeru
    private void createTimeline() {
        timer = new Timeline(new KeyFrame(Duration.millis(1), event -> {
            millisecondsElapsed++;
            updateTimerLabel();
        }));
        timer.setCycleCount(Timeline.INDEFINITE);  // Run indefinitely
    }

//--------------------------------------------------------------------------------------------------------------------------------------------------------

    private void createOverlay(){
        topRectangle = new Rectangle(0,0,stage.getWidth(),yMin);
        bottomRectangle = new Rectangle(0,yMax,stage.getWidth(),stage.getHeight()-yMax);
        leftRectangle = new Rectangle(0,0,xMin,stage.getHeight());
        rightRectangle = new Rectangle(xMax,0,stage.getWidth()-xMax,stage.getHeight());


        topRectangle.setFill(javafx.scene.paint.Color.WHITE);
        bottomRectangle.setFill(javafx.scene.paint.Color.WHITE);
        leftRectangle.setFill(javafx.scene.paint.Color.WHITE);
        rightRectangle.setFill(javafx.scene.paint.Color.WHITE);

        centerPane.getChildren().addAll(topRectangle,bottomRectangle,leftRectangle,rightRectangle);
    }

    public void overlayRectangles(){
        topRectangle.toFront();
        bottomRectangle.toFront();
        leftRectangle.toFront();
        rightRectangle.toFront();
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------

    //nastaveni velikosti jednotlivych tlacitek
    public void updateLayout() {
        System.out.println("update layout");
        if (root != null){
            //Velikost buttonHlavniMenu

            buttonHlavniMenu.setPrefWidth(room0Width /8);
            buttonHlavniMenu.setPrefHeight(room0Height /8);

            //velikost buttonStop a start
            labelInstrukce.setPrefSize(room0Width /8, room0Height /8);
            buttonStop.setPrefSize(room0Width /8, room0Height /8);
            buttonResume.setPrefSize(room0Width /8, room0Height /8);
            buttonReset.setPrefSize(room0Width /8, room0Height /8);

            //velikost bottom textu
            bottomText.setPrefSize(room0Width /8, room0Height /8);
            bottomText.setFont(new Font(20));

        }

    }

    private void initializeLines(double xMin, double xMax, double yMin, double yMax){
        Line top = new Line(0, 1, -yMin);
        Line bottom = new Line(0, 1, -yMax);
        Line left = new Line(1, 0, -xMin);
        Line right = new Line(1, 0, -xMax);

        Point topRight = new Point(top, right);
        Point topLeft = new Point(top, left);
        Point bottomLeft = new Point(bottom, left);
        Point bottomRight = new Point(bottom, right);

        roomCorners = List.of(topLeft, bottomLeft,bottomRight, topRight );
        roomWalls = List.of(top, bottom, left, right);


    }

    public List<Line> getRoomWalls() {
        return roomWalls;
    }

    public List<Point> getRoomCorners() {
        return roomCorners;
    }




    //zmeni se scena na hlavni menu
    @FXML
    public void handleButtonHlavniMenuClick() throws IOException {
        System.out.println("switch hlavni menu");
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

        // Reset celkovaVychylka of each Pixel in the grid
        if (pixelManager != null) {
            pixelManager.resetPixelGridDisplacement();  // Reset all pixel displacements to 0
        }

    }

    private void updateTimerLabel() {
        // Calculate hours, minutes, seconds, and milliseconds from elapsed time
        int hours = millisecondsElapsed / (3600 * 1000);
        int minutes = (millisecondsElapsed % (3600 * 1000)) / (60 * 1000);
        int seconds = (millisecondsElapsed % (60 * 1000)) / 1000;
        int milliseconds = millisecondsElapsed % 1000;

        // Update the label text with milliseconds precision
        bottomText.setText(String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds));
    }


    // reseni kliknuti na obdelnik
    @FXML
    public  void handlePaneClick(MouseEvent event) {
        // Get the click coordinates
        double x = event.getX();
        double y = event.getY();

        // Check if the click was inside the rectangle using the built-in contains() method
        if (rectangle.contains(x, y)) {
            // Save the coordinates if the click was on the rectangle
            waveManager.createWave(x,y,this, 0,0,100,1);


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
                    waveManager.updateWaves(Room0Controller.this);
                }
            }));

            waveTimeline.setCycleCount(Timeline.INDEFINITE); // Run forever
            waveTimeline.play(); // Start the timeline
        }
    }

    public Pixel[][] getPixelGrid(){
        return pixelManager.getPixelGrid();
    }
}
