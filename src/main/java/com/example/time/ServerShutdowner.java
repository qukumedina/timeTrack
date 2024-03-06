package com.example.time;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ServerShutdowner {

    public static void main(String[] args) {

        // Here you can include the TCP client logic after the JavaFX application is launched
        String serverName = "localhost"; // Server name or IP
        int port = 6789; // Port number

        try (Socket clientSocket = new Socket(serverName, port)) {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Scanner scanner = new Scanner(System.in);
            System.out.print("Send shutdown to shutdown the server: ");
            String command;
                while ( (command = scanner.nextLine()) != null ) {
                    if ("shutdown".equals(command)) {
                        // Sending login details to the server
                        outToServer.writeBytes("SHUTDOWN"+'\n');
                        break;
                    }
                }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

