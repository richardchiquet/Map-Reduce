import java.io.*;
import java.util.*;
import java.lang.Thread;

public class Reduce extends Thread {
    private Integer num;
    private ArrayList<HashMap<String,Integer>> hashMaps;
    public int stat;
    public HashMap<String,Integer> red;
    private Coordinateur coordinateur;

    public Reduce(Integer number, Coordinateur coordinateur){
        this.coordinateur = coordinateur;
        this.stat = 0;
        this.hashMaps = new ArrayList<HashMap<String,Integer>>();
        this.red = new HashMap<String,Integer>();
        this.num = number;
        this.coordinateur.reduces.add(this);
    }

    public void addHashMap(HashMap<String, Integer> hm) {
        this.hashMaps.add(hm);
    }
    public void resetHasMaps(){this.hashMaps=new ArrayList<HashMap<String, Integer>>();}
    public void resetRed(){this.red = new HashMap<String, Integer>();}

    public int getStat(){return stat;}

    public void fusionRed(HashMap<String,Integer> hm){
        Set keys = hm.keySet();
        Iterator itr = keys.iterator();
        String key = "";
        while(itr.hasNext()){
            key = (String)itr.next();
            if(this.red.containsKey(key)){
                this.red.put(key, red.get(key)+hm.get(key));
            }
            else{
                red.put(key,hm.get(key));
            }
        }
    }
    public void statut(String eta){
        if(eta=="fini"){ this.stat =1;}
        else{this.stat =0;}
    }

    @Override
    public void run(){
        System.out.println("Reduce"+ String.valueOf(this.num)+" commence à travailler");
        if(this.hashMaps.size()>1){
            this.red = this.hashMaps.get(0);
            for(int i=1;i<hashMaps.size();i++){
                this.fusionRed(hashMaps.get(i));
            }
        }
        else{this.red = this.hashMaps.get(0);}
        this.statut("fini");
        this.coordinateur.addHashMap(this.red);
        System.out.println("Reduce"+ String.valueOf(this.num)+" a fini");
    }


}
