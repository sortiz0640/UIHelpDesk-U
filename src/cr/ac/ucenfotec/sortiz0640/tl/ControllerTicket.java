package cr.ac.ucenfotec.sortiz0640.tl;

import cr.ac.ucenfotec.sortiz0640.bl.logic.GestorApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Controlador de gestión de tickets del sistema HelpDesk (solo JavaFX)
 */
public class ControllerTicket {

    @FXML private Label lblUsuarioInfo;
    @FXML private TextField txtAsunto;
    @FXML private TextArea txtDescripcion;
    @FXML private ComboBox<String> cbDepartamentos;
    @FXML private Label lblMensaje;

    private GestorApp gestorApp;
    private ControllerApp controllerApp;

    /**
     * Constructor vacío requerido por JavaFX
     */
    public ControllerTicket() {
    }

    /**
     * Inicializa el controlador con las dependencias necesarias
     */
    public void inicializar(GestorApp gestorApp, ControllerApp controllerApp) {
        this.gestorApp = gestorApp;
        this.controllerApp = controllerApp;
        configurarInterfaz();
        cargarDepartamentos();
    }

    @FXML
    private void initialize() {
    }

    private void configurarInterfaz() {
        if (gestorApp != null && gestorApp.esSesionActiva()) {
            String correoUsuario = gestorApp.obtenerCorreoUsuarioActual();
            lblUsuarioInfo.setText("Usuario: " + correoUsuario.split("@")[0]);
        }
    }

    private void cargarDepartamentos() {
        if (gestorApp != null) {

            ArrayList<String> departamentos = gestorApp.obtenerDepartamentos();

            cbDepartamentos.getItems().clear();

            if (departamentos != null && !departamentos.isEmpty()) {

                cbDepartamentos.getItems().addAll(departamentos);
                cbDepartamentos.setDisable(false);

            } else {

                cbDepartamentos.getItems().add("No hay departamentos disponibles");
                cbDepartamentos.setDisable(true);
            }
        }
    }


    @FXML
    private void crearTicket() {
        // Validaciones
        if (txtAsunto.getText().trim().isEmpty()) {
            mostrarMensaje("El asunto es obligatorio");
            return;
        }

        if (txtDescripcion.getText().trim().isEmpty()) {
            mostrarMensaje("La descripción es obligatoria");
            return;
        }

        if (cbDepartamentos.getValue() == null ||
                cbDepartamentos.getValue().equals("No hay departamentos disponibles")) {
            mostrarMensaje("Debe seleccionar un departamento");
            return;
        }

        String asunto = txtAsunto.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String departamento = cbDepartamentos.getValue();

        boolean resultado = gestorApp.crearTicket(asunto, descripcion, departamento);

        if (resultado) {
            regresarAlHome();
        } else {
            mostrarMensaje("Ha ocurrido un error. El ticket no se ha podido crear. Intente nuevamente");
        }
    }

    @FXML
    private void cancelarCreacion() {
        regresarAlHome();
    }

    @FXML
    private void regresarAlHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/ucenfotec/sortiz0640/ui/app.fxml"));
            Parent root = loader.load();

            ControllerApp mainController = loader.getController();
            mainController.inicializar(gestorApp);

            Stage stage = (Stage) lblUsuarioInfo.getScene().getWindow();
            Scene scene = new Scene(root, 1280, 720);
            stage.setScene(scene);
            stage.setTitle("Sistema de Tickets - Panel Principal");

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo regresar al home", Alert.AlertType.ERROR);
        }
    }

    private void mostrarMensaje(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.setVisible(true);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}