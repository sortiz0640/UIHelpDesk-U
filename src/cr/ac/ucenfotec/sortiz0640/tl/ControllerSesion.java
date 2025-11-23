package cr.ac.ucenfotec.sortiz0640.tl;

import cr.ac.ucenfotec.sortiz0640.bl.logic.GestorApp;
import cr.ac.ucenfotec.sortiz0640.ui.ViewSesion;
import cr.ac.ucenfotec.sortiz0640.util.UI;
import cr.ac.ucenfotec.sortiz0640.util.Validations;
import java.io.IOException;

/**
 * Controlador de autenticación y gestión de sesión del sistema.
 * Maneja el inicio de sesión de usuarios y coordina el acceso a la aplicación principal.
 * Actúa como punto de entrada al sistema después de la autenticación exitosa.
 *
 * @author Sebastian Ortiz
 * @version 1.0
 * @since 2025
 */

public class ControllerSesion {

    private UI interfaz;
    private ViewSesion app;
    private Validations validator;
    private GestorApp gestorApp;
    private ControllerApp controllerApp;

    /**
     * Constructor que inicializa el controlador de sesión.
     * Configura las dependencias necesarias para la autenticación y navegación.
     *
     * @param gestorApp Gestor principal de la aplicación para operaciones de negocio
     * @param controllerApp Controlador de la aplicación principal al que se accede tras login
     */

    public ControllerSesion(GestorApp gestorApp, ControllerApp controllerApp) {
        this.interfaz = new UI();
        this.app = new ViewSesion();
        this.validator = new Validations();
        this.gestorApp = gestorApp;
        this.controllerApp = controllerApp;
    }

    /**
     * Inicia el flujo de autenticación del sistema.
     * Muestra el menú de inicio de sesión y procesa las opciones del usuario
     * hasta que decida salir o se autentique exitosamente.
     *
     * @throws IOException Si ocurre un error de entrada/salida durante la interacción
     */

    public void start() throws IOException {
        int opcion = -1;
        do {
            app.mostrarMenu();
            opcion = interfaz.leerOpcion();
            procesarOpcion(opcion);
        } while (opcion != 0);
    }

    /**
     * Procesa la opción seleccionada por el usuario en el menú de sesión.
     *
     * @param opcion Opción seleccionada (1: Iniciar sesión, 0: Salir)
     * @throws IOException Si ocurre un error durante el procesamiento
     */

    public void procesarOpcion(int opcion) throws IOException {
        switch (opcion) {
            case 1: iniciarSesion(); break;
            case 0: break;
            default: interfaz.imprimirMensaje("[INFO] Opción no válida. Intente nuevamente! \n"); break;
        }
    }

    /**
     * Gestiona el proceso de inicio de sesión del usuario.
     * Solicita credenciales (correo y contraseña), valida la autenticación
     * y da acceso a la aplicación principal si las credenciales son correctas.
     * En caso exitoso, transfiere el control al ControllerApp.
     *
     * @throws IOException Si ocurre un error al leer las credenciales o iniciar la aplicación
     */

    public void iniciarSesion() throws IOException {
        String correo = validator.correo();
        String password = validator.password();

        boolean estado = gestorApp.iniciarSesion(correo, password);

        if (!estado) {
            interfaz.imprimirMensaje("[INFO] El usuario o la contraseña no son correctos. Intente nuevamente\n");
            return;
        }

        interfaz.imprimirMensaje("[INFO] Sesión iniciada correctamente\n");

        controllerApp.start();
    }
}