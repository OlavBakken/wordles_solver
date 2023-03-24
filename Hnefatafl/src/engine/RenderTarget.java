package engine;
import java.awt.image.BufferedImage;

public class RenderTarget{

    int WIDTH, HEIGHT;
    BufferedImage buffer = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
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