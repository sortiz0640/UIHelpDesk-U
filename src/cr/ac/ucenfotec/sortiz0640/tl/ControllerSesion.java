package cr.ac.ucenfotec.sortiz0640.tl;

import cr.ac.ucenfotec.sortiz0640.bl.logic.GestorApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Controlador de autenticación y gestión de sesión del sistema JavaFX.
 * Maneja el inicio de sesión de usuarios mediante interfaz gráfica.
 *
 * @author Sebastian Ortiz
 * @version 2.0
 * @since 2025
 */
public class ControllerSesion {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private GestorApp gestorApp;
    private ControllerApp controllerApp;

    /**
     * Constructor vacío requerido por JavaFX.
     */
    public ControllerSesion() {
    }

    /**
     * Inicializa el controlador con las dependencias necesarias.
     * Llamado desde Main después de cargar el FXML.
     *
     * @param gestorApp Gestor principal de la aplicación
     * @param controllerApp Controlador principal de la aplicación
     */
    public void inicializar(GestorApp gestorApp, ControllerApp controllerApp) {
        this.gestorApp = gestorApp;
    }
    /**
     * Maneja el evento de clic en el botón de login.
     */
    @FXML
    private void handleLogin() throws SQLException, NoSuchAlgorithmException {
        String correo = emailField.getText().trim();
        String password = passwordField.getText();

        // Validar campos vacíos
        if (correo.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Error", "Por favor complete todos los campos", Alert.AlertType.ERROR);
            return;
        }

        // Validar formato de correo
        if (!correo.contains("@ucenfotec.ac.cr")) {
            mostrarAlerta("Error", "Debe usar un correo institucional (@ucenfotec.ac.cr)", Alert.AlertType.ERROR);
            return;
        }

        // Intentar iniciar sesión
        boolean estado = gestorApp.iniciarSesion(correo, password);

        if (!estado) {
            mostrarAlerta("Error de autenticación",
                    "El usuario o la contraseña no son correctos.\nIntente nuevamente.",
                    Alert.AlertType.ERROR);
            passwordField.clear();
            return;
        }

        // Login exitoso
        try {
            abrirVentanaPrincipal();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la aplicación principal", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Abre la ventana principal de la aplicación.
     */
    private void abrirVentanaPrincipal() throws IOException, SQLException {
        // Cargar la vista principal
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/cr/ac/ucenfotec/sortiz0640/ui/app.fxml")
        );
        Parent root = loader.load();

        // Obtener el controlador de la app principal e inyectar dependencias
        ControllerApp controllerApp = loader.getController();
        controllerApp.inicializar(gestorApp);

        // Cambiar escena
        Stage stage = (Stage) emailField.getScene().getWindow();
        Scene scene = new Scene(root, 1280, 720);
        stage.setScene(scene);
        stage.setTitle("Sistema de Tickets - Panel Principal");
    }

    /**
     * Muestra una alerta con el mensaje especificado.
     */
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Método que se ejecuta automáticamente después de cargar el FXML
     * Puede usarse para configuraciones iniciales de la interfaz
     */
    @FXML
    private void initialize() {
        emailField.requestFocus();
    }
}