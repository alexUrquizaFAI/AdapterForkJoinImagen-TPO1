package Estructura_Proyecto;

//Lo uso para almacenar la cuadricula de pixeles y acceder a sus propiedades visuales
import java.awt.image.BufferedImage;
//Lo uso para verificar si el archivo existe y para indicar donde leer o guardar
import java.io.File;
//Lo uso para leer y escribir formatos de imagen estandar como JPG o PNG
import javax.imageio.ImageIO;


public class AdapterImagenJVM implements ImageBuffer { 
    
    //Variable que nos permitira acceder a las propiedades visuales de la imagen
    private BufferedImage imagenOriginal;

    private int[] pixeles;
    private int ancho; 
    private int altura;

    public AdapterImagenJVM(String rutaImagen) throws Exception {
        
        // Cargar la imagen utilizando ImageIO
        File archivoImagen = new File(rutaImagen);
        if (!archivoImagen.exists()) {
            throw new Exception("El archivo de imagen no existe: " + rutaImagen);
        }
        
        //Decodificamos el archivo 
        this.imagenOriginal = ImageIO.read(archivoImagen);

        if (this.imagenOriginal == null) {
            throw new Exception("Formato de imagen no soportado.");
        }

        //Obtenemos el ancho de la imagen original
        this.ancho = this.imagenOriginal.getWidth();
        //Obetenemos la altura de la imagen original
        this.altura = this.imagenOriginal.getHeight();
        //Extraemos los colores de la imagen bidimensional(laImagenOriginal) y lo guardamos en un arrgelo unidimensional
        this.pixeles = this.imagenOriginal.getRGB(0, 0, this.ancho, this.altura, null, 0, this.ancho);
    }

    @Override
    public int[] getPixeles() {
        return this.pixeles;
    }

    @Override
    public int getAncho() {
        return this.ancho;
    }

    @Override
    public int getAltura() {
        return this.altura;
    }

    public void guardar(String rutaSalida) throws Exception {
        //Tomamos el arreglo de pixeles ya modificado y lo devolvemos a la imagen original
        this.imagenOriginal.setRGB(0, 0, this.ancho, this.altura, null, 0, this.ancho);
        //Obtenemos el tipo de archivo, si es png o jpg
        String formato = rutaSalida.substring(rutaSalida.lastIndexOf(".") + 1);
        //Guardamos en el disco
        ImageIO.write(imagenOriginal, formato, new File(rutaSalida));
    }
}
