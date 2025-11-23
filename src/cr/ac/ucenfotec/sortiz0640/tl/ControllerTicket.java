package cr.ac.ucenfotec.sortiz0640.tl;

import cr.ac.ucenfotec.sortiz0640.bl.logic.GestorApp;
import cr.ac.ucenfotec.sortiz0640.ui.ViewTicket;
import cr.ac.ucenfotec.sortiz0640.util.UI;
import cr.ac.ucenfotec.sortiz0640.util.Validations;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Controlador de gestión de tickets del sistema HelpDesk.
 * Maneja todas las operaciones relacionadas con tickets: creación, listado,
 * eliminación y actualización de estados. Implementa diferentes niveles de permisos
 * según el rol del usuario (operaciones generales vs. administrativas).
 *
 * @author Sebastian Ortiz
 * @version 1.0
 * @since 2025
 */

public class ControllerTicket {

    private UI interfaz;
    private ViewTicket app;
    private GestorApp gestorApp;
    private Validations validator;

    /**
     * Constructor que inicializa el controlador de tickets.
     *
     * @param gestorApp Gestor principal para operaciones de negocio relacionadas con tickets
     */

    public ControllerTicket(GestorApp gestorApp) {
        this.interfaz = new UI();
        this.app = new ViewTicket();
        this.gestorApp = gestorApp;
        this.validator = new Validations();
    }

    /**
     * Inicia el flujo de gestión de tickets.
     * Muestra el menú de tickets y procesa las opciones hasta que el usuario
     * decida regresar al menú principal.
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
     * Procesa la opción seleccionada por el usuario en el menú de gestión de tickets.
     *
     * @param opcion Opción seleccionada del menú
     *               (1: Crear ticket, 2: Mis tickets, 3: Eliminar,
     *                4: Actualizar estado, 5: Tickets por departamento, 0: Regresar)
     * @throws IOException Si ocurre un error durante el procesamiento
     */

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

    /**
     * Crea un nuevo ticket en el sistema.
     * Solicita asunto, descripción y departamento al que se asignará el ticket.
     * Verifica que existan departamentos registrados antes de permitir la creación.
     * Muestra los departamentos disponibles para facilitar la selección.
     * El ticket se asocia automáticamente al usuario de la sesión actual.
     *
     * @throws IOException Si ocurre un error al leer los datos del ticket
     */

    public void crear() throws IOException {
        if (!gestorApp.existenDepartamentos()) {
            interfaz.imprimirMensaje("[INFO] No existen departamentos registrados. Debe registrar al menos un departamento antes de crear tickets\n");
            return;
        }

        String asunto = validator.asunto();
        String descripcion = validator.descripcion();

        interfaz.imprimirMensaje("\n[INFO] Departamentos disponibles:\n");
        mostrarDepartamentos();

        String correoDepartamento = validator.correo();

        String resultado = gestorApp.crearTicket(asunto, descripcion, correoDepartamento);
        interfaz.imprimirMensaje(resultado);
    }

    /**
     * Lista todos los tickets creados por el usuario actual.
     * Muestra un mensaje si el usuario no tiene tickets creados.
     * Esta operación está disponible para todos los usuarios.
     */

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

    /**
     * Lista todos los tickets asignados a un departamento específico.
     * Muestra los departamentos disponibles antes de solicitar el correo.
     * Requiere permisos de administrador para ejecutarse.
     *
     * @throws IOException Si ocurre un error al leer el correo del departamento
     */

    public void listarTicketsPorDepartamento() throws IOException {
        if (!gestorApp.tienePermisosAdmin()) {
            interfaz.imprimirMensaje("[INFO] El usuario no tiene permisos para ejecutar esta opción\n");
            return;
        }

        if (!gestorApp.existenDepartamentos()) {
            interfaz.imprimirMensaje("[INFO] No existen departamentos registrados\n");
            return;
        }

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

    /**
     * Elimina un ticket del sistema por su ID.
     * Muestra todos los tickets antes de solicitar el ID a eliminar.
     * Requiere permisos de administrador para ejecutarse.
     *
     * @throws IOException Si ocurre un error al leer el ID del ticket
     */

    public void eliminar() throws IOException {
        String ticketId = solicitarTicketId();

        if (ticketId == null) {
            return;
        }

        interfaz.imprimirMensaje(gestorApp.eliminarTicket(ticketId));
    }

    /**
     * Actualiza el estado de un ticket existente.
     * Permite cambiar el estado a EN_PROGRESO o RESUELTO.
     * Muestra todos los tickets antes de solicitar el ID a actualizar.
     * Requiere permisos de administrador para ejecutarse.
     *
     * @throws IOException Si ocurre un error al leer el ID del ticket o el nuevo estado
     */

    public void actualizarEstado() throws IOException {
        String ticketId = solicitarTicketId();

        if (ticketId == null) {
            return;
        }

        int estado = validator.estado();
        interfaz.imprimirMensaje(gestorApp.actualizarEstadoTicket(ticketId, estado));
    }

    /**
     * Método auxiliar privado que muestra todos los departamentos registrados.
     * Utilizado para mostrar opciones disponibles al crear o filtrar tickets.
     */

    private void mostrarDepartamentos() {
        ArrayList<String> departamentos = gestorApp.listarTodosDepartamentos();

        for (String departamento : departamentos) {
            interfaz.imprimirMensaje(departamento);
        }
        interfaz.imprimirMensaje("\n");
    }

    /**
     * Método auxiliar privado que muestra todos los tickets registrados en el sistema.
     * Utilizado antes de operaciones administrativas como eliminar o actualizar estado.
     */

    private void mostrarTodosTickets() {
        ArrayList<String> tickets = gestorApp.listarTodosTickets();

        for (String ticket : tickets) {
            interfaz.imprimirMensaje(ticket);
        }

        interfaz.imprimirMensaje("\n");
    }

    /**
     * Método auxiliar privado que solicita y valida el ID de un ticket.
     * Verifica permisos de administrador y existencia de tickets antes de solicitar el ID.
     * Muestra todos los tickets registrados para facilitar la selección.
     *
     * @return ID del ticket ingresado por el usuario, o null si no se puede proceder
     *         (falta de permisos o no hay tickets registrados)
     * @throws IOException Si ocurre un error al leer el ID del ticket
     */

    private String solicitarTicketId() throws IOException {
        if (!gestorApp.tienePermisosAdmin()) {
            interfaz.imprimirMensaje("[INFO] El usuario no tiene permisos para ejecutar esta opción\n");
            return null;
        }

        if (!gestorApp.existenTickets()) {
            interfaz.imprimirMensaje("[INFO] No hay tickets registrados\n");
            return null;
        }

        interfaz.imprimirMensaje("\n[INFO] Tickets registrados:\n");
        mostrarTodosTickets();

        return validator.ticketId();
    }
}