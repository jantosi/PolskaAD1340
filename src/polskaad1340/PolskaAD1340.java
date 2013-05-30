/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package polskaad1340;

import items.Ax;
import items.Pack;
import items.Vehicle;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import statistics.CourierStatistics;
import statistics.KnightStatistics;
import statistics.MerchantStatistics;
import statistics.ThiefStatistics;
import statistics.WoodmanStatistics;

import agents.Agent;
import agents.Courier;
import agents.Knight;
import agents.Merchant;
import agents.Thief;
import agents.Woodman;
import agents.skills.Attack;

import clips.ClipsEnvironment;

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


            //test
            om.getForegroundTileGrid().get(0).set(0, om.tileFromNumber(1895));

            ObiektPierwszegoPlanu opp = new ObiektPierwszegoPlanu(0, 0);
            om.addObjectToForegroundList(opp);
           // opp.move(20, 20);

            om.drawAllTiles();

            ClipsEnvironment clipsEnv = new ClipsEnvironment();

            String evalString = "(find-all-facts ((?k kratka)) TRUE)";
            PrimitiveValue pv1 = clipsEnv.getWorldEnv().eval(evalString);
            for (int i = 0; i < pv1.size(); i++) {
                System.out.print("KRATKA ");
                System.out.print(" id: " + pv1.get(i).getFactSlot("id"));
                System.out.print(", X: " + pv1.get(i).getFactSlot("pozycjaX"));
                System.out.println(", Y: " + pv1.get(i).getFactSlot("pozycjaY"));
            }

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
            KnightStatistics ks = new KnightStatistics();
            ArrayList<Attack> attacks = new ArrayList<Attack>();
            attacks.add(new Attack(10, 10, "Attack1"));
            attacks.add(new Attack(20, 20, "Attack2"));
            attacks.add(new Attack(30, 30, "Attack3"));
            Knight agent = new Knight("Knight1", attacks, ks);
            agent.setGold(550);
            
            System.out.println(agent.toString());
            clipsEnv.getWorldEnv().assertString(agent.toString());
            
        } catch (Exception ex) {
            Logger.getLogger(PolskaAD1340.class.getName()).log(Level.SEVERE, null, ex);
        }

        om.setVisible(true);
        System.out.println("done and done.");

    }
}
