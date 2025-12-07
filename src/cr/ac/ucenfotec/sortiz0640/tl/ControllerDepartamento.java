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
     * Configura la columna de acciones con botones "Ver" y "Eliminar"
     */
    private void configurarColumnaAcciones() {
        colAcciones.setCellFactory(param -> new TableCell<String[], Void>() {
            private final Button btnVer = new Button("Ver");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox buttonBox = new HBox(5, btnVer, btnEliminar);
            private boolean initialized = false;

            {
                if (!initialized) {
                    btnVer.setStyle("-fx-background-color: #00a6fb; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 5 10;");
                    btnEliminar.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 5 10;");

                    btnVer.setOnAction(event -> {
                        String[] departamento = getTableView().getItems().get(getIndex());
                        if (departamento != null && departamento.length > 1) {
                            mostrarDetallesDepartamento(departamento[1]); // Correo está en posición 1
                        }
                    });

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
     * Muestra los detalles del departamento en una ventana emergente
     */

    private void mostrarDetallesDepartamento(String correoDepartamento) {
        try {
            String[] departamentoData = gestorApp.obtenerDetallesDepartamento(correoDepartamento);

            if (departamentoData == null) {
                mostrarAlerta("Error", "No se encontró el departamento", Alert.AlertType.ERROR);
                return;
            }

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initStyle(StageStyle.UTILITY);
            dialog.setTitle("Detalles del Departamento: " + departamentoData[0]);

            VBox mainVBox = new VBox(20);
            mainVBox.setPadding(new Insets(20));
            mainVBox.setStyle("-fx-background-color: #f8fafc;");
            mainVBox.setPrefWidth(400);

            Label title = new Label("Detalles del Departamento");
            title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1e40af;");

            GridPane grid = new GridPane();
            grid.setVgap(10);
            grid.setHgap(15);
            grid.setPadding(new Insets(10));

            String[] labels = {"Nombre:", "Correo:", "Descripción:"};

            for (int i = 0; i < labels.length && i < departamentoData.length; i++) {
                Label label = new Label(labels[i]);
                label.setStyle("-fx-font-weight: bold; -fx-text-fill: #374151;");

                if (i == 2) { // Descripción - usar TextArea
                    TextArea value = new TextArea(departamentoData[i] != null ? departamentoData[i] : "N/A");
                    value.setWrapText(true);
                    value.setEditable(false);
                    value.setPrefHeight(80);
                    value.setStyle("-fx-background-color: #f3f4f6; -fx-border-color: #d1d5db;");
                    GridPane.setColumnSpan(value, 2);
                    grid.add(label, 0, i);
                    grid.add(value, 0, i + 1);
                    GridPane.setRowSpan(value, 1);
                } else {
                    Label value = new Label(departamentoData[i] != null ? departamentoData[i] : "N/A");
                    value.setWrapText(true);
                    value.setStyle("-fx-text-fill: #6b7280;");
                    grid.add(label, 0, i);
                    grid.add(value, 1, i);
                }
            }

            Button btnCerrar = new Button("Cerrar");
            btnCerrar.setStyle("-fx-background-color: #6b7280; -fx-text-fill: white; -fx-padding: 8 15;");
            btnCerrar.setOnAction(e -> dialog.close());

            HBox buttonPanel = new HBox();
            buttonPanel.getChildren().add(btnCerrar);
            buttonPanel.setAlignment(Pos.CENTER_RIGHT);

            mainVBox.getChildren().addAll(title, grid, buttonPanel);

            Scene scene = new Scene(mainVBox);
            dialog.setScene(scene);
            dialog.setResizable(false);
            dialog.showAndWait();

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo mostrar los detalles del departamento", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }


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
            mainVBox.setPrefWidth(400);

            Label title = new Label("Nuevo Departamento");
            title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1e40af;");

            GridPane form = new GridPane();
            form.setVgap(10);
            form.setHgap(10);

            TextField txtNombre = new TextField();
            txtNombre.setPromptText("Nombre del departamento");
            TextField txtCorreo = new TextField();
            txtCorreo.setPromptText("departamento@ucenfotec.ac.cr");
            TextArea txtDescripcion = new TextArea();
            txtDescripcion.setPromptText("Descripción de funciones");
            txtDescripcion.setPrefHeight(80);
            txtDescripcion.setWrapText(true);

            form.add(new Label("Nombre:"), 0, 0);
            form.add(txtNombre, 1, 0);
            form.add(new Label("Correo:"), 0, 1);
            form.add(txtCorreo, 1, 1);
            form.add(new Label("Descripción:"), 0, 2);
            form.add(txtDescripcion, 1, 2);

            Label lblError = new Label();
            lblError.setStyle("-fx-text-fill: #dc2626;");

            HBox buttonBox = new HBox(10);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);

            Button btnCancelar = new Button("Cancelar");
            btnCancelar.setStyle("-fx-background-color: #6b7280; -fx-text-fill: white;");
            btnCancelar.setOnAction(e -> dialog.close());

            Button btnGuardar = new Button("Guardar");
            btnGuardar.setStyle("-fx-background-color: #10b981; -fx-text-fill: white;");
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

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}