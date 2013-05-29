/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package polskaad1340;

import java.util.logging.Level;
import java.util.logging.Logger;

import CLIPSJNI.Environment;
import CLIPSJNI.PrimitiveValue;

/**
 *
 * @author Kuba
 */
public class PolskaAD1340 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        //bugfix, patrz http://stackoverflow.com/questions/13575224/comparison-method-violates-its-general-contract-timsort-and-gridlayout

        // TODO code application logic here
        OknoMapy om = new OknoMapy();
                
        try {
            LadowanieMapy lm = new LadowanieMapy("/maps/example.json");
            om.importBackgroundTileGrid(lm.getMap());
            
            om.setForegroundTileGrid(om.createTileGrid(lm.getMapSize(), 0));
            om.drawAllTiles();
            
            Environment env = new Environment();
            env.load("src/logika_CLIPS/swiat_szablony.clp");
            env.load("src/logika_CLIPS/swiat_logika.clp");
            env.load("src/logika_CLIPS/test.clp");
            env.reset();
            String evalString = "(find-all-facts ((?k kratka)) TRUE)";
            PrimitiveValue pv1 = env.eval(evalString);
            for (int i = 0; i < pv1.size(); i++) {
            	System.out.print("KRATKA ");
            	System.out.print(" id: " + pv1.get(i).getFactSlot("id"));
            	System.out.print(", X: " + pv1.get(i).getFactSlot("pozycjaX"));
            	System.out.println(", Y: " + pv1.get(i).getFactSlot("pozycjaY"));
            }
            
        
        } catch (Exception ex) {
            Logger.getLogger(PolskaAD1340.class.getName()).log(Level.SEVERE, null, ex);
        }

        om.setVisible(true);
        System.out.println("done and done.");
       
    }
}
