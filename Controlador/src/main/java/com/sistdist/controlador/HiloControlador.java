package com.sistdist.controlador;

import java.io.PrintWriter;
import java.util.Map;

public class HiloControlador extends Thread {
    private final Map<Integer, Double> humedades;
    private final Map<Integer, PrintWriter> conexionesEV;

    // pesos (ajustables)
    private final double w1 = 0.5, w2 = 0.3, w3 = 0.2;

    public HiloControlador(Map<Integer, Double> humedades, Map<Integer, PrintWriter> conexionesEV) {
        this.humedades = humedades;
        this.conexionesEV = conexionesEV;
    }

    private double inr(double H, double T, double R) {
        return w1*(1 - H/100.0) + w2*(T/40.0) + w3*(R/1000.0);
    }

    private int minutosRiego(double inr) {
        if (inr > 0.9) return 10;
        if (inr > 0.8) return 7;
        if (inr > 0.7) return 5;
        return 0;
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (int parcelaId : humedades.keySet()) {
                    double H = humedades.get(parcelaId);
                    double T = Controlador.temp;
                    double R = Controlador.rad;
                    boolean L = Controlador.lluvia;

                    double val = inr(H, T, R);
                    boolean aptoPorINR = (val > 0.7);
                    int mins = (!L && aptoPorINR) ? minutosRiego(val) : 0;

                    // Log siempre
                    String accion = (mins > 0) ? "REGAR " + mins + "m"
                                   : (L && aptoPorINR) ? "NO REGAR (LLUVIA)"
                                   : "NO REGAR";
                    System.out.printf("[CTRL] P%d | H=%.1f | T=%.1f | R=%.0f | L=%s | INR=%.3f -> %s%n",
                            parcelaId, H, T, R, L ? "1" : "0", val, accion);

                    // Si corresponde regar
                    if (mins > 0) {
                        synchronized (Controlador.lockBomba) {
                            if (!Controlador.fertirrigando) {
                                // Encender bomba (id=6)
                                PrintWriter bomba = conexionesEV.get(6);
                                if (bomba != null) {
                                    bomba.println("abrir");
                                    bomba.flush();
                                }

                                // Abrir válvula de la parcela
                                PrintWriter ev = conexionesEV.get(parcelaId);
                                if (ev != null) {
                                    ev.println("abrir");
                                    ev.flush();

                                    Thread.sleep(mins * 1000); // simulación (segundos en lugar de minutos)

                                    ev.println("cerrar");
                                    ev.flush();
                                }

                                // Apagar bomba
                                if (bomba != null) {
                                    bomba.println("cerrar");
                                    bomba.flush();
                                }
                            } else {
                                System.out.println("[CTRL] No se riega P" + parcelaId + " porque fertirriego está activo.");
                            }
                        }
                    }
                }
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
