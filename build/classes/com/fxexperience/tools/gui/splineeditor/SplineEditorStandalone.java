package com.fxexperience.tools.gui.splineeditor;

import com.fxexperience.tools.application.ToolsApp;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Jasper Potts, Eric Canull
 */
public class SplineEditorStandalone extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        SplineEditor splineEditor = new SplineEditor();
        Scene scene = new Scene(splineEditor);
        scene.getStylesheets().add(Application.class.getResource("Tools.css").toExternalForm());

        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
