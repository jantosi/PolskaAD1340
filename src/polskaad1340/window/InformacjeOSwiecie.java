/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package polskaad1340.window;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Kuba
 */
public class InformacjeOSwiecie {
//klasa-resource
    private static final Map<Integer, String> opisyKafelkow;
    static
    {
        opisyKafelkow = new HashMap<>();
        
        opisyKafelkow.put(72,"trawa");

        opisyKafelkow.put(133,"droga płatna");
        opisyKafelkow.put(577,"droga");

        opisyKafelkow.put(992,"tabliczka miasta");

        opisyKafelkow.put(1379,"wycięty las");
        opisyKafelkow.put(1061,"las");
        opisyKafelkow.put(1029,"las");
        opisyKafelkow.put(1030,"las");

        opisyKafelkow.put(1346,"krzewy");

        opisyKafelkow.put(1808,"miasto1");
        opisyKafelkow.put(1840,"miasto2");

        opisyKafelkow.put(1895,"fioletowy kryształ - obiekt testowy");
        
        opisyKafelkow.put(1940,"legowisko smoków1");
        opisyKafelkow.put(1941,"legowisko smoków2");
        opisyKafelkow.put(1972,"legowisko smoków3");
        opisyKafelkow.put(1973,"legowisko smoków4");
        
        opisyKafelkow.put(1088, "poslaniec");
        opisyKafelkow.put(1662, "drwal");
        

    }
    
    public InformacjeOSwiecie() {
        
    }

    public static Map<Integer, String> getOpisyKafelkow() {
        return opisyKafelkow;
    }
    
    public static String getOpisKafelka(int key)
    {
        return opisyKafelkow.get(key);
    }
    
    public static Set<Integer> getKluczeKafelka(String opisKafelka)
    {
        Set<Integer> matches = new HashSet<>();
        for (Map.Entry<Integer, String> entry : opisyKafelkow.entrySet()) {
            Integer integer = entry.getKey();
            String string = entry.getValue();
            
            if(string.equals(opisKafelka))
            {
                matches.add(integer);
            }
        }
        
        return matches;
    }
    
    public static boolean arePointsAdjacent(Point a, Point b)
    {
        int dx = a.x - b.x;
        int dy = a.y - b.y;
        
        if((Math.abs(dx)==1 && dy==0)||(Math.abs(dy)==1 && dx==0))
        {
            return true;
        }
        else {
            return false;
        }
    }
    
    public static Point[] getNeighboursOfPoint(Point p)
    {
        Point[] neighs = new Point[4]; //neigh1, neigh2, neigh3, neigh4;
        
        neighs[0] = (Point) p.clone();
        neighs[1] = (Point) p.clone();
        neighs[2] = (Point) p.clone();
        neighs[3] = (Point) p.clone();

        neighs[0].translate(-1, 0);
        neighs[1].translate(1, 0);
        neighs[2].translate(0, -1);
        neighs[3].translate(0, 1);
        //czterech sąsiadów punktu; lewo, prawo, góra, dół.
        
        return neighs;
    }
}
