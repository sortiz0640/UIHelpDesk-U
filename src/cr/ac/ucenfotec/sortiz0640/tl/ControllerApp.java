package cr.ac.ucenfotec.sortiz0640.tl;
import cr.ac.ucenfotec.sortiz0640.bl.logic.GestorApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

public class ControllerApp {

    @FXML private Label lblUsuario;
    @FXML private Label lblTitulo;
    @FXML private Button btnUsuarios;
    @FXML private Button btnDepartamentos;
    @FXML private TableView<String[]> tableTickets; // CAMBIO IMPORTANTE
    @FXML private TableColumn<String[], String> colId;
    @FXML private TableColumn<String[], String> colAsunto;
    @FXML private TableColumn<String[], String> colDepartamento;
    @FXML private TableColumn<String[], String> colUsuario;
    @FXML private TableColumn<String[], String> colCategoria;
    @FXML private TableColumn<String[], String> colEmocion;
    @FXML private TableColumn<String[], String> colEstado;
    @FXML private TableColumn<String[], Void> colAcciones;

    private GestorApp gestorApp;

    public void inicializar(GestorApp gestorApp) {
        this.gestorApp = gestorApp;
        configurarInterfaz();
        configurarTabla();
        cargarTickets();
    }

    @FXML
    private void initialize() {
        // JavaFX llama esto automáticamente
    }

    private void configurarInterfaz() {
        String correoUsuario = gestorApp.obtenerCorreoUsuarioActual();
        lblUsuario.setText("Home, @" + correoUsuario.split("@")[0]);

        if (gestorApp.tienePermisosAdmin()) {
            btnUsuarios.setVisible(true);
            btnDepartamentos.setVisible(true);
            lblTitulo.setText("Todos los Tickets");
        } else {
            btnUsuarios.setVisible(false);
            btnDepartamentos.setVisible(false);
            lblTitulo.setText("Mis Tickets");
        }
    }

    /**
     * CONFIGURACIÓN SIMPLIFICADA - Sin clases internas, sin propiedades complejas
     */
    private void configurarTabla() {
        // Configurar cada columna para extraer el dato correcto del array
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[0]));
        colAsunto.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[1]));
        colDepartamento.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[2]));
        colUsuario.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[3]));
        colCategoria.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[4]));
        colEmocion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[5]));
        colEstado.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[6]));

        // Configurar botones de acción solo para admin
        if (gestorApp.tienePermisosAdmin()) {
            configurarBotonesAccion();
        } else {
            colAcciones.setVisible(false);
        }
    }

    /**
     * CARGA DIRECTA - Sin transformaciones complicadas
     */
    @FXML
    private void cargarTickets() {
        ArrayList<String[]> ticketsData;

        if (gestorApp.tienePermisosAdmin()) {
            ticketsData = gestorApp.obtenerTodosTicketsFormato();
        } else {
            String correoUsuario = gestorApp.obtenerCorreoUsuarioActual();
            ticketsData = gestorApp.obtenerTicketsDelUsuarioFormato(correoUsuario);
        }

        // Convertir directamente a ObservableList
        ObservableList<String[]> items = FXCollections.observableArrayList();
        if (ticketsData != null) {
            items.addAll(ticketsData);
        }

        tableTickets.setItems(items);
        System.out.println("Filas cargadas: " + items.size());
    }

    /**
     * Configuración simple de botones
     */
    private void configurarBotonesAccion() {
        colAcciones.setCellFactory(param -> new TableCell<String[], Void>() {
            private final Button btnActualizar = new Button("Actualizar");
            private final Button btnEliminar = new Button("Eliminar");

            {
                btnActualizar.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-size: 10px;");
                btnEliminar.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white; -fx-font-size: 10px;");

                btnActualizar.setOnAction(event -> {
                    String[] ticket = getTableView().getItems().get(getIndex());
                    actualizarTicket(ticket[0]); // ID está en posición 0
                });

                btnEliminar.setOnAction(event -> {
                    String[] ticket = getTableView().getItems().get(getIndex());
                    eliminarTicket(ticket[0]); // ID está en posición 0
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox(5);
                    hbox.getChildren().addAll(btnActualizar, btnEliminar);
                    setGraphic(hbox);
                }
            }
        });
    }

    // Los demás métodos (actualizarTicket, eliminarTicket, etc.) se mantienen igual
    private void actualizarTicket(String ticketId) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("EN_PROGRESO", "EN_PROGRESO", "RESUELTO");
        dialog.setTitle("Actualizar Estado");
        dialog.setHeaderText("Ticket ID: " + ticketId);
        dialog.setContentText("Seleccione el nuevo estado:");

        dialog.showAndWait().ifPresent(estado -> {
            int estadoNum = estado.equals("EN_PROGRESO") ? 1 : 2;
            String resultado = gestorApp.actualizarEstadoTicket(ticketId, estadoNum);
            mostrarAlerta("Actualización", resultado, Alert.AlertType.INFORMATION);
            cargarTickets(); // Recargar
        });
    }

    private void eliminarTicket(String ticketId) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro de eliminar el ticket " + ticketId + "?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String resultado = gestorApp.eliminarTicket(ticketId);
                mostrarAlerta("Eliminación", resultado, Alert.AlertType.INFORMATION);
                cargarTickets(); // Recargar
            }
        });
    }

    // Los demás métodos (abrirCrearTicket, cerrarSesion, etc.) se mantienen igual
    @FXML
    private void abrirCrearTicket() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/ucenfotec/sortiz0640/ui/tiquete.fxml"));
            Parent root = loader.load();

            ControllerTicket controllerTicket = loader.getController();
            controllerTicket.inicializar(gestorApp, this);

            Stage stage = (Stage) lblUsuario.getScene().getWindow();
            Scene scene = new Scene(root, 1280, 720);
            stage.setScene(scene);
            stage.setTitle("Sistema de Tickets - Crear Ticket");

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la ventana de creación de tickets", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void cerrarSesion() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cerrar Sesión");
        confirmacion.setHeaderText("¿Está seguro de cerrar sesión?");

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    gestorApp.cerrarSesion();
                    volverAlLogin();
                } catch (IOException e) {
                    mostrarAlerta("Error", "No se pudo cerrar la sesión", Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void volverAlLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/ucenfotec/sortiz0640/ui/sesion.fxml"));
        Parent root = loader.load();

        // Re-inyectar dependencias en el controlador de sesión
        ControllerSesion controllerSesion = loader.getController();
        controllerSesion.inicializar(gestorApp, this);

        Stage stage = (Stage) lblUsuario.getScene().getWindow();
        Scene scene = new Scene(root, 1280, 720);
        stage.setScene(scene);
        stage.setTitle("Sistema de Tickets - Login");
        stage.setResizable(false);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void recargarTickets() {
        cargarTickets();
    }

    @FXML
    private void abrirUsuarios() {
        try {
            mostrarAlerta("Funcionalidad en desarrollo",
                    "La gestión de usuarios estará disponible próximamente",
                    Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir la gestión de usuarios", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirDepartamentos() {
        try {
            mostrarAlerta("Funcionalidad en desarrollo",
                    "La gestión de departamentos estará disponible próximamente",
                    Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir la gestión de departamentos", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
}