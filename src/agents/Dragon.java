package agents;

import java.util.ArrayList;

import polskaad1340.window.OknoMapy;
import world.MapFrame;
import CLIPSJNI.PrimitiveValue;
import agents.skills.Attack;

/**
 * Klasa definiująca smoka.
 * @author Piotrek
 */
public class Dragon extends Agent {

    /**
     * Lista ataków agenta.
     * @var ArrayList<Attack>
     */
    private ArrayList<Attack> _attacks;
    public Dragon() {
        this.mapFrame = new MapFrame();
        this._attacks = new ArrayList<Attack>();
    }
	
    public Dragon(String id, String pathToClipsFile, MapFrame mapFrame, OknoMapy om) {
    	super(id, pathToClipsFile);
    	 //this.setGold(0);
         
        this.mapFrame = mapFrame;
        this.opp = om.nowyObiektPierwszegoPlanu(mapFrame.getX(), mapFrame.getY(), this.id, 1126);
        this._attacks = new ArrayList<Attack>();
        this.setFieldOfView(5);
        this.velocity = 5;
        this.possibleMove = 5;
    }
	public void loadFromClips(PrimitiveValue pv) {
		try {
			this.id = pv.getFactSlot("id").toString();
			this.velocity = pv.getFactSlot("predkosc").intValue();
			//this.extraVelocity = pv.getFactSlot("dodatekPredkosc").intValue();
			this.energy = pv.getFactSlot("energia").intValue();
			this.energyLoss = pv.getFactSlot("strataEnergii").intValue();
			this.energyRecovery = pv.getFactSlot("odnawianieEnergii").intValue();
			this.gold = pv.getFactSlot("zloto").intValue();
			this.fieldOfView = pv.getFactSlot("poleWidzenia").intValue();
			this.mapFrame.setId(pv.getFactSlot("idKratki").intValue());
			this.possibleMove = pv.getFactSlot("mozliwyRuch").intValue();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	 /**
     * Getter dla listy wszystkich ataków.
     * @return ARrayList<Attack>
     */
    public ArrayList<Attack> getAttacks() {
        return this._attacks;
    }
    
    /**
     * Dodawanie nowego ataku.
     * @param Attack attack
     * @return Agent
     */
    public Agent addAttack(Attack attack) {
        this._attacks.add(attack);
        
        return this;
    }
    
    /**
     * Usuwanie ataku.
     * @param Attack attack
     * @return Agent
     */
    public Agent removeAttack(Attack attack) {
        if(this._attacks.indexOf(attack) != -1) {
            this._attacks.remove(attack);
        }
        
        return this;
    }
    
    @Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("(smok");
		buffer.append(" ( ataki ");
		for (int i = 0; i < this._attacks.size(); i++) {
			buffer.append(_attacks.get(i).get_id());
			if (i < this._attacks.size() - 1) {
				buffer.append(" ");
			}
		}
		buffer.append(")");

		buffer.append(" ( id ");
		buffer.append(id);
		buffer.append(" )");
		
		buffer.append(" ( mozliwyRuch ");
		buffer.append(possibleMove);
		buffer.append(" )");

		buffer.append(" ( idKratki ");
		buffer.append(this.mapFrame.getId());
		buffer.append(" )");
		buffer.append(" ( poleWidzenia ");
		buffer.append(fieldOfView);
		buffer.append(" ) ( predkosc ");
		buffer.append(velocity);
		buffer.append(" ) ( energia ");
		buffer.append(energy);
		buffer.append(" ) ( strataEnergii ");
		buffer.append(energyLoss);
		buffer.append(" ) ( odnawianieEnergii ");
		buffer.append(energyRecovery);
		buffer.append(" ) ( zloto ");
		buffer.append(gold);
		buffer.append(" ) )");
		return buffer.toString();
	}

}
