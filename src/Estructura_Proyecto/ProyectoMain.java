package Estructura_Proyecto;
import java.util.concurrent.ForkJoinPool;
import java.util.Scanner;
public class ProyectoMain {
    public static void main(String[] args) throws Exception {
        //
        Scanner scanner = new Scanner(System.in);
        boolean seguir = true;
        System.out.print("Ingrese la ruta del archivo: ");
        String caminoEntrada = scanner.nextLine();

        String extension = caminoEntrada.substring(caminoEntrada.lastIndexOf('.') + 1).toLowerCase();

        // CORRECCIÓN: Declaramos la variable 'buffer' fuera del if para que sea accesible en todo el método
        ImageBuffer buffer = null;

        if (extension.equals("jpg") || extension.equals("png")) {
            buffer = new AdapterImagenJVM(caminoEntrada);

        } else if (extension.equals("pgm") || extension.equals("txt")) {
            buffer = new AdapterImagenPGM(caminoEntrada);

        } else {
            System.out.println("Formato no soportado.");
            seguir = false;
        }
         if (seguir && buffer != null) { // También añadí una protección extra aquí verificando != null
            System.out.println("Ingrese el filtro que desea aplicar: ");
            System.out.println("1. Invertir colores");
            System.out.println("2. Escala de grises");
            System.out.println("3. Subir brillo (40 unidades)");
            int opcion = scanner.nextInt();
            FiltroImagen.TipoFiltro filtro;
            
            if (opcion == 1) {
                filtro = FiltroImagen.TipoFiltro.INVERTIR;
            }else if (opcion == 2) {
                filtro = FiltroImagen.TipoFiltro.ESCALA_GRIS;
            }else {
                filtro = FiltroImagen.TipoFiltro.BRILLO;
            }
            
            ForkJoinPool pool = new ForkJoinPool();
            pool.invoke(new FiltroImagen(buffer.getPixeles(), 0, buffer.getPixeles().length, filtro));
            buffer.guardar("resultado." + extension);
            System.out.println("Imagen guardada con éxito como: resultado." + extension);
            scanner.close();
            
        }
    }
}
