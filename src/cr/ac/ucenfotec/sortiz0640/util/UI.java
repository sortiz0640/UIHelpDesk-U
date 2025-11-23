package cr.ac.ucenfotec.sortiz0640.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class UI {

    private BufferedReader in;
    private PrintStream out;

    public UI () {
        in = new BufferedReader(new InputStreamReader(System.in));
        out = System.out;
    }

    public String leerTexto() throws IOException {
        return in.readLine();
    }
    public void imprimirMensaje(String msj){
        out.println(msj);
    }

    public int leerOpcion() throws IOException {
        return Integer.parseInt(in.readLine());
    }
}
