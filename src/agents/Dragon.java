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
}
