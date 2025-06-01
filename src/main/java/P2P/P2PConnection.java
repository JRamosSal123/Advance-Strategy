package P2P;

import java.io.*;
import java.net.*;

public class P2PConnection {

    private BufferedReader input;
    private PrintWriter output;
    private Socket socket;

    public void connectAsServer(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("[SERVER] Esperando conexión en el puerto " + port + "...");
        socket = serverSocket.accept();
        System.out.println("[SERVER] Conectado a " + socket.getInetAddress());
        setupStreams();
    }

    public void connectAsClient(String host, int port) throws IOException {
        System.out.println("[CLIENT] Conectando a " + host + ":" + port + "...");
        socket = new Socket(host, port);
        System.out.println("[CLIENT] Conectado al servidor.");
        setupStreams();
    }

    private void setupStreams() throws IOException {
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
    }

    public void send(String message) {
        if (output != null) {
            output.println(message);
        }
    }

    public String receive() throws IOException {
        if (input != null) {
            return input.readLine();
        }
        return null;
    }

    public void close() {
        System.out.println("[P2P] Cerrando conexión...");
        try {
            if (output != null) {
                output.close();
                output = null;
            }
        } catch (Exception e) {
            System.err.println("[P2P] Error al cerrar output: " + e.getMessage());
        }

        try {
            if (input != null) {
                input.close();
                input = null;
            }
        } catch (IOException e) {
            System.err.println("[P2P] Error al cerrar input: " + e.getMessage());
        }

        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            System.err.println("[P2P] Error al cerrar socket: " + e.getMessage());
        }
    }


    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}

