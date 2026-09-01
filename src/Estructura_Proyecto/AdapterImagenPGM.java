package Estructura_Proyecto;


import java.io.File;
//Lo uso para escribir en un archivo
import java.io.FileWriter;
//Lo uso para utilizar metodos que me permitan modificar un archivo de una manera mas sencilla
import java.io.PrintWriter;
//Lo uso para leer un texto
import java.util.Scanner;

public class AdapterImagenPGM implements ImageBuffer.imageBuffer {

    private int[] pixeles;
    private int ancho;
    private int altura;

    //Utilizamos esta varaible para representar el limite maximo de intensidad que puede tomar un pixel
    private int valorColorMax;

    //Se encarga de cargar, validar y adaptar la imagen PGM en nuestro formato interno el cual es el arreglo unidimensional(pixeles)
    public AdapterImagenPGM(String rutaArchivo) throws Exception {
        //Abrimos y leemos el archivo
        Scanner sc = new Scanner(new File(rutaArchivo));
        String numMagico = nextValidToken(sc);

        //"P2" nos indica que el archivo es un mapa de grises guardado en formato ASCII
        if (!numMagico.equals("P2")) {
            sc.close();
            throw new Exception("Formate pgm no valido, se esperaba 'P2'");
        }

        //Leemos el ancho, alto y el valor maximo de gris
        this.ancho = Integer.parseInt(nextValidToken(sc));
        this.altura = Integer.parseInt(nextValidToken(sc));
        this.valorColorMax = Integer.parseInt(nextValidToken(sc));

        this.pixeles = new int[this.altura * this.ancho];

        int gris;
        int alfa;

        //Leemos cada valor de gris y desarrollamos el arreglo unidimensional
        for (int i = 0; i < pixeles.length; i++) {
            gris = Integer.parseInt(nextValidToken(sc));
            //Vamos replicando el valor de gris en los tres canales(Rojo, Verde y Azul)
            alfa = 255;
            this.pixeles[i] = (alfa << 24) | (gris << 16) | (gris << 8) | gris;
        }
        sc.close();
    }

    private String nextValidToken(Scanner sc) {
        String comentario = "";
        String token;

        //Verificamos si existe un elemento o palabra(token) para leer
        while (sc.hasNext() && comentario == "") {

            token = sc.next();
            
            //Verificamos si la cadena de texto guardado en la variable token comienza con "#"
            if (token.startsWith("#")) {
                sc.nextLine();
            } else {
                comentario = token;
            }
        }
        return comentario;
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

    @Override
    public void guardar(String rutaSalida) throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter(rutaSalida));

        //Escribir en la cabecera PGM
        writer.println("P2");
        //Agregamos un comentario
        writer.println("# Archivo generado por procesador paralelo");

        writer.println(this.ancho + " " + this.altura);
        writer.println(this.valorColorMax);

        // Escribir los datos de los píxeles
        int count = 0;
        int argb;
        int gray;
        
        for (int i = 0; i < this.pixeles.length; i++) {
            // Extraer el canal rojo (como es gris, R, G y B son iguales)
            argb = pixeles[i];
            gray = (argb >> 16) & 0xFF; 

            writer.print(gray + " ");

            count++;
            // Salto de línea cada cierto ancho para mantener el formato legible
            if (count % ancho == 0) {
                writer.println();
            }
        }
        writer.close();
    }
}
