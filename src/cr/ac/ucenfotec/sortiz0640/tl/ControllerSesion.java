package cr.ac.ucenfotec.sortiz0640.tl;

import cr.ac.ucenfotec.sortiz0640.bl.logic.GestorApp;
import cr.ac.ucenfotec.sortiz0640.ui.ViewSesion;
import cr.ac.ucenfotec.sortiz0640.util.UI;
import cr.ac.ucenfotec.sortiz0640.util.Validations;

import java.io.IOException;

public class ControllerSesion {

    private UI interfaz = new UI();
    private ViewSesion app = new ViewSesion();
    private Validations validator = new Validations();
    private GestorApp gestorApp;
    private ControllerApp controllerApp;

    public ControllerSesion(GestorApp gestorApp, ControllerApp controllerApp) {
        this.gestorApp = gestorApp;
        this.controllerApp = controllerApp;
    }

    public void start() throws IOException {
        int opcion = -1;
        do {
            app.mostrarMenu();
            opcion = interfaz.leerOpcion();
            procesarOpcion(opcion);
        } while (opcion != 0);
    }

    public void procesarOpcion(int opcion) throws IOException {
        switch (opcion) {
            case 1: iniciarSesion(); break;
            case 0: break;
            default: interfaz.imprimirMensaje("[INFO] Opción no válida. Intente nuevamente! \n"); break;
        }
    }

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