package agents;

import items.Horse;
import items.Item;
import items.Pack;

import java.util.ArrayList;
import java.util.Arrays;

import polskaad1340.window.OknoMapy;
import world.MapFrame;
import CLIPSJNI.PrimitiveValue;
import clips.ClipsEnvironment;

/**
 * Klasa definiująca posłańca.
 * 
 * @author Piotrek
 */
public class Courier extends Agent {
    
    /**
     * Udźwig.
     */
    protected int capacity;
    
    /**
     * Lista paczek.
     */
    protected ArrayList<String> packages;
    
    /**
     * Koń.
     */
    protected String horse;
    protected int deliveredPacks;
    protected int allPacks;
    protected int[] deliveryTimes;
    protected double deliveredToAll;
    protected double meanDeliveryTime;
    
    public Courier() {
    	this.mapFrame = new MapFrame();
    	this.packages = new ArrayList<String>();
    	this.deliveryTimes = new int[0];
    }
    
    /**
     * Konstruktor. Nadanie domyślnego udźwigu.
     */
    public Courier(String id, String pathToClipsFile, String clipsResultFile, MapFrame mapFrame, OknoMapy om) {
        super(id, pathToClipsFile, clipsResultFile);

        this.capacity = 15;
        this.packages = new ArrayList<String>();
        this.setHorse(null);
        this.mapFrame = mapFrame;
        this.velocity = 1;
        this.fieldOfView = 1;
        this.possibleMove = 1;
        this.opp = om.nowyObiektPierwszegoPlanu(mapFrame.getX(), mapFrame.getY(), this.id, 1088);
        this.deliveryTimes = new int[0];
    }
    
    
	public void loadFromClips(PrimitiveValue pv) {
		try {
			this.id = pv.getFactSlot("id").toString();
			this.velocity = pv.getFactSlot("predkosc").intValue();
			this.extraVelocity = pv.getFactSlot("dodatekPredkosc").intValue();
			this.energy = pv.getFactSlot("energia").intValue();
			this.energyLoss = pv.getFactSlot("strataEnergii").intValue();
			this.energyRecovery = pv.getFactSlot("odnawianieEnergii").intValue();
			this.gold = pv.getFactSlot("zloto").intValue();
			this.fieldOfView = pv.getFactSlot("poleWidzenia").intValue();
			this.capacity = pv.getFactSlot("udzwig").intValue();
			this.horse = !pv.getFactSlot("kon").toString().equalsIgnoreCase("nil") ? pv.getFactSlot("kon").toString() : null;
			this.target = !pv.getFactSlot("cel").toString().equalsIgnoreCase("nil")? pv.getFactSlot("cel").toString() : null;
			this.mapFrame.setId(pv.getFactSlot("idKratki").intValue());
			this.possibleMove = pv.getFactSlot("mozliwyRuch").intValue();
			this.deliveredPacks = pv.getFactSlot("dostarczonePaczki").intValue();
			this.allPacks = pv.getFactSlot("otrzymanePaczki").intValue();
			
			String deliveryTimesTmp = pv.getFactSlot("czasyDostarczenia").toString();
			String[] deliveryTimesTmp2 = deliveryTimesTmp.replace("(", "").replace(")", "").split(" ");
			this.deliveryTimes = new int[deliveryTimesTmp2.length];
			double meanTime = 0;
			for (int i = 0; i < deliveryTimesTmp2.length; i++) {
				if (!deliveryTimesTmp2[i].equals("")) {
					this.deliveryTimes[i] = Integer.valueOf(deliveryTimesTmp2[i]);
					meanTime += this.deliveryTimes[i]; 
				}
			}
			this.meanDeliveryTime = meanTime/(double)this.deliveredPacks;
			
			if (this.allPacks != 0) {
				this.deliveredToAll = (double)this.deliveredPacks / (double)this.allPacks * 100.0;
			}
			
			String packsTmp = pv.getFactSlot("paczki").toString();
			String[] packs = packsTmp.replace("(", "").replace(")", "").split(" ");
			this.packages = new ArrayList<String>(Arrays.asList(packs));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Item> findItems(ClipsEnvironment clipsEnv) {
		ArrayList<Item> foundItems = new ArrayList<Item>();
		
		if (this.horse != null) {
			try {
				String evalString = "(find-all-facts ((?k kon))(eq ?k:id " + this.horse + "))";
				PrimitiveValue pv1 = clipsEnv.getWorldEnv().eval(evalString);
				Horse horseTmp = new Horse(pv1.get(0));
				foundItems.add(horseTmp);
			} catch (Exception e) {

			}
		}

		if (!this.packages.isEmpty()) {
			for (String pack : this.packages) {
				try {
					if (!pack.equals("")) {
						String evalString = "(find-all-facts ((?p paczka))(eq ?p:id " + pack + "))";
						PrimitiveValue pv1 = clipsEnv.getWorldEnv().eval(evalString);
						Pack packTmp = new Pack(pv1.get(0));
						foundItems.add(packTmp);
					}
				} catch (Exception e) {

				}
			}
		}
		
		return foundItems;
	}
    
    public int getCapacity() {
        return this.capacity;
    }
    
    public Courier setCapacity(int capacity) {
        this.capacity = capacity;
        
        return this;
    }
    
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("(poslaniec ( udzwig ");
		buffer.append(this.capacity);
		buffer.append(" )"); 
		if (!this.packages.isEmpty()) {
			buffer.append(" ( paczki ");
			for (int i = 0; i < this.packages.size(); i++) {
				if (!this.packages.get(i).equals("")) {
					buffer.append(this.packages.get(i));
					buffer.append(" ");
				}
			}
			buffer.append(")");
		}
		
		if (this.deliveryTimes.length > 0) {
			buffer.append(" ( czasyDostarczenia ");
			for (int i = 0; i < this.deliveryTimes.length; i++) {
				buffer.append(this.deliveryTimes[i]);
				buffer.append(" ");
			}
			buffer.append(")");
		}
		
		if (horse != null) {
			buffer.append(" ( kon ");
			buffer.append(this.horse).append(" )");
		}
		buffer.append(" ( id ");
		buffer.append(this.id);
		buffer.append(" ) ( poleWidzenia ");
		buffer.append(this.fieldOfView);
		buffer.append(" ) ( predkosc ");
		buffer.append(this.velocity);
		buffer.append(" ) ( dodatekPredkosc ");
		buffer.append(this.extraVelocity);
		buffer.append(" ) ( energia ");
		buffer.append(this.energy);
		buffer.append(" ) ( strataEnergii ");
		buffer.append(this.energyLoss);
		buffer.append(" ) ( odnawianieEnergii ");
		buffer.append(this.energyRecovery);
		buffer.append(" ) ( dostarczonePaczki ");
		buffer.append(this.deliveredPacks);
		buffer.append(" ) ( otrzymanePaczki ");
		buffer.append(this.allPacks);
		buffer.append(" ) ( dostarczoneDoOtrzymane ");
		buffer.append(this.deliveredToAll);
		buffer.append(" ) ( sredniCzasDostarczenia ");
		buffer.append(this.meanDeliveryTime);
		buffer.append(" ) ( zloto ");
		buffer.append(this.gold);
		buffer.append(" ) ( mozliwyRuch ");
		buffer.append(this.possibleMove);
		buffer.append(" ) ( idKratki ");
		buffer.append(this.mapFrame.getId());
		if (this.target != null) {
			buffer.append(" ) ( cel ");
			buffer.append(this.target);
		}
		buffer.append(" ) )");
		return buffer.toString();
	}

	public String getHorse() {
		return horse;
	}

	public void setHorse(String horse) {
		this.horse = horse;
	}

	public ArrayList<String> getPackages() {
		return packages;
	}

	public void setPackages(ArrayList<String> packages) {
		this.packages = packages;
	}

}
