package cr.ac.ucenfotec.sortiz0640;

import cr.ac.ucenfotec.sortiz0640.bl.logic.GestorApp;
import cr.ac.ucenfotec.sortiz0640.tl.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase principal de la aplicación JavaFX.
 * Inicializa el sistema y muestra la ventana de login.
 */
public class Main extends Application {

    private static GestorApp gestorApp;
    private static ControllerApp controllerApp;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/cr/ac/ucenfotec/sortiz0640/ui/sesion.fxml")
        );
        Parent root = loader.load();

        ControllerSesion controllerSesion = loader.getController();
        controllerSesion.inicializar(gestorApp, null); // null porque no necesitas controllerApp aquí

        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setTitle("Sistema de Tickets - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {
        // 1. Inicializar el gestor de la aplicación
        gestorApp = new GestorApp();

        // 3. Agregar datos iniciales
        gestorApp.agregarUsuario("Administrador", "SYS ADMIN", "admin@ucenfotec.ac.cr", "adminAdmin", 1);
        gestorApp.agregarUsuario("Sebastian", "Ortiz Vargas", "sortiz0640@ucenfotec.ac.cr", "abc123456", 3);

        gestorApp.agregarDepartamento("Escuela de Ingeniería", "Gestión y administración de carreras informáticas", "escuelaingenieria@ucenfotec.ac.cr");
        gestorApp.agregarDepartamento("Escuela de Administración", "Gestión y administración de carreras administrativas", "escuelaadministracion@ucenfotec.ac.cr");

        // 4. Lanzar la aplicación JavaFX
        launch(args);
    }
}