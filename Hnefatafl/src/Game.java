public class Game {
    Piece board[][];
    Suit turn = Suit.BLACK;
    static final int directions[][] = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    boolean move(int startX, int startY, int endX, int endY){
        // potential illegal moves
        if (board[startX][startY] == null || board[endX][endY] != null) return false;
        if (startX != endX && startY != endY) return false;
        if (startX == endX && startY == endY) return false;
        for (int x = Math.min(startX, endX) + 1; x < Math.max(startX, endX); x++) if (board[x][startY] != null) return false;
        for (int y = Math.min(startY, endY) + 1; y < Math.max(startY, endY); y++) if (board[startX][y] != null) return false;

        //illegal squares for pawns
        if (board[startX][startY].type == Type.PAWN && endX == 5 && endY == 5) return false;
        if (board[startX][startY].type == Type.PAWN && endX == 0 && endY == 0) return false;
        if (board[startX][startY].type == Type.PAWN && endX == 0 && endY == 10) return false;
        if (board[startX][startY].type == Type.PAWN && endX == 10 && endY == 0) return false;
        if (board[startX][startY].type == Type.PAWN && endX == 10 && endY == 10) return false;

        //wrong color
        if (board[startX][startY].color != turn) return false;

        System.out.printf("moving piece\n");
        if (turn == Suit.BLACK) turn = Suit.WHITE;
        else if (turn == Suit.WHITE) turn = Suit.BLACK;

        //move piece
        board[endX][endY] = board[startX][startY];
        board[startX][startY] = null;
        int x = endX;
        int y = endY;

        //check for captures
        for (int[] dir: directions){
            int dx = dir[0], dy = dir[1];
            if (x+dx < 0 || x+dx > 10) continue;
            if (y+dy < 0 || y+dy > 10) continue;
            if (board[x+dx][y+dy] == null) continue;
            if (board[x][y].color == board[x+dx][y+dy].color) continue;

            if (x+2*dx < 0 || x+2*dx > 10) {board[x+dx][y+dy] = null; continue;}
            if (y+2*dy < 0 || y+2*dy > 10) {board[x+dx][y+dy] = null; continue;}
            if (x+2*dx == 5 && y+2*dy == 5 && board[5][5] == null) {board[x+dx][y+dy] = null; continue;}

            if (board[x+2*dx][y+2*dy] == null) continue;
            if (board[x][y].color == board[x+2*dx][y+2*dy].color) {board[x+dx][y+dy] = null; continue;}
        }

        return true;
    }

    boolean whiteHasWon(){
        if (board[0][0].type == Type.KING) return true;
        if (board[0][10].type == Type.KING) return true;
        if (board[10][0].type == Type.KING) return true;
        if (board[10][10].type == Type.KING) return true;
        return false;
    }

    boolean blackHasWon(){
        for (int x = 0; x < 11; x++){
            for (int y = 0; y < 11; y++){
                if (board[x][y].type == Type.KING) return false;
            }
        }
        return false;
    }

    Game(){
        board = new Piece[11][11];
        board[5][5] = new Piece(Suit.WHITE, Type.KING);
        board[5][3] = new Piece(Suit.WHITE, Type.PAWN);
        board[5][4] = new Piece(Suit.WHITE, Type.PAWN);
        board[5][6] = new Piece(Suit.WHITE, Type.PAWN);
        board[5][7] = new Piece(Suit.WHITE, Type.PAWN);
        board[3][5] = new Piece(Suit.WHITE, Type.PAWN);
        board[4][5] = new Piece(Suit.WHITE, Type.PAWN);
        board[6][5] = new Piece(Suit.WHITE, Type.PAWN);
        board[7][5] = new Piece(Suit.WHITE, Type.PAWN);
        board[4][4] = new Piece(Suit.WHITE, Type.PAWN);
        board[4][6] = new Piece(Suit.WHITE, Type.PAWN);
        board[6][4] = new Piece(Suit.WHITE, Type.PAWN);
        board[6][6] = new Piece(Suit.WHITE, Type.PAWN);

        board[0][3] = new Piece(Suit.BLACK, Type.PAWN);
        board[0][4] = new Piece(Suit.BLACK, Type.PAWN);
        board[0][5] = new Piece(Suit.BLACK, Type.PAWN);
        board[0][6] = new Piece(Suit.BLACK, Type.PAWN);
        board[0][7] = new Piece(Suit.BLACK, Type.PAWN);
        board[1][5] = new Piece(Suit.BLACK, Type.PAWN);

        board[10][3] = new Piece(Suit.BLACK, Type.PAWN);
        board[10][4] = new Piece(Suit.BLACK, Type.PAWN);
        board[10][5] = new Piece(Suit.BLACK, Type.PAWN);
        board[10][6] = new Piece(Suit.BLACK, Type.PAWN);
        board[10][7] = new Piece(Suit.BLACK, Type.PAWN);
        board[9][5] = new Piece(Suit.BLACK, Type.PAWN);

        board[3][0] = new Piece(Suit.BLACK, Type.PAWN);
        board[4][0] = new Piece(Suit.BLACK, Type.PAWN);
        board[5][0] = new Piece(Suit.BLACK, Type.PAWN);
        board[6][0] = new Piece(Suit.BLACK, Type.PAWN);
        board[7][0] = new Piece(Suit.BLACK, Type.PAWN);
        board[5][1] = new Piece(Suit.BLACK, Type.PAWN);

        board[3][10] = new Piece(Suit.BLACK, Type.PAWN);
        board[4][10] = new Piece(Suit.BLACK, Type.PAWN);
        board[5][10] = new Piece(Suit.BLACK, Type.PAWN);
        board[6][10] = new Piece(Suit.BLACK, Type.PAWN);
        board[7][10] = new Piece(Suit.BLACK, Type.PAWN);
        board[5][9] = new Piece(Suit.BLACK, Type.PAWN);
    }

}
