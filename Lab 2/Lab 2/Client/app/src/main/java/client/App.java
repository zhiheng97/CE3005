import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class App {

    static int QOTD_PORT = 17;
    static String SERVER_NAME = "hwlab1.scse.ntu.edu.sg";
    static int buf_len = 512;

    public static void main(String[] args) {

        //
        // 1. Open UDP socket
        //
        DatagramSocket socket = null;
        try {
            InetAddress serverAddress = InetAddress.getByName(SERVER_NAME);
            socket = new DatagramSocket();
            socket.connect(serverAddress, QOTD_PORT);
            System.out.println("UDP Client connected on port: " + QOTD_PORT + " to server: " + serverAddress.getCanonicalHostName());
        } catch (UnknownHostException uhe) {
        	uhe.printStackTrace();
        	System.exit(-1);
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        try {
            //
            // 2. Send UDP request to server
            //
            String content = "Loh Zhi Heng, A29, " + InetAddress.getLocalHost().getHostAddress();
            byte[] buf = content.getBytes();
            System.out.println("Content being sent to server: " + content);

            DatagramPacket request = new DatagramPacket(buf, buf.length);
            System.out.println("Senidng out request...");
            socket.send(request);
            System.out.println("Request sent...");

            //
            // 3. Receive UDP reply from server
            //
            byte[] resBuf = new byte[buf_len];
            DatagramPacket reply = new DatagramPacket(resBuf, resBuf.length);
            System.out.println("Waiting for reply...");
            socket.receive(reply);
            System.out.println("Server has responded to request...");

            String resContent = new String(resBuf);
            System.out.println("Response content: " + resContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
