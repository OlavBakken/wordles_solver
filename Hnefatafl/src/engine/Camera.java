package engine;

public class Camera{
    public double x = 0, y = 0, z = 0; // position
    public double xz = 0, yz = 0; // rotation

    public Camera(){
        x = 0;
        y = 0;
        z = 0;
        xz = 0;
        yz = 0;
    }

    Matrix getTransform(){
        return Matrix.identity().rotateX(-yz).rotateY(-xz).translate(-x, -y, -z); //TODO more rotations
    }
}