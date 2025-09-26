package com.sistdist.electrovalvulafertirrigacion;

import java.io.*;
import java.net.*;

public class ElectrovalvulaFertirriego {
    public static void main(String[] args) {
        int id = 7; // ID de fertirriego
        try (Socket socket = new Socket("127.0.0.1", 20000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Identificación
            out.println("electrovalvula;" + id);

            String cmd;
            while ((cmd = in.readLine()) != null) {
                if ("abrir".equals(cmd)) {
                    System.out.println("[EV" + id + "] Válvula FERTI abierta.");
                } else if ("cerrar".equals(cmd)) {
                    System.out.println("[EV" + id + "] Válvula FERTI cerrada.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

