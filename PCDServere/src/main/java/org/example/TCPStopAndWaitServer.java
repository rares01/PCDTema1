package org.example;

import java.io.*;
import java.net.*;

import java.io.*;
import java.net.*;

public class TCPStopAndWaitServer {
    // define the host and port to use
    static final int PORT = 12345;
    static final String HOST = "localhost";

    // define the buffer size
    static final int BUFFER_SIZE = 32767;

    public static void main(String[] args) throws IOException {
        // create a TCP socket and bind it to the host and port
        ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(HOST));
        System.out.println("Server listening on " + HOST + ":" + PORT);

        // wait for a client to connect
        Socket clientSocket = serverSocket.accept();
        System.out.println("Connected by " + clientSocket.getRemoteSocketAddress());

        // receive the message size from the client
        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        long messageSize = in.readLong();

        System.out.println("Receiving message of size " + messageSize + " bytes...");

        // receive the message from the client in chunks
        int receivedBytes = 0;
        int numMessages = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        while (receivedBytes < messageSize) {

            int bytesToRead = (int) Math.min(BUFFER_SIZE, messageSize - receivedBytes);
            int bytesReceived = clientSocket.getInputStream().read(buffer, 0, bytesToRead);
            receivedBytes += bytesReceived;
            numMessages++;

            // process the received data (e.g. write it to a file)

            // send an acknowledgement to the client
            clientSocket.getOutputStream().write("OK".getBytes());
        }

        System.out.printf("Protocol: TCP - Streaming, Messages received: %d, Bytes received: %d%n",
                numMessages, messageSize);

        // close the server socket
        serverSocket.close();

        System.out.println("Message received successfully");
    }
}