/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package polskaad1340;

import java.util.logging.Level;
import java.util.logging.Logger;

import polskaad1340.window.LadowanieMapy;
import polskaad1340.window.OknoMapy;
import world.Cataclysm;
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

        try {
            LadowanieMapy lm = new LadowanieMapy("/maps/example.json");
            om.importBackgroundTileGrid(lm.getMap());
            om.setForegroundTileGrid(om.createTileGrid(lm.getMapSize(), 0));
            om.drawAllTiles();

            ClipsEnvironment clipsEnv = new ClipsEnvironment();
            World world = new World(clipsEnv, lm, om);
            //world.loadFromClips();
            world.getCataclysms().add(new Cataclysm("kleska1", 23, 34, 2, 5, 3));
            world.saveToClips(clipsEnv);
            clipsEnv.getWorldEnv().run();
           
            world.changeItemPrices();
            
            
            //clipsEnv.displayWorldFacts();
            
            /*            CourierStatistics cs = new CourierStatistics();
             Courier agent = new Courier("poslaniec1", 10, cs);
             agent.setCapacity(500);
             agent.addPackage(new Pack("Pack1", 10));
             agent.addPackage(new Pack("Pack2", 10));*/

            /* MerchantStatistics ms = new MerchantStatistics();
             Merchant agent = new Merchant("kupiec1", 100, ms);*/
            /*  ThiefStatistics ts = new ThiefStatistics();
             Thief agent = new Thief("Thief1", ts);*/
            /*        WoodmanStatistics ws = new WoodmanStatistics();
             Woodman agent = new Woodman("Woodman1", 100, ws);
             agent.setGold(500);
             agent.buyAx(new Ax("Ax1", 10, 10, 10));
             agent.buyVehicle(new Vehicle("v1", 10, 10, 10));*/

//            KnightStatistics ks = new KnightStatistics();
//            ArrayList<Attack> attacks = new ArrayList<Attack>();
//            attacks.add(new Attack(10, 10, "Attack1"));
//            attacks.add(new Attack(20, 20, "Attack2"));
//            attacks.add(new Attack(30, 30, "Attack3"));
//            Knight agent = new Knight("Knight1", attacks, ks);
//            agent.setGold(550);
//            
//           // System.out.println(agent.toString());
//            clipsEnv.getWorldEnv().assertString(agent.toString());
        } catch (Exception ex) {
            Logger.getLogger(PolskaAD1340.class.getName()).log(Level.SEVERE, null, ex);
        }

        om.setVisible(true);
        System.out.println("done and done.");

    }
}
