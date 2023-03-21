import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.TreeSet;

public class Client implements GameView{
    String serverAdress = "localhost";
    int serverPort = 2143;
    Socket server;
    BufferedOutputStream out;
    BufferedInputStream in;

    static final byte requestMove = 0;
    static final byte requestPieceAtPosition = 1;
    static final byte requestPieces = 2;
    static final byte requestWinner = 3;
    static final byte isNull = 0;
    static final byte isNotNull = 1;
    static final byte noWinner = 0;
    static final byte whiteHasWon = 1;
    static final byte blackHasWon = 2;

    static final Suit[] COLORS = new Suit[]{Suit.WHITE, Suit.BLACK};
    static final Type[] TYPES = new Type[]{Type.KING, Type.PAWN};

    Client() throws Exception{
        server = new Socket(serverAdress, serverPort);
        out = new BufferedOutputStream(server.getOutputStream());
        in = new BufferedInputStream(server.getInputStream());
    }

    @Override
    public boolean move(Position start, Position end){
        try {
            out.write(requestMove);
            out.write(new byte[]{(byte) start.x, (byte) start.y, (byte) end.x, (byte) end.y});
            out.flush();
            return in.read() == 1;
        } catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    @Override
    public Piece pieceAt(Position pos) {
        try {
            out.write(requestPieceAtPosition);
            out.write(new byte[]{(byte) pos.x, (byte) pos.y});
            out.flush();
            if (in.read() == isNull) return null;
            else return new Piece(COLORS[in.read()], TYPES[in.read()], pos);
        } catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    @Override
    public TreeSet<Piece> getPieces() {
        try {
            out.write(requestPieces);
            out.flush();
            int n = in.read();
            TreeSet<Piece> pieces = new TreeSet<>();
            for (int i = 0; i < n; i++) pieces.add(new Piece(COLORS[in.read()], TYPES[in.read()], new Position(in.read(), in.read())));
            return pieces;
        } catch (Exception e){
            System.out.println(e);
            return new TreeSet<>();
        }
    }

    @Override
    public boolean whiteHasWon() {
        try {
            out.write(requestWinner);
            out.flush();
            return in.read() == whiteHasWon;
        } catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    @Override
    public boolean blackHasWon() {
        try {
            out.write(requestWinner);
            out.flush();
            return in.read() == blackHasWon;
        } catch (Exception e){
            System.out.println(e);
            return false;
        }
    }
}
