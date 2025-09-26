package com.sistdist.sistfertirrigacion;

import java.io.*;
import java.net.*;

public class SistFertirrigacion {
    public static void main(String[] args) {
        int id = 7; // ID para el sistema de fertirriego
        try (Socket socket = new Socket("127.0.0.1", 20000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("electrovalvula;" + id);
            System.out.println("[Fertirriego] Conectado al controlador.");

            String cmd;
            while ((cmd = in.readLine()) != null) {
                if ("abrir".equals(cmd)) {
                    System.out.println("[Fertirriego] Válvula abierta, inyectando fertilizante.");
                } else if ("cerrar".equals(cmd)) {
                    System.out.println("[Fertirriego] Válvula cerrada, detenido.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
