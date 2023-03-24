package engine;

public class Vector{
    public double v[];

    Vector(){
        v = new double[4];
    }

    Vector(double x, double y, double z){
        v = new double[4];
        v[0] = x;
        v[1] = y;
        v[2] = z;
        v[3] = 0d;
    }

    Vector(double x, double y, double z, double w){
        v = new double[4];
        v[0] = x;
        v[1] = y;
        v[2] = z;
        v[3] = w;
    }

    double getX(){
        return v[0];
    }

    double getY(){
        return v[1];
    }

    double getZ(){
        return v[2];
    }

    double getW(){
        return v[3];
    }

    double[] getArray(){
        return v;
    }

    double dot(Vector u){
        return dot(u.getArray());
    }

    private double dot(double[] u){
        return u[0]*v[0] + u[1]*v[1] + u[2]*v[2] + u[3]*v[3];
    }
}