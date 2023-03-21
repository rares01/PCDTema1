package org.example;

import java.net.*;
import java.io.*;
import java.util.Arrays;

import java.io.IOException;
import java.net.*;

public class UDPStreamingServer {
    public static void main(String[] args) throws IOException {
        // Define the host and port to use
        String HOST = "localhost";
        int PORT = 1235;

        // Define the buffer size
        int BUFFER_SIZE = 32767;

        // Create a UDP socket
        DatagramSocket socket = new DatagramSocket(PORT, InetAddress.getByName(HOST));
        System.out.println("Server listening on " + HOST + ":" + PORT);

        // Receive data
        byte[] buffer = new byte[BUFFER_SIZE];
        int receivedBytes = 0;
        int numMessages = 0;
        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            receivedBytes += packet.getLength();
            numMessages++;
            if (packet.getLength() == 0) {
                break;
            }

            System.out.println("Protocol: UDP - Streaming, Messages received: " + numMessages + ", Bytes received: " + receivedBytes);
        }

        // Close the socket
        socket.close();
    }

}
