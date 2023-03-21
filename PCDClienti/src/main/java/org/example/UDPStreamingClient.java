package org.example;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class UDPStreamingClient {
    // define the host and port to use
    private static final String HOST = "localhost";
    private static final int PORT = 1235;

    // calculate time
    private static long start_time = 0;
    private static long end_time = 0;

    // define the buffer size
    private static final int BUFFER_SIZE = 32767;

    // define the message size to send (in bytes)
    private static final long MESSAGE_SIZE = 500L * 1024L * 1024L;  // 1 GB

    public static void main(String[] args) {
        DatagramSocket clientSocket = null;
        try {
            // Create a UDP socket
            clientSocket = new DatagramSocket();

            // Generate a fixed amount of random data to send
            byte[] dataToSend = new byte[(int) MESSAGE_SIZE];
            Arrays.fill(dataToSend, (byte) '0');

            start_time = System.currentTimeMillis();
            int packetsSent = 0;
            int sentBytes = 0;
            while (sentBytes < MESSAGE_SIZE) {
                int bytesToSend = Math.min(BUFFER_SIZE, (int) (MESSAGE_SIZE - sentBytes));
                DatagramPacket sendPacket = new DatagramPacket(dataToSend, sentBytes, bytesToSend, InetAddress.getByName(HOST), PORT);
                clientSocket.send(sendPacket);
                sentBytes += bytesToSend;
                packetsSent++;
            }
            end_time = System.currentTimeMillis();
            System.out.printf("Time: %dms, Messages sent: %d, Bytes sent: %d\n", end_time - start_time, packetsSent, sentBytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (clientSocket != null) {
                clientSocket.close();
            }
        }
    }
}