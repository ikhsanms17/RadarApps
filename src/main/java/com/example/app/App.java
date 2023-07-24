package com.example.app;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;

import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {

    private static final int WIDTH = 1000;   // Width of the radar display
    private static final int HEIGHT = 700;  // Height of the radar display
    private MapView mapView;
    private Slider slider;
    private GraphicsOverlay graphicsOverlay;
    private Arc scanningArea;
    private double rotationSpeed = 1.0; // Nilai default
    double centerX = WIDTH / 2; // Koordinat x pusat
    double centerY = HEIGHT / 2; // Koordinat y pusat
    double radius = 275; // Radius lingkaran
    double radius1 = 220; // Radius lingkaran 1
    double radius2 = 150; // Radius lingkaran 2
    double radius3 = 80; // Radius lingkaran 3
    double radius4 = 290; // Radius lingkaran 3
    int CENTER_X = (WIDTH - 15) / 2;
    int CENTER_Y = (HEIGHT + 6) / 2;
    int numLines = 72; // Jumlah garis vertikal 1
    int numLines1 = 24; // Jumlah garis vertikal 2
    int numLines2 = 360; // Jumlah garis vertikal kecil
    int numLines3 = 8; // garis +
    int MIN_NUMBER = 0;
    int MAX_NUMBER = 330;
    int RADIUS = 315;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Note: it is not best practice to store API keys in source code.
        // The API key is referenced here for the convenience of this tutorial.
        String yourApiKey = "AAPK262c106448034a9e982fbc8b2d2cf950oXHKnb3EJwvrPGU-uBFNwy-p76PSsnT6BI0UYGyakqd5tHEa3sCXUhVv5q3g_qGm";
        ArcGISRuntimeEnvironment.setApiKey(yourApiKey);

        // create a JavaFX scene with a stack pane as the root node, and add it to the scene
        StackPane root = new StackPane();
        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.TRANSPARENT);

        // create a map view to display the map and add it to the stack pane
        mapView = new MapView();
        root.getChildren().add(mapView);
        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_DARK_GRAY);

        // create node group for radar display
        Group group = new Group();

        // add node group into node root
        root.getChildren().add(group);

        // set the map on the map view
        mapView.setMap(map);
        mapView.setViewpoint(new Viewpoint(
                -6.8743094530729225, 107.58553101717864, 1440));

        // set title, scene and show for scene
        primaryStage.setTitle("Prototype Radar App");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Create a graphics overlay to display the location A and B
        graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);

        // Add location A and B as graphics
        addLocationGraphic(-6.874616655729794, 107.5851627232247);
        addLocationGraphic(-6.8740397250218255, 107.58630542664204);

        // create radio buttons for choosing camera controller
        RadioButton radio4 = new RadioButton("RADIO 4KM");
        radio4.setTextFill(Color.WHITE);
        RadioButton radio2 = new RadioButton("RADIO 2KM");
        radio2.setTextFill(Color.WHITE);
        RadioButton radio = new RadioButton("RADIO 1KM");
        radio.setTextFill(Color.WHITE);
        radio.setSelected(true);
        slider = new Slider(0.1, 2.0, rotationSpeed); // Slider dengan nilai minimal 0.1 dan maksimal 2.0

        // Event listener untuk mengatur nilai kecepatan putaran objek
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            rotationSpeed = newValue.doubleValue();
        });

        // set the buttons to a toggle group
        ToggleGroup toggleGroup = new ToggleGroup();
        radio4.setToggleGroup(toggleGroup);
        radio2.setToggleGroup(toggleGroup);
        radio.setToggleGroup(toggleGroup);

        // set the radio buttons to choose which camera controller is active
//            radio4.setOnAction(event -> mapView.setCameraController());
//
//            radio2.setOnAction(event -> mapView.setCameraController());
//
//            radio.setOnAction(event -> mapView.setCameraController());

        // create a control panel
        VBox controlsVBox = new VBox(10);
        controlsVBox.setBackground(new Background(new BackgroundFill(Paint.valueOf("rgba(0, 0, 0, 0.3)"), CornerRadii.EMPTY, Insets.EMPTY)));
        controlsVBox.setPadding(new Insets(10.0));
        controlsVBox.setMaxSize(150, 80);
        controlsVBox.getStyleClass().add("panel-region");

        // add radio buttons to the control panel
        controlsVBox.getChildren().addAll(radio4, radio2, radio, slider);

        // add scene view, label and control panel to the stack pane
        root.getChildren().add(controlsVBox);
        root.setAlignment(controlsVBox, Pos.TOP_LEFT);
        root.setMargin(controlsVBox, new Insets(60, 0, 0, 20));

        // Create radar circle
        Circle radarCircle = new Circle(WIDTH / 2, HEIGHT / 2, 290);
        radarCircle.setStroke(Color.GREEN);
        radarCircle.setStrokeWidth(2);
        radarCircle.setFill(null);
        group.getChildren().add(radarCircle);

        Circle radarCircle1 = new Circle(WIDTH / 2, HEIGHT / 2, 220);
        radarCircle1.setStroke(Color.GREEN);
        radarCircle1.setStrokeWidth(2);
        radarCircle1.setFill(null);
        group.getChildren().add(radarCircle1);

        Circle radarCircle2 = new Circle(WIDTH / 2, HEIGHT / 2, 150);
        radarCircle2.setStroke(Color.GREEN);
        radarCircle2.setStrokeWidth(2);
        radarCircle2.setFill(null);
        group.getChildren().add(radarCircle2);

        Circle radarCircle3 = new Circle(WIDTH / 2, HEIGHT / 2, 80);
        radarCircle3.setStroke(Color.GREEN);
        radarCircle3.setStrokeWidth(2);
        radarCircle3.setFill(null);
        group.getChildren().add(radarCircle3);

        // create a vertical line
        for (int i = 0; i < numLines; i++) {
            double angle = 360.0 / numLines * i;
            double startX = centerX + Math.cos(Math.toRadians(angle)) * (radius);
            double startY = centerY + Math.sin(Math.toRadians(angle)) * (radius);
            double endX = centerX + Math.cos(Math.toRadians(angle)) * (radius + 25);
            double endY = centerY + Math.sin(Math.toRadians(angle)) * (radius + 25);

            Line line = new Line(startX, startY, endX, endY);
            line.getTransforms().add(new Rotate(angle, centerX, centerY)); // Transform rounded
            line.setStroke(Color.GREEN);
            group.getChildren().add(line);
        }

        // create a vertical line
        for (int i = 0; i < numLines; i++) {
            double angle = 360.0 / numLines1 * i;
            double sX1 = centerX + Math.cos(Math.toRadians(angle)) * radius1;
            double sY1 = centerY + Math.sin(Math.toRadians(angle)) * radius1;
            double eX1 = centerX + Math.cos(Math.toRadians(angle)) * (radius1 + 10);
            double eY1 = centerY + Math.sin(Math.toRadians(angle)) * (radius1 + 10);

            Line line = new Line(sX1, sY1, eX1, eY1);
            line.getTransforms().add(new Rotate(angle, centerX, centerY)); // Transform rounded
            line.setStroke(Color.GREEN);
            group.getChildren().add(line);
        }

        for (int i = 0; i < numLines; i++) {
            double angle = 360.0 / numLines1 * i;
            double sX2 = centerX + Math.cos(Math.toRadians(angle)) * radius2;
            double sY2 = centerY + Math.sin(Math.toRadians(angle)) * radius2;
            double eX2 = centerX + Math.cos(Math.toRadians(angle)) * (radius2 + 10);
            double eY2 = centerY + Math.sin(Math.toRadians(angle)) * (radius2 + 10);

            Line line = new Line(sX2, sY2, eX2, eY2);
            line.getTransforms().add(new Rotate(angle, centerX, centerY)); // Transform rounded
            line.setStroke(Color.GREEN);
            group.getChildren().add(line);
        }

        for (int i = 0; i < numLines; i++) {
            double angle = 360.0 / numLines1 * i;
            double sX3 = centerX + Math.cos(Math.toRadians(angle)) * radius3;
            double sY3 = centerY + Math.sin(Math.toRadians(angle)) * radius3;
            double eX3 = centerX + Math.cos(Math.toRadians(angle)) * (radius3 + 10);
            double eY3 = centerY + Math.sin(Math.toRadians(angle)) * (radius3 + 10);

            Line line = new Line(sX3, sY3, eX3, eY3);
            line.getTransforms().add(new Rotate(angle, centerX, centerY)); // Transform rounded
            line.setStroke(Color.GREEN);
            group.getChildren().add(line);
        }

        for (int i = 0; i < numLines; i++) {
            double angle = 360.0 / numLines3 * i;
            double sX4 = centerX + Math.cos(Math.toRadians(angle)) * 0;
            double sY4 = centerY + Math.sin(Math.toRadians(angle)) * 0;
            double eX4 = centerX + Math.cos(Math.toRadians(angle)) * (0 + 10);
            double eY4 = centerY + Math.sin(Math.toRadians(angle)) * (0 + 10);

            Line line = new Line(sX4, sY4, eX4, eY4);
            line.getTransforms().add(new Rotate(angle, centerX, centerY)); // Transformasi rounded
            line.setStroke(Color.GREEN);
            group.getChildren().add(line);
        }

        for (int i = 0; i < numLines2; i++) {
            double angle = 360.0 / numLines2 * i;
            double sX = centerX + Math.cos(Math.toRadians(angle)) * radius4;
            double sY = centerY + Math.sin(Math.toRadians(angle)) * radius4;
            double eX = centerX + Math.cos(Math.toRadians(angle)) * (radius4 - 10);
            double eY = centerY + Math.sin(Math.toRadians(angle)) * (radius4 - 10);

            Line line2 = new Line(sX, sY, eX, eY);
            line2.getTransforms().add(new Rotate(angle, centerX, centerY)); // Transformasi rounded
            line2.setStroke(Color.GREEN);
            group.getChildren().add(line2);
        }

        // shape scanning area
        scanningArea = new Arc();
        scanningArea.setCenterX(WIDTH / 2);
        scanningArea.setCenterY(HEIGHT / 2);
        scanningArea.setRadiusX(290);
        scanningArea.setRadiusY(290);
        scanningArea.setStartAngle(90 + (30 / 2));
        scanningArea.setLength(30);
        scanningArea.setType(ArcType.ROUND);
        scanningArea.setFill(Color.GREEN);
        scanningArea.setOpacity(0.5);
        scanningArea.setStrokeWidth(2);

        // Add the scanning area to the root node
        group.getChildren().add(scanningArea);

        // Create a Timeline for rotating the scanning area
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.01), event -> {
                    scanningArea.setStartAngle(scanningArea.getStartAngle() - rotationSpeed);
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // numbering scala
        for (int number = MIN_NUMBER; number <= MAX_NUMBER; number += 30) {
            double angle = Math.toRadians(number - 90);  // Convert angle to radians
            double x = CENTER_X + RADIUS * Math.cos(angle);
            double y = CENTER_Y + RADIUS * Math.sin(angle);

            Text text = new Text(String.valueOf(number));
            text.setFont(Font.font("Arial", 10));
            text.setStroke(Color.GREEN);
            text.setTextAlignment(TextAlignment.RIGHT);
            text.setX(x);
            text.setY(y);
            group.getChildren().add(text);
        }
    }

    // Metode untuk mengatur kecepatan putaran objek dalam ArcGIS Scene
    private void setRotationSpeed(double speed) {
        rotationSpeed = speed;
    }

    // Metode untuk membuat grafik overlay dan menambahkan objek poin ke dalamnya
    private void addLocationGraphic(double latitude, double longitude) {
        // Create a point graphic at the specified location
        Point point = new Point(longitude, latitude, SpatialReferences.getWgs84());
        SimpleMarkerSymbol pointSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, 0xFF0000FF, 10);
        Graphic locationGraphic = new Graphic(point, pointSymbol);

        // Add the graphic to the graphics overlay
        graphicsOverlay.getGraphics().add(locationGraphic);
        graphicsOverlay.getOpacity();


    }

    @Override
    public void stop() {
        if (mapView != null) {
            mapView.dispose();
        }
    }
}

