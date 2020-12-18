import java.io.*;
import java.util.*;
import java.lang.Thread;

public class Map extends Thread {
    private ArrayList<String> texts;
    private int stat;
    private Integer num;
    private HashMap<String, Integer> map;
    private Coordinateur coordinateur;
    private Scanner text;


    public Map(Coordinateur coordinateur, Integer number){
         this.coordinateur = coordinateur;
         this.coordinateur.maps.add(this);
         this.map = new HashMap<String,Integer>();
         this.num = number;
         this.texts = new ArrayList<String>();
         this.stat = 0;
    }
    public void addText(String name){
        this.texts.add(name);
    }

    public int getStat() {
        return this.stat;
    }

    public void resetMap(){this.map = new HashMap<String, Integer>();}
    public void resetTexts(){this.texts = new ArrayList<String>();}


    public void addOnMap(String text) throws IOException {
        File fichier = new File(this.coordinateur.getFolder() + text);
        FileReader fileR = new FileReader(fichier);
        BufferedReader br = new BufferedReader(fileR);
        String s;
        String[] mots = null;
        while (((s = br.readLine()) != null)) {
            mots = s.split(" ");
            for (String wrd : mots) {
                if (wrd.length() > 0) {
                    char ch = wrd.charAt(0);
                    if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) {
                        if (map.containsKey(wrd)) {
                            map.put(wrd, map.get(wrd) + 1);
                        } else {
                            map.put(wrd, 1);
                        }
                    }
                }
            }
        }
    }
    public void statut(String eta){
        if(eta=="fini"){ this.stat =1;}
        else{this.stat =0;}
    }

    @Override
    public void run(){
        System.out.println("Map"+ String.valueOf(this.num)+" commence à travailler");
        for(int i=0; i<texts.size();i++){
            try {
                this.addOnMap(this.texts.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.coordinateur.addHashMap(this.map);
        this.statut("fini");
        System.out.println(this.stat);
        System.out.println("Map"+ String.valueOf(this.num)+" a fini");

    }

}
