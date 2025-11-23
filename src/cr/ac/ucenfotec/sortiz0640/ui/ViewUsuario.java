package cr.ac.ucenfotec.sortiz0640.ui;

import cr.ac.ucenfotec.sortiz0640.util.UI;

public class ViewUsuario {

    private UI interfaz = new UI();

    public void mostrarMenu() {
        interfaz.imprimirMensaje("===================================");
        interfaz.imprimirMensaje("HELPDESK U: USUARIOS ");
        interfaz.imprimirMensaje("===================================");
        interfaz.imprimirMensaje("[1] Registrar ");
        interfaz.imprimirMensaje("[2] Eliminar");
        interfaz.imprimirMensaje("[3] Listar por Correo ");
        interfaz.imprimirMensaje("[4] Listar Todos");
        interfaz.imprimirMensaje("[0] Regresar");
        interfaz.imprimirMensaje("===================================\n");
    }
}
