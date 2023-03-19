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
    boolean pieceHeld;
    int heldX, heldY;
    int mouseX, mouseY;
    int startX, endX, startY, endY;
    boolean executeMove;
    Random random = new Random();

    void doRandomMove(){
        while (!game.move(random.nextInt(11), random.nextInt(11), random.nextInt(11), random.nextInt(11)));
    }

    GUI(){
        game = new Game();
        doRandomMove();
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / dx - 1;
                int y = e.getY() / dy - 1;
                if (x > 10 || y > 10) return;
                if (game.board[x][y] == null) return;
                pieceHeld = true;
                heldX = x;
                heldY = y;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int x = e.getX() / dx - 1;
                int y = e.getY() / dy - 1;
                System.out.printf("trying to move <%d, %d> to <%d, %d>\n", heldX, heldY, x, y);
                if (!pieceHeld) return;
                pieceHeld = false;
                if (x > 10 || y > 10) return;
                startX = heldX;
                startY = heldY;
                endX = x;
                endY = y;
                executeMove = true;
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
        if (executeMove){
            executeMove = false;
            game.move(startX, startY, endX, endY);
            doRandomMove();
        }
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

        for (int x = 0; x < 11; x++){
            for (int y = 0; y < 11; y++){
                if (pieceHeld && x == heldX && y == heldY) continue;
                if (game.board[x][y] == null) continue;
                if (game.board[x][y].type == Type.KING) g.fillRect((x+1)*dx, (y+1)*dy, dx, dy);
                else if (game.board[x][y].color == Suit.WHITE) g.fillOval((x+1)*dx, (y+1)*dy, dx, dy);
                else g.drawOval((x+1)*dx, (y+1)*dy, dx, dy);
            }
        }

        if (pieceHeld){
            if (game.board[heldX][heldY].type == Type.KING) g.fillRect(mouseX - dx/2, mouseY - dy/2, dx, dy);
            else if (game.board[heldX][heldY].color == Suit.WHITE) g.fillOval(mouseX - dx/2, mouseY - dy/2, dx, dy);
            else g.drawOval(mouseX - dx/2, mouseY - dy/2, dx, dy);
        }
    }
}
