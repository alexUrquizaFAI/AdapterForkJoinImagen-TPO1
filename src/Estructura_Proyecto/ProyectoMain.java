package Estructura_Proyecto;
import java.util.concurrent.ForkJoinPool;
import java.util.Scanner;
public class ProyectoMain {
    public static void main(String[] args) throws Exception {
        //
        Scanner scanner = new Scanner(System.in);
        //Utilizamos esta variable para controlar si el programa debe continuar o no, dependiendo de si el formato de la imagen es soportado
        boolean seguir = true;
        System.out.print("Ingrese la ruta del archivo: ");
        String caminoEntrada = scanner.nextLine();
        //Esta variable podria ser .jpg, .png o .pgm
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
            //Como no sabemos el formato de la imagen, no podemos aplicar filtros a ella
            seguir = false;
        }
         if (seguir && buffer != null) { // También añadí una protección extra aquí verificando != null
            System.out.println("Ingrese el filtro que desea aplicar: ");
            System.out.println("1. Invertir colores");
            System.out.println("2. Escala de grises");
            System.out.println("3. Subir brillo (40 unidades)");
            int opcion = scanner.nextInt();
            //Creamos una variable de tipo FiltroImagen.TipoFiltro
            //Al declaralo de esta manera, esta variable puede tomar cualquiera de los valores definidos en el enum TipoFiltro de la clase FiltroImagen
            //Debemos declararlo como FiltroImagen.TipoFiltro para que el compilador sepa que estamos haciendo referencia al enum TipoFiltro que está dentro de la clase FiltroImagen
            FiltroImagen.TipoFiltro filtro;
            
            if (opcion == 1) {
                //Si el usuario elige la opción 1, aplicamos el filtro de invertir colores
                filtro = FiltroImagen.TipoFiltro.INVERTIR;
            }else if (opcion == 2) {
                //Si el usuario elige la opción 2, aplicamos el filtro de escala de grises
                filtro = FiltroImagen.TipoFiltro.ESCALA_GRIS;
            }else {
                //Si el usuario elige la opción 3, aplicamos el filtro de subir brillo
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
