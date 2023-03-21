package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPStopAndWaitServer {
    public static void main(String[] args) throws IOException, IOException {
        // Define the host and port to use
        String HOST = "localhost";
        int PORT = 12345;

        // Define the buffer size
        int BUFFER_SIZE = 32753;

        // Create a UDP socket
        DatagramSocket serverSocket = new DatagramSocket(PORT, InetAddress.getByName(HOST));
        System.out.println("Server started on " + HOST + ":" + PORT);

        // Receive data
        byte[] buffer = new byte[BUFFER_SIZE];
        int receivedBytes = 0;
        int numMessages = 0;
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            serverSocket.receive(receivePacket);
            receivedBytes += receivePacket.getLength();
            numMessages++;

            // Send an acknowledgement
            DatagramPacket sendPacket = new DatagramPacket("OK".getBytes(), "OK".getBytes().length, receivePacket.getAddress(), receivePacket.getPort());
            serverSocket.send(sendPacket);

            if (receivePacket.getLength() == 0) {
                break;
            }
        }

        // Print results
        System.out.println("Protocol: UDP - StopWait, Messages received: " + numMessages + ", Bytes received: " + receivedBytes);

        // Close the socket
        serverSocket.close();
    }
}
