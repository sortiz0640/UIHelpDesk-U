package cr.ac.ucenfotec.sortiz0640;

import cr.ac.ucenfotec.sortiz0640.bl.logic.GestorApp;
import cr.ac.ucenfotec.sortiz0640.tl.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Clase principal de la aplicación JavaFX.
 * Inicializa el sistema, carga el gestor principal y muestra la ventana de inicio de sesión.
 *
 * @author Sebastian Ortiz
 * @version 1.0
 * @since 2025
 */
public class Main extends Application {

    private static GestorApp gestorApp;
    private static ControllerApp controllerApp;

    /**
     * Método de inicio de JavaFX.
     * Carga la escena de sesión (login) y muestra el escenario principal.
     *
     * @param primaryStage El escenario principal (ventana) de la aplicación.
     * @throws Exception Si ocurre un error al cargar el FXML.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/cr/ac/ucenfotec/sortiz0640/ui/sesion.fxml")
        );
        Parent root = loader.load();

        ControllerSesion controllerSesion = loader.getController();
        controllerSesion.inicializar(gestorApp, null);

        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setTitle("Sistema de Tickets - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Punto de entrada principal de la aplicación.
     * Inicializa la lógica de negocio (GestorApp) y lanza la interfaz gráfica.
     *
     * @param args Argumentos de línea de comandos.
     * @throws IOException Si hay error de entrada/salida.
     * @throws SQLException Si hay error de conexión a la BD.
     * @throws ClassNotFoundException Si no se encuentran las clases necesarias.
     */
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        gestorApp = new GestorApp();
        launch(args);
    }
}