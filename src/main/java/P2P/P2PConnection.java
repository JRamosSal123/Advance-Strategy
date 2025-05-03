package P2P;

import java.io.*;
import java.net.*;

public class P2PConnection {

    // Method to start a server
    /*public void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[SERVER] Listening on port " + port);

            Socket clientSocket = serverSocket.accept();
            System.out.println("[SERVER] Connected to " + clientSocket.getInetAddress());

            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            String clientMessage;
            while ((clientMessage = input.readLine()) != null) {
                System.out.println("Client: " + clientMessage);

                if (clientMessage.equalsIgnoreCase("exit")) {
                    System.out.println("[SERVER] Connection closed by client.");
                    break;
                }

                System.out.print("You: ");
                String reply = scanner.nextLine();
                output.println(reply);

                if (reply.equalsIgnoreCase("exit")) {
                    System.out.println("[SERVER] Connection closed.");
                    break;
                }
            }

            clientSocket.close();
            scanner.close();

        } catch (IOException e) {
            System.err.println("[SERVER] Error: " + e.getMessage());
        }
    }

    // Method to start a client
    public void startClient(String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            System.out.println("[CLIENT] Connected to server " + host + ":" + port);

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            String serverMessage;
            while (true) {
                System.out.print("You: ");
                String message = scanner.nextLine();
                output.println(message);

                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("[CLIENT] Connection closed.");
                    break;
                }

                serverMessage = input.readLine();
                System.out.println("Server: " + serverMessage);

                if (serverMessage.equalsIgnoreCase("exit")) {
                    System.out.println("[CLIENT] Server closed the connection.");
                    break;
                }
            }

            scanner.close();

        } catch (IOException e) {
            System.err.println("[CLIENT] Error: " + e.getMessage());
        }
    }

    public void main() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Select mode: \n1. Server\n2. Client");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 1) {
            System.out.print("Enter port to listen on: ");
            int port = scanner.nextInt();
            startServer(port);
        } else if (choice == 2) {
            System.out.print("Enter server host: ");
            String host = scanner.nextLine();
            System.out.print("Enter server port: ");
            int port = scanner.nextInt();
            startClient(host, port);
        } else {
            System.out.println("Invalid choice.");
        }

        scanner.close();
    }

    public String receive() {
        return null;
    }

    public void send(String s) {
    }*/
    private BufferedReader input;
    private PrintWriter output;
    private Socket socket;

    // Método para iniciar como servidor
    public void connectAsServer(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("[SERVER] Esperando conexión en el puerto " + port + "...");
        socket = serverSocket.accept();
        System.out.println("[SERVER] Conectado a " + socket.getInetAddress());
        setupStreams();
    }

    // Método para iniciar como cliente
    public void connectAsClient(String host, int port) throws IOException {
        System.out.println("[CLIENT] Conectando a " + host + ":" + port + "...");
        socket = new Socket(host, port);
        System.out.println("[CLIENT] Conectado al servidor.");
        setupStreams();
    }

    // Inicializa los flujos de entrada y salida
    private void setupStreams() throws IOException {
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
    }

    // Enviar mensaje al otro jugador
    public void send(String message) {
        if (output != null) {
            output.println(message);
        }
    }

    // Recibir mensaje del otro jugador (bloquea hasta recibir algo)
    public String receive() throws IOException {
        if (input != null) {
            return input.readLine();
        }
        return null;
    }

    // Cerrar conexión
    public void close() {
        System.out.println("[P2P] Cerrando conexión...");
        // Cerrar el output primero para evitar que el otro extremo se quede esperando
        try {
            if (output != null) {
                output.close(); // No lanza IOException, pero por claridad se incluye aquí
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

    // Verifica si está conectado
    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}

