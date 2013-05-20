/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package polskaad1340;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Kuba
 */
public class LadowanieMapy {

    private ArrayList<ArrayList<Integer>> map;
    private final int tileOffset = 1;
    
    public int getMapSize()
    {
        return map.size();
    }

    public ArrayList<ArrayList<Integer>> getMap() {
        return map;
    }
    
    public LadowanieMapy(String resourcePath) throws Exception {
        StringBuilder contents = null;
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(PolskaAD1340.class.getResourceAsStream(resourcePath)))) {
            contents = new StringBuilder();
            while (reader.ready()) {
                contents.append(reader.readLine());
            }
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }

        String stringContents = contents.toString();
        
        Pattern p = Pattern.compile("data\":\\[([0-9, ]+)\\]");
        Matcher m = p.matcher(stringContents);
        m.find();

        String rawfound = m.group(1);
        String[] mapData;
        mapData = rawfound.split(",");

        System.out.println(mapData.length);
        int mapSize = (int) Math.sqrt(mapData.length);
        if (mapSize * mapSize != mapData.length) {
            throw new Exception("Mapa nie jest kwadratowa");
        }
        
        map = new ArrayList<>(mapSize);
        
        for (int i = 0; i < mapSize; i++) {
            map.add(new ArrayList<Integer>(mapSize));
            for (int j = 0; j < mapSize; j++) {
                map.get(i).add(Integer.parseInt(mapData[j+i*mapSize].trim(),10)-tileOffset);
            }
        }
    }
}
