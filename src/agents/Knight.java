package agents;
import items.*;

/**
 * Klasa definiująca rycerza.
 * @author Piotrek
 */
public class Knight extends Agent {
    
    /**
     * Zbroja rycerza.
     * @var Armor
     */
    protected Armor _armor;
    
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
}
