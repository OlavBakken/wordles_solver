import java.util.List;
import java.util.TreeSet;

public interface GameView {
    public boolean move(Position start, Position end);
    public Piece pieceAt(Position pos);
    public TreeSet<Piece> getPieces();
    public boolean whiteHasWon();
    public boolean blackHasWon();

}
