package com.sistdist.bomba;

import java.io.*;
import java.net.*;

public class Bomba {
    public static void main(String[] args) {
        int id = 6; // mismo ID que usás en el controlador
        try (Socket socket = new Socket("127.0.0.1", 20000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("electrovalvula;" + id);
            System.out.println("[Bomba] Conectada al controlador.");

            String cmd;
            while ((cmd = in.readLine()) != null) {
                if ("abrir".equals(cmd)) {
                    System.out.println("[Bomba] Encendida.");
                } else if ("cerrar".equals(cmd)) {
                    System.out.println("[Bomba] Apagada.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

