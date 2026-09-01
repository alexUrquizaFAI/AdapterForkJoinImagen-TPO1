package Estructura_Proyecto;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.Scanner;
public class ProyectoMain {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la ruta del archivo: ");
        String caminoEntrada = scanner.nextLine();

        ImageBuffer.imageBuffer buffer;

        String extension = caminoEntrada.substring(caminoEntrada.lastIndexOf('.') + 1).toLowerCase();

        if (extension.equals("jpg") || extension.equals("png")) {
            buffer = new AdapterImagenJVM(caminoEntrada);

        } else if (extension.equals("pgm") || extension.equals("txt")) {
            buffer = new AdapterImagenPGM(caminoEntrada);

        } else {
            System.out.println("Formato no soportado.");
        }
    }
}
