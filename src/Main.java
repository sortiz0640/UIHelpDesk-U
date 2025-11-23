import cr.ac.ucenfotec.sortiz0640.bl.logic.GestorApp;
import cr.ac.ucenfotec.sortiz0640.tl.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        GestorApp app = new GestorApp();

        app.agregarUsuario("Administrador", "SYS ADMIN", "admin@ucenfotec.ac.cr", "adminAdmin", 1);
        app.agregarUsuario("Sebastian", "Ortiz Vargas", "sortiz0640@ucenfotec.ac.cr", "abc123456", 3);

        app.agregarDepartamento("Escuela de Ingeniería", "Gestión y administración de carreras informáticas", "escuelaingenieria@ucenfotec.ac.cr");
        app.agregarDepartamento("Escuela de Administración", "Gestión y administración de carreras administrativas", "escuelaadministracion@ucenfotec.ac.cr");

        ControllerUsuario controllerUsuario = new ControllerUsuario(app);
        ControllerDepartamento controllerDepartamento = new ControllerDepartamento(app);
        ControllerTicket controllerTicket = new ControllerTicket(app);
        ControllerApp controllerApp = new ControllerApp(controllerUsuario, controllerTicket, controllerDepartamento, app);
        ControllerSesion controllerSesion = new ControllerSesion(app, controllerApp);

        controllerSesion.start();
    }
}