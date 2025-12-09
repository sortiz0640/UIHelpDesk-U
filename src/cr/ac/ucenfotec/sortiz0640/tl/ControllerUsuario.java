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
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControllerUsuario {

    @FXML private Label lblUsuario;
    @FXML private Label lblTitulo;
    @FXML private TableView<String[]> tableUsuarios;
    @FXML private TableColumn<String[], String> colNombre;
    @FXML private TableColumn<String[], String> colApellidos;
    @FXML private TableColumn<String[], String> colCorreo;
    @FXML private TableColumn<String[], String> colRol;
    @FXML private TableColumn<String[], Void> colAcciones;

    private GestorApp gestorApp;
    private ControllerApp controllerApp;

    public void inicializar(GestorApp gestorApp, ControllerApp controllerApp) throws SQLException {
        this.gestorApp = gestorApp;
        this.controllerApp = controllerApp;
        configurarInterfaz();
        configurarTabla();
        cargarUsuarios();
    }

    @FXML
    private void initialize() {
    }

    private void configurarInterfaz() {
        String correoUsuario = gestorApp.obtenerCorreoUsuarioActual();
        lblUsuario.setText("Usuarios, @" + correoUsuario.split("@")[0]);
        lblTitulo.setText("Gestión de Usuarios");
    }

    private void configurarTabla() {
        // Configurar las columnas de datos
        colNombre.setCellValueFactory(data -> {
            String[] row = data.getValue();
            return row != null && row.length > 0 ?
                    new javafx.beans.property.SimpleStringProperty(row[0]) :
                    new javafx.beans.property.SimpleStringProperty("");
        });

        colApellidos.setCellValueFactory(data -> {
            String[] row = data.getValue();
            return row != null && row.length > 1 ?
                    new javafx.beans.property.SimpleStringProperty(row[1]) :
                    new javafx.beans.property.SimpleStringProperty("");
        });

        colCorreo.setCellValueFactory(data -> {
            String[] row = data.getValue();
            return row != null && row.length > 2 ?
                    new javafx.beans.property.SimpleStringProperty(row[2]) :
                    new javafx.beans.property.SimpleStringProperty("");
        });

        colRol.setCellValueFactory(data -> {
            String[] row = data.getValue();
            return row != null && row.length > 3 ?
                    new javafx.beans.property.SimpleStringProperty(row[3]) :
                    new javafx.beans.property.SimpleStringProperty("");
        });

        // Configurar columna de acciones
        configurarColumnaAcciones();
    }

    /**
     * Configura la columna de acciones con botones "Ver" y "Eliminar"
     */
    private void configurarColumnaAcciones() {
        colAcciones.setCellFactory(param -> new TableCell<String[], Void>() {
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox buttonBox = new HBox(5, btnEliminar);
            private boolean initialized = false;

            {
                if (!initialized) {
                    btnEliminar.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 5 10;");

                    btnEliminar.setOnAction(event -> {
                        String[] usuario = getTableView().getItems().get(getIndex());
                        if (usuario != null && usuario.length > 2) {
                            eliminarUsuario(usuario[2]); // Correo está en posición 2
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
                    setGraphic(buttonBox);
                }
            }
        });
    }

    private void eliminarUsuario(String correoUsuario) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro de eliminar el usuario: " + correoUsuario + "?");
        confirmacion.setContentText("Esta acción no se puede deshacer. Se eliminarán todos los tickets asociados.");

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String resultado = null;
                try {
                    resultado = gestorApp.eliminarUsuario(correoUsuario);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                mostrarAlerta("Eliminación", resultado, Alert.AlertType.INFORMATION);
                try {
                    cargarUsuarios(); // Recargar
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @FXML
    private void cargarUsuarios() throws SQLException {
        ArrayList<String[]> usuariosData = gestorApp.obtenerTodosUsuariosFormato();

        ObservableList<String[]> items = FXCollections.observableArrayList();
        if (usuariosData != null) {
            items.addAll(usuariosData);
        }

        tableUsuarios.setItems(items);
        tableUsuarios.refresh();
    }

    @FXML
    private void abrirAgregarUsuario() {
        try {
            // Crear ventana emergente para agregar usuario
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initStyle(StageStyle.UTILITY);
            dialog.setTitle("Agregar Nuevo Usuario");

            // Crear formulario
            VBox mainVBox = new VBox(15);
            mainVBox.setPadding(new Insets(20));
            mainVBox.setStyle("-fx-background-color: #f8fafc;");
            mainVBox.setPrefWidth(500); // Aumentado de 400 a 500

            Label title = new Label("Nuevo Usuario");
            title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1e40af;");

            GridPane form = new GridPane();
            form.setVgap(10);
            form.setHgap(10);
            form.setPrefWidth(460);

            // Campos de entrada con ancho completo
            TextField txtNombre = new TextField();
            txtNombre.setPromptText("Nombre");
            txtNombre.setPrefWidth(350);

            TextField txtApellidos = new TextField();
            txtApellidos.setPromptText("Apellidos");
            txtApellidos.setPrefWidth(350);

            TextField txtCorreo = new TextField();
            txtCorreo.setPromptText("correo@ucenfotec.ac.cr");
            txtCorreo.setPrefWidth(350);

            PasswordField txtPassword = new PasswordField();
            txtPassword.setPromptText("Contraseña");
            txtPassword.setPrefWidth(350);

            ComboBox<String> cbRol = new ComboBox<>();
            cbRol.getItems().addAll("ADMIN", "ESTUDIANTE", "FUNCIONARIO");
            cbRol.setValue("ESTUDIANTE");
            cbRol.setPrefWidth(350);

            // Labels con estilo
            Label lblNombre = new Label("Nombre:");
            lblNombre.setStyle("-fx-font-weight: bold; -fx-text-fill: #374151;");
            lblNombre.setMinWidth(80);

            Label lblApellidos = new Label("Apellidos:");
            lblApellidos.setStyle("-fx-font-weight: bold; -fx-text-fill: #374151;");
            lblApellidos.setMinWidth(80);

            Label lblCorreo = new Label("Correo:");
            lblCorreo.setStyle("-fx-font-weight: bold; -fx-text-fill: #374151;");
            lblCorreo.setMinWidth(80);

            Label lblPassword = new Label("Contraseña:");
            lblPassword.setStyle("-fx-font-weight: bold; -fx-text-fill: #374151;");
            lblPassword.setMinWidth(80);

            Label lblRol = new Label("Rol:");
            lblRol.setStyle("-fx-font-weight: bold; -fx-text-fill: #374151;");
            lblRol.setMinWidth(80);

            // Agregar al GridPane
            form.add(lblNombre, 0, 0);
            form.add(txtNombre, 1, 0);
            form.add(lblApellidos, 0, 1);
            form.add(txtApellidos, 1, 1);
            form.add(lblCorreo, 0, 2);
            form.add(txtCorreo, 1, 2);
            form.add(lblPassword, 0, 3);
            form.add(txtPassword, 1, 3);
            form.add(lblRol, 0, 4);
            form.add(cbRol, 1, 4);

            Label lblError = new Label();
            lblError.setStyle("-fx-text-fill: #dc2626;");

            HBox buttonBox = new HBox(10);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);

            Button btnCancelar = new Button("Cancelar");
            btnCancelar.setStyle("-fx-background-color: #6b7280; -fx-text-fill: white; -fx-padding: 8 15;");
            btnCancelar.setOnAction(e -> dialog.close());

            Button btnGuardar = new Button("Guardar");
            btnGuardar.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-padding: 8 15;");
            btnGuardar.setOnAction(e -> {
                if (validarFormulario(txtNombre, txtApellidos, txtCorreo, txtPassword, lblError)) {
                    int rolNum = cbRol.getValue().equals("ADMIN") ? 1 :
                            cbRol.getValue().equals("ESTUDIANTE") ? 2 : 3;

                    String resultado = null;
                    try {
                        resultado = gestorApp.agregarUsuario(
                                txtNombre.getText(),
                                txtApellidos.getText(),
                                txtCorreo.getText(),
                                txtPassword.getText(),
                                rolNum
                        );
                    } catch (SQLException | NoSuchAlgorithmException ex) {
                        throw new RuntimeException(ex);
                    }
                    mostrarAlerta("Resultado", resultado, Alert.AlertType.INFORMATION);
                    dialog.close();
                    try {
                        cargarUsuarios();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            buttonBox.getChildren().addAll(btnCancelar, btnGuardar);
            mainVBox.getChildren().addAll(title, form, lblError, buttonBox);

            Scene scene = new Scene(mainVBox);
            dialog.setScene(scene);
            dialog.setResizable(false);
            dialog.showAndWait();

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir el formulario", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private boolean validarFormulario(TextField nombre, TextField apellidos,
                                      TextField correo, PasswordField password,
                                      Label errorLabel) {
        if (nombre.getText().trim().isEmpty() || apellidos.getText().trim().isEmpty() ||
                correo.getText().trim().isEmpty() || password.getText().isEmpty()) {
            errorLabel.setText("Todos los campos son obligatorios");
            return false;
        }

        if (!correo.getText().contains("@ucenfotec.ac.cr")) {
            errorLabel.setText("Debe usar un correo institucional (@ucenfotec.ac.cr)");
            return false;
        }

        if (password.getText().length() < 6) {
            errorLabel.setText("La contraseña debe tener al menos 6 caracteres");
            return false;
        }

        errorLabel.setText("");
        return true;
    }

    @FXML
    private void regresarAlHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/ucenfotec/sortiz0640/ui/app.fxml"));
            Parent root = loader.load();

            ControllerApp mainController = loader.getController();
            mainController.inicializar(gestorApp);

            Stage stage = (Stage) lblUsuario.getScene().getWindow();
            Scene scene = new Scene(root, 1280, 720);
            stage.setScene(scene);
            stage.setTitle("Sistema de Tickets - Panel Principal");

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo regresar al home", Alert.AlertType.ERROR);
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}