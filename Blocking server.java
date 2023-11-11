import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BlockingServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server is listening on port 12345...");

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Blocking call, waits for client connection
                System.out.println("Accepted connection from: " + clientSocket);

                // Handle client communication
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();

                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    String message = new String(buffer, 0, bytesRead);
                    System.out.println("Received message: " + message);

                    // Echo the message back to the client
                    outputStream.write(buffer, 0, bytesRead);
                    outputStream.flush();
                }

                // Close the connection
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
