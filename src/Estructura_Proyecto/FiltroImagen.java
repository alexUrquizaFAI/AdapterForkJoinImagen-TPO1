package Estructura_Proyecto;
//Xime
import java.util.concurrent.RecursiveAction;
public class FiltroImagen extends RecursiveAction {
    private static final int UMBRAL= 100000;
	private final int[] pixeles;
	private final int inicio;
	private final int fin;
	private final TipoFiltro filtro;

    public enum TipoFiltro{INVERTIR, ESCALA_GRIS, BRILLO}

    public FiltroImagen(int[] pixeles, int inicio, int fin, TipoFiltro filtro) {
        this.pixeles = pixeles;
        this.inicio = inicio;
        this.fin = fin;
        this.filtro = filtro;
    }
    //implementar el metodo abstracto compute() de la clase RecursiveAction
    @Override
    protected void compute() {
        if((fin - inicio)<=UMBRAL){
            aplicarFiltro();
        }else{
            int mitad = inicio + ((fin - inicio)/2);
            FiltroImagen tareaIzquierda = new FiltroImagen(pixeles, inicio, mitad, filtro);
            FiltroImagen tareaDerecha = new FiltroImagen(pixeles, mitad, fin, filtro);
            invokeAll(tareaIzquierda, tareaDerecha);
        }
    }
    public void aplicarFiltro(){
        for(int i = inicio; i<fin; i++){
            int argb = pixeles[i];
            //Extraigo el valor de cada color con manipulacion de bits (muy util la vdd)
            // cada color comprende un valor entre 0 y 255, por lo que se puede representar con 8 bits (1 byte)
            int a = (argb>>24) & 0xFF;
            int r = (argb>>16) & 0xFF;
            int g = (argb>>8) & 0xFF;
            int b = argb & 0xFF;

            switch(filtro){
                case INVERTIR:
                    r = 255-r;
                    g= 255-g;
                    b= 255-b;
                    break;
                case ESCALA_GRIS:
                //investigado: porcentaje de percepcion de colores (r=29.9%; g=58.7%; b=11.4%)
                    int gris = (int)((0.299*r)+(0.587*g)+(0.114*b));//Esto es para obtener la tonalidad del gris
                    r=g=b=gris;
                    break;
                case BRILLO:
                //Subir el brillo a cada color intentando no superar el maximo de 255
                //Como caso simple aumentamos el brillo en 40 unidades (solo si no supera el maximo de 255)
                    r = Math.min(255, (r+40));
                    g = Math.min(255, (g+40));
                    b = Math.min(255, (b+40));
                    break;
            }
            //Reconstruimos el valor de color con manipulacion de bits
            pixeles[i] = (a<<24) | (r<<16) | (g<<8) | b;
        }
    }

}
