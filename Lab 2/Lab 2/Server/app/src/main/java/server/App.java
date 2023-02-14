import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class main {

    static int QOTD_PORT = 17;
    static String QUOTE = "Good morning! :D";
    static int buf_len = 512;

    public static void main(String[] args) {
        //
        // 1. Open UDP socket at well-known port
        //
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(QOTD_PORT);
            System.out.println("UDP Server listening on port " + QOTD_PORT);
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        while (true) {
            try {
                //
                // 2. Listen for UDP request from client
                //
                byte[] buf = new byte[buf_len];
                DatagramPacket request = new DatagramPacket(buf, buf.length);
                System.out.println("Waiting for request...");
                socket.receive(request);

                String req = new String(buf);
                System.out.println("Received: " + req);

                InetAddress clientAdd = request.getAddress();
                int clientPort = request.getPort();
                System.out.println("From client: " + clientAdd.getCanonicalHostName());

                //
                // 3. Send UDP reply to client
                //
                String replyContent = QUOTE;
                byte[] resBuf = replyContent.getBytes("UTF-8");
                System.out.println("Reply content: " + replyContent);

                DatagramPacket reply = new DatagramPacket(resBuf, resBuf.length, clientAdd, clientPort);
                System.out.println("Sending response...");
                socket.send(reply);
                System.out.println("Response sent!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}