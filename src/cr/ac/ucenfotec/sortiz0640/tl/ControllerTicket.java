package cr.ac.ucenfotec.sortiz0640.tl;

import cr.ac.ucenfotec.sortiz0640.bl.logic.GestorApp;
import cr.ac.ucenfotec.sortiz0640.ui.ViewTicket;
import cr.ac.ucenfotec.sortiz0640.util.UI;
import cr.ac.ucenfotec.sortiz0640.util.Validations;

import java.io.IOException;
import java.util.ArrayList;

public class ControllerTicket {

    private UI interfaz = new UI();
    private ViewTicket app = new ViewTicket();
    private GestorApp gestorApp;
    private Validations validator = new Validations();

    public ControllerTicket(GestorApp gestorApp) {
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
            case 1: crear(); break;
            case 2: listarMisTickets(); break;
            case 3: eliminar(); break;
            case 4: actualizarEstado(); break;
            case 5: listarTicketsPorDepartamento(); break;
            case 0: break;
            default: interfaz.imprimirMensaje("[INFO] Opción no válida. Intente nuevamente! \n");
        }
    }

    public void crear() throws IOException {

        if (!gestorApp.existenDepartamentos()) {
            interfaz.imprimirMensaje("[INFO] No existen departamentos registrados. Debe registrar al menos un departamento antes de crear tickets\n");
            return;
        }

        String asunto = validator.asunto();
        String descripcion = validator.descripcion();

        // Mostrar departamentos disponibles
        interfaz.imprimirMensaje("\n[INFO] Departamentos disponibles:\n");
        mostrarDepartamentos();

        String correoDepartamento = validator.correo();

        String resultado = gestorApp.crearTicket(asunto, descripcion, correoDepartamento);
        interfaz.imprimirMensaje(resultado);
    }

    public void listarMisTickets() {

        String correoUsuario = gestorApp.obtenerCorreoUsuarioActual();
        ArrayList<String> lista = gestorApp.listarTicketsPorUsuario(correoUsuario);

        if (lista == null || lista.isEmpty()) {
            interfaz.imprimirMensaje("[INFO] No tienes tickets creados\n");
            return;
        }

        interfaz.imprimirMensaje("[INFO] Tus tickets:\n");
        for (String ticket : lista) {
            interfaz.imprimirMensaje(ticket);
        }
    }

    public void listarTicketsPorDepartamento() throws IOException {

        if (!gestorApp.tienePermisosAdmin()) {
            interfaz.imprimirMensaje("[INFO] El usuario no tiene permisos para ejecutar esta opción\n");
            return;
        }

        if (!gestorApp.existenDepartamentos()) {
            interfaz.imprimirMensaje("[INFO] No existen departamentos registrados\n");
            return;
        }

        // Mostrar departamentos disponibles
        interfaz.imprimirMensaje("\n[INFO] Departamentos disponibles:\n");
        mostrarDepartamentos();

        String correoDepartamento = validator.correo();
        ArrayList<String> lista = gestorApp.listarTicketsPorDepartamento(correoDepartamento);

        if (lista == null || lista.isEmpty()) {
            interfaz.imprimirMensaje("[INFO] El departamento no tiene tickets asignados\n");
            return;
        }

        interfaz.imprimirMensaje("\n[INFO] Tickets del departamento:\n");
        for (String ticket : lista) {
            interfaz.imprimirMensaje(ticket);
        }
    }

    public void eliminar() throws IOException {

        String ticketId = solicitarTicketId();

        if (ticketId == null) {
            return;
        }

        interfaz.imprimirMensaje(gestorApp.eliminarTicket(ticketId));
    }

    public void actualizarEstado() throws IOException {

        String ticketId = solicitarTicketId();

        if (ticketId == null) {
            return;
        }

        int estado = validator.estado();
        interfaz.imprimirMensaje(gestorApp.actualizarEstadoTicket(ticketId, estado));
    }

    // Métodos auxiliares

    private void mostrarDepartamentos() {
        ArrayList<String> departamentos = gestorApp.listarTodosDepartamentos();

        for (String departamento : departamentos) {
            interfaz.imprimirMensaje(departamento);
        }
        interfaz.imprimirMensaje("\n");
    }

    private void mostrarTodosTickets() {
        ArrayList<String> tickets = gestorApp.listarTodosTickets();

        for (String ticket : tickets) {
            interfaz.imprimirMensaje(ticket);
        }

        interfaz.imprimirMensaje("\n");
    }

    private String solicitarTicketId() throws IOException {

        if (!gestorApp.tienePermisosAdmin()) {
            interfaz.imprimirMensaje("[INFO] El usuario no tiene permisos para ejecutar esta opción\n");
            return null;
        }

        if (!gestorApp.existenTickets()) {
            interfaz.imprimirMensaje("[INFO] No hay tickets registrados\n");
            return null;
        }

        // Mostrar todos los tickets
        interfaz.imprimirMensaje("\n[INFO] Tickets registrados:\n");
        mostrarTodosTickets();

        return validator.ticketId();

    }
}