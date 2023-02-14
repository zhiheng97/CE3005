import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class main {

    static int QOTD_PORT = 17;
    static String QUOTE = "Good morning! :D";
    static int buf_len = 512;

    public static void main(String[] args) {
    	//UDPServer();
        TCPServer();
    }

	private static void TCPServer() {
		//
		// 1. Open TCP socket at well-known port
		//
		ServerSocket parentSocket = null;
		try {
			parentSocket = new ServerSocket(QOTD_PORT);
			System.out.println("TCP Server listening on port: " + QOTD_PORT);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		while (true) {
			try {
				//
				// 2. Listen to establish TCP connection with clnt
				//
				Socket childSocket = parentSocket.accept();
				//
				// 3. Create new thread to handle client connection
				//
				ClientHandler client =
						new ClientHandler(childSocket);
				Thread thread = new Thread(client);
				thread.start();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
 		}
	}

	private static void UDPServer() {
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

class ClientHandler implements Runnable {
	 private Socket socket;
	 static String QUOTE = "Good morning! :D";
	 
	 ClientHandler(Socket socket) {
		 this.socket = socket;
	 }
	 
	 public void run() {
		//
		// 4. Receive TCP request from client
		//
		InputStream is;
		try {
			is = socket.getInputStream();
			int buf_len = is.available();
			byte[] reqBuf = new byte[buf_len];
			reqBuf = is.readAllBytes();
			System.out.println("Received: " + new String(reqBuf));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		//
		// 5. Send TCP reply to client
		//
		try {
			String replyContent = QUOTE;
			System.out.println("Reply content: " + replyContent);
			OutputStream os = socket.getOutputStream();
			System.out.println("Sending response...");
			os.write(replyContent.getBytes());
			System.out.println("Response sent!");
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	 }
}
