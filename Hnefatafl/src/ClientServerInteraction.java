import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;
import java.util.TreeSet;

public class ClientServerInteraction extends Thread{
    static final byte requestMove = 0;
    static final byte requestPieceAtPosition = 1;
    static final byte requestPieces = 2;
    static final byte requestWinner = 3;
    static final byte isNull = 0;
    static final byte isNotNull = 1;
    static final byte noWinner = 0;
    static final byte whiteHasWon = 1;
    static final byte blackHasWon = 2;
    Socket socket;
    GameView game;

    ClientServerInteraction(Socket socket, GameView game) {
        this.socket = socket;
        this.game = game;
    }

    public void run() {
        try {
            BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
            BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());

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
            e.printStackTrace();
        }
    }
}
