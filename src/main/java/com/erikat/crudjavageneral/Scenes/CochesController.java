package com.erikat.crudjavageneral.Scenes;

import com.erikat.crudjavageneral.DAO.CocheDAOHibernate;
import com.erikat.crudjavageneral.DAO.CocheDAOInterface;
import com.erikat.crudjavageneral.DAO.CocheDAOMongo;
import com.erikat.crudjavageneral.DAO.CocheDAOMySQL;
import com.erikat.crudjavageneral.Model.Coche;
import com.erikat.crudjavageneral.Util.FXUtils;
import com.erikat.crudjavageneral.Util.R;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.apache.commons.codec.digest.DigestUtils;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CochesController extends Controller implements Initializable {

    CocheDAOMySQL cocheDAOMySQL;
    CocheDAOMongo cocheDAOMongo;
    CocheDAOHibernate cocheDAOHibernate;
    List<CocheDAOInterface> listaDAO;
    List<TextField> textFields;
    int numRB;
    @FXML
    private TableView<Coche> cochesTView;

    @FXML
    private TextField codTField;

    @FXML
    private ToggleGroup dbGroup;

    @FXML
    private RadioButton hiberRB;

    @FXML
    private TableColumn<Coche, Integer> idTCol;

    @FXML
    private TableColumn<Coche, String> marcaTCol;

    @FXML
    private TextField marcaTField;

    @FXML
    private TableColumn<Coche, String> matrTCol;

    @FXML
    private TextField matrTField;

    @FXML
    private TableColumn<Coche, String> modeloTCol;

    @FXML
    private TextField modeloTField;

    @FXML
    private RadioButton mongoRB;

    @FXML
    private Button multaBtt;

    @FXML
    private RadioButton mySQLRB;

    @FXML
    private ComboBox<String> tipoCBox;

    @FXML
    private TableColumn<Coche, String> tipoTCol;

    @FXML
    void onActualizarClick() { //Función que salta al dar a "Actualizar"
        /*
        Requisitos para que se actualice:
            1. Todos los campos estén rellenos
            2. Existe la matrícula en la BBDD
            3. Los datos del coche no sean exactamente los mismos
         */
        if (FXUtils.anyContentIsNull(textFields, List.of(tipoCBox))){ //Si cualquier campo es nulo
            FXUtils.makeAlert(Alert.AlertType.ERROR, "No puede haber campos vacíos", "Error de formulario");
        } else {
            Coche cocheMatr = listaDAO.get(numRB).buscarCoche(matrTField.getText());
            if (cocheMatr!=null){
                Coche nuevoCoche = new Coche(matrTField.getText(), marcaTField.getText(), modeloTField.getText(), tipoCBox.getValue(), R.encrypt(codTField.getText()));
                if (nuevoCoche.equals(cocheMatr)){
                    FXUtils.makeAlert(Alert.AlertType.ERROR, "¿Por qué querrías actualizar un coche si no ha cambiado nada?", "Error de actualización");
                } else {
                    nuevoCoche.setId(cocheMatr.getId()); //Le pongo al coche el ID del anterior (Ya que no tiene ninguna forma de llegar a este)
                    nuevoCoche.setMultas(cocheMatr.getMultas()); //Le pongo la lista de multas
                    for (CocheDAOInterface dao : listaDAO) {
                        if (dao.actualizarCoche(nuevoCoche)) {
                            FXUtils.makeAlert(Alert.AlertType.INFORMATION, "Coche actualizado correctamente", "Actualización");
                        } else {
                            FXUtils.makeAlert(Alert.AlertType.ERROR, "Error en la base de datos", "Error de BD");
                        }
                    }
                    tableRefresh();
                }
            } else {
                FXUtils.makeAlert(Alert.AlertType.ERROR, "No hemos encontrado ese coche en la base de datos",  "Error de actualización");
            }
        }
    }

    /*--------------------------------------------------------------------------------------------*/

    @FXML
    void onBorrarClick() { //Función que salta al dar a "Borrar"
        /*
        Requisitos para que se borre:
            1. El campo de la matrícula tiene información
            2. Existe esa matrícula en la BBDD
         */
        if (matrTField.getText().isEmpty()){ //Si no encuentra texto:
            FXUtils.makeAlert(Alert.AlertType.ERROR, "El campo de matrícula no puede estar vacío", "Error de borrado");
        } else {
            Coche coche = listaDAO.get(numRB).buscarCoche(matrTField.getText());
            if (coche==null){
                FXUtils.makeAlert(Alert.AlertType.ERROR, "No se ha encontrado el coche que buscas", "Error de borrado");
            } else {
                Optional<ButtonType> botonPulsado = FXUtils.makeAlert(Alert.AlertType.CONFIRMATION, "¿Quieres borrar este coche?", "Borrar");
                if(botonPulsado.isPresent() && botonPulsado.get() == ButtonType.OK){
                    for (CocheDAOInterface dao : listaDAO) {
                        if (dao.borrarCoche(coche)) {
                            FXUtils.makeAlert(Alert.AlertType.INFORMATION, "Coche borrado correctamente", "Coche borrado");
                            flush(); //Borra los datos
                        } else {
                            FXUtils.makeAlert(Alert.AlertType.ERROR, "Error insesperado, ha ocurrido algo con la base de datos mientras se borraba", "Error inesperado");
                        }
                    }
                    tableRefresh(); //Refresca la tabla
                }
            }
        }
    }

    /*--------------------------------------------------------------------------------------------*/

    @FXML
    void onContentClicked() { //Función que salta al pulsar un elemento de la tabla
        Coche coche = cochesTView.getSelectionModel().getSelectedItem();
        if (coche!=null){
            matrTField.setText(coche.getMatricula());
            marcaTField.setText(coche.getMarca());
            modeloTField.setText(coche.getModelo());
            tipoCBox.setValue(coche.getTipo());
            codTField.setText(coche.getCodSecreto());
        }
    }

    /*--------------------------------------------------------------------------------------------*/

    @FXML
    void onInsertarClick() { //Función que salta al dar a "Insertar"
        /*
        Requisitos para que se inserte:
            1. Todos los campos estén rellenos
            2. No exista esa matrícula en la BBDD
         */
        if (FXUtils.anyContentIsNull(textFields, List.of(tipoCBox))){ //Si cualquier campo es nulo
            FXUtils.makeAlert(Alert.AlertType.ERROR, "No puede haber campos vacíos", "Error de formulario");
        } else {
            Coche cocheMatr = listaDAO.get(numRB).buscarCoche(matrTField.getText());
            if (cocheMatr==null){
                Coche nuevoCoche = new Coche(matrTField.getText(), marcaTField.getText(), modeloTField.getText(), tipoCBox.getValue(), R.encrypt(codTField.getText()));
                for (CocheDAOInterface dao : listaDAO){
                    if (dao.insertarCoche(nuevoCoche)) {
                        FXUtils.makeAlert(Alert.AlertType.INFORMATION, "Coche insertado correctamente", "Inserción de coche");

                    } else {
                        FXUtils.makeAlert(Alert.AlertType.ERROR, "Error insesperado, ha ocurrido algo con la base de datos mientras se borraba", "Error inesperado");
                    }
                }
                tableRefresh();
            } else {
                FXUtils.makeAlert(Alert.AlertType.ERROR, "Se ha encontrado un coche con esa matrícula en la base de datos", "Error de inserción");
            }
        }
    }

    /*--------------------------------------------------------------------------------------------*/

    @FXML
    void onLimpiarClick() { //Función que salta al dar a "Limpiar"
        flush(); //Llama a la función de limpieza de formulario
    }

    /*--------------------------------------------------------------------------------------------*/

    @FXML
    void onMultasClick() {
        if (matrTField.getText().isEmpty()){
            FXUtils.makeAlert(Alert.AlertType.ERROR, "Rellena el campo de matrícula o selecciona un coche de la tabla", "Error");
        } else {
            Coche coche = listaDAO.get(numRB).buscarCoche(matrTField.getText());
            if (coche==null){
                FXUtils.makeAlert(Alert.AlertType.ERROR, "No se ha encontrado el coche", "Error de coche");
            } else {
                MultasController controller = (MultasController) FXUtils.openInNewStage("Multas", "MultasView.fxml");
                controller.setItems(coche);
            }
        }
    }

    @FXML
    void onRefresh(){
        tableRefresh();
    }
    /*--------------------------------------------------------------------------------------------*/

    void flush(){ //Función de borrado de elementos del formulario
        marcaTField.setText("");
        matrTField.setText("");
        modeloTField.setText("");
        codTField.setText("");
        tipoCBox.setValue(null);
    }

    /*--------------------------------------------------------------------------------------------*/

    void tableRefresh() { //Función de carga de datos en la tabla
        numRB = switch (((RadioButton)dbGroup.getSelectedToggle()).getText()){
            case "Hibernate" -> 0;
            case "MongoDB" -> 1;
            default -> 2;
        }; //Dependiendo de si se activa uno u otro cambia la posición de la lista DAO:
        List<Coche> coches = listaDAO.get(numRB).listarCoches(); //Lista los coches según el DAO de la lista
        ObservableList<Coche> listaCoches = FXCollections.observableArrayList(coches);
        cochesTView.setItems(listaCoches);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cocheDAOHibernate = new CocheDAOHibernate();
        cocheDAOMongo = new CocheDAOMongo();
        cocheDAOMySQL = new CocheDAOMySQL();

        listaDAO = List.of(cocheDAOHibernate, cocheDAOMongo, cocheDAOMySQL);
        //Creo una lista con todos los DAO

        mySQLRB.setSelected(true); //Selecciono por defecto uno de los radiobutton (Me decanté por el de MySQL)

        //Tabla:
        idTCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipoTCol.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        matrTCol.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        marcaTCol.setCellValueFactory(new PropertyValueFactory<>("marca"));
        modeloTCol.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        tableRefresh();

        //ComboBox:
        List<String> listaTipos = List.of("SUV", "Monovolumen", "Familiar", "Coupé", "Descapotable");
        tipoCBox.getItems().addAll(listaTipos);

        textFields = List.of(marcaTField, matrTField, modeloTField, codTField); //Guardo todos los textField en una lista que usaré más tarde para hacer verificaciones

    }
}
