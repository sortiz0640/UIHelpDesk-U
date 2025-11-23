package cr.ac.ucenfotec.sortiz0640.tl;

import cr.ac.ucenfotec.sortiz0640.bl.logic.GestorApp;
import cr.ac.ucenfotec.sortiz0640.ui.ViewApp;
import cr.ac.ucenfotec.sortiz0640.util.UI;
import java.io.IOException;

/**
 * Controlador principal de la aplicación HelpDesk.
 * Coordina la navegación entre los diferentes módulos del sistema (Usuarios, Departamentos, Tickets).
 * Actúa como menú principal después de la autenticación exitosa del usuario.
 *
 * @author Sebastian Ortiz
 * @version 1.0
 * @since 2025
 */

public class ControllerApp {

    private UI interfaz;
    private ViewApp app;
    private ControllerUsuario usuario;
    private ControllerTicket ticket;
    private ControllerDepartamento departamento;
    private GestorApp gestorApp;

    /**
     * Constructor que inicializa el controlador principal con sus dependencias.
     * Establece las conexiones con los controladores especializados y el gestor de aplicación.
     *
     * @param usuario Controlador del módulo de gestión de usuarios
     * @param ticket Controlador del módulo de gestión de tickets
     * @param departamento Controlador del módulo de gestión de departamentos
     * @param gestorApp Gestor principal para operaciones de negocio
     */

    public ControllerApp(ControllerUsuario usuario, ControllerTicket ticket,
                         ControllerDepartamento departamento, GestorApp gestorApp) {
        this.interfaz = new UI();
        this.app = new ViewApp();
        this.usuario = usuario;
        this.ticket = ticket;
        this.departamento = departamento;
        this.gestorApp = gestorApp;
    }

    /**
     * Inicia el flujo principal de la aplicación.
     * Muestra el menú principal y procesa las opciones del usuario
     * hasta que decida cerrar sesión (opción 0).
     *
     * @throws IOException Si ocurre un error de entrada/salida durante la navegación
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
     * Procesa la opción seleccionada por el usuario en el menú principal.
     * Delega el control al controlador especializado correspondiente según la opción.
     *
     * @param opcion Opción seleccionada del menú
     *               (1: Gestión de Usuarios, 2: Gestión de Departamentos,
     *                3: Gestión de Tickets, 0: Cerrar sesión)
     * @throws IOException Si ocurre un error durante el procesamiento
     */

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