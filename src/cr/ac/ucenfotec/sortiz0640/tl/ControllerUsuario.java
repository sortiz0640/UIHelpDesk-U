package cr.ac.ucenfotec.sortiz0640.tl;

import cr.ac.ucenfotec.sortiz0640.bl.logic.GestorApp;
import cr.ac.ucenfotec.sortiz0640.ui.ViewUsuario;
import cr.ac.ucenfotec.sortiz0640.util.UI;
import cr.ac.ucenfotec.sortiz0640.util.Validations;

import java.io.IOException;
import java.util.ArrayList;

public class ControllerUsuario {

    private UI interfaz = new UI();
    private ViewUsuario app = new ViewUsuario();
    private GestorApp gestorApp;
    private Validations validator = new Validations();

    public ControllerUsuario(GestorApp gestorApp) {
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
            case 1: registrar(); break;
            case 2: eliminar(); break;
            case 3: listarPorCorreo(); break;
            case 4: listarTodos(); break;
            case 0: break;
            default: interfaz.imprimirMensaje("[INFO] Opción no válida. Intente nuevamente! \n");
        }
    }

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

    public void eliminar() throws IOException {

        if (!gestorApp.tienePermisosAdmin()) {
            interfaz.imprimirMensaje("[INFO] El usuario no tiene permisos para ejecutar esta opción\n");
            return;
        }

        // Mostrar usuarios disponibles
        interfaz.imprimirMensaje("\n[INFO] Usuarios registrados:\n");
        mostrarUsuarios();

        String correo = validator.correo();
        interfaz.imprimirMensaje(gestorApp.eliminarUsuario(correo));
    }

    public void listarPorCorreo() throws IOException {

        if (!gestorApp.tienePermisosAdmin()) {
            interfaz.imprimirMensaje("[INFO] El usuario no tiene permisos para ejecutar esta opción\n");
            return;
        }

        String correo = validator.correo();
        interfaz.imprimirMensaje("\n" + gestorApp.listarUsuarioPorCorreo(correo));
    }

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