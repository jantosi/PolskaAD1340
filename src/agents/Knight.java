package agents;
import items.Armor;

import java.util.ArrayList;

import statistics.KnightStatistics_Interface;
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
    private Armor _armor;
    
    /**
     * Lista ataków agenta.
     * @var ArrayList<Attack>
     */
    private ArrayList<Attack> _attacks;
    
    private KnightStatistics_Interface _stat;
    
    public Knight(String id, String pathToClipsFile, ArrayList<Attack> attacks, KnightStatistics_Interface stat) {
    	super(id, pathToClipsFile);
    	this.setGold(0);
    	this._attacks = attacks;
    	this._stat = stat;
    }
    
    /**
     * Setter dla zbroi.
     * @param Armor armor
     * @return Knight
     */
    public Knight setArmor(Armor armor) {
        this._armor = armor;
        
        return this;
    }
    
    
    /**
     * Getter dla zbroi.
     * @return Armor
     */
    public Armor getArmor() {
        return this._armor;
    }


	public Armor get_armor() {
		return _armor;
	}


	public void set_armor(Armor _armor) {
		this._armor = _armor;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("(rycerz");
		if (_armor != null) {
			buffer.append(" (zbroja ");
			buffer.append(_armor.getId());
			buffer.append(") ");
		}
		buffer.append(" (ataki ");
		for (int i = 0; i < this._attacks.size(); i++) {
			buffer.append(_attacks.get(i).get_id());
			if (i < this._attacks.size() - 1) {
				buffer.append(" ");
			}
		}
		buffer.append(")");

		buffer.append(" (id ");
		buffer.append(_id);
		buffer.append(")");
		
		buffer.append(" (mozliwyRuch ");
		buffer.append(_possibleMove);
		buffer.append(")");

		buffer.append(" (idKratki ");
		buffer.append(this.mapFrame.getId());
		buffer.append(")");
		buffer.append(" (poleWidzenia ");
		buffer.append(_fieldOfView);
		buffer.append(") (predkosc ");
		buffer.append(_velocity);
		buffer.append(") (energia ");
		buffer.append(_energy);
		buffer.append(") (strataEnergii ");
		buffer.append(_energyLoss);
		buffer.append(") (odnawianieEnergii ");
		buffer.append(_energyRecovery);
		buffer.append(") (zloto ");
		buffer.append(_gold);
		buffer.append("))");
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
