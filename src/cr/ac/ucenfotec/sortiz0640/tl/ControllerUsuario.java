package cr.ac.ucenfotec.sortiz0640.tl;

import cr.ac.ucenfotec.sortiz0640.bl.logic.GestorApp;
import cr.ac.ucenfotec.sortiz0640.ui.ViewUsuario;
import cr.ac.ucenfotec.sortiz0640.util.UI;
import cr.ac.ucenfotec.sortiz0640.util.Validations;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Controlador de gestión de usuarios del sistema.
 * Maneja todas las operaciones relacionadas con usuarios: registro, eliminación,
 * consultas y listados. Valida permisos administrativos para operaciones protegidas.
 *
 * @author Sebastian Ortiz
 * @version 1.0
 * @since 2025
 */

public class ControllerUsuario {

    private UI interfaz;
    private ViewUsuario app;
    private GestorApp gestorApp;
    private Validations validator;

    /**
     * Constructor que inicializa el controlador de usuarios.
     *
     * @param gestorApp Gestor principal para operaciones de negocio relacionadas con usuarios
     */

    public ControllerUsuario(GestorApp gestorApp) {
        this.interfaz = new UI();
        this.app = new ViewUsuario();
        this.gestorApp = gestorApp;
        this.validator = new Validations();
    }

    /**
     * Inicia el flujo de gestión de usuarios.
     * Muestra el menú de usuarios y procesa las opciones hasta que el usuario
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
     * Procesa la opción seleccionada por el usuario en el menú de gestión de usuarios.
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
     * Registra un nuevo usuario en el sistema.
     * Solicita y valida todos los datos necesarios (nombre, apellidos, correo, contraseña, rol).
     * Requiere permisos de administrador para ejecutarse.
     *
     * @throws IOException Si ocurre un error al leer los datos del usuario
     */

    public void registrar() throws IOException {
        if (!gestorApp.tienePermisosAdmin()) {
            interfaz.imprimirMensaje("[INFO] El usuario no tiene permisos para ejecutar esta opción\n");
            return;
        }

        String nombre = validator.nombre();
        String apellidos = validator.apellidos();
        String correo = validator.correo();
        String password = validator.password();
        int rol = validator.rol();

        interfaz.imprimirMensaje(gestorApp.agregarUsuario(nombre, apellidos, correo, password, rol));
    }

    /**
     * Elimina un usuario del sistema por su correo electrónico.
     * Muestra la lista de usuarios registrados antes de solicitar el correo a eliminar.
     * Requiere permisos de administrador para ejecutarse.
     *
     * @throws IOException Si ocurre un error al leer el correo del usuario
     */

    public void eliminar() throws IOException {
        if (!gestorApp.tienePermisosAdmin()) {
            interfaz.imprimirMensaje("[INFO] El usuario no tiene permisos para ejecutar esta opción\n");
            return;
        }

        interfaz.imprimirMensaje("\n[INFO] Usuarios registrados:\n");
        mostrarUsuarios();

        String correo = validator.correo();
        interfaz.imprimirMensaje(gestorApp.eliminarUsuario(correo));
    }

    /**
     * Lista la información de un usuario específico por su correo electrónico.
     * Requiere permisos de administrador para ejecutarse.
     *
     * @throws IOException Si ocurre un error al leer el correo del usuario
     */

    public void listarPorCorreo() throws IOException {
        if (!gestorApp.tienePermisosAdmin()) {
            interfaz.imprimirMensaje("[INFO] El usuario no tiene permisos para ejecutar esta opción\n");
            return;
        }

        String correo = validator.correo();
        interfaz.imprimirMensaje("\n" + gestorApp.listarUsuarioPorCorreo(correo));
    }

    /**
     * Lista todos los usuarios registrados en el sistema.
     * Muestra un mensaje si no hay usuarios registrados.
     * Requiere permisos de administrador para ejecutarse.
     */

    public void listarTodos() {
        if (!gestorApp.tienePermisosAdmin()) {
            interfaz.imprimirMensaje("[INFO] El usuario no tiene permisos para ejecutar esta opción\n");
            return;
        }

        ArrayList<String> lista = gestorApp.listarTodosUsuarios();

        if (lista == null || lista.isEmpty()) {
            interfaz.imprimirMensaje("[INFO] No existen usuarios registrados\n");
            return;
        }

        interfaz.imprimirMensaje("[INFO] Lista de usuarios registrados:\n");
        for (String usuario : lista) {
            interfaz.imprimirMensaje(usuario);
        }
    }

    /**
     * Método auxiliar privado que muestra todos los usuarios registrados.
     * Utilizado internamente para mostrar usuarios antes de operaciones como eliminación.
     */

    private void mostrarUsuarios() {
        ArrayList<String> usuarios = gestorApp.listarTodosUsuarios();

        if (usuarios == null || usuarios.isEmpty()) {
            interfaz.imprimirMensaje("[INFO] No hay usuarios registrados\n");
            return;
        }

        for (String usuario : usuarios) {
            interfaz.imprimirMensaje(usuario);
        }
        interfaz.imprimirMensaje("\n");
    }
}