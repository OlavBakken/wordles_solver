package engine;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;

public class Mesh{
    public static final Mesh CUBE = Assets.getCube();

    //TODO: implement PYRAMID, abusing lambda functions
    public static final Mesh PYRAMID = new Function<Integer, Mesh>() {
        public Mesh apply(Integer a) {return new Mesh();}
    }.apply(0);

    public ArrayList<Point> vertices = new ArrayList<>();
    public ArrayList<Point> uv = new ArrayList<>();
    public ArrayList<Integer[]> triangles = new ArrayList<>();

    public Mesh addTriangle(double[][] vertices, double[][] uv) {
        assert (vertices.length == 3);
        for (int i = 0; i < 3; i++)
            assert (vertices[i].length == 3);
        assert (uv.length == 3);
        for (int i = 0; i < 3; i++)
            assert (uv[i].length == 2);

        ArrayList<Integer> triangle = new ArrayList<Integer>();
        for (double vertex[] : vertices) {
            triangle.add(this.vertices.size());
            this.vertices.add(new Point(vertex));
        }
        for (double vertex[] : uv) {
            triangle.add(this.uv.size());
            this.uv.add(new Point(vertex));
        }
        triangles.add(Arrays.copyOf(triangle.toArray(), 6, Integer[].class));
        return this;
    }

    public Iterable<Point[]> getClip(Matrix transform) {
        return new MeshIterable(transform);
    }
    
    public class MeshIterable implements Iterable<Point[]> {
        Matrix transform;
        Point nextTriangle[];
        Point secondTriangle[];
        boolean secondTriangleValid;
        int index;

        public Iterator<Point[]> iterator(){
            return new Iterator<Point[]>() {
                public boolean hasNext() {
                    if (secondTriangleValid){
                        nextTriangle = secondTriangle;
                        secondTriangleValid = false;
                        return true;
                    }
                    while (index < triangles.size()){
                        if (computeNextTriangle(index++)) return true;
                    }
                    return false;
                }

                public Point[] next() {
                    return nextTriangle;
                }
            };
        }

        MeshIterable(Matrix transform){
            this.transform = transform;
            index = 0;
        }
        
        private boolean computeNextTriangle(int id){
            nextTriangle = new Point[6];
            nextTriangle[0] = transform.multiply(vertices.get(triangles.get(id)[0]));
            nextTriangle[1] = transform.multiply(vertices.get(triangles.get(id)[1]));
            nextTriangle[2] = transform.multiply(vertices.get(triangles.get(id)[2]));
            nextTriangle[3] = uv.get(triangles.get(id)[3]);
            nextTriangle[4] = uv.get(triangles.get(id)[4]);
            nextTriangle[5] = uv.get(triangles.get(id)[5]);
            if (validVertices() == 3) { //triangle in front of near plane
                return true;
            }
            sortNextTriangle();
            if (validVertices() == 2){
                secondTriangleValid = true;
                secondTriangle = new Point[6];
                double s = intersection(nextTriangle[0], nextTriangle[2]);
                double t = intersection(nextTriangle[1], nextTriangle[2]);
                secondTriangle[0] = nextTriangle[0];
                secondTriangle[1] = nextTriangle[1];
                secondTriangle[2] = nextTriangle[0].interpolate(nextTriangle[2], s);
                secondTriangle[3] = nextTriangle[3];
                secondTriangle[4] = nextTriangle[4];
                secondTriangle[5] = nextTriangle[3].interpolate(nextTriangle[5], s);

                nextTriangle[0] = nextTriangle[0].interpolate(nextTriangle[2], s);
                nextTriangle[2] = nextTriangle[1].interpolate(nextTriangle[2], t);
                nextTriangle[3] = nextTriangle[3].interpolate(nextTriangle[5], s);
                nextTriangle[5] = nextTriangle[4].interpolate(nextTriangle[5], t);
                return true;
            }
            if (validVertices() == 1){
                double s = intersection(nextTriangle[0], nextTriangle[1]);
                double t = intersection(nextTriangle[0], nextTriangle[2]);
                nextTriangle[1] = nextTriangle[0].interpolate(nextTriangle[1], s);
                nextTriangle[2] = nextTriangle[0].interpolate(nextTriangle[2], t);
                nextTriangle[4] = nextTriangle[3].interpolate(nextTriangle[4], s);
                nextTriangle[5] = nextTriangle[3].interpolate(nextTriangle[5], t);
                return true;
            }
            return false;
        }

        private double intersection(Point p1, Point p2){
            return ( p1.getZ() - 0.1) / (p1.getZ() - p2.getZ());
        }

        private void sortNextTriangle(){
            if (nextTriangle[0].getZ() < nextTriangle[1].getZ()){
                Point tmp = nextTriangle[0];
                nextTriangle[0] = nextTriangle[1];
                nextTriangle[1] = tmp;
                tmp = nextTriangle[3];
                nextTriangle[3] = nextTriangle[4];
                nextTriangle[4] = tmp;
            }
            if (nextTriangle[1].getZ() < nextTriangle[2].getZ()){
                Point tmp = nextTriangle[1];
                nextTriangle[1] = nextTriangle[2];
                nextTriangle[2] = tmp;
                tmp = nextTriangle[4];
                nextTriangle[4] = nextTriangle[5];
                nextTriangle[5] = tmp;
            }
            if (nextTriangle[0].getZ() < nextTriangle[1].getZ()){
                Point tmp = nextTriangle[0];
                nextTriangle[0] = nextTriangle[1];
                nextTriangle[1] = tmp;
                tmp = nextTriangle[3];
                nextTriangle[3] = nextTriangle[4];
                nextTriangle[4] = tmp;
            }
        }

        private int validVertices(){
            int res = 0;
            for (int i = 0; i < 3; i++){
                if (nextTriangle[i].getZ() >= 0.1) res++;
            }
            return res;
        }
    }
}