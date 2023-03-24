package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.image.*;
import java.awt.*;
import java.util.stream.*;

public class Scene{
    HashMap<String, TexturedModel> labels = new HashMap<>(); //allows us to access individual models by a string id
    ArrayList<TexturedModel> models = new ArrayList<>();
    HashMap<String, Camera> cameras = new HashMap<>();
    double zbuffer[][];

    public void addModel(TexturedModel m){
        models.add(m);
    }

    public void addModel(TexturedModel m, String id){
        models.add(m);
        labels.put(id, m);
    }

    public void addCamera(Camera cam, String id){
        cameras.put(id, cam);
    }

    public Camera getCameraById(String id){
        return cameras.get(id);
    }

    public void render(String id, BufferedImage buffer){
        Matrix transform = cameras.get(id).getTransform();
        int WIDTH = buffer.getWidth(), HEIGHT = buffer.getHeight();
        zbuffer = new double[WIDTH][HEIGHT];
        Graphics g = buffer.getGraphics(); //TODO implement pixelbuffer, zbuffer, idbuffer wrapper
        g.clearRect(0, 0, WIDTH, HEIGHT);
        //g.setColor(Color.WHITE);
        models.parallelStream().forEach(model -> model.render(buffer, zbuffer, transform));
        /* for (TexturedModel model: models){
            model.render(buffer, zbuffer, transform);
        }*/
    }

    public void shadeTriangle(Point p1, Point p2, Point p3, BufferedImage buffer, int color){
        if (p1.getY() > p2.getY()){
            Point tmp = p1;
            p1 = p2;
            p2 = tmp;
        }
        if (p2.getY() > p3.getY()){
            Point tmp = p3;
            p3 = p2;
            p2 = tmp;
        }
        if (p1.getY() > p2.getY()){
            Point tmp = p1;
            p1 = p2;
            p2 = tmp;
        }
        int start = (int) p1.getY() + 1;
        double x12[] = interpolate(p1.getY(), p1.getX(), p2.getY(), p2.getX());
        double x13[] = interpolate(p1.getY(), p1.getX(), p3.getY(), p3.getX());
        double x23[] = interpolate(p2.getY(), p2.getX(), p3.getY(), p3.getX());
        double z12[] = interpolate(p1.getY(), p1.getZ(), p2.getY(), p2.getZ());
        double z13[] = interpolate(p1.getY(), p1.getZ(), p3.getY(), p3.getZ());
        double z23[] = interpolate(p2.getY(), p2.getZ(), p3.getY(), p3.getZ());
        System.out.printf("%d %d %d\n", x12.length, x13.length, x23.length);
        for (int i = 0; i < x12.length; i++) shadeHorizontalLine(x12[i], z12[i], x13[i], z13[i], start + i, buffer, (i == 0 ? -1 : color));
        for (int i = x12.length; i < x13.length; i++) shadeHorizontalLine(x23[i - x12.length], z23[i - x12.length], x13[i], z13[i], start + i, buffer, (i == x13.length -1 ? -1 : color));
    }

    public void shadeHorizontalLine(double x0, double z0, double x1, double z1, int y, BufferedImage buffer, int color){
        if (x0 > x1){
            double tmp = x0;
            x0 = x1;
            x1 = tmp;
            tmp = z0;
            z0 = z1;
            z1 = tmp;
        }
        double z_inv[] = interpolate(x0, z0, x1, z1);
        int start = (int) x0 + 1;
        int end = (int) x1 + 1;
        System.out.printf("%d %d\n", end-start, z_inv.length);
        for (int x = start; x < end; x++){
            if (zbuffer[x][y] < z_inv[x-start]){
                zbuffer[x][y] = z_inv[x-start];
                buffer.setRGB(x, y, (x == start || x == end-1 ? -1 : 0xFF000000 | (color - (int) (10*1/z_inv[x-start]))) );
            }
        }
    }

    public void drawTriangle(Point p1, Point p2, Point p3, BufferedImage buffer){
        if (p1.getY() > p2.getY()){
            Point tmp = p1;
            p1 = p2;
            p2 = tmp;
        }
        if (p2.getY() > p3.getY()){
            Point tmp = p3;
            p3 = p2;
            p2 = tmp;
        }
        if (p1.getY() > p2.getY()){
            Point tmp = p1;
            p1 = p2;
            p2 = tmp;
        }
        int start = (int) p1.getY() + 1;
        double x12[] = interpolate(p1.getY(), p1.getX(), p2.getY(), p2.getX());
        double x13[] = interpolate(p1.getY(), p1.getX(), p3.getY(), p3.getX());
        double x23[] = interpolate(p2.getY(), p2.getX(), p3.getY(), p3.getX());
        System.out.printf("%d %d %d\n", x12.length, x13.length, x23.length);
        for (int i = 0; i < x12.length; i++) drawHorizontalLine(x12[i], x13[i], start + i, buffer);
        for (int i = x12.length; i < x13.length; i++) drawHorizontalLine(x23[i - x12.length], x13[i], start + i, buffer);
    } 

    public void drawHorizontalLine(double x0, double x1, double y, BufferedImage buffer){
        if (x0 > x1){
            double tmp = x0;
            x0 = x1;
            x1 = tmp;
        }
        for (int x = (int) x0; x < x1; x++){
            buffer.setRGB(x, (int) y, -1);
        }
    }

    public double[] interpolate(double x0, double y0, double x1, double y1){
        //System.out.println(x0 + " " + x1);
        int start = (int) x0+1;
        int end = (int) x1+1;
        double results[] = new double[end - start];
        double a = (y1 - y0) / (x1 - x0);
        double b = y0 - a*x0;
        for (int i = 0; i < end  - start; i++) results[i] = a*(start + i) + b;
        return results;
    }

    public double[] interpolate(double x0, double y0, double x1, double y1, int clamp0, int clamp1){
        //System.out.println(x0 + " " + x1);
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

    public void drawLine(double x0, double y0, double x1, double y1, BufferedImage buffer){
        System.out.printf("%f %f %f %f\n", x0, y0, x1, y1);
        double dx = x1 - x0;
        double dy = y1 - y0;
        if (Math.abs(dx) > Math.abs(dy)){
            if (x1 < x0){
                double tmp = x0;
                x0 = x1;
                x1 = tmp;
                tmp = y0;
                y0 = y1;
                y1 = tmp;
                dx = -dx;
                dy = -dy;
            }
            double a = dy / dx;
            double b = y0 - a*x0;
            for (int x = (int) x0; x < x1; x++){
                buffer.setRGB(x, (int) (a*x+b), -1);
            }
        }
        else {
            if (y1 < y0){
                double tmp = x0;
                x0 = x1;
                x1 = tmp;
                tmp = y0;
                y0 = y1;
                y1 = tmp;
                dx = -dx;
                dy = -dy;
            }
            double a = dx / dy;
            double b = x0 - a*y0;
            for (int y = (int) y0; y < y1; y++){
                buffer.setRGB((int) (a*y+b), y, -1);
            }
        }
    }
}