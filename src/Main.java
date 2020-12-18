import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] arg) throws FileNotFoundException{
        Coordinateur coord = new Coordinateur(System.getProperty("user.dir")+"/textes/");
        coord.addName("96.txt");
        coord.addName("l'ane.txt");
        coord.addName("Notre dame de paris.txt");
        coord.addName("Les orientalles.txt");
        Map map1 = new Map(coord,1);
        Map map2 = new Map(coord,2);
        Map map3 = new Map(coord,3);
        Reduce red1 = new Reduce(1,coord);
        Reduce red2 = new Reduce(2,coord);

        coord.start();
    }


}
