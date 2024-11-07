package com.erikat.crudjavageneral;

import com.erikat.crudjavageneral.Util.FXUtils;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXUtils.openInThisStage(stage, "Coches", "CochesView.fxml");
    }

    public static void main(String[] args) {
        launch();
    }

    /*
    RECORDATORIO:
        - Quitar module-info.java
        - Añadir una aplicación
        - VM Options: [--module-path "C:\Program Files\Java\openjfx-23_windows-x64_bin-sdk\javafx-sdk-23\lib" --add-modules javafx.controls,javafx.fxml]
     */
}