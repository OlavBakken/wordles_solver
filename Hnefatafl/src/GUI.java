import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

public class GUI extends Panel {
    Game game;
    BufferedImage buffer;
    int dx;
    int dy;
    Piece heldPiece;
    int mouseX, mouseY;
    int startX, endX, startY, endY;
    boolean executeMove;
    Random random = new Random();

    GUI(){
        game = new Game();
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / dx - 1;
                int y = e.getY() / dy - 1;
                if (!game.board.containsKey(new Position(x, y))) return;
                heldPiece = game.board.get(new Position(x, y));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int x = e.getX() / dx - 1;
                int y = e.getY() / dy - 1;
                if (heldPiece == null) return;
                if (x > 10 || y > 10) return;
                game.move(heldPiece, new Position(x, y));
                if (game.whiteHasWon()) System.out.printf("white has won\n");
                if (game.blackHasWon()) System.out.printf("black has won\n");
                heldPiece = null;
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
    }

    @Override
    public void update(Graphics g){
        if (buffer == null) buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        paint(buffer.getGraphics());
        g.drawImage(buffer, 0, 0, null);
    }

    @Override
    public void paint(Graphics g){
        dx = getWidth() / 13;
        dy = getHeight() / 13;

        g.setColor(Color.BLACK);
        g.fillRect(0,0, getWidth(), getHeight());
        g.setColor(Color.WHITE);

        for (Piece piece: game.pieces){
                if (heldPiece != null && heldPiece.equals(piece)) continue;
                if (piece.type == Type.KING) g.fillRect((piece.pos.x+1)*dx, (piece.pos.y+1)*dy, dx, dy);
                else if (piece.color == Suit.WHITE) g.fillOval((piece.pos.x+1)*dx, (piece.pos.y+1)*dy, dx, dy);
                else g.drawOval((piece.pos.x+1)*dx, (piece.pos.y+1)*dy, dx, dy);
        }

        if (heldPiece != null){
            if (heldPiece.type == Type.KING) g.fillRect(mouseX - dx/2, mouseY - dy/2, dx, dy);
            else if (heldPiece.color == Suit.WHITE) g.fillOval(mouseX - dx/2, mouseY - dy/2, dx, dy);
            else g.drawOval(mouseX - dx/2, mouseY - dy/2, dx, dy);
        }
    }
}
