package cr.ac.ucenfotec.sortiz0640.tl;

import cr.ac.ucenfotec.sortiz0640.bl.logic.GestorApp;
import cr.ac.ucenfotec.sortiz0640.ui.ViewDepartamento;
import cr.ac.ucenfotec.sortiz0640.util.UI;
import cr.ac.ucenfotec.sortiz0640.util.Validations;

import java.io.IOException;
import java.util.ArrayList;

public class ControllerDepartamento {

    private UI interfaz = new UI();
    private ViewDepartamento app = new ViewDepartamento();
    private GestorApp gestorApp;
    private Validations validator = new Validations();

    public ControllerDepartamento(GestorApp gestorApp) {
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
        String descripcion = validator.descripcion();
        String correo = validator.correo();

        interfaz.imprimirMensaje(gestorApp.agregarDepartamento(nombre, descripcion, correo));
    }

    public void eliminar() throws IOException {

        if (!gestorApp.tienePermisosAdmin()) {
            interfaz.imprimirMensaje("[INFO] El usuario no tiene permisos para ejecutar esta opción\n");
            return;
        }

        // Mostrar departamentos disponibles
        interfaz.imprimirMensaje("\n[INFO] Departamentos registrados:\n");
        mostrarDepartamentos();

        String correo = validator.correo();
        interfaz.imprimirMensaje(gestorApp.eliminarDepartamento(correo));
    }

    public void listarPorCorreo() throws IOException {

        if (!gestorApp.tienePermisosAdmin()) {
            interfaz.imprimirMensaje("[INFO] El usuario no tiene permisos para ejecutar esta opción\n");
            return;
        }

        String correo = validator.correo();
        interfaz.imprimirMensaje("\n" + gestorApp.listarDepartamentoPorCorreo(correo));
    }

    public void listarTodos() {

        ArrayList<String> lista = gestorApp.listarTodosDepartamentos();

        if (lista == null || lista.isEmpty()) {
            interfaz.imprimirMensaje("[INFO] No existen departamentos registrados\n");
            return;
        }

        interfaz.imprimirMensaje("[INFO] Lista de departamentos:\n");
        for (String departamento : lista) {
            interfaz.imprimirMensaje(departamento);
        }
    }


    private void mostrarDepartamentos() {
        ArrayList<String> departamentos = gestorApp.listarTodosDepartamentos();

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