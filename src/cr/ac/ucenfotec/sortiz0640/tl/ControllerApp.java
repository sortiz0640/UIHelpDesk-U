package cr.ac.ucenfotec.sortiz0640.tl;

import cr.ac.ucenfotec.sortiz0640.bl.logic.GestorApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import java.io.IOException;
import java.util.ArrayList;

public class ControllerApp {

    @FXML private Label lblUsuario;
    @FXML private Label lblTitulo;
    @FXML private Button btnUsuarios;
    @FXML private Button btnDepartamentos;
    @FXML private TableView<String[]> tableTickets;
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

    private void configurarTabla() {
        // Configurar las columnas de datos
        colId.setCellValueFactory(data -> {
            String[] row = data.getValue();
            return row != null && row.length > 0 ?
                    new javafx.beans.property.SimpleStringProperty(row[0]) :
                    new javafx.beans.property.SimpleStringProperty("");
        });

        colAsunto.setCellValueFactory(data -> {
            String[] row = data.getValue();
            return row != null && row.length > 1 ?
                    new javafx.beans.property.SimpleStringProperty(row[1]) :
                    new javafx.beans.property.SimpleStringProperty("");
        });

        colDepartamento.setCellValueFactory(data -> {
            String[] row = data.getValue();
            return row != null && row.length > 2 ?
                    new javafx.beans.property.SimpleStringProperty(row[2]) :
                    new javafx.beans.property.SimpleStringProperty("");
        });

        colUsuario.setCellValueFactory(data -> {
            String[] row = data.getValue();
            return row != null && row.length > 3 ?
                    new javafx.beans.property.SimpleStringProperty(row[3]) :
                    new javafx.beans.property.SimpleStringProperty("");
        });

        colCategoria.setCellValueFactory(data -> {
            String[] row = data.getValue();
            return row != null && row.length > 4 ?
                    new javafx.beans.property.SimpleStringProperty(row[4]) :
                    new javafx.beans.property.SimpleStringProperty("");
        });

        colEmocion.setCellValueFactory(data -> {
            String[] row = data.getValue();
            return row != null && row.length > 5 ?
                    new javafx.beans.property.SimpleStringProperty(row[5]) :
                    new javafx.beans.property.SimpleStringProperty("");
        });

        colEstado.setCellValueFactory(data -> {
            String[] row = data.getValue();
            return row != null && row.length > 6 ?
                    new javafx.beans.property.SimpleStringProperty(row[6]) :
                    new javafx.beans.property.SimpleStringProperty("");
        });

        // Configurar columna de acciones
        if (gestorApp.tienePermisosAdmin()) {
            configurarColumnaAcciones();
        } else {
            colAcciones.setVisible(false);
        }
    }

    /**
     * Configura la columna de acciones con botón "Ver"
     */
    private void configurarColumnaAcciones() {
        colAcciones.setCellFactory(param -> new TableCell<String[], Void>() {
            private final Button btnVer = new Button("Ver");
            private boolean initialized = false;

            {
                if (!initialized) {
                    btnVer.setStyle("-fx-background-color: #00a6fb; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 5 10;");
                    btnVer.setOnAction(event -> {
                        String[] ticket = getTableView().getItems().get(getIndex());
                        if (ticket != null && ticket.length > 0) {
                            mostrarDetallesTicket(ticket[0]);
                        }
                    });
                    initialized = true;
                }
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    setGraphic(btnVer);
                }
            }
        });
    }

    /**
     * Muestra los detalles del ticket en una ventana emergente
     */
    private void mostrarDetallesTicket(String ticketId) {
        try {
            // Obtener detalles del ticket
            // Si tu GestorApp no tiene obtenerDetallesTicket, puedes usar los datos existentes
            // Buscar el ticket en la tabla actual
            String[] ticketData = null;
            for (String[] ticket : tableTickets.getItems()) {
                if (ticket[0].equals(ticketId)) {
                    ticketData = ticket;
                    break;
                }
            }

            if (ticketData == null) {
                mostrarAlerta("Error", "No se encontró el ticket seleccionado", Alert.AlertType.ERROR);
                return;
            }

            // Crear ventana emergente
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initStyle(StageStyle.UTILITY);
            dialog.setTitle("Detalles del Ticket #" + ticketId);

            // Crear layout principal
            VBox mainVBox = new VBox(20);
            mainVBox.setPadding(new Insets(20));
            mainVBox.setStyle("-fx-background-color: #f8fafc;");
            mainVBox.setPrefWidth(500);

            // Título
            Label title = new Label("Detalles del Ticket");
            title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1e40af;");

            // Grid para detalles
            GridPane grid = new GridPane();
            grid.setVgap(10);
            grid.setHgap(15);
            grid.setPadding(new Insets(10));

            // Añadir detalles al grid
            String[] labels = {"ID:", "Asunto:", "Departamento:", "Usuario:",
                    "Categoría:", "Emoción:", "Estado:"};

            for (int i = 0; i < labels.length && i < ticketData.length; i++) {
                Label label = new Label(labels[i]);
                label.setStyle("-fx-font-weight: bold; -fx-text-fill: #374151;");

                Label value = new Label(ticketData[i] != null ? ticketData[i] : "N/A");
                value.setWrapText(true);
                value.setStyle("-fx-text-fill: #6b7280;");

                grid.add(label, 0, i);
                grid.add(value, 1, i);

                // Hacer que la descripción ocupe más espacio si es necesario
                if (labels[i].equals("Asunto:")) {
                    GridPane.setColumnSpan(value, 2);
                }
            }

            // Área para descripción adicional
            Label descLabel = new Label("Descripción:");
            descLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #374151;");

            TextArea descArea = new TextArea("Descripción detallada del ticket #" + ticketId +
                    "\n\nEste es un ejemplo de descripción completa.");
            descArea.setWrapText(true);
            descArea.setEditable(false);
            descArea.setPrefHeight(100);
            descArea.setStyle("-fx-background-color: #f3f4f6; -fx-border-color: #d1d5db;");

            grid.add(descLabel, 0, labels.length);
            grid.add(descArea, 0, labels.length + 1);
            GridPane.setColumnSpan(descArea, 2);

            // Panel de botones (solo para admin)
            HBox buttonPanel = new HBox(15);
            buttonPanel.setAlignment(Pos.CENTER_RIGHT);
            buttonPanel.setPadding(new Insets(20, 0, 0, 0));

            if (gestorApp.tienePermisosAdmin()) {
                Button btnActualizar = new Button("Actualizar Estado");
                btnActualizar.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-padding: 8 15;");
                btnActualizar.setOnAction(e -> {
                    dialog.close();
                    actualizarEstadoTicket(ticketId);
                });

                Button btnEliminar = new Button("Eliminar");
                btnEliminar.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white; -fx-padding: 8 15;");
                btnEliminar.setOnAction(e -> {
                    dialog.close();
                    eliminarTicket(ticketId);
                });

                buttonPanel.getChildren().addAll(btnActualizar, btnEliminar);
            }

            // Botón cerrar
            Button btnCerrar = new Button("Cerrar");
            btnCerrar.setStyle("-fx-background-color: #6b7280; -fx-text-fill: white; -fx-padding: 8 15;");
            btnCerrar.setOnAction(e -> dialog.close());

            HBox closePanel = new HBox();
            closePanel.getChildren().add(btnCerrar);
            closePanel.setAlignment(Pos.CENTER_LEFT);

            // Si no es admin, solo mostrar botón cerrar
            if (!gestorApp.tienePermisosAdmin()) {
                buttonPanel.getChildren().add(btnCerrar);
            } else {
                // Para admin, agregar panel de cierre también
                VBox finalLayout = new VBox(20);
                finalLayout.getChildren().addAll(title, grid, buttonPanel, closePanel);
                mainVBox.getChildren().addAll(finalLayout);
            }

            // Configurar escena
            Scene scene = new Scene(mainVBox);
            dialog.setScene(scene);
            dialog.setResizable(false);
            dialog.showAndWait();

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo mostrar los detalles del ticket", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void actualizarEstadoTicket(String ticketId) {
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

    @FXML
    private void cargarTickets() {
        ArrayList<String[]> ticketsData;

        if (gestorApp.tienePermisosAdmin()) {
            ticketsData = gestorApp.obtenerTodosTicketsFormato();
        } else {
            String correoUsuario = gestorApp.obtenerCorreoUsuarioActual();
            ticketsData = gestorApp.obtenerTicketsDelUsuarioFormato(correoUsuario);
        }

        ObservableList<String[]> items = FXCollections.observableArrayList();
        if (ticketsData != null) {
            items.addAll(ticketsData);
        }

        tableTickets.setItems(items);

        // Forzar actualización de las celdas
        tableTickets.refresh();
    }

    // Los demás métodos se mantienen igual
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

    @FXML
    private void abrirUsuarios() {
        mostrarAlerta("Funcionalidad en desarrollo",
                "La gestión de usuarios estará disponible próximamente",
                Alert.AlertType.INFORMATION);
    }

    @FXML
    private void abrirDepartamentos() {
        mostrarAlerta("Funcionalidad en desarrollo",
                "La gestión de departamentos estará disponible próximamente",
                Alert.AlertType.INFORMATION);
    }
}