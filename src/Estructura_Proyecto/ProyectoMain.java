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

        //Declaramos la variable 'buffer' fuera del if para que sea accesible en todo el método
        //No sabemos el formato de la imagen, por lo que necesitamos un buffer que se adapte a cada formato
        ImageBuffer buffer = null;

        if (extension.equals("jpg") || extension.equals("png")) {
            //Si el archivo es de formato JPG o PNG, implementamos el AdapterImagenJVM para adaptarlo a nuestro formato interno
            buffer = new AdapterImagenJVM(caminoEntrada);

        } else if (extension.equals("pgm") || extension.equals("txt")) {
            //Si el archivo es de formato PGM, implementamos el AdapterImagenPGM para adaptarlo a nuestro formato interno
            buffer = new AdapterImagenPGM(caminoEntrada);

        } else {
            //Si el archivo no es de formato JPG, PNG o PGM, mostramos un mensaje de error
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
            //Creamos un pool de hilos para procesar la imagen en paralelo
            ForkJoinPool pool = new ForkJoinPool();
            //Creamos una tarea para aplicar el filtro a la imagen y la invocamos en el pool de hilos
            pool.invoke(new FiltroImagen(buffer.getPixeles(), 0, buffer.getPixeles().length, filtro));
            //Guardamos la imagen modificada en un archivo de salida con el mismo formato que el de entrada
            buffer.guardar("resultado." + extension);
            System.out.println("Imagen guardada con éxito como: resultado." + extension);
            scanner.close();
        }
    }
}
