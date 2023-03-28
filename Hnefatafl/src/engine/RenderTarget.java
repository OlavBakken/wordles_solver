package engine;
import java.awt.image.BufferedImage;

public class RenderTarget{

    int WIDTH, HEIGHT;
    BufferedImage buffer;
    double[][] zbuffer;
    int[][] idbuffer;

    public RenderTarget(int width, int height){
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        zbuffer = new double[width][height];
        idbuffer = new int[width][height];
        WIDTH = width;
        HEIGHT = height;
    }
}