package cr.ac.ucenfotec.sortiz0640.tl;

import cr.ac.ucenfotec.sortiz0640.bl.logic.GestorApp;
import cr.ac.ucenfotec.sortiz0640.ui.ViewApp;
import cr.ac.ucenfotec.sortiz0640.util.UI;

import java.io.IOException;

public class ControllerApp {

    private UI interfaz = new UI();
    private ViewApp app = new ViewApp();
    private ControllerUsuario usuario;
    private ControllerTicket ticket;
    private ControllerDepartamento departamento;
    private GestorApp gestorApp;

    public ControllerApp(ControllerUsuario usuario, ControllerTicket ticket,
                         ControllerDepartamento departamento, GestorApp gestorApp) {
        this.usuario = usuario;
        this.ticket = ticket;
        this.departamento = departamento;
        this.gestorApp = gestorApp;
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
            case 1: usuario.start(); break;
            case 2: departamento.start(); break;
            case 3: ticket.start(); break;
            case 0: gestorApp.cerrarSesion(); break;
            default: interfaz.imprimirMensaje("[INFO] Opción no válida. Intente nuevamente! \n");
        }
    }
}