package agents;
import java.util.ArrayList;

import polskaad1340.window.OknoMapy;
import statistics.KnightStatistics_Interface;
import world.MapFrame;
import CLIPSJNI.PrimitiveValue;
import agents.skills.Attack;

/**
 * Klasa definiująca rycerza.
 * @author Piotrek
 */
public class Knight extends Agent {
    
    /**
     * Zbroja rycerza.
     * @var Armor
     */
    private String _armor;
    
    /**
     * Lista ataków agenta.
     * @var ArrayList<Attack>
     */
    private ArrayList<Attack> _attacks;
    
    private KnightStatistics_Interface _stat;
    
    public Knight(String id, String pathToClipsFile, String clipsResultFile, MapFrame mapFrame, OknoMapy om) {
    	super(id, pathToClipsFile, clipsResultFile);
    	this._attacks = new ArrayList<Attack>();
    	this.velocity = 2;
    	this.fieldOfView = 2;
    	this.possibleMove = 2;
    	this.mapFrame = mapFrame;
    	
    	 this.opp = om.nowyObiektPierwszegoPlanu(mapFrame.getX(), mapFrame.getY(), this.id, 1274);
    }
    
    public Knight() {
    	this.mapFrame = new MapFrame();
    	this._attacks = new ArrayList<Attack>();
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
			this._armor = !pv.getFactSlot("zbroja").toString().equalsIgnoreCase("nil")? pv.getFactSlot("zbroja").toString() : null; 

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("(rycerz");
		if (_armor != null) {
			buffer.append(" ( zbroja ");
			buffer.append(_armor);
			buffer.append(" ) ");
		}
		buffer.append(" (ataki ");
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

	public ArrayList<Attack> get_attacks() {
		return _attacks;
	}

	public void set_attacks(ArrayList<Attack> _attacks) {
		this._attacks = _attacks;
	}

	public KnightStatistics_Interface get_stat() {
		return _stat;
	}

	public void set_stat(KnightStatistics_Interface _stat) {
		this._stat = _stat;
	}

}
