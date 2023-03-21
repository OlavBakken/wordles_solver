import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeSet;

public class Server {
    static final byte requestMove = 0;
    static final byte requestPieceAtPosition = 1;
    static final byte requestPieces = 2;
    static final byte requestWinner = 3;
    static final byte isNull = 0;
    static final byte isNotNull = 1;
    static final byte noWinner = 0;
    static final byte whiteHasWon = 1;
    static final byte blackHasWon = 2;

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket1 = new ServerSocket(2143);
        ServerSocket serverSocket2 = new ServerSocket(2144);
        GameView game = new Game();
        Socket socket1 = serverSocket1.accept();
        Socket socket2 = serverSocket2.accept();
        Thread t1 = new ClientServerInteraction(socket1, game);
        Thread t2 = new ClientServerInteraction(socket2, game);

        t1.start();
        t2.start();
    }
}
