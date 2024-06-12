package org.example;

import java.io.*;
import java.util.Random;

public class Main {

    private static final String FILENAME = "numeros.txt";
    private static final int CANTIDAD_NUMEROS = 1000000;
    private static final int LIMITE_INFERIOR = 1;
    private static final int LIMITE_SUPERIOR = 10000;
    private static  int  HILOS_PROCESOS = 2;
    public static void main(String[] args) {


        generarArchivo();

        long tiempoInicio = System.currentTimeMillis();
        long sumaSecuencial = sumaSecuencial(new File(FILENAME));
        long tiempoFinal = System.currentTimeMillis();
        System.out.println("---------------------------------------------");
        System.out.println("La suma de los elementos del arreglo secuencial es: " + sumaSecuencial);

        System.out.println("Tiempo de ejecución de la suma Secuencial: " + (tiempoFinal - tiempoInicio) + "ms");
        System.out.println("---------------------------------------------");

        tiempoInicio = System.currentTimeMillis();
        long sumaParalelo = sumaParalela(new File(FILENAME),HILOS_PROCESOS);
        tiempoFinal = System.currentTimeMillis();

        System.out.println("La suma de los elementos del arreglo paralelo con (2) hilos es: " + sumaParalelo);

        System.out.println("Tiempo de ejecución de la suma Paralela: " + (tiempoFinal - tiempoInicio) + "ms");
        System.out.println("---------------------------------------------");

        HILOS_PROCESOS = 4;
        tiempoInicio = System.currentTimeMillis();
        sumaParalelo = sumaParalela(new File(FILENAME),HILOS_PROCESOS);
        tiempoFinal = System.currentTimeMillis();

        System.out.println("La suma de los elementos del arreglo paralelo con (4) hilos es: " + sumaParalelo);

        System.out.println("Tiempo de ejecución de la suma Paralela: " + (tiempoFinal - tiempoInicio) + "ms");
        System.out.println("---------------------------------------------");

        HILOS_PROCESOS = 8;
        tiempoInicio = System.currentTimeMillis();
        sumaParalelo = sumaParalela(new File(FILENAME),HILOS_PROCESOS);
        tiempoFinal = System.currentTimeMillis();

        System.out.println("La suma de los elementos del arreglo paralelo con (8) hilos es: " + sumaParalelo);

        System.out.println("Tiempo de ejecución de la suma Paralela: " + (tiempoFinal - tiempoInicio) + "ms");
        System.out.println("---------------------------------------------");

    }

    //Este es el metodo para realizar la generacion del millon de numeros, para cumplir con el objetivo
    //del punto 1, que dice lo siguiente:

    //1. Genera un archivo con 1,000,000 de registros comprendido entre 1 y 10,000, el
    //cual deberá usar como base para los demás cálculos.
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

    //Este es el metodo para realizar la suma secuencial, para cumplir con el objetivo
    //del punto 2, que dice lo siguiente:

    //2. Escribe un programa secuencial que sume los elementos de un arreglo de un
    //millón de enteros.
    private static long sumaSecuencial(File file) {
        long suma = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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

    //Este es el metodo para realizar la suma en paralelo, para cumplir con el objetivo
    //del punto 3, que dice lo siguiente:

    //3. Modifica tu programa para que use múltiples hilos o procesos para realizar la
    //suma en paralelo. Divide el arreglo en partes iguales para cada hilo/proceso.
    private static long sumaParalela(File file, int cantHilos) {
        long [] resultados = new long[cantHilos];
        Thread[] hilos = new Thread[cantHilos];
        long sumaTotal = 0;


        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Leer todos los números del archivo y almacenarlos en un arreglo
            int[] numeros = reader.lines().mapToInt(Integer::parseInt).toArray();
            int tamanoSubarreglo = (int) Math.ceil((double) numeros.length / HILOS_PROCESOS);

            // Crear y ejecutar los hilos
            for (int i = 0; i < HILOS_PROCESOS; i++) {
                final int inicio = i * tamanoSubarreglo;
                final int fin = Math.min(inicio + tamanoSubarreglo, numeros.length); // Asegurar que no exceda el tamaño del arreglo
                final int indice = i;
                hilos[i] = new Thread(() -> {
                    long suma = 0;
                    for (int j = inicio; j < fin; j++) {
                        suma += numeros[j];
                    }
                    resultados[indice] = suma;
                });
                hilos[i].start();
            }

            // Esperar a que todos los hilos terminen
            for (Thread hilo : hilos) {
                hilo.join();
            }

            // Sumar los resultados de cada hilo
            for (long suma : resultados) {
                sumaTotal += suma;
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }

        return sumaTotal;
    }
}