import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MessagePasser {
    Socket socket;
    InputStream in;
    OutputStream out;


    MessagePasser(Socket socket) throws Exception{
        this.socket = socket;
        in = socket.getInputStream();
        out = socket.getOutputStream();
    }

    public void sendMessage(String message) throws Exception{
        byte n = (byte) message.length();
        byte[] sendBytes = new byte[n+1];
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        sendBytes[0] = n;
        for (int i = 0; i < n; i++){
            sendBytes[i+1] = messageBytes[i];
        }
        out.write(sendBytes);
    }

    public String receiveMessage() throws Exception{
        int n = in.read();
        String message = "";
        for (byte c: in.readNBytes(n)) message += (char) c;
        return message;
    }

    public void close() throws Exception{
        in.close();
        out.close();
        socket.close();
    }
}
