package Estructura_Proyecto;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class AdapterImagenPGM implements ImageBuffer.imageBuffer {
    private int[] pixeles;
    private int ancho;
    private int altura;
    private int valorColorMax;

    public AdapterImagenPGM(String rutaArchivo) throws Exception {
        Scanner sc = new Scanner(new File(rutaArchivo));
        String numMagico = nextValidToken(sc);
        if (!numMagico.equals("P2")) {
            sc.close();
            throw new Exception("Formate pgm no valido, se esperaba 'P2'");
        }
        this.ancho = Integer.parseInt(nextValidToken(sc));
        this.altura = Integer.parseInt(nextValidToken(sc));
        this.valorColorMax = Integer.parseInt(nextValidToken(sc));
        this.pixeles = new int[this.altura * this.ancho];
        int gris;
        int alfa;
        for (int i = 0; i < pixeles.length; i++) {
            gris = Integer.parseInt(nextValidToken(sc));
            alfa = 255;
            this.pixeles[i] = (alfa << 24) | (gris << 16) | (gris << 8) | gris;
        }
        sc.close();
    }

    private String nextValidToken(Scanner sc) {
        while (sc.hasNext()) {
            String token = sc.next();
            if (token.startsWith("#")) {
                sc.nextLine();
            } else {
                return token;
            }
        }
        return "";
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

        // Escribir cabecera PGM
        writer.println("P2");
        writer.println("# Archivo generado por procesador paralelo");
        writer.println(this.ancho + " " + this.altura);
        writer.println(this.valorColorMax);

        // Escribir los datos de los píxeles
        int count = 0;
        for (int i = 0; i < this.pixeles.length; i++) {
            // Extraer el canal rojo (como es gris, R, G y B son iguales)
            int argb = pixeles[i];
            int gray = (argb >> 16) & 0xFF; 

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
