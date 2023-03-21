package org.example;

import java.io.IOException;
import java.net.*;
import java.net.Socket;
import java.util.Arrays;

public class TCPStreamingClient {
    // define the host and port to use
    public static final String HOST = "localhost";
    public static final int PORT = 1235;
    public static final int BUFFER_SIZE = 32767;
    public static final int MESSAGE_SIZE = 500 * 1024 * 1024; // 1 GB

    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket clientSocket = new Socket(HOST, PORT);
        System.out.println("Connected to server at " + clientSocket.getRemoteSocketAddress());

        byte[] buffer = new byte[BUFFER_SIZE];
        byte[] message = new byte[MESSAGE_SIZE];
        Arrays.fill(message, (byte) '0');

        long startTime = System.currentTimeMillis();
        int sentBytes = 0;
        int numMessages = 0;

        while (sentBytes < MESSAGE_SIZE) {
            int bytesToSend = Math.min(BUFFER_SIZE, MESSAGE_SIZE - sentBytes);
            System.arraycopy(message, sentBytes, buffer, 0, bytesToSend);
            clientSocket.getOutputStream().write(buffer, 0, bytesToSend);
            sentBytes += bytesToSend;
            numMessages += 1;
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Time: " + (endTime - startTime) + "ms, Messages sent: " + numMessages + ", Bytes sent: " + sentBytes);

        clientSocket.close();
    }
}