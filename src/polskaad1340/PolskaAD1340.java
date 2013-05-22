/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package polskaad1340;

import java.util.logging.Level;
import java.util.logging.Logger;

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
            
        
        } catch (Exception ex) {
            Logger.getLogger(PolskaAD1340.class.getName()).log(Level.SEVERE, null, ex);
        }

        om.setVisible(true);
        System.out.println("done and done.");
       
    }
}
