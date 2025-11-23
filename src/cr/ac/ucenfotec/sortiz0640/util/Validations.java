package cr.ac.ucenfotec.sortiz0640.util;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;

public class Validations {

    private UI interfaz = new UI();
    EmailValidator validator = EmailValidator.getInstance();

    public String correo() throws IOException {
        String correo = "";
        do {

            interfaz.imprimirMensaje("Ingrese el correo [ucenfotec.ac.cr] : ");
            correo = interfaz.leerTexto();

            if (!validator.isValid(correo) || correo.isBlank()) {
                interfaz.imprimirMensaje("El correo no puede estar vacio y debe cumplir con el formato indicado ");
            }
        } while (correo == null || correo.isBlank() || !validator.isValid(correo));

        return correo;
    }

    public String password() throws IOException {

        String password = "";
        do {
            interfaz.imprimirMensaje("Ingrese su password [mínimo 8 caracteres]");
            password = interfaz.leerTexto();

            if (password == null || password.isBlank() || password.length() < 8) {
                interfaz.imprimirMensaje("El password no puede estar vacio [mínimo 8 caracteres] . ");
            }

        } while (password == null || password.isBlank() || password.length() < 8);

        return password;
    }

    public String nombre() throws IOException {

        String nombre;

        do {

            interfaz.imprimirMensaje("Ingrese el nombre: ");
            nombre = interfaz.leerTexto();

            if (nombre == null ||  nombre.isBlank()) {
                interfaz.imprimirMensaje("[ERR] El nombre no puede estar vacio. ");
            }

        } while (nombre == null ||  nombre.isBlank());

        return nombre;
    }

    public String apellidos() throws IOException {

        String nombre;

        do {

            interfaz.imprimirMensaje("Ingrese los apellidos [Ejem: Ortiz Vargas]");
            nombre = interfaz.leerTexto();

            if (nombre == null ||  nombre.isBlank()) {
                interfaz.imprimirMensaje("[ERR] El nombre no puede estar vacio. ");
            }

        } while (nombre == null ||  nombre.isBlank());

        return nombre;
    }

    public int rol() throws IOException {
        int rol;
        do {

            interfaz.imprimirMensaje("Seleccione un rol [1 = ADMIN ] [2 = ESTUDIANTE ] [3 = FUNCIONARIO ]  ");
            rol = interfaz.leerOpcion();

            if (rol < 1 || rol > 3) {
                interfaz.imprimirMensaje("[ERR] Opción invalida. Intente nuevamente ");
            }
        } while (rol < 1 || rol > 3);
        return rol;
    }

    public String descripcion() throws IOException {
        String descripcion = "";

        do {
            interfaz.imprimirMensaje("Ingrese la descripción: ");
            descripcion = interfaz.leerTexto();

            if (descripcion == null ||  descripcion.isBlank()) {
                interfaz.imprimirMensaje("[ERR] El nombre no puede estar vacio. ");
            }

        } while (descripcion == null ||  descripcion.isBlank());
        return descripcion;
    }

    public String asunto() throws IOException {
        String asunto = "";

        do {
            interfaz.imprimirMensaje("Ingrese el asunto: ");
            asunto = interfaz.leerTexto();

            if (asunto == null ||  asunto.isBlank()) {
                interfaz.imprimirMensaje("[ERR] El asunto no puede estar vacio. ");
            }

        } while (asunto == null ||  asunto.isBlank());

        return asunto;
    }

    public String ticketId() throws IOException {

        String nombre;

        do {

            interfaz.imprimirMensaje("Ingrese la identificación del ticket: ");
            nombre = interfaz.leerTexto();

            if (nombre == null ||  nombre.isBlank()) {
                interfaz.imprimirMensaje("[ERR] El id no puede estar vacio. ");
            }

        } while (nombre == null ||  nombre.isBlank());

        return nombre;
    }

    public int estado() throws IOException {
        int estado;
        do {

            interfaz.imprimirMensaje("Seleccione un estado [1 - EN PROGRESO ][2 - RESUELTO ]");
            estado = interfaz.leerOpcion();

            if (estado < 1 || estado > 3) {
                interfaz.imprimirMensaje("[ERR] Opción invalida. Intente nuevamente ");
            }
        } while (estado < 1 || estado > 3);

        return estado;
    }

}
