package Estructura_Proyecto;
//Xime
import java.util.concurrent.RecursiveAction;

public class FiltroImagen extends RecursiveAction {
    private static final int UMBRAL = 100000;
	private final int[] pixeles;
	private final int inicio;
	private final int fin;
	private final TipoFiltro filtro;

    public enum TipoFiltro{INVERTIR, ESCALA_GRIS, BRILLO}

    public FiltroImagen(int[] pixeles, int inicio, int fin, TipoFiltro filtro) {
        //Inicializamos las variables de instancia con los valores recibidos en el constructor
        this.pixeles = pixeles;
        this.inicio = inicio;
        this.fin = fin;
        this.filtro = filtro;
    }
    //implementar el metodo abstracto compute() de la clase RecursiveAction
    @Override
    protected void compute() {
        //Si el tamaño de la imagen es menor o igual que el umbral, aplicamos el filtro
        if((fin - inicio) <= UMBRAL){
            aplicarFiltro();
        }else{
            //Sino, dividimos la tarea en dos sub-tareas y las ejecutamos en paralelo
            //Calculamos la mitad del tamaño de la imagen
            int mitad = inicio + ((fin - inicio) / 2);
            //Creamos dos tareas para aplicar el filtro a cada parte de la imagen
            //La primera tarea aplicará el filtro a la parte izquierda de la imagen desde el inicio hasta la mitad
            FiltroImagen tareaIzquierda = new FiltroImagen(pixeles, inicio, mitad, filtro);
            //La segunda tarea aplicará el filtro a la parte derecha de la imagen desde la mitad hasta el fin
            FiltroImagen tareaDerecha = new FiltroImagen(pixeles, mitad, fin, filtro);
            //Invocamos las dos tareas en paralelo
            invokeAll(tareaIzquierda, tareaDerecha);
        }
    }
    public void aplicarFiltro(){
        //Para cada pixel de la imagen, extraemos el valor de cada color (rojo, verde y azul) y aplicamos el filtro correspondiente
        for(int i = inicio; i < fin; i++){
            int argb = pixeles[i];
            //Extraigo el valor de cada color con manipulacion de bits (muy util la vdd)
            //cada color comprende un valor entre 0 y 255, por lo que se puede representar con 8 bits (1 byte)
            //Para obtener a hacemos un desplazamiento de 24 bits hacia la derecha (>>24) y obtenemos los ultimos 8 bits (0xFF)
            int a = (argb >> 24) & 0xFF;
            //Para obtener r hacemos un desplazamiento de 16 bits hacia la derecha (>>16) y obtenemos los ultimos 8 bits (0xFF)
            int r = (argb >> 16) & 0xFF;
            //Para obtener g hacemos un desplazamiento de 8 bits hacia la derecha (>>8) y obtenemos los ultimos 8 bits (0xFF)
            int g = (argb >> 8) & 0xFF;
            //Para obtener b hacemos un desplazamiento de 0 bits hacia la derecha (&) y obtenemos los ultimos 8 bits (0xFF)
            int b = argb & 0xFF;

            //Segun el filtro seleccionado, aplicamos la transformacion correspondiente 
            switch(filtro){
                case INVERTIR:
                    //Como 255 equivale al color blanco y 0 al color negro, invertir el negro es equivalente a restarle 0 a 255 (valor máximo) y obtener el color blanco
                    //y asi con todos los colores
                    r = 255 - r;
                    g = 255 - g;
                    b = 255 - b;
                    break;
                case ESCALA_GRIS:
                //investigado: porcentaje de percepcion de colores (r=29.9%; g=58.7%; b=11.4%)
                    //Calculamos la media de cada color para obtener la tonalidad del gris
                    //Como el resultado es real, lo convertimos a entero para evitar problemas de precisión
                    int gris = (int) ( (0.299 * r) + (0.587 * g) + (0.114 * b));//Esto es para obtener la tonalidad del gris
                    //Ahora todos los colores se ponen en el mismo valor de gris
                    r= g = b = gris;
                    break;
                case BRILLO:
                //Subir el brillo a cada color intentando no superar el maximo de 255
                //Como caso simple aumentamos el brillo en 40 unidades (solo si no supera el maximo de 255)
                //Math.min() es una funcion que devuelve el menor valor entre dos valores
                    r = Math.min(255, (r + 40));
                    g = Math.min(255, (g + 40));
                    b = Math.min(255, (b + 40));
                    break;
            }
            //Reconstruimos el valor de color con manipulacion de bits
            // "|" es el operador OR a nivel de bits, que nos permite combinar los valores de cada color en un solo valor ARGB
            pixeles[i] = (a << 24) | (r << 16) | (g << 8) | b;
        }
    }

}
