package agents.skills;

/**
 * Klasa opisująca ataki agenta.
 * @author Piotrek
 */
public class Attack {
    
	/**
	 * Identyfikator ataku
	 */
	private String _id;
	
    /**
     * Wartość zadanych obrażeń tym atakiem.
     * @var int
     */
    protected int _value;
    
    /**
     * Utrata energii po użyciu tego ataku.
     * @var int
     */
    protected int _energyLoss;
    
    /**
     * Konstruktor. Ustawienie domyślnej wartości ataku i utraty energii po jego użyciu.
     * @param int value
     * @param int energyLoss 
     */
    public Attack(int value, int energyLoss, String id) {
        this.setValue(value);
        this.setEnergyLoss(energyLoss);
        this._id = id;
    }
    
    /**
     * Getter dla wartości ataku.
     * @return int
     */
    public int getValue() {
        return this._value;
    }
    
    /**
     * Setter dla wartości ataku.
     * @param int value
     * @return Attack
     */
    public Attack setValue(int value) {
        this._value = value;
        
        return this;
    }
    
    /**
     * Getter dla utraty energii.
     * @return int
     */
    public int getEnergyLoss() {
        return this._energyLoss;
    }
    
    /**
     * Setter dla utraty energii.
     * @param int energyLoss
     * @return Attack
     */
    public Attack setEnergyLoss(int energyLoss) {
        this._energyLoss = energyLoss;
        
        return this;
    }

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}
}
