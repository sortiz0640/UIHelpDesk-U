package cr.ac.ucenfotec.sortiz0640.ui;

import cr.ac.ucenfotec.sortiz0640.util.UI;

public class ViewSesion {

    private UI interfaz = new UI();

    public void mostrarMenu() {
        interfaz.imprimirMensaje("===================================");
        interfaz.imprimirMensaje("HELPDESK U: BIENVENIDO ");
        interfaz.imprimirMensaje("===================================");
        interfaz.imprimirMensaje("[1] Iniciar Sesion ");
        interfaz.imprimirMensaje("[0] Salir del programa ");
        interfaz.imprimirMensaje("===================================\n");
    }
}
