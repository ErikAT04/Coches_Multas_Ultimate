package com.erikat.crudjavageneral.Scenes;

import com.erikat.crudjavageneral.DAO.MultaDAOHibernate;
import com.erikat.crudjavageneral.DAO.MultaDAOInterface;
import com.erikat.crudjavageneral.DAO.MultaDAOMongo;
import com.erikat.crudjavageneral.DAO.MultaDAOMySQL;
import com.erikat.crudjavageneral.Model.Coche;
import com.erikat.crudjavageneral.Model.Multa;
import com.erikat.crudjavageneral.Util.FXUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MultasController extends Controller{
    private Coche cocheMultado;

    private MultaDAOMongo multaDAOMongo;
    private MultaDAOHibernate multaDAOHibernate;
    private MultaDAOMySQL multaDAOMySQL;

    private List<MultaDAOInterface> listaDAO;

    int numRB;
    @FXML
    private DatePicker datePicker;

    @FXML
    private TableColumn<Multa, LocalDate> fechaTCol;

    @FXML
    private TableColumn<Multa, Integer> idTCol;

    @FXML
    private TextField idTField;

    @FXML
    private TextField matrTField;

    @FXML
    private TableView<Multa> multaTView;

    @FXML
    private TableColumn<Multa, Double> precioTCol;

    @FXML
    private TextField precioTField;

    @FXML
    private ToggleGroup dbGroup;
    @FXML
    private RadioButton mySQLRB;

    @FXML
    void onActualizarClick() {
        if (idTField.getText().isEmpty() || precioTField.getText().isEmpty() || datePicker.getValue()==null){
            FXUtils.makeAlert(Alert.AlertType.ERROR, "Los campos deben estar rellenos", "Error de campos");
        } else {
            try {
                int id = Integer.parseInt(idTField.getText());
                double precio = Double.parseDouble(precioTField.getText());
                LocalDate fecha = datePicker.getValue();

                Multa multa = multaDAOHibernate.buscarMulta(id);
                if(multa!=null){
                    Multa multaNueva = new Multa(id, precio, fecha, cocheMultado);
                    for (MultaDAOInterface multaDAO : listaDAO) {
                        if (multaDAO.actualizarMulta(multaNueva)) {
                            FXUtils.makeAlert(Alert.AlertType.INFORMATION, "Multa actualizada correctamente", "Actualización");
                        } else {
                            FXUtils.makeAlert(Alert.AlertType.ERROR, "Ha habido un problema con la base de datos", "Error de actualización");
                        }
                    }
                    tableRefresh();
                }else{
                    FXUtils.makeAlert(Alert.AlertType.ERROR, "No existe la multa en la base de datos", "Error de actualización");
                }
            }catch (NumberFormatException e){
                FXUtils.makeAlert(Alert.AlertType.ERROR, "El identificador y el precio deben ser números", "Error de actualización");
            }
        }
    }

    @FXML
    void onBorrarClick() {
        if (idTField.getText().isEmpty()){
            FXUtils.makeAlert(Alert.AlertType.ERROR, "Debe haber información en el campo de id", "Error de campos");
        } else {
            try {
                int id = Integer.parseInt(idTField.getText());
                Multa multa = multaDAOHibernate.buscarMulta(id);
                if (multa!=null){
                    Optional<ButtonType> botonPulsado = FXUtils.makeAlert(Alert.AlertType.CONFIRMATION, "¿Quieres borrar esta multa?", "Borrar");
                    if(botonPulsado.isPresent() && botonPulsado.get() == ButtonType.OK){
                        for (MultaDAOInterface multaDAO : listaDAO) {
                            if (multaDAO.borrarMulta(multa)) {
                                FXUtils.makeAlert(Alert.AlertType.INFORMATION, "Coche borrado correctamente", "Multa borrada");
                            } else {
                                FXUtils.makeAlert(Alert.AlertType.ERROR, "Error insesperado, ha ocurrido algo con la base de datos mientras se borraba", "Error inesperado");
                            }
                            flush(); //Borra los datos
                            tableRefresh(); //Refresca la tabla
                        }
                    }
                } else {
                    FXUtils.makeAlert(Alert.AlertType.ERROR, "No se ha encontrado esa multa", "Error de multa");
                }
            }catch (NumberFormatException e){
                FXUtils.makeAlert(Alert.AlertType.ERROR, "El id tiene que ser un número", "Error de campos");
            }
        }
    }

    @FXML
    void onDateChosen() {
        try {
            LocalDate date = datePicker.getValue();
        }catch (Exception e){
            FXUtils.makeAlert(Alert.AlertType.ERROR, "Fecha no introducida correctamente", "Fecha");
            datePicker.setValue(LocalDate.now());
        }
    }

    @FXML
    void onInsertarClick() {
        if (idTField.getText().isEmpty()){
            if (precioTField.getText().isEmpty() || datePicker.getValue()==null){
                FXUtils.makeAlert(Alert.AlertType.ERROR, "Los datos de precio y fecha deben tener contenido", "Error de campos");
            } else {
                try {
                    double precio = Double.parseDouble(precioTField.getText());
                    LocalDate fecha = datePicker.getValue();
                    Multa multa = new Multa(precio, fecha, cocheMultado);
                    for (MultaDAOInterface multaDAO : listaDAO) {
                        if (multaDAO.insertarMulta(multa)) {
                            FXUtils.makeAlert(Alert.AlertType.INFORMATION, "Multa insertada correctamente", "Inserción");
                        } else {
                            FXUtils.makeAlert(Alert.AlertType.ERROR, "Ha habido un problema con la base de datos", "Error de actualización");
                        }
                    }
                    tableRefresh();
                }catch (NumberFormatException e){
                    FXUtils.makeAlert(Alert.AlertType.ERROR, "El precio debe ser un valor numérico", "Error de datos");
                }
            }
        } else {
            FXUtils.makeAlert(Alert.AlertType.ERROR, "Para insertar, el identificador debe estar vacío", "Error de campos");
        }
    }

    @FXML
    void onItemPicked() {
        Multa multa = multaTView.getSelectionModel().getSelectedItem();
        if (multa!=null){
            idTField.setText(String.valueOf(multa.getId()));
            precioTField.setText(String.valueOf(multa.getPrecio()));
            datePicker.setValue(multa.getFecha());
        }
    }

    @FXML
    void onLimpiarClick() {
        flush();
    }

    public void flush(){
        idTField.setText("");
        precioTField.setText("");
        datePicker.setValue(null);
    }

    public void tableRefresh(){
        numRB = switch (((RadioButton)dbGroup.getSelectedToggle()).getText()){
            case "Hibernate" -> 0;
            case "MongoDB" -> 1;
            default -> 2;
        };
        List<Multa> listaMultas = listaDAO.get(numRB).listarMultas(cocheMultado.getMatricula());
        ObservableList<Multa> listaMultasFX = FXCollections.observableArrayList(listaMultas);
        multaTView.setItems(listaMultasFX);
    }

    public void setItems(Coche coche){
        cocheMultado = coche;
        matrTField.setText(cocheMultado.getMatricula());

        multaDAOMySQL = new MultaDAOMySQL();
        multaDAOHibernate = new MultaDAOHibernate();
        multaDAOMongo = new MultaDAOMongo();

        listaDAO = List.of(multaDAOHibernate, multaDAOMongo, multaDAOMySQL);

        mySQLRB.setSelected(true);

        idTCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        fechaTCol.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        precioTCol.setCellValueFactory(new PropertyValueFactory<>("precio"));
        tableRefresh();
    }

    public void onRefresh(ActionEvent actionEvent) {
        tableRefresh();
    }
}
