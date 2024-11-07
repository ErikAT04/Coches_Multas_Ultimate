package com.erikat.crudjavageneral.Util;

import com.erikat.crudjavageneral.Scenes.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class FXUtils {
    /*------------------------------------------------------------------------------------------*/

    //Función que devuelve la respuesta a un Alert:
    public static Optional<ButtonType> makeAlert(Alert.AlertType alertType, String mensaje, String titulo){
        Alert alert = new Alert(alertType);
        alert.setContentText(mensaje);
        alert.setTitle(titulo);
        return alert.showAndWait();
    }

    /*------------------------------------------------------------------------------------------*/

    public static Controller openInNewStage(String titulo, String ruta){ //Función que introduce un nuevo stage con una escena
        Controller controller = null;
        try{
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(R.getUIResource(ruta));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            controller = loader.getController();
        }catch (Exception e){
            System.out.println("Error de Apertura");
        }
        return controller;
    }

    /*------------------------------------------------------------------------------------------*/

    public static Controller openInThisStage(Stage stage, String titulo, String ruta){ //Función que cambia de escena
        Controller controller = null;
        try{
            FXMLLoader loader = new FXMLLoader(R.getUIResource(ruta));
            Scene scene = new Scene(loader.load());
            stage.setTitle(titulo);
            stage.setScene(scene);
            controller = loader.getController();
            if (!stage.isShowing()){
                stage.show();
            }
        }catch (Exception e){
            System.out.println("Error de Apertura: " + e.getMessage());
        }
        return controller;
    }

    public static boolean anyContentIsNull(List<TextField> texts, List<ComboBox<String>> cbList){
        boolean res = false;
        for (TextField tfield:texts){
            if (tfield.getText().isEmpty()){
                res = true;
            }
        }
        for (ComboBox<String> cb:cbList){
            if (cb.getValue()==null){
                res = true;
            }
        }
        return res;
    }
}
