package cr.ac.ucenfotec.sortiz0640.tl;

import cr.ac.ucenfotec.sortiz0640.bl.logic.GestorApp;
import cr.ac.ucenfotec.sortiz0640.ui.ViewDepartamento;
import cr.ac.ucenfotec.sortiz0640.util.UI;
import cr.ac.ucenfotec.sortiz0640.util.Validations;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Controlador de gestión de departamentos del sistema.
 * Maneja todas las operaciones relacionadas con departamentos: registro, eliminación,
 * consultas y listados. Valida permisos administrativos para la mayoría de operaciones.
 *
 * @author Sebastian Ortiz
 * @version 1.0
 * @since 2025
 */

public class ControllerDepartamento {

    private UI interfaz;
    private ViewDepartamento app;
    private GestorApp gestorApp;
    private Validations validator;

    /**
     * Constructor que inicializa el controlador de departamentos.
     *
     * @param gestorApp Gestor principal para operaciones de negocio relacionadas con departamentos
     */

    public ControllerDepartamento(GestorApp gestorApp) {
        this.interfaz = new UI();
        this.app = new ViewDepartamento();
        this.gestorApp = gestorApp;
        this.validator = new Validations();
    }

    /**
     * Inicia el flujo de gestión de departamentos.
     * Muestra el menú de departamentos y procesa las opciones hasta que el usuario
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
     * Procesa la opción seleccionada por el usuario en el menú de gestión de departamentos.
     *
     * @param opcion Opción seleccionada del menú
     *               (1: Registrar, 2: Eliminar, 3: Buscar por correo,
     *                4: Listar todos, 0: Regresar)
     * @throws IOException Si ocurre un error durante el procesamiento
     */

    public void procesarOpcion(int opcion) throws IOException {
        switch (opcion) {
            case 1: registrar(); break;
            case 2: eliminar(); break;
            case 3: listarPorCorreo(); break;
            case 4: listarTodos(); break;
            case 0: break;
            default: interfaz.imprimirMensaje("[INFO] Opción no válida. Intente nuevamente! \n");
        }
    }

    /**
     * Registra un nuevo departamento en el sistema.
     * Solicita y valida todos los datos necesarios (nombre, descripción, correo).
     * Requiere permisos de administrador para ejecutarse.
     *
     * @throws IOException Si ocurre un error al leer los datos del departamento
     */

    public void registrar() throws IOException {
        if (!gestorApp.tienePermisosAdmin()) {
            interfaz.imprimirMensaje("[INFO] El usuario no tiene permisos para ejecutar esta opción\n");
            return;
        }

        String nombre = validator.nombre();
        String descripcion = validator.descripcion();
        String correo = validator.correo();

        interfaz.imprimirMensaje(gestorApp.agregarDepartamento(nombre, descripcion, correo));
    }

    /**
     * Elimina un departamento del sistema por su correo electrónico.
     * También elimina todos los tickets asociados al departamento (eliminación en cascada).
     * Muestra la lista de departamentos registrados antes de solicitar el correo a eliminar.
     * Requiere permisos de administrador para ejecutarse.
     *
     * @throws IOException Si ocurre un error al leer el correo del departamento
     */

    public void eliminar() throws IOException {
        if (!gestorApp.tienePermisosAdmin()) {
            interfaz.imprimirMensaje("[INFO] El usuario no tiene permisos para ejecutar esta opción\n");
            return;
        }

        interfaz.imprimirMensaje("\n[INFO] Departamentos registrados:\n");
        mostrarDepartamentos();

        String correo = validator.correo();
        interfaz.imprimirMensaje(gestorApp.eliminarDepartamento(correo));
    }

    /**
     * Lista la información de un departamento específico por su correo electrónico.
     * Requiere permisos de administrador para ejecutarse.
     *
     * @throws IOException Si ocurre un error al leer el correo del departamento
     */

    public void listarPorCorreo() throws IOException {
        if (!gestorApp.tienePermisosAdmin()) {
            interfaz.imprimirMensaje("[INFO] El usuario no tiene permisos para ejecutar esta opción\n");
            return;
        }

        String correo = validator.correo();
        interfaz.imprimirMensaje("\n" + gestorApp.listarDepartamentoPorCorreo(correo));
    }

    /**
     * Lista todos los departamentos registrados en el sistema.
     * Muestra un mensaje si no hay departamentos registrados.
     * Esta operación está disponible para todos los usuarios.
     */

    public void listarTodos() {
        ArrayList<String> lista = gestorApp.obtenerDepartamentos();

        if (lista == null || lista.isEmpty()) {
            interfaz.imprimirMensaje("[INFO] No existen departamentos registrados\n");
            return;
        }

        interfaz.imprimirMensaje("[INFO] Lista de departamentos:\n");
        for (String departamento : lista) {
            interfaz.imprimirMensaje(departamento);
        }
    }

    /**
     * Método auxiliar privado que muestra todos los departamentos registrados.
     * Utilizado internamente para mostrar departamentos antes de operaciones como eliminación.
     */

    private void mostrarDepartamentos() {
        ArrayList<String> departamentos = gestorApp.obtenerDepartamentos();

        if (departamentos == null || departamentos.isEmpty()) {
            interfaz.imprimirMensaje("[INFO] No hay departamentos registrados\n");
            return;
        }

        for (String departamento : departamentos) {
            interfaz.imprimirMensaje(departamento);
        }
        interfaz.imprimirMensaje("\n");
    }
}