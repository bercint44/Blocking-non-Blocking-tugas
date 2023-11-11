import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class NonBlockingClient {
    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("localhost", 12345));
            socketChannel.configureBlocking(false);

            System.out.println("Connected to server.");

            Scanner scanner = new Scanner(System.in);
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (true) {
                System.out.print("Enter message (type 'exit' to quit): ");
                String message = scanner.nextLine();

                if ("exit".equalsIgnoreCase(message)) {
                    break;
                }

                buffer.clear();
                buffer.put(message.getBytes());
                buffer.flip();

                // Send the message to the server
                while (buffer.hasRemaining()) {
                    socketChannel.write(buffer);
                }

                // Receive the echoed message from the server
                buffer.clear();
                while (socketChannel.read(buffer) == 0) {
                    // Wait for the server to respond (non-blocking)
                }

                buffer.flip();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);
                String receivedMessage = new String(data);
                System.out.println("Server says: " + receivedMessage);
            }

            // Close the connection
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
