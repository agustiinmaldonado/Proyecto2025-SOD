package com.sistdist.controlador;

import java.util.Random;

public class Fertirrigacion extends Thread {
    private final Bomba bomba;
    private final Random rnd = new Random();

    public Fertirrigacion(Bomba bomba) { this.bomba = bomba; }

    @Override
    public void run() {
        while (true) {
            try {
                // cada ~10–20 s intenta preparar solución
                Thread.sleep(10000 + rnd.nextInt(10000));
                String quien = "Fertirrigacion";
                bomba.adquirir(quien);
                System.out.println("[Fertirrigacion] Preparando solucion...");
                Thread.sleep(5000 + rnd.nextInt(5000));
                System.out.println("[Fertirrigacion] Finalizado.");
                bomba.liberar(quien);
            } catch (InterruptedException e) { return; }
        }
    }
}
