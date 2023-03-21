package org.example;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class TCPStopAndWaitClient {
    // define the host and port to use
    static final String HOST = "localhost";
    static final int PORT = 12345;

    static long start_time = 0;
    static long end_time = 0;

    // define the buffer size
    static final int BUFFER_SIZE = 32767;

    // define the message size to send (in bytes)
//    static final long MESSAGE_SIZE = 1000L * 1024 * 1024;  // 500 MB
     static final long MESSAGE_SIZE = 500L * 1024 * 1024;  // 1 GB

    public static void main(String[] args) {
        try {
            // create a TCP socket and connect to the server
            Socket socket = new Socket(HOST, PORT);

            // send the message size to the server
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeLong(MESSAGE_SIZE);
            out.flush();

            byte[] buffer = new byte[BUFFER_SIZE];
            start_time = System.currentTimeMillis();

            // send the message to the server in chunks
            long sent_bytes = 0;
            int num_messages = 0;
            while (sent_bytes < MESSAGE_SIZE) {
                int bytes_to_send = (int) Math.min(BUFFER_SIZE, MESSAGE_SIZE - sent_bytes);
                ByteBuffer.wrap(buffer).putLong(sent_bytes).position(8);
                socket.getOutputStream().write(buffer, 0, bytes_to_send);
                sent_bytes += bytes_to_send;
                num_messages++;

                // wait for an acknowledgement from the server
                DataInputStream in = new DataInputStream(socket.getInputStream());
                while (!in.readBoolean()) {
                    // if the acknowledgement is not received, re-send the previous chunk
                    socket.getOutputStream().write(buffer, 0, bytes_to_send + 8);
                }
            }

            end_time = System.currentTimeMillis();
            System.out.println("OK");
            System.out.printf("Time: %.3f sec, Messages sent: %d, Bytes sent: %d%n",
                    (end_time - start_time) / 1000.0, num_messages, sent_bytes);

            // close the socket
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
