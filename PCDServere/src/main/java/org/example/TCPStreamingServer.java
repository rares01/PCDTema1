package org.example;

import java.io.*;
import java.net.*;

public class TCPStreamingServer {
    // define the host and port to use
    public static final int PORT = 1235;
    public static final int BUFFER_SIZE = 32767;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server listening on localhost:" + PORT + "...");

        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected from " + clientSocket.getRemoteSocketAddress());

        int receivedBytes = 0;
        int numMessages = 0;
        byte[] buffer = new byte[BUFFER_SIZE];

        while (true) {
            int numBytesRead = clientSocket.getInputStream().read(buffer);
            receivedBytes += numBytesRead;
            numMessages += 1;
            if (numBytesRead == -1) {
                break;
            }
            // Process the data received from the client
            String receivedData = new String(buffer, 0, numBytesRead);
            System.out.println("Received data: " + receivedData);
        }

        System.out.println("Protocol: TCP - Streaming, Messages received: " + numMessages + ", Bytes received: " + receivedBytes);

        // Close the sockets
        clientSocket.close();
        serverSocket.close();
    }
}