package items;

/**
 * Klasa definiująca konie.
 * 
 * @author Piotrek
 */
public class Horse extends Item {
	
    /**
     * Prędkość konia.
     * @var int
     */
    protected int _velocity;
    
    /**
     * Udźwig konia
     * @var int
     */
    protected int _capacity;
    
    /**
     * Zmniejszenie zmęczenia agenta w procentach wyrażonych jako liczba np. 0.3 dla 30%.
     * @var float
     */
    protected float _agentEnergyLossDecrease;
    
    /**
     * Konstruktor. Ustawienie domyślnej prędkości, udźwigu, zmiejszenia zmęczenia agenta, 
     * ceny, szybkości zużycia.
     * @param int velocity
     * @param int capacity 
     * @param int agentEnergyLossDecrease 
     * @param int price 
     * @param int wearSpeed 
     */
    public Horse(String id, int velocity, int capacity, int agentEnergyLossDecrease, int price, int wearSpeed) {
    	super(price, wearSpeed, id);
    	
    	this._velocity = velocity;
        this._capacity = capacity;
        this._agentEnergyLossDecrease = agentEnergyLossDecrease;
    }
    
    /**
     * Getter dla prędkości konia.
     * @return int
     */
    public int getVelocity() {
        return this._velocity;
    }
    
    /**
     * Setter dla prędkości konia
     * @param int velocity
     * @return Horse
     */
    public Horse setVelocity(int velocity) {
        this._velocity = velocity;
        
        return this;
    }
    
    /**
     * Getter dla udźwigu.
     * @return int
     */
    public int getCapacity() {
        return this._capacity;
    }
    
    /**
     * Setter dla udźwigu.
     * @param int capacity
     * @return Horse 
     */
    public Horse setCapacity(int capacity) {
        this._capacity = capacity;
        
        return this;
    }
    
    /**
     * Getter dla zmniejszenia zużycia agenta.
     * @return float
     */
    public float getAgentEnergyLossDecrease() {
        return this._agentEnergyLossDecrease;
    }
    
    /**
     * Setter dla zmniejszenia zużycia agenta.
     * @param float agentEnergyLossDecrease
     * @return Horse
     */
    public Horse setAgentEnergyLossDecrease(float agentEnergyLossDecrease) {
        this._agentEnergyLossDecrease = agentEnergyLossDecrease;
        
        return this;
    }
}
