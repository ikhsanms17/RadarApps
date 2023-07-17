package com.example.app;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {

    private static final int WIDTH = 1000;   // Width of the radar display
    private static final int HEIGHT = 700;  // Height of the radar display

    private MapView mapView;

    double centerX = WIDTH / 2; // Koordinat x pusat
    double centerY = HEIGHT / 2; // Koordinat y pusat
    double radius = 275; // Radius lingkaran
    double radius1 = 220; // Radius lingkaran 1
    double radius2 = 150; // Radius lingkaran 2
    double radius3 = 80; // Radius lingkaran 3
    double radius4 = 290; // Radius lingkaran 3
    int numLines = 72; // Jumlah garis vertikal 1
    int numLines1 = 24; // Jumlah garis vertikal 2
    int numLines2 = 360; // Jumlah garis vertikal kecil
    int numLines3 = 8; // garis +
    int MIN_NUMBER = 0;
    int MAX_NUMBER = 330;
    int RADIUS = 315;
    int CENTER_X = (WIDTH - 15) / 2;
    int CENTER_Y = (HEIGHT + 6) / 2;



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
        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_TOPOGRAPHIC);

        // Create radar circle
        Circle radarCircle = new Circle(WIDTH / 2, HEIGHT / 2, 290);
        radarCircle.setStroke(Color.GREEN);
        radarCircle.setStrokeWidth(2);
        radarCircle.setFill(Color.TRANSPARENT);
        root.getChildren().add(radarCircle);

        Circle radarCircle1 = new Circle(WIDTH / 2, HEIGHT / 2, 220);
        radarCircle1.setStroke(Color.GREEN);
        radarCircle1.setStrokeWidth(2);
        radarCircle1.setFill(null);
        root.getChildren().add(radarCircle1);

        Circle radarCircle2 = new Circle(WIDTH / 2, HEIGHT / 2, 150);
        radarCircle2.setStroke(Color.GREEN);
        radarCircle2.setStrokeWidth(2);
        radarCircle2.setFill(null);
        root.getChildren().add(radarCircle2);

        Circle radarCircle3 = new Circle(WIDTH / 2, HEIGHT / 2, 80);
        radarCircle3.setStroke(Color.GREEN);
        radarCircle3.setStrokeWidth(2);
        radarCircle3.setFill(null);
        root.getChildren().add(radarCircle3);

        // membuat node group
        Group group = new Group();

        // Membuat garis-garis vertikal 1
        for (int i = 0; i < numLines; i++) {
            double angle = 360.0 / numLines * i;
            double startX = centerX + Math.cos(Math.toRadians(angle)) * (radius);
            double startY = centerY + Math.sin(Math.toRadians(angle)) * (radius);
            double endX = centerX + Math.cos(Math.toRadians(angle)) * (radius + 25);
            double endY = centerY + Math.sin(Math.toRadians(angle)) * (radius + 25);

            Line line = new Line(startX, startY, endX, endY);
            line.getTransforms().add(new Rotate(angle, centerX, centerY)); // Transformasi melingkar
            line.setStroke(Color.GREEN);
            group.getChildren().add(line);
        }

        // Membuat garis-garis vertikal 2
        for (int i = 0; i < numLines; i++) {
            double angle = 360.0 / numLines1 * i;
            double sX1 = centerX + Math.cos(Math.toRadians(angle)) * radius1;
            double sY1 = centerY + Math.sin(Math.toRadians(angle)) * radius1;
            double eX1 = centerX + Math.cos(Math.toRadians(angle)) * (radius1 + 10);
            double eY1 = centerY + Math.sin(Math.toRadians(angle)) * (radius1 + 10);

            Line line = new Line(sX1, sY1, eX1, eY1);
            line.getTransforms().add(new Rotate(angle, centerX, centerY)); // Transformasi melingkar
            line.setStroke(Color.GREEN);
            group.getChildren().add(line);
        }

        // Membuat garis-garis vertikal 3
        for (int i = 0; i < numLines; i++) {
            double angle = 360.0 / numLines1 * i;
            double sX2 = centerX + Math.cos(Math.toRadians(angle)) * radius2;
            double sY2 = centerY + Math.sin(Math.toRadians(angle)) * radius2;
            double eX2 = centerX + Math.cos(Math.toRadians(angle)) * (radius2 + 10);
            double eY2 = centerY + Math.sin(Math.toRadians(angle)) * (radius2 + 10);

            Line line = new Line(sX2, sY2, eX2, eY2);
            line.getTransforms().add(new Rotate(angle, centerX, centerY)); // Transformasi melingkar
            line.setStroke(Color.GREEN);
            group.getChildren().add(line);
        }

        // Membuat garis-garis vertikal 4
        for (int i = 0; i < numLines; i++) {
            double angle = 360.0 / numLines1 * i;
            double sX3 = centerX + Math.cos(Math.toRadians(angle)) * radius3;
            double sY3 = centerY + Math.sin(Math.toRadians(angle)) * radius3;
            double eX3 = centerX + Math.cos(Math.toRadians(angle)) * (radius3 + 10);
            double eY3 = centerY + Math.sin(Math.toRadians(angle)) * (radius3 + 10);

            Line line = new Line(sX3, sY3, eX3, eY3);
            line.getTransforms().add(new Rotate(angle, centerX, centerY)); // Transformasi melingkar
            line.setStroke(Color.GREEN);
            group.getChildren().add(line);
        }

        // garis titik tengah
        for (int i = 0; i < numLines; i++) {
            double angle = 360.0 / numLines3 * i;
            double sX4 = centerX + Math.cos(Math.toRadians(angle)) * 0;
            double sY4 = centerY + Math.sin(Math.toRadians(angle)) * 0;
            double eX4 = centerX + Math.cos(Math.toRadians(angle)) * (0 + 10);
            double eY4 = centerY + Math.sin(Math.toRadians(angle)) * (0 + 10);

            Line line = new Line(sX4, sY4, eX4, eY4);
            line.getTransforms().add(new Rotate(angle, centerX, centerY)); // Transformasi melingkar
            line.setStroke(Color.GREEN);
            group.getChildren().add(line);
        }

        // Membuat garis-garis vertikal kecil
        for (int i = 0; i < numLines2; i++) {
            double angle = 360.0 / numLines2 * i;
            double sX = centerX + Math.cos(Math.toRadians(angle)) * radius4;
            double sY = centerY + Math.sin(Math.toRadians(angle)) * radius4;
            double eX = centerX + Math.cos(Math.toRadians(angle)) * (radius4 - 10);
            double eY = centerY + Math.sin(Math.toRadians(angle)) * (radius4 - 10);

            Line line2 = new Line(sX, sY, eX, eY);
            line2.getTransforms().add(new Rotate(angle, centerX, centerY)); // Transformasi melingkar
            line2.setStroke(Color.GREEN);
            group.getChildren().add(line2);
        }

        // shape scanning area
        Arc scanningArea = new Arc();
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
                    scanningArea.setStartAngle(scanningArea.getStartAngle() - 1);
                })
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        //create objek biru
        Circle circle2 = new Circle(300, 170, 5);
        circle2.setFill(Color.BLUE);
        group.getChildren().add(circle2);

        // Menjalankan checker tumpukan
        AnimationTimer collisionChecker = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (circle2.intersects(scanningArea.getLayoutBounds())) {
                    circle2.setOpacity(1);
                } else {
                    circle2.setOpacity(0);
                }
            }
        };

        collisionChecker.start();

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

        // memasukan node group kedalam node root
        root.getChildren().add(group);

        // set the map on the map view
        mapView.setMap(map);
        mapView.setViewpoint(new Viewpoint(
                -6.8743094530729225, 107.58553101717864, 1440));

        // set tiitle, scene dan menampilkan scene
        primaryStage.setTitle("Prototype Radar App");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    @Override
    public void stop() {
        if (mapView != null) {
            mapView.dispose();
        }
    }

    public static void main(String[] args) {
       launch(args);
    }

}