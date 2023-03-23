import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Main2 {

    public static void main(String args[]) throws Exception{
        Frame frame = new Frame();
        frame.setSize(800, 800);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        GUI gui = new GUI(2144);
        frame.add(gui);

        frame.setVisible(true);

        while (true){
            Thread.sleep(33);
            gui.repaint();
        }
    }
}
