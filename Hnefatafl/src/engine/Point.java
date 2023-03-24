package engine;

import java.util.Arrays;

public class Point extends Vector{
    Point(){
        super(0, 0, 0, 1);
    }

    public Point(double x, double y, double z){
        super(x, y, z, 1);
    }

    Point(double x, double y, double z, double w){
        super(x, y, z, w);
    }

    Point(double[] values){
        v = new double[4];
        Arrays.fill(v, 1);
        for (int i = 0; i < values.length; i++) v[i] = values[i];
    }

    Point interpolate(Point p2, double a){
        return new Point(
            (1-a)*v[0] + a*p2.v[0],
            (1-a)*v[1] + a*p2.v[1],
            (1-a)*v[2] + a*p2.v[2],
            (1-a)*v[3] + a*p2.v[3]);
    }

    public Point projection(int width, int height){
        return new Point(v[0]/v[2]*width + width/2, v[1]/v[2]*height + height / 2, 1/v[2], v[3]);
    }
}