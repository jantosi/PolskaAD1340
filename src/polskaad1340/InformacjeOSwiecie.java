/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package polskaad1340;

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

        opisyKafelkow.put(1808,"miasto");
        opisyKafelkow.put(1840,"miasto");

        opisyKafelkow.put(1940,"legowisko smoków");
        opisyKafelkow.put(1941,"legowisko smoków");
        opisyKafelkow.put(1972,"legowisko smoków");
        opisyKafelkow.put(1973,"legowisko smoków");

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
}
