package engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;

public class Assets {
    static double points[][] = {
        {-1, -1, -1},
        {-1, -1, 1},
        {-1, 1, -1},
        {-1, 1, 1},
        {1, -1, -1},
        {1, -1, 1},
        {1, 1, -1},
        {1, 1, 1},
    };
    
    static double triangles[][][] = {
        {points[0], points[2], points[1]},
        {points[1], points[2], points[3]},
        {points[4], points[6], points[5]},
        {points[5], points[6], points[7]},

        {points[0], points[1], points[4]},
        {points[4], points[1], points[5]},
        {points[2], points[3], points[6]},
        {points[6], points[3], points[7]},

        {points[0], points[2], points[4]},
        {points[4], points[2], points[6]},
        {points[1], points[3], points[5]},
        {points[5], points[3], points[7]},
    };

    static double uvpoints[][] = {
        {0, 0},
        {0, 3},
        {3, 0},
        {3, 3}
    };

    static double uvTriangles[][][] = {
        {uvpoints[0], uvpoints[1], uvpoints[2]},
        {uvpoints[2], uvpoints[1], uvpoints[3]},
        {uvpoints[0], uvpoints[1], uvpoints[2]},
        {uvpoints[2], uvpoints[1], uvpoints[3]},

        {uvpoints[0], uvpoints[1], uvpoints[2]},
        {uvpoints[2], uvpoints[1], uvpoints[3]},
        {uvpoints[0], uvpoints[1], uvpoints[2]},
        {uvpoints[2], uvpoints[1], uvpoints[3]},

        {uvpoints[0], uvpoints[1], uvpoints[2]},
        {uvpoints[2], uvpoints[1], uvpoints[3]},
        {uvpoints[0], uvpoints[1], uvpoints[2]},
        {uvpoints[2], uvpoints[1], uvpoints[3]},
    };

    public static Mesh getCube(){
        Mesh cube = new Mesh();
        for (int i = 0; i < 12; i++) cube.addTriangle(triangles[i], uvTriangles[i]);
        return cube;
    }

    public static Mesh readOBJ(String filepath)throws Throwable{
        Random r = new Random();
        BufferedReader br = new BufferedReader(new FileReader(new File(filepath)));
        Mesh mesh = new Mesh();
        while (true){
            String input = br.readLine();
            if (input == null) break;
            String tokens[] = input.split(" ");
            if (tokens[0].compareTo("v") == 0){
                //System.out.println(input);
                //for (String token: tokens) System.out.println(token);
                mesh.vertices.add(new Point(
                    Double.parseDouble(tokens[2]),
                    -Double.parseDouble(tokens[4]), //swapping x-z axes, flip y
                    Double.parseDouble(tokens[3])));
            }
            if (tokens[0].compareTo("vt") == 0){
                double u = Double.parseDouble(tokens[1]);
                double v = 0;
                if (tokens.length > 2) v = Double.parseDouble(tokens[2]);
                mesh.uv.add(new Point(u, v, 0));
            }
            if (tokens[0].compareTo("f") == 0){
                int t0 = Integer.parseInt(tokens[1].split("/")[0])-1;
                int uv0 = tokens[1].split("/").length > 1 ? Integer.parseInt(tokens[1].split("/")[1])-1 : r.nextInt(3);
                int t1;
                int uv1;
                int t2 = Integer.parseInt(tokens[2].split("/")[0])-1;
                int uv2 = tokens[2].split("/").length > 1 ? Integer.parseInt(tokens[2].split("/")[1])-1 : r.nextInt(3);
                for (int i = 3; i < tokens.length; i++){
                    t1 = t2;
                    uv1  = uv2;
                    t2 = Integer.parseInt(tokens[i].split("/")[0])-1;
                    uv2 = tokens[i].split("/").length > 1 ? Integer.parseInt(tokens[i].split("/")[1])-1 : r.nextInt(3);
                    Integer[] triangle = {
                        t0,
                        t1,
                        t2,
                        uv0,
                        uv1,
                        uv2
                    };

                    mesh.triangles.add(triangle);
                }
            }
        }
        br.close();
        return mesh;
    }
}