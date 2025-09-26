package com.sistdist.controlador;

import java.io.*;
import java.net.*;
import java.util.logging.*;

public class HiloReceptorLluvia extends Thread {
    private final int id;
    private final BufferedReader br;

    public HiloReceptorLluvia(Socket s, int id) {
        this.id = id;
        try {
            this.br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void run() {
        System.out.println("Receptor de lluvia iniciado (id=" + id + ")");
        while (true) {
            try {
                String entrada = br.readLine();
                if (entrada == null) break;

                int valor = Integer.parseInt(entrada.trim()); // 0 o 1
                Controlador.lluvia = (valor > 0);

                System.out.printf("[CTRL] SensorLluvia (id=%d) -> %s%n",
                        id, (Controlador.lluvia ? "LLUEVE" : "NO LLUEVE"));

            } catch (IOException ex) {
                Logger.getLogger(HiloReceptorLluvia.class.getName()).log(Level.SEVERE, null, ex);
                break;
            } catch (NumberFormatException nfe) {
                System.out.println("Valor de lluvia inválido");
            }
        }
    }
}

