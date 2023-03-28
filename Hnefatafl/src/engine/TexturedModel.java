package engine;

import java.awt.image.*;

public class TexturedModel implements Renderable{

    public BufferedImage texture = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
    public double x = 0, y = 0, z = 0;
    public double xz = 0, yz = 0, xy = 0;
    public Matrix bufferedTransformation;
    public Mesh mesh;

    public boolean inBoundingBox(Matrix transform) {
        return true;
    }

    public Iterable<Point[]> getClip(Matrix transform) {
        return mesh.getClip(transform);
    }

    public void render(BufferedImage buffer, double[][] zbuffer, Matrix transform){
        Matrix modelTransform = transform.multiply(getTransform());
        if (!inBoundingBox(modelTransform)) return;
        int WIDTH = buffer.getWidth(), HEIGHT = buffer.getHeight();
        for (Point[] triangle: mesh.getClip(modelTransform)){
            Point p1 = triangle[0], p2 = triangle[1], p3 = triangle[2];
            Point uv1 = triangle[3], uv2 = triangle[4], uv3 = triangle[5];
            shadeTextureTriangle(
                p1.projection(WIDTH, HEIGHT),
                p2.projection(WIDTH, HEIGHT),
                p3.projection(WIDTH, HEIGHT),
                buffer,
                zbuffer,
                uv1,
                uv2,
                uv3,
                texture);
        }
    }

    public TexturedModel addTriangle(double[][] vertices, double[][] uv) {
        if (mesh == null) mesh = new Mesh();
        mesh.addTriangle(vertices, uv);
        return this;
    }

    Matrix getTransform() {
        return Matrix.identity().translate(x, y, z).rotateY(xz).rotateZ(xy).rotateX(yz); //TODO implement general rotation
    }

    public void setTexture(BufferedImage texture) {
        this.texture = texture;
    }

    public void shadeTextureTriangle(Point p1, Point p2, Point p3, BufferedImage buffer, double[][] zbuffer, Point uv1, Point uv2, Point uv3, BufferedImage texture){
        if (p1.getY() > p2.getY()){
            Point tmp = p1;
            p1 = p2;
            p2 = tmp;
            tmp = uv1;
            uv1 = uv2;
            uv2 = tmp;
        }
        if (p2.getY() > p3.getY()){
            Point tmp = p3;
            p3 = p2;
            p2 = tmp;
            tmp = uv3;
            uv3 = uv2;
            uv2 = tmp;
        }
        if (p1.getY() > p2.getY()){
            Point tmp = p1;
            p1 = p2;
            p2 = tmp;
            tmp = uv1;
            uv1 = uv2;
            uv2 = tmp;
        }
        int start = Math.max(0, (int) p1.getY() + 1);
        double x12[] = interpolate(p1.getY(), p1.getX(), p2.getY(), p2.getX(), 0, buffer.getHeight()-1);
        double x13[] = interpolate(p1.getY(), p1.getX(), p3.getY(), p3.getX(), 0, buffer.getHeight()-1);
        double x23[] = interpolate(p2.getY(), p2.getX(), p3.getY(), p3.getX(), 0, buffer.getHeight()-1);
        double z12[] = interpolate(p1.getY(), p1.getZ(), p2.getY(), p2.getZ(), 0, buffer.getHeight()-1);
        double z13[] = interpolate(p1.getY(), p1.getZ(), p3.getY(), p3.getZ(), 0, buffer.getHeight()-1);
        double z23[] = interpolate(p2.getY(), p2.getZ(), p3.getY(), p3.getZ(), 0, buffer.getHeight()-1);
        double uvx12[] = interpolate(p1.getY(), uv1.getX(), p2.getY(), uv2.getX(), 0, buffer.getHeight()-1);
        double uvx13[] = interpolate(p1.getY(), uv1.getX(), p3.getY(), uv3.getX(), 0, buffer.getHeight()-1);
        double uvx23[] = interpolate(p2.getY(), uv2.getX(), p3.getY(), uv3.getX(), 0, buffer.getHeight()-1);
        double uvy12[] = interpolate(p1.getY(), uv1.getY(), p2.getY(), uv2.getY(), 0, buffer.getHeight()-1);
        double uvy13[] = interpolate(p1.getY(), uv1.getY(), p3.getY(), uv3.getY(), 0, buffer.getHeight()-1);
        double uvy23[] = interpolate(p2.getY(), uv2.getY(), p3.getY(), uv3.getY(), 0, buffer.getHeight()-1);
        for (int i = 0; i < x12.length; i++) shadeTextureLine(x12[i], z12[i], x13[i], z13[i], start + i, buffer, zbuffer, uvx12[i], uvy12[i], uvx13[i], uvy13[i], texture);
        for (int i = x12.length; i < x13.length; i++) shadeTextureLine(x23[i - x12.length], z23[i - x12.length], x13[i], z13[i], start + i, buffer, zbuffer,
        uvx23[i - x12.length], uvy23[i - x12.length], uvx13[i], uvy13[i], texture);
    }

    public void shadeTextureLine(double x0, double z0, double x1, double z1, int y, BufferedImage buffer, double[][] zbuffer,
    double uvx0, double uvy0, double uvx1, double uvy1, BufferedImage texture){
        if (x0 > x1){
            double tmp = x0;
            x0 = x1;
            x1 = tmp;
            tmp = uvx0;
            uvx0 = uvx1;
            uvx1 = tmp;
            tmp = uvy0;
            uvy0 = uvy1;
            uvy1 = tmp;
            tmp = z0;
            z0 = z1;
            z1 = tmp;
        }
        int start = Math.max(0, (int) x0 + 1);
        int end = Math.min(buffer.getWidth(), (int) x1 + 1);
        double z_inv[] = interpolate(x0, z0, x1, z1, 0, buffer.getWidth());
        double uvxs[] = interpolate(x0, uvx0, x1, uvx1, 0, buffer.getWidth());
        double uvys[] = interpolate(x0, uvy0, x1, uvy1, 0, buffer.getWidth());
        for (int x = start; x < end; x++){
            if (zbuffer[x][y] < z_inv[x-start]){
                zbuffer[x][y] = z_inv[x-start];
                double color_sub = (1 + 0.02/(zbuffer[x][y]*zbuffer[x][y]));
                int brightness = Math.max(Math.min(255, (int) (255 - 20/zbuffer[x][y])), 0);
                int color = 0xFFFFFFFF & getPixel(uvxs[x-start], uvys[x-start], texture);
                int red = 0xFF&(color>>16);
                int green = 0xFF&(color>>8);
                int blue = 0xFF&color;
                red = (int) (red/color_sub);
                green = (int) (green/color_sub);
                blue = (int) (blue/color_sub);
                int color2 = 0xFF000000 | (red<<16) | (green<<8) | blue;
                int color3 = brightness<<24 | color;
                buffer.setRGB(x, y, color2);
            }
        }
    }

    private double[] interpolate(double x0, double y0, double x1, double y1, int clamp0, int clamp1){
        int start = Math.max(clamp0, (int) x0+1);
        int end = Math.min(clamp1, (int) x1+1);
        if (start >= end){
            return new double[0];
        }   
        double results[] = new double[end - start];
        double a = (y1 - y0) / (x1 - x0);
        double b = y0 - a*x0;
        for (int i = 0; i < end  - start; i++) results[i] = a*(start + i) + b;
        return results;
    }

    private int getPixel(double x, double y, BufferedImage texture){
        int w = texture.getWidth(), h = texture.getHeight();
        return texture.getRGB( ((int) (x*w) % w + w) % w, ((int) (y*h) % h + h) % h);
    }
}