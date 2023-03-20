public class Piece implements Comparable<Piece>{
    Position pos;
    Suit color;
    Type type;

    Piece(Suit color, Type type, Position pos){
        this.color = color;
        this.type = type;
        this.pos = pos;
    }

    public int compareTo(Piece piece){
        if (this.color != piece.color) return this.color.ordinal() - piece.color.ordinal();
        if (this.type != piece.type) return this.type.ordinal() - piece.type.ordinal();
        return this.pos.compareTo(piece.pos);
    }
}
