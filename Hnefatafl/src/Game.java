import java.util.*;

public class Game implements GameView{
    static final Suit WHITE = Suit.WHITE;
    static final Suit BLACK = Suit.BLACK;
    static final Type KING = Type.KING;
    static final Type PAWN = Type.PAWN;
    static final TreeSet<Position> CORNERS = new TreeSet<>(List.of(new Position[]{new Position(0, 0),
                                                                                                        new Position(0, 10),
                                                                                                        new Position(10, 0),
                                                                                                        new Position(10, 10)}));
    static final Position CENTER = new Position(5, 5);

    Piece king;

    TreeSet<Piece> pieces;
    TreeMap<Position, Piece> board;
    Suit turn = Suit.BLACK;
    static final int directions[][] = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    public Piece pieceAt(Position pos){
        return board.get(pos);
    }

    public TreeSet<Piece> getPieces(){
        return pieces;
    }

    public boolean move(Position start, Position end){
        Piece piece= board.get(start);

        // potential illegal moves
        if (!board.containsKey(start) || board.containsKey(end)) return false;
        if (start.equals(end)) return false;
        if (start.x != end.x && start.y != end.y) return false;
        for (int x = Math.min(start.x, end.x) + 1; x < Math.max(start.x, end.x); x++) if (board.containsKey(new Position(x, start.y))) return false;
        for (int y = Math.min(start.y, end.y) + 1; y < Math.max(start.y, end.y); y++) if (board.containsKey(new Position(start.x, y))) return false;

        //illegal squares for pawns
        if (piece.type == Type.PAWN && end.x == 5 && end.y == 5) return false;
        if (piece.type == Type.PAWN && end.x == 0 && end.y == 0) return false;
        if (piece.type == Type.PAWN && end.x == 0 && end.y == 10) return false;
        if (piece.type == Type.PAWN && end.x == 10 && end.y == 0) return false;
        if (piece.type == Type.PAWN && end.x == 10 && end.y == 10) return false;

        //wrong color
        if (piece.color != turn) return false;

        System.out.printf("moving piece from %s to %s\n", start, end);
        if (turn == Suit.BLACK) turn = Suit.WHITE;
        else if (turn == Suit.WHITE) turn = Suit.BLACK;

        //move piece
        pieces.remove(piece);
        board.remove(piece.pos);
        piece.pos = end;
        pieces.add(piece);
        board.put(end, piece);
        int x = end.x;
        int y = end.y;

        //check for captures
        for (int[] dir: directions){
            int dx = dir[0], dy = dir[1];
            Piece defender = board.get(new Position(x+dx, y+dy));
            Piece Squeezer = board.get(new Position(x+2*dx, y+2*dy));

            if (defender == null || defender.color == piece.color || defender.type == KING) continue;

            if (CORNERS.contains(new Position(x+2*dx, y+2*dy))){
                System.out.println(defender.pos);
                pieces.remove(defender);
                board.remove(defender.pos);
            }
            if (Squeezer == null || Squeezer.color != piece.color) continue;
            pieces.remove(defender);
            board.remove(defender.pos);
        }

        return true;
    }

    public boolean whiteHasWon(){
        return CORNERS.contains(king.pos);
    }

    public boolean blackHasWon(){
        int x = king.pos.x, y = king.pos.y;
        for (int[] dir: directions){
            int dx = dir[0], dy = dir[1];
            Position pos = new Position(x+dx, y+dy);
            if (!(board.containsKey(pos) && board.get(pos).color == BLACK) && !CENTER.equals(pos)) return false;
        }
        return true;
    }

    Game(){
        pieces = new TreeSet<>();
        board = new TreeMap<>();

        // WHITE PIECES
        king = new Piece(WHITE, KING, new Position(5, 5));
        pieces.add(king);
        pieces.add(new Piece(WHITE, PAWN, new Position(5, 6)));
        pieces.add(new Piece(WHITE, PAWN, new Position(5, 7)));
        pieces.add(new Piece(WHITE, PAWN, new Position(6, 6)));
        pieces.add(new Piece(WHITE, PAWN, new Position(5, 4)));
        pieces.add(new Piece(WHITE, PAWN, new Position(5, 3)));
        pieces.add(new Piece(WHITE, PAWN, new Position(4, 4)));
        pieces.add(new Piece(WHITE, PAWN, new Position(6, 5)));
        pieces.add(new Piece(WHITE, PAWN, new Position(7, 5)));
        pieces.add(new Piece(WHITE, PAWN, new Position(6, 4)));
        pieces.add(new Piece(WHITE, PAWN, new Position(4, 5)));
        pieces.add(new Piece(WHITE, PAWN, new Position(3, 5)));
        pieces.add(new Piece(WHITE, PAWN, new Position(4, 6)));

        //BLACK PIECES
        pieces.add(new Piece(BLACK, PAWN, new Position(3, 0)));
        pieces.add(new Piece(BLACK, PAWN, new Position(4, 0)));
        pieces.add(new Piece(BLACK, PAWN, new Position(5, 0)));
        pieces.add(new Piece(BLACK, PAWN, new Position(6, 0)));
        pieces.add(new Piece(BLACK, PAWN, new Position(7, 0)));
        pieces.add(new Piece(BLACK, PAWN, new Position(5, 1)));

        pieces.add(new Piece(BLACK, PAWN, new Position(3, 10)));
        pieces.add(new Piece(BLACK, PAWN, new Position(4, 10)));
        pieces.add(new Piece(BLACK, PAWN, new Position(5, 10)));
        pieces.add(new Piece(BLACK, PAWN, new Position(6, 10)));
        pieces.add(new Piece(BLACK, PAWN, new Position(7, 10)));
        pieces.add(new Piece(BLACK, PAWN, new Position(5, 9)));

        pieces.add(new Piece(BLACK, PAWN, new Position(0, 3)));
        pieces.add(new Piece(BLACK, PAWN, new Position(0, 4)));
        pieces.add(new Piece(BLACK, PAWN, new Position(0, 5)));
        pieces.add(new Piece(BLACK, PAWN, new Position(0, 6)));
        pieces.add(new Piece(BLACK, PAWN, new Position(0, 7)));
        pieces.add(new Piece(BLACK, PAWN, new Position(1, 5)));

        pieces.add(new Piece(BLACK, PAWN, new Position(10, 3)));
        pieces.add(new Piece(BLACK, PAWN, new Position(10, 4)));
        pieces.add(new Piece(BLACK, PAWN, new Position(10, 5)));
        pieces.add(new Piece(BLACK, PAWN, new Position(10, 6)));
        pieces.add(new Piece(BLACK, PAWN, new Position(10, 7)));
        pieces.add(new Piece(BLACK, PAWN, new Position(9, 5)));

        for (Piece piece: pieces) board.put(piece.pos, piece);
    }
}
