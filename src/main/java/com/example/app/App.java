package com.example.app;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.*;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

import java.text.DecimalFormat;
import java.util.Random;

public class App extends Application {

    private MapView mapView;
    private Slider slider;
    private Slider slider2;
    private Group group;
    private StackPane root;
    private GraphicsOverlay graphicsOverlay;
    private Arc scanningArea;
    private Circle radarCircle, radarCircle1, radarCircle2, radarCircle3, circle;
    private Text text, text1, text2, text3, text4;
    private Line line, line2;
    private Point point, point2, posisi, nextPoint, pointView;
    private Graphic graphic, graphic2;
    private SimpleMarkerSymbol symbol, symbol2;
    private AnimationTimer animationTimer, animationTimer2;
    private Timeline timeline;
    private Label labelLatLong, label;
    private Random random = new Random();

    private static final int WIDTH = 1000;   // Width of the radar display
    private static final int HEIGHT = 700;  // Height of the radar display
    private double centerX = WIDTH / 2; // Koordinat x pusat
    private double centerY = HEIGHT / 2; // Koordinat y pusat
    private double rotationSpeed = 1.0; // Nilai default
    private double radiusA = 280;
    private double radiusA1 = 210;
    private double radiusA2 = 140;
    private double radiusA3 = 70;
    private double radRange = 10;
    private double radNum = 305;
    private double radNum1 = 35;
    private double radNum2 = 110;
    private double radNum3 = 170;
    private double radNum4 = 230;
    private double radLine = 265;
    private double radLine4 = 280;
    private double radLine1 = 210;
    private double radLine2 = 140;
    private double radLine3 = 70;
    private double radiusRadar = 30;
    private double progress = 0;
    private double scale = 200000;

    private double radScl = scale;

    private double CENTER_X = 492.5; // 1000 - 15 = 985 : 2 = 492.5
    private double CENTER_Y = 353; // 700 + 6 = 706 : 2 = 353

    private int numLines = 72; // Jumlah garis vertikal 1
    private int numLines1 = 24; // Jumlah garis vertikal 2
    private int numLines2 = 360; // Jumlah garis vertikal kecil
    private int numLines3 = 8; // garis +

    private double angle = 360.0 / numLines;
    private double angle1 = 360.0 / numLines1;
    private double angle2 = 360.0 / numLines2;
    private double angle3 = 360.0 / numLines3;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // API ArcGIS for Maps
        String yourApiKey = "AAPK262c106448034a9e982fbc8b2d2cf950oXHKnb3EJwvrPGU-uBFNwy-p76PSsnT6BI0UYGyakqd5tHEa3sCXUhVv5q3g_qGm";
        ArcGISRuntimeEnvironment.setApiKey(yourApiKey);

        // create a JavaFX scene with a stack pane as the root node, and add it to the scene
        root = new StackPane();
        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.TRANSPARENT);

        // create a map view to display the map and add it to the stack pane
        mapView = new MapView();
        root.getChildren().add(mapView);
        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_DARK_GRAY);

        // create node group for radar display
        group = new Group();

        // add node group into node root
        root.getChildren().add(group);

        // set the map on the map view
        mapView.setMap(map);
        mapView.setViewpoint(new Viewpoint(-6.8743094530729225, 107.58553101717864, scale));
        mapView.setOnScroll(null);
        mapView.setOnMousePressed(null);
        mapView.setOnMouseDragged(null);

        // set title, scene and show for scene
        primaryStage.setTitle("Prototype Radar App");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Create a graphics overlay to display the location A and B
        graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);

        // add location A and B
        addLocationGraphic( -6.989816573099767, 107.68786246812824, -6.838321160281938, 107.48785712513322);

        // create radio buttons for choosing camera controller
        RadioButton radio4 = new RadioButton("RADIO A");
        radio4.setTextFill(Color.WHITE);
        RadioButton radio2 = new RadioButton("RADIO B");
        radio2.setTextFill(Color.WHITE);
        RadioButton radio1 = new RadioButton("RADIO C");
        radio1.setTextFill(Color.WHITE);
        radio4.setSelected(true);

        // Create a Label to display latitude and longitude
        label = new Label();
        label.setText("Speed Rotation: ");
        label.setTextFill(Color.WHITE);

        // create slider button for speed
        slider = new Slider(0.1, 2.0, rotationSpeed); // Slider dengan nilai minimal 0.1 dan maksimal 2.0

        // Event listener untuk mengatur nilai kecepatan putaran objek
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            rotationSpeed = newValue.doubleValue();
        });

        // Create a Label to display latitude and longitude
        labelLatLong = new Label();
        labelLatLong.setStyle("-fx-background-color: white; -fx-padding: 5px;");

        // set the buttons to a toggle group
        ToggleGroup toggleGroup = new ToggleGroup();
        radio4.setToggleGroup(toggleGroup);
        radio2.setToggleGroup(toggleGroup);
        radio1.setToggleGroup(toggleGroup);

        // set the radio buttons to choose which radio is active
        radio4.setOnAction(event -> buttonRadioA());
        radio2.setOnAction(event -> buttonRadioB());
        radio1.setOnAction(event -> buttonRadioC());

        // create a control panel
        VBox controlsVBox = new VBox(10);
        controlsVBox.setBackground(new Background(new BackgroundFill(Paint.valueOf("rgba(0, 0, 0, 0.3)"), CornerRadii.EMPTY, Insets.EMPTY)));
        controlsVBox.setPadding(new Insets(10.0));
        controlsVBox.setMaxSize(150, 80);
        controlsVBox.getStyleClass().add("panel-region");

        // create a control panel
        VBox controlsVBox2 = new VBox(0);
        controlsVBox2.setBackground(new Background(new BackgroundFill(Paint.valueOf("rgba(0, 0, 0, 0.3)"), CornerRadii.EMPTY, Insets.EMPTY)));
        controlsVBox2.setPadding(new Insets(5,5,5,5));
        controlsVBox2.setMaxSize(500, 5);
        controlsVBox2.getStyleClass().add("panel-region");

        // add radio buttons to the control panel
        controlsVBox.getChildren().addAll(radio4, radio2, radio1, label, slider);

        // add radio buttons to the control panel
        controlsVBox2.getChildren().add(labelLatLong);

        // add scene view, label and control panel to the stack pane
        root.getChildren().add(controlsVBox);
        root.setAlignment(controlsVBox, Pos.TOP_LEFT);
        root.setMargin(controlsVBox, new Insets(60, 0, 0, 20));

        root.getChildren().add(controlsVBox2);
        root.setAlignment(controlsVBox2, Pos.BOTTOM_RIGHT);
        root.setMargin(controlsVBox2, new Insets(2, 2, 20, 2));

        // Create radar circle
        radarCircle = new Circle(centerX, centerY, radiusA);
        radarCircle.setStroke(Color.WHITE);
        radarCircle.setStrokeWidth(2);
        radarCircle.setFill(null);
        group.getChildren().add(radarCircle);

        radarCircle1 = new Circle(centerX, centerY, radiusA1);
        radarCircle1.setStroke(Color.WHITE);
        radarCircle1.setStrokeWidth(2);
        radarCircle1.setFill(null);
        group.getChildren().add(radarCircle1);

        radarCircle2 = new Circle(centerX, centerY, radiusA2);
        radarCircle2.setStroke(Color.WHITE);
        radarCircle2.setStrokeWidth(2);
        radarCircle2.setFill(null);
        group.getChildren().add(radarCircle2);

        radarCircle3 = new Circle(centerX, centerY, radiusA3);
        radarCircle3.setStroke(Color.WHITE);
        radarCircle3.setStrokeWidth(2);
        radarCircle3.setFill(null);
        group.getChildren().add(radarCircle3);

        // create a vertical line
        for (int i = 0; i < numLines; i++) {
            line = new Line();
            line.setStartX(centerX + Math.cos(Math.toRadians(angle * i)) * radLine);
            line.setStartY(centerY + Math.sin(Math.toRadians(angle * i)) * radLine);
            line.setEndX(centerX + Math.cos(Math.toRadians(angle * i)) * (radLine + 25));
            line.setEndY(centerY + Math.sin(Math.toRadians(angle * i)) * (radLine + 25));
            line.getTransforms().add(new Rotate(angle * i, centerX, centerY)); // Transform rounded
            line.setStroke(Color.WHITE);
            group.getChildren().add(line);
        }

        // create a vertical line
        for (int i = 0; i < numLines; i++) {
            line = new Line();
            line.setStartX(centerX + Math.cos(Math.toRadians(angle1 * i)) * radLine1);
            line.setStartY(centerY + Math.sin(Math.toRadians(angle1 * i)) * radLine1);
            line.setEndX(centerX + Math.cos(Math.toRadians(angle1 * i)) * (radLine1 + 10));
            line.setEndY(centerY + Math.sin(Math.toRadians(angle1 * i)) * (radLine1 + 10));
            line.getTransforms().add(new Rotate(angle1 * i, centerX, centerY)); // Transform rounded
            line.setStroke(Color.WHITE);
            group.getChildren().add(line);
        }

        for (int i = 0; i < numLines; i++) {
            line = new Line();
            line.setStartX(centerX + Math.cos(Math.toRadians(angle1 * i)) * radLine2);
            line.setStartY(centerY + Math.sin(Math.toRadians(angle1 * i)) * radLine2);
            line.setEndX(centerX + Math.cos(Math.toRadians(angle1 * i)) * (radLine2 + 10));
            line.setEndY(centerY + Math.sin(Math.toRadians(angle1 * i)) * (radLine2 + 10));
            line.getTransforms().add(new Rotate(angle1 * i, centerX, centerY)); // Transform rounded
            line.setStroke(Color.WHITE);
            group.getChildren().add(line);
        }

        for (int i = 0; i < numLines; i++) {
            line = new Line();
            line.setStartX(centerX + Math.cos(Math.toRadians(angle1 * i)) * radLine3);
            line.setStartY(centerY + Math.sin(Math.toRadians(angle1 * i)) * radLine3);
            line.setEndX(centerX + Math.cos(Math.toRadians(angle1 * i)) * (radLine3 + 10));
            line.setEndY(centerY + Math.sin(Math.toRadians(angle1 * i)) * (radLine3 + 10));
            line.getTransforms().add(new Rotate(angle1 * i, centerX, centerY)); // Transform rounded
            line.setStroke(Color.WHITE);
            group.getChildren().add(line);
        }

        for (int i = 0; i < numLines; i++) {
            line = new Line();
            line.setStartX(centerX + Math.cos(Math.toRadians(angle3 * i)) * 0);
            line.setStartY(centerY + Math.sin(Math.toRadians(angle3 * i)) * 0);
            line.setEndX(centerX + Math.cos(Math.toRadians(angle3 * i)) * (0 + 7));
            line.setEndY(centerY + Math.sin(Math.toRadians(angle3 * i)) * (0 + 7));
            line.getTransforms().add(new Rotate(angle3 * i, centerX, centerY)); // Transformasi rounded
            line.setStroke(Color.WHITE);
            group.getChildren().add(line);
        }

        for (int i = 0; i < numLines2; i++) {
            line2 = new Line();
            line2.setStartX(centerX + Math.cos(Math.toRadians(angle2 * i)) * radLine4);
            line2.setStartY(centerY + Math.sin(Math.toRadians(angle2 * i)) * radLine4);
            line2.setEndX(centerX + Math.cos(Math.toRadians(angle2 * i)) * (radLine4 - 10));
            line2.setEndY(centerY + Math.sin(Math.toRadians(angle2 * i)) * (radLine4 - 10));
            line2.getTransforms().add(new Rotate(angle2 * i, centerX, centerY)); // Transformasi rounded
            line2.setStroke(Color.WHITE);
            group.getChildren().add(line2);
        }

        // shape scanning area
        scanningArea = new Arc();
        scanningArea.setCenterX(centerX);
        scanningArea.setCenterY(centerY);
        scanningArea.setRadiusX(radiusA);
        scanningArea.setRadiusY(radiusA);
        scanningArea.setStartAngle(90 + (radiusRadar / 2));
        scanningArea.setLength(radiusRadar);
        scanningArea.setType(ArcType.ROUND);
        scanningArea.setFill(Color.WHITE);
        scanningArea.setOpacity(0.5);
        scanningArea.setStrokeWidth(2);

        // Add the scanning area to the root node
        group.getChildren().add(scanningArea);

        // Create a Timeline for rotating the scanning area
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.01), event -> {
                    scanningArea.setStartAngle(scanningArea.getStartAngle() - rotationSpeed);
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // numbering scala
        for (int num = 0; num <= 330; num += 30) {
            double angle4 = Math.toRadians(num - 90);
            text = new Text(String.valueOf(num));
            text.setFont(Font.font("Arial", 10));
            text.setStroke(Color.WHITE);
            text.setTextAlignment(TextAlignment.RIGHT);
            text.setX(CENTER_X + radNum * Math.cos(angle4));
            text.setY(CENTER_Y + radNum * Math.sin(angle4));
            group.getChildren().add(text);
        }

        // numbering range scala
        text1 = new Text();
        text1.setText(3500 + " m");
        text1.setFont(Font.font("Arial", 8));
        text1.setStroke(Color.WHITE);
        text1.setX(centerX + radNum1);
        text1.setY(centerY + radRange);
        group.getChildren().add(text1);

        // numbering range scala
        text2 = new Text();
        text2.setText(7000 + " m");
        text2.setFont(Font.font("Arial", 8));
        text2.setStroke(Color.WHITE);
        text2.setX(centerX + radNum2);
        text2.setY(centerY + radRange);
        group.getChildren().add(text2);

        // numbering range scala
        text3 = new Text();
        text3.setText(10500 + " m");
        text3.setFont(Font.font("Arial", 8));
        text3.setStroke(Color.WHITE);
        text3.setX(centerX + radNum3);
        text3.setY(centerY + radRange);
        group.getChildren().add(text3);

        // numbering range scala
        text4 = new Text();
        text4.setText(14000 + " m");
        text4.setFont(Font.font("Arial", 8));
        text4.setStroke(Color.WHITE);
        text4.setX(centerX + radNum4);
        text4.setY(centerY + radRange);
        group.getChildren().add(text4);

    }

    // Metode untuk mengatur kecepatan putaran objek dalam ArcGIS Scene
    private void setRotationSpeed(double speed) {
        rotationSpeed = speed;
    }

    // Metode untuk membuat grafik overlay dan menambahkan objek poin ke dalamnya
    private void addLocationGraphic(double x, double y, double x1, double y1) {
        // Create a point graphic at the specified location
        point = new Point(y, x, SpatialReferences.getWgs84());
        point2 = new Point(y1, x1, SpatialReferences.getWgs84());

        // Create a symbol for the moving object (a simple red circle)
        symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE, 10);

        // Create the graphic with the start point and symbol
        graphic = new Graphic(point, symbol);

        // Start animation
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Update progress (value between 0 and 1)
                progress += 0.001; // Change this value to adjust animation speed
                if (progress >= 1) {
                    progress = 0;
                }

                // Calculate current location based on progress
                double currentX = point.getX() + (point2.getX() - point.getX()) * progress;
                double currentY = point.getY() + (point2.getY() - point.getY()) * progress;
                posisi = new Point(currentX, currentY, SpatialReferences.getWgs84());

                // Set the current location for the graphic
                graphic.setGeometry(posisi);

                // logic object scanning
                // ??

                // display lat long
                DecimalFormat df = new DecimalFormat("0.000000");
                labelLatLong.setText("Latitude: " + df.format(currentY) + ", Longitude: " + df.format(currentX)
                        + ", Jarak Objek: " + df/* + jarak */ + " m");
            }
        };

        animationTimer.start();

        // Add the graphic to the graphics overlay
        graphicsOverlay.getGraphics().add(graphic);
        graphicsOverlay.getOpacity();
    }

    private void buttonRadioA() {
        radScl = scale;
        text1.setText(3500 + " m");
        text2.setText(7000 + " m");
        text3.setText(10500 + " m");
        text4.setText(14000 + " m");

        updateScale();
    }

    private void buttonRadioB() {
        radScl = scale - 1000;
        text1.setText(2500 + " m");
        text2.setText(5000 + " m");
        text3.setText(7500 + " m");
        text4.setText(10000 + " m");

        updateScale();
    }

    private void buttonRadioC() {
        radScl = scale - 2000;
        text1.setText(1500 + " m");
        text2.setText(3000 + " m");
        text3.setText(4500 + " m");
        text4.setText(5000 + " m");

        updateScale();
    }

    private void updateScale() {

        mapView.setViewpoint(new Viewpoint(-6.8743094530729225, 107.58553101717864, radScl));

    }

    @Override
    public void stop() {
        if (mapView != null) {
            mapView.dispose();
        }
    }
}