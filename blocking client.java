import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class BlockingClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            System.out.println("Connected to server.");

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("Enter message (type 'exit' to quit): ");
                String message = scanner.nextLine();

                if ("exit".equalsIgnoreCase(message)) {
                    break;
                }

                // Send the message to the server
                outputStream.write(message.getBytes());
                outputStream.flush();

                // Receive the echoed message from the server
                byte[] buffer = new byte[1024];
                int bytesRead = inputStream.read(buffer);
                String receivedMessage = new String(buffer, 0, bytesRead);
                System.out.println("Server says: " + receivedMessage);
            }

            // Close the connection
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
