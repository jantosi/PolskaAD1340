/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package polskaad1340;

import java.util.logging.Level;
import java.util.logging.Logger;

import polskaad1340.window.ControlButtonsListeners;
import polskaad1340.window.LadowanieMapy;
import polskaad1340.window.OknoMapy;
import world.World;
import clips.ClipsEnvironment;

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
        om.setVisible(true);

		try {
			LadowanieMapy lm = new LadowanieMapy("/maps/example.json");
			om.importBackgroundTileGrid(lm.getMap());
			om.setForegroundTileGrid(om.createTileGrid(lm.getMapSize(), 0));
			om.drawAllTiles();
			
			ClipsEnvironment clipsEnv = new ClipsEnvironment();
			World world = new World(clipsEnv, lm, om);

			Inference inference = new Inference(clipsEnv, world, om);
			ControlButtonsListeners controlListeners = new ControlButtonsListeners(om, inference);
			controlListeners.activateListeners();
			

		} catch (Exception ex) {
            Logger.getLogger(PolskaAD1340.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("done and done.");

    }
}
