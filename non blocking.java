import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NonBlockingServer {
    public static void main(String[] args) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(12345));
            serverSocketChannel.configureBlocking(false);

            System.out.println("Server is listening on port 12345...");

            List<SocketChannel> clients = new ArrayList<>();

            while (true) {
                SocketChannel client = serverSocketChannel.accept(); // Non-blocking
                if (client != null) {
                    System.out.println("Accepted connection from: " + client);
                    client.configureBlocking(false);
                    clients.add(client);
                }

                Iterator<SocketChannel> iterator = clients.iterator();
                while (iterator.hasNext()) {
                    SocketChannel channel = iterator.next();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int bytesRead = channel.read(buffer);

                    if (bytesRead == -1) {
                        // Client disconnected
                        System.out.println("Client disconnected: " + channel);
                        iterator.remove();
                        channel.close();
                    } else if (bytesRead > 0) {
                        // Message received, echo back to the client
                        buffer.flip();
                        byte[] data = new byte[bytesRead];
                        buffer.get(data);
                        String message = new String(data);
                        System.out.println("Received message from " + channel + ": " + message);

                        // Echo the message back to the client
                        channel.write(ByteBuffer.wrap(data));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
