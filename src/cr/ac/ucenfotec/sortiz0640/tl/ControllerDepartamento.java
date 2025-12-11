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
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Controlador para la gestión de departamentos.
 * Permite listar, agregar y eliminar departamentos del sistema.
 *
 * @author Sebastian Ortiz
 * @version 1.0
 * @since 2025
 */
public class ControllerDepartamento {

    @FXML private Label lblUsuario;
    @FXML private Label lblTitulo;
    @FXML private TableView<String[]> tableDepartamentos;
    @FXML private TableColumn<String[], String> colNombre;
    @FXML private TableColumn<String[], String> colCorreo;
    @FXML private TableColumn<String[], String> colDescripcion;
    @FXML private TableColumn<String[], Void> colAcciones;

    private GestorApp gestorApp;
    private ControllerApp controllerApp;

    /**
     * Inicializa el controlador de gestión de departamentos.
     *
     * @param gestorApp Instancia del gestor de lógica de negocio.
     * @param controllerApp Instancia del controlador principal.
     * @throws SQLException Si ocurre un error al cargar los departamentos.
     */
    public void inicializar(GestorApp gestorApp, ControllerApp controllerApp) throws SQLException {
        this.gestorApp = gestorApp;
        this.controllerApp = controllerApp;
        configurarInterfaz();
        configurarTabla();
        cargarDepartamentos();
    }

    @FXML
    private void initialize() {
        // JavaFX llama esto automáticamente
    }

    private void configurarInterfaz() {
        String correoUsuario = gestorApp.obtenerCorreoUsuarioActual();
        lblUsuario.setText("Departamentos, @" + correoUsuario.split("@")[0]);
        lblTitulo.setText("Gestión de Departamentos");
    }

    /**
     * Configura las columnas de la tabla de departamentos.
     */
    private void configurarTabla() {
        // Configurar las columnas de datos
        colNombre.setCellValueFactory(data -> {
            String[] row = data.getValue();
            return row != null && row.length > 0 ?
                    new javafx.beans.property.SimpleStringProperty(row[0]) :
                    new javafx.beans.property.SimpleStringProperty("");
        });

        colCorreo.setCellValueFactory(data -> {
            String[] row = data.getValue();
            return row != null && row.length > 1 ?
                    new javafx.beans.property.SimpleStringProperty(row[1]) :
                    new javafx.beans.property.SimpleStringProperty("");
        });

        colDescripcion.setCellValueFactory(data -> {
            String[] row = data.getValue();
            return row != null && row.length > 2 ?
                    new javafx.beans.property.SimpleStringProperty(row[2]) :
                    new javafx.beans.property.SimpleStringProperty("");
        });

        // Configurar columna de acciones
        configurarColumnaAcciones();
    }

    /**
     * Configura la columna de acciones con botón "Eliminar".
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
                        String[] departamento = getTableView().getItems().get(getIndex());
                        if (departamento != null && departamento.length > 1) {
                            eliminarDepartamento(departamento[1]); // Correo está en posición 1
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

    /**
     * Elimina un departamento seleccionado tras confirmación.
     *
     * @param correoDepartamento Correo del departamento a eliminar.
     */
    private void eliminarDepartamento(String correoDepartamento) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro de eliminar el departamento: " + correoDepartamento + "?");
        confirmacion.setContentText("Esta acción no se puede deshacer. Se eliminarán todos los tickets asociados.");

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String resultado = null;
                try {
                    resultado = gestorApp.eliminarDepartamento(correoDepartamento);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                mostrarAlerta("Eliminación", resultado, Alert.AlertType.INFORMATION);
                try {
                    cargarDepartamentos(); // Recargar
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Carga y muestra todos los departamentos en la tabla.
     *
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    @FXML
    private void cargarDepartamentos() throws SQLException {
        ArrayList<String[]> departamentosData = gestorApp.obtenerTodosDepartamentosFormato();

        ObservableList<String[]> items = FXCollections.observableArrayList();
        if (departamentosData != null) {
            items.addAll(departamentosData);
        }

        tableDepartamentos.setItems(items);
        tableDepartamentos.refresh();
    }

    /**
     * Abre un formulario modal para agregar un nuevo departamento.
     */
    @FXML
    private void abrirAgregarDepartamento() {
        try {
            // Crear ventana emergente para agregar departamento
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initStyle(StageStyle.UTILITY);
            dialog.setTitle("Agregar Nuevo Departamento");

            // Crear formulario
            VBox mainVBox = new VBox(15);
            mainVBox.setPadding(new Insets(20));
            mainVBox.setStyle("-fx-background-color: #f8fafc;");
            mainVBox.setPrefWidth(500); // Aumentado de 400 a 500

            Label title = new Label("Nuevo Departamento");
            title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1e40af;");

            GridPane form = new GridPane();
            form.setVgap(10);
            form.setHgap(10);
            form.setPrefWidth(460); // Ancho del formulario

            // Campos de entrada con ancho completo
            TextField txtNombre = new TextField();
            txtNombre.setPromptText("Nombre del departamento");
            txtNombre.setPrefWidth(350); // Ancho fijo para los campos

            TextField txtCorreo = new TextField();
            txtCorreo.setPromptText("departamento@ucenfotec.ac.cr");
            txtCorreo.setPrefWidth(350);

            TextArea txtDescripcion = new TextArea();
            txtDescripcion.setPromptText("Descripción de funciones");
            txtDescripcion.setPrefHeight(80);
            txtDescripcion.setPrefWidth(350);
            txtDescripcion.setWrapText(true);

            // Labels con estilo
            Label lblNombre = new Label("Nombre:");
            lblNombre.setStyle("-fx-font-weight: bold; -fx-text-fill: #374151;");
            lblNombre.setMinWidth(80);

            Label lblCorreo = new Label("Correo:");
            lblCorreo.setStyle("-fx-font-weight: bold; -fx-text-fill: #374151;");
            lblCorreo.setMinWidth(80);

            Label lblDescripcion = new Label("Descripción:");
            lblDescripcion.setStyle("-fx-font-weight: bold; -fx-text-fill: #374151;");
            lblDescripcion.setMinWidth(80);

            // Agregar al GridPane
            form.add(lblNombre, 0, 0);
            form.add(txtNombre, 1, 0);
            form.add(lblCorreo, 0, 1);
            form.add(txtCorreo, 1, 1);
            form.add(lblDescripcion, 0, 2);
            form.add(txtDescripcion, 1, 2);

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
                if (validarFormulario(txtNombre, txtCorreo, txtDescripcion, lblError)) {
                    String resultado = " ";
                    try {
                        resultado = gestorApp.agregarDepartamento(
                                txtNombre.getText(),
                                txtDescripcion.getText(),
                                txtCorreo.getText()
                        );
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                    mostrarAlerta("Resultado", resultado, Alert.AlertType.INFORMATION);
                    dialog.close();
                    try {
                        cargarDepartamentos();
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

    /**
     * Valida los campos del formulario de departamento.
     */
    private boolean validarFormulario(TextField nombre, TextField correo,
                                      TextArea descripcion, Label errorLabel) {
        if (nombre.getText().trim().isEmpty() || correo.getText().trim().isEmpty() ||
                descripcion.getText().trim().isEmpty()) {
            errorLabel.setText("Todos los campos son obligatorios");
            return false;
        }

        if (!correo.getText().contains("@ucenfotec.ac.cr")) {
            errorLabel.setText("Debe usar un correo institucional (@ucenfotec.ac.cr)");
            return false;
        }

        errorLabel.setText("");
        return true;
    }

    /**
     * Regresa a la vista principal.
     */
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

        } catch (IOException | SQLException e) {
            mostrarAlerta("Error", "No se pudo regresar al home", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Muestra una alerta simple.
     */
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}