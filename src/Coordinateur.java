import java.io.*;
import java.lang.Thread;
import java.util.*;
import java.util.concurrent.*;


public class Coordinateur extends Thread{
    private HashMap<String,Integer> fin;
    private ArrayList<HashMap<String, Integer>> hashMaps;
    public ArrayList<String> names;
    public String folder;
    ArrayList<Map> maps = new ArrayList<>();
    ArrayList<Reduce> reduces = new ArrayList<>();

    public Coordinateur(String folder){
        this.maps = maps;
        this.reduces = reduces;
        this.folder = folder;
        this.names= new ArrayList<String>();
        this.hashMaps = new ArrayList<HashMap<String,Integer>>();
        this.fin = new HashMap<String,Integer>();
    }


    public HashMap<String, Integer> getFin(){return fin;}

    public void addMap(Map map){this.maps.add(map);}
    public void addReduce(Reduce reduce ){this.reduces.add(reduce);}
    public void addName(String name){
        this.names.add(name);
    }
    public void addHashMap(HashMap<String,Integer> hm){this.hashMaps.add(hm);}
    public void addNames(String name){this.names.add(name);}

    public ArrayList<String> getNames() {return names;}
    public String getFolder() {return folder;}
    public ArrayList<Map> getMaps(){return maps;}
    public ArrayList<Reduce> getReduces() {return reduces;}

    private void setMap() throws FileNotFoundException {
        for(int nnumb=0;nnumb<this.maps.size()+1;nnumb++){
            this.maps.get(nnumb%this.maps.size()).addText(this.names.get(nnumb));
        }
    }

    public void startMaps(){
        for(Map map : this.maps){
            map.start();
        }
    }

    public boolean tMapsFinish(){
        for(Map map : this.maps){
            if(map.getStat() == 0) {
                return false;
            }
        }
        return true;
    }
    private void resetMaps() {
        for(Map map : this.maps){
            map.statut("reset");
            map.resetTexts();
            map.resetMap();
        }

    }
    private void setReduces(){
        for(int i=0;i<this.hashMaps.size();i++){
            this.reduces.get(i%this.reduces.size()).addHashMap(this.hashMaps.get(i));
        }
    }

    private void startReduces(){
            for(Reduce red : this.reduces){red.start();}
    }

    private boolean tReducesFinish(){
        for(Reduce reduce : this.reduces){
            if(reduce.getStat() == 0){
                return false;
            }
        }
        return true;
    }
    private void resetReduces(){
        for(Reduce red : this.reduces){
            red.statut("reset");
            red.resetHasMaps();
            red.resetRed();
        }
    }


    public void finalReduce(){
        for(HashMap<String,Integer> hm : this.hashMaps){
            Set keys = hm.keySet();
            Iterator itr = keys.iterator();
            String key = "";
            while(itr.hasNext()){
                key = (String)itr.next();
                if(this.fin.containsKey(key)){
                    this.fin.put(key, this.fin.get(key)+hm.get(key));
                }
                else{
                    this.fin.put(key,hm.get(key));
                }
            }
        }
    }




    public void run() {
        System.out.println("Le coordinateur reparti les charges entre les maps");
        try {
            this.setMap();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Le coordinateur  demande aux maps de travailler");
        this.startMaps();
        boolean testm = false;
        int i=0;
        while(testm!=true){
            testm = this.tMapsFinish();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.resetMaps();
        System.out.println("Tous les maps ont fini de travailler");
        System.out.println("Le coordinateur répartie les charges entre les reduces");
        this.setReduces();
        System.out.println("Les reduces demande aux reduces de travailler");
        this.hashMaps = new ArrayList<HashMap<String, Integer>>();
        this.startReduces();
        boolean testr = false;
        while(testr!=true){
            testr = this.tReducesFinish();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println("Les reduces ont fini de travailler");
        System.out.println("Un dernier reduce pour la fin");

        this.finalReduce();

        System.out.println("C'est fini");
        System.out.println(this.fin);


    }



}
