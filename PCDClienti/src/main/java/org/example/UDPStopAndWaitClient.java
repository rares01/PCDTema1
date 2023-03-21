package org.example;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class UDPStopAndWaitClient {

    public static void main(String[] args) throws IOException {
        // Define the host and port to use
        String HOST = "localhost";
        int PORT = 12345;

        // Define the buffer size
        int BUFFER_SIZE = 32753;

        // Create a UDP socket
        DatagramSocket clientSocket = new DatagramSocket();

        // Generate some test data
        byte[] buffer = new byte[500 * 1024 * 1024]; // 1 GB

        long startTime = System.currentTimeMillis();
        int sentBytes = 0;
        int numMessages = 0;
        for (int i = 0; i < buffer.length; i += BUFFER_SIZE) {
            int length = Math.min(BUFFER_SIZE, buffer.length - i);
            byte[] chunk = new byte[length];
            System.arraycopy(buffer, i, chunk, 0, length);
            DatagramPacket sendPacket = new DatagramPacket(chunk, length, InetAddress.getByName(HOST), PORT);
            clientSocket.send(sendPacket);
            sentBytes += length;
            numMessages++;

            // Wait for an acknowledgement
            byte[] receiveBuffer = new byte[BUFFER_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, BUFFER_SIZE);
            clientSocket.receive(receivePacket);
            if (!new String(receivePacket.getData()).trim().equals("OK")) {
                System.err.println("Invalid acknowledgement received.");
                break;
            }
        }

        // Calculate elapsed time
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Time: " + elapsedTime + "ms, Messages sent: " + numMessages + ", Bytes sent: " + sentBytes);

        // Close the socket
        clientSocket.close();
    }
}
