import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameServer extends Thread{
    static final int NEXT_MOVE = 0;
    static final int FORFEIT = 0;

    MessagePasser player1;
    MessagePasser player2;
    GameView game;

    GameServer(Socket player1, Socket player2, GameView game) throws Exception{
        this.player1 = new MessagePasser(player1);
        this.player2 = new MessagePasser(player2);
        this.game = game;
    }

    public void run() {
        try {
            player1.sendMessage("PLAYER1");
            player2.sendMessage("PLAYER2");

            String move;
            Pattern movePattern = Pattern.compile("MOVE:<(\\d+),(\\d+)>,<(\\d+),(\\d+)>"); // matches f.ex "MOVE:<1,3>,<4,5>"
            Matcher moveMatcher;

            while (true) {
                //move from player 1
                move = player1.receiveMessage();
                moveMatcher = movePattern.matcher(move);
                moveMatcher.find();
                Position start = new Position(Integer.parseInt(moveMatcher.group(1)), Integer.parseInt(moveMatcher.group(2)));
                Position end = new Position(Integer.parseInt(moveMatcher.group(3)), Integer.parseInt(moveMatcher.group(4)));
                game.move(start, end);
                player2.sendMessage(String.format("MOVE:<%d,%d>,<%d,%d>", start.x, start.y, end.x, end.y));
                if (game.whiteHasWon()){
                    player1.close();
                    player2.close();
                    break;
                }

                //move from player2
                move = player2.receiveMessage();
                moveMatcher = movePattern.matcher(move);
                moveMatcher.find();
                start = new Position(Integer.parseInt(moveMatcher.group(1)), Integer.parseInt(moveMatcher.group(2)));
                end = new Position(Integer.parseInt(moveMatcher.group(3)), Integer.parseInt(moveMatcher.group(4)));
                game.move(start, end);
                player1.sendMessage(String.format("MOVE:<%d,%d>,<%d,%d>", start.x, start.y, end.x, end.y));
                if (game.whiteHasWon()){
                    player1.close();
                    player2.close();
                    break;
                }

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
