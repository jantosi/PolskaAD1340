package agents;

import java.util.ArrayList;

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
	
    public Dragon(String id, String pathToClipsFile) {
    	super(id, pathToClipsFile);
    	 this.setGold(0);
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
		buffer.append(" (ataki ");
		for (int i = 0; i < this._attacks.size(); i++) {
			buffer.append(_attacks.get(i).get_id());
			if (i < this._attacks.size() - 1) {
				buffer.append(" ");
			}
		}
		buffer.append(")");

		buffer.append(" (id ");
		buffer.append(id);
		buffer.append(")");
		
		buffer.append(" (mozliwyRuch ");
		buffer.append(possibleMove);
		buffer.append(")");

		buffer.append(" (idKratki ");
		buffer.append(this.mapFrame.getId());
		buffer.append(")");
		buffer.append(" (poleWidzenia ");
		buffer.append(fieldOfView);
		buffer.append(") (predkosc ");
		buffer.append(velocity);
		buffer.append(") (energia ");
		buffer.append(energy);
		buffer.append(") (strataEnergii ");
		buffer.append(energyLoss);
		buffer.append(") (odnawianieEnergii ");
		buffer.append(energyRecovery);
		buffer.append(") (zloto ");
		buffer.append(gold);
		buffer.append("))");
		return buffer.toString();
	}

}
