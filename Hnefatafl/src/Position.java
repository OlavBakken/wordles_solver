public class Position implements Comparable<Position>{
    int x, y;

    Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int compareTo(Position pos){
        if (this.x != pos.x) return this.x - pos.x;
        return this.y - pos.y;
    }

    public boolean equals(Position pos){
        if (pos == null) return false;
        return (this.compareTo(pos) == 0);
    }

    public String toString(){
        return String.format("<%d, %d>", x, y);
    }
}
