package org.example;

import java.io.*;
import java.util.Random;

public class Main {

    private static final String FILENAME = "numeros.txt";
    private static final int CANTIDAD_NUMEROS = 1000000;
    private static final int LIMITE_INFERIOR = 1;
    private static final int LIMITE_SUPERIOR = 10000;
    public static void main(String[] args) {


        generarArchivo();

        long suma = sumarElementos();
        System.out.println("La suma de los elementos del arreglo es: " + suma);


    }

    private static void generarArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))) {
            Random random = new Random();
            for (int i = 0; i < CANTIDAD_NUMEROS; i++) {
                int numero = LIMITE_INFERIOR + random.nextInt(LIMITE_SUPERIOR);
                writer.write(Integer.toString(numero));
                writer.newLine();
            }
            System.out.println("Archivo generado correctamente.");
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo: " + e.getMessage());
        }
    }

    private static long sumarElementos() {
        long suma = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                int numero = Integer.parseInt(linea);
                suma += numero;
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return suma;
    }
}