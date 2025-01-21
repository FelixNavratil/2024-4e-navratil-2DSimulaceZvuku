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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

public class Room0Controller {

    //inicializace promennych
    Room0 mistnost0Class = new Room0();
    private int mistnost0SceneHeight = mistnost0Class.getMistnost0ScreenHeight();
    private int mistnost0SceneWidth = mistnost0Class.getMistnost0ScreenWidth();
    private double rectangleHeight = (double)mistnost0SceneHeight/4;
    private double rectangleSize = (double) mistnost0SceneWidth/4;
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

//--------------------------------------------------------------------------------------------------------------------------------------------------------

    //getters
    public int getMistnost0SceneHeight(){
        return mistnost0SceneHeight;
    }
    public int getMistnost0SceneWidth(){
        return mistnost0SceneWidth;
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
    private WaveManager waveManager = new WaveManager(waveFactory, centerPane);
    private Timeline waveTimeline;

    public void setSceneDimensions(Scene scene, int height, int width) {

        stage.setScene(scene);
        stage.setHeight(height);
        stage.setWidth(width);
        updateLayout();
    }

    public void setStage(Stage stage) {
        System.out.println("set stage");
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
            System.out.println("Stage size set to: " + width + " x " + height);
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
        waveFactory = new WaveFactory();
        if (centerPane == null) {
            System.err.println("Error: centerPane is null. Check your FXML file.");
        }
        waveManager = new WaveManager(waveFactory, centerPane);

        createTimeline();
        buttonResume.setDisable(true);
        buttonStop.setDisable(true);
        buttonReset.setDisable(true);
        centerPane.setPrefHeight(mistnost0SceneHeight);
        centerPane.setPrefWidth(mistnost0SceneWidth);

        
        System.out.println("-----------inicializace skoncila-------------");
    }

    public void initializeRectangle(double x, double y) {
        if (rectangle != null) {
            rectangle.setWidth(rectangleSize);
            rectangle.setHeight(rectangleSize);
            rectangle.setX(x- rectangleSize / 2);
            rectangle.setY(y- rectangleSize);
            
            xMin = rectangle.getX();
            xMax = rectangle.getX() + rectangle.getWidth();
            yMin = rectangle.getY();
            yMax = rectangle.getY() + rectangle.getHeight();
            
            initializeLines(xMin, xMax, yMin, yMax);

            // Print the boundaries after initializing the rectangle
            System.out.println("xMin: " + xMin);
            System.out.println("xMax: " + xMax);
            System.out.println("yMin: " + yMin);
            System.out.println("yMax: " + yMax);

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

    //nastaveni velikosti jednotlivych tlacitek
    public void updateLayout() {
        System.out.println("update layout");
        if (root != null){
            //Velikost buttonHlavniMenu

            buttonHlavniMenu.setPrefWidth(mistnost0SceneWidth /8);
            buttonHlavniMenu.setPrefHeight(mistnost0SceneHeight/8);

            //velikost buttonStop a start
            labelInstrukce.setPrefSize(mistnost0SceneWidth /8, mistnost0SceneHeight/8);
            buttonStop.setPrefSize(mistnost0SceneWidth /8, mistnost0SceneHeight/8);
            buttonResume.setPrefSize(mistnost0SceneWidth /8, mistnost0SceneHeight/8);
            buttonReset.setPrefSize(mistnost0SceneWidth /8, mistnost0SceneHeight/8);

            //velikost bottom textu
            bottomText.setPrefSize(mistnost0SceneWidth /8, mistnost0SceneHeight/8);
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

        /*
        System.out.println("Top Left Corner: " + topLeft.toString());
        System.out.println("Top Right Corner: " + topRight.toString());
        System.out.println("Bottom Left Corner: " + bottomLeft.toString());
        System.out.println("Bottom Right Corner: " + bottomRight.toString());
        System.out.println("Top Line: " + top.toString());
        System.out.println("Bottom Line: " + bottom.toString());
        System.out.println("Left Line: " + left.toString());
        System.out.println("Right Line: " + right.toString());
         */
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
        }

        // Update the button states
        buttonStop.setDisable(true);
        buttonResume.setDisable(false);
        buttonReset.setDisable(false);

        System.out.println("Wave and timer stopped.");
    }

    //Resume tlacitko
    @FXML
    public void handleButtonResumeClick() {
        // Resume the main timer
        timer.play();

        // Resume the wave updates
        if (waveTimeline != null) {
            waveTimeline.play(); // Resume wave expansion and updates
        }

        // Update the button states
        buttonStop.setDisable(false);
        buttonResume.setDisable(true);
        buttonReset.setDisable(false);

        System.out.println("Wave and timer resumed.");
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

        // Stop the wave timeline and clear the waves
        if (waveTimeline != null) {
            waveTimeline.stop(); // Stop the periodic updates
            waveTimeline = null; // Reset the timeline instance
        }

        // Reset the WaveManager
        waveManager.resetWaves(); // Clear all active waves
        centerPane.getChildren().removeIf(node -> node != rectangle); // Remove waves from UI except for the rectangle
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
            waveManager.createWave(x,y,this);
            timer.play();
            buttonStop.setDisable(false);
            buttonResume.setDisable(true);
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
}
