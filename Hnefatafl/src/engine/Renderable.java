package engine;
import java.awt.image.BufferedImage;

interface Renderable{
    public void render(BufferedImage buffer, double zbuffer[][], Matrix transform); //TODO create renderTarget Class which wraps pixelbuffer,zbuffer, possibly idbuffer
    public static int test(){return 5;}
}