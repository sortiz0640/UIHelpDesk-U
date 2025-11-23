package cr.ac.ucenfotec.sortiz0640.ui;
import cr.ac.ucenfotec.sortiz0640.util.UI;

public class ViewApp {

    private UI interfaz = new UI();

    public void mostrarMenu() {
        interfaz.imprimirMensaje("===================================");
        interfaz.imprimirMensaje("HELPDESK U: Menú Principal ");
        interfaz.imprimirMensaje("===================================");
        interfaz.imprimirMensaje("[1] Usuarios");
        interfaz.imprimirMensaje("[2] Departamentos");
        interfaz.imprimirMensaje("[3] Tickets");
        interfaz.imprimirMensaje("[0] Cerrar sesion");
        interfaz.imprimirMensaje("===================================\n");
    }
}
