import engine.*;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

public class Test {
    static Scene scene;

    public static void main(String[] args) throws Exception{
        Frame frame = new Frame();
        frame.setSize(800, 800);
        scene = new Scene();
        TexturedModel model = new TexturedModel();
        model.mesh = Assets.getCube();
        model.z = 5;
        model.texture.setRGB(0,0, Color.WHITE.getRGB());
        scene.addModel(model, "cube");
        scene.addCamera(new Camera(), "camera");
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(42);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(42);
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

        frame.add(new BufferedPanel(){
            BufferedImage buffer;

            @Override
            public void paint(Graphics g){
                buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics bufferGraphics = buffer.getGraphics();
                bufferGraphics.setColor(Color.BLACK);
                bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
                scene.render("camera", buffer);
                g.drawImage(buffer, 0, 0, null);
            }
        });

        frame.setVisible(true);
        while (true){
            model.z += 0.1;
            Thread.sleep(100);
            frame.getComponent(0).repaint();
        }
    }
}
