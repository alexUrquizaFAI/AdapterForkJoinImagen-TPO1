package Estructura_Proyecto;

//Interfaz Target (Patron Adapter)
public interface ImageBuffer {
        //Arreglo unidimensional de pixeles que representa la imagen
        int[] getPixeles();
        
        //Ancho de la imagen
        int getAncho();

        //Altura de la imagen
        int getAltura();

        //Metodo para guardar la imagen en un archivo
        void guardar(String rutaSalida) throws Exception;

}
