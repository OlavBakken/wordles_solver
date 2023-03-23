import java.net.Socket;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client implements GameView{
    String serverAdress = "localhost";
    int serverPort;
    Game game;
    MessagePasser server;
    Suit ourColor;
    Thread callBackThread;

    Client(int port) throws Exception{
        serverPort = port;
        server = new MessagePasser(new Socket(serverAdress, serverPort));
        game = new Game();
        String init = server.receiveMessage();
        System.out.println(init);
        System.out.printf("We are %s\n", init);
        if (init.compareTo("PLAYER1") == 0) ourColor = Suit.BLACK;
        else ourColor = Suit.WHITE;
        callBackThread = new Thread(){
            @Override
            public void run(){
                try {
                    String move;
                    Pattern movePattern = Pattern.compile("MOVE:<(\\d+),(\\d+)>,<(\\d+),(\\d+)>"); // matches f.ex "MOVE:<1,3>,<4,5>"
                    Matcher moveMatcher;

                    while (true){
                        move = server.receiveMessage();
                        moveMatcher = movePattern.matcher(move);
                        moveMatcher.find();
                        Position start = new Position(Integer.parseInt(moveMatcher.group(1)), Integer.parseInt(moveMatcher.group(2)));
                        Position end = new Position(Integer.parseInt(moveMatcher.group(3)), Integer.parseInt(moveMatcher.group(4)));
                        game.move(start, end);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        callBackThread.start();
    }

    @Override
    public boolean move(Position start, Position end){
        try {
            if (game.turn != ourColor) return false;
            game.move(start, end);
            server.sendMessage(String.format("MOVE:<%d,%d>,<%d,%d>", start.x, start.y, end.x, end.y));
            return true;
        } catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    @Override
    public Piece pieceAt(Position pos) {
        return game.pieceAt(pos);
    }

    @Override
    public Suit getOurColour(){
        return ourColor;
    }

    @Override
    public TreeSet<Piece> getPieces() {
        return game.getPieces();
    }

    @Override
    public boolean whiteHasWon() {
        return game.whiteHasWon();
    }

    @Override
    public boolean blackHasWon() {
        return game.blackHasWon();
    }
}
