package engine;

import java.awt.*;
import java.util.ArrayList;

public abstract class Engine {
    ArrayList<GameObject> gameObjects;
    int WIDTH = 800, HEIGHT = 800;
    String TITLE = "no title";

    public abstract void init();

    public void run() throws Exception{
        init();
        Frame frame = new Frame();
        frame.setSize(WIDTH, HEIGHT);
        frame.setTitle(TITLE);
        Panel panel = new BufferedPanel();
        frame.add(panel);
        while (true) {
            Thread.sleep(33);
            for (GameObject gameObject: gameObjects){
                if (gameObject.properties.contains(GameObjectProperties.VISIBLE)) System.out.println();
                if (gameObject.properties.contains(GameObjectProperties.CLICKABLE)) System.out.println();
            }
        }
    }
}
