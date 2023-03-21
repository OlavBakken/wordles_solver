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

    GameView game;

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(2143);
        Socket socket1 = serverSocket.accept();
        //Socket socket2 = serverSocket.accept();
        GameView game = new Game();

        Thread t = new Thread() {
            public void run() {
                try {
                    BufferedInputStream in = new BufferedInputStream(socket1.getInputStream());
                    BufferedOutputStream out = new BufferedOutputStream(socket1.getOutputStream());

                    while (true) {
                        int request = in.read();
                        switch (request) {
                            case requestMove: {
                                Position start = new Position(in.read(), in.read());
                                Position end = new Position(in.read(), in.read());
                                out.write((game.move(start, end) ? 1 : 0));
                                out.flush();
                                break;
                            }
                            case requestPieceAtPosition: {
                                Position pos = new Position(in.read(), in.read());
                                Piece piece = game.pieceAt(pos);
                                if (piece == null) out.write(isNull);
                                else out.write(isNotNull);
                                out.write(new byte[]{(byte) piece.color.ordinal(), (byte) piece.type.ordinal()});
                                out.flush();
                                break;
                            }
                            case requestPieces: {
                                TreeSet<Piece> pieces = game.getPieces();
                                out.write(pieces.size());
                                for (Piece piece : pieces) {
                                    out.write(new byte[]{(byte) piece.color.ordinal(), (byte) piece.type.ordinal(), (byte) piece.pos.x, (byte) piece.pos.y});
                                }
                                out.flush();
                                break;
                            }
                            case requestWinner: {
                                if (game.whiteHasWon()) out.write(whiteHasWon);
                                else if (game.blackHasWon()) out.write(blackHasWon);
                                else out.write(noWinner);
                                out.flush();
                            }
                        }
                    }
                }
                catch (Exception e) {
                    System.out.println("server crashed");
                }
            }
        };

        t.start();
    }
}
