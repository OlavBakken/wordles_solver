package engine;

public class Matrix{
    private double v[][];

    Matrix(){
        v = new double[4][4];
    }

    public static Matrix identity(){
        Matrix res = new Matrix();
        for (int i = 0; i < 4; i++){
            res.v[i][i] = 1;
        }
        return res;
    }

    public Matrix translate(double x, double y, double z){
        Matrix res = Matrix.identity();
        res.v[0][3] = x;
        res.v[1][3] = y;
        res.v[2][3] = z;
        return this.multiply(res);
    }

    public Matrix rotateX(double angle){
        Matrix res = Matrix.identity();
        res.v[1][1] = Math.cos(angle);
        res.v[1][2] = Math.sin(angle);
        res.v[2][1] = -res.v[1][2];
        res.v[2][2] = res.v[1][1];
        return this.multiply(res);
    }

    public Matrix rotateY(double angle){
        Matrix res = Matrix.identity();
        res.v[0][0] = Math.cos(angle);
        res.v[0][2] = Math.sin(angle);
        res.v[2][0] = -res.v[0][2];
        res.v[2][2] = res.v[0][0];
        return this.multiply(res);
    }

    public Matrix rotateZ(double angle){
        Matrix res = Matrix.identity();
        res.v[0][0] = Math.cos(angle);
        res.v[0][1] = Math.sin(angle);
        res.v[1][0] = -res.v[0][1];
        res.v[1][1] = res.v[0][0];
        return this.multiply(res);
    }

    @Deprecated
    public static Matrix rotate(double x, double y, double z){
        if (y != 0){
            Matrix res = Matrix.identity();
            res.v[0][0] = Math.cos(y);
            res.v[0][2] = Math.sin(y);
            res.v[2][0] = -res.v[0][2];
            res.v[2][2] = res.v[0][0];
            return res;
        }
        Matrix res = Matrix.identity();
        res.v[1][1] = Math.cos(x);
        res.v[1][2] = Math.sin(x);
        res.v[2][1] = -res.v[1][2];
        res.v[2][2] = res.v[1][1];
        return res;
        //TODO remaining rotation axes
    }

    public Matrix multiply(Matrix B){
        Matrix res = new Matrix();
        for (int i = 0; i < 4; i++){
            for (int k = 0; k < 4; k++){
                for (int j = 0; j < 4; j++){
                    res.v[i][j] += v[i][k]*B.v[k][j];
                }
            }
        }
        return res;
    }

    public Vector multiply(Vector vec){
        Vector res = new Vector();
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                res.getArray()[i] += v[i][j]*vec.getArray()[j];
            }
        }
        return res;
    }

    public Point multiply(Point p){
        Point res = new Point(0, 0, 0, 0);
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                res.getArray()[i] += v[i][j]*p.getArray()[j];
            }
        }
        return res;
    }
}