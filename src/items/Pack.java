package items;

/**
 * Klasa definiująca paczki posłańców.
 * 
 * @author Piotrek
 */
public class Pack extends Item {
    
    /**
     * Masa paczki.
     * @var int
     */
    protected int _mass;
    
    /**
     * Czas dostarczenia przesyłki.
     * @var int
     */
    protected int _deliveryTime;
    
    /**
     * Dostarczona?
     * @TODO Sprawdzić potrzebność tego parametru.
     */
    protected boolean _deliveried;
    
    /**
     * Konstruktor. Nadanie paczce masy.
     * @param mass 
     */
    public Pack(String id, int mass) {
    	super(mass * 5, 0, id);
    	
    	this._mass = mass;
        this._deliveried = false;
    }
    
    /**
     * Getter dla masy paczki.
     * @return int
     */
    public int getMass() {
        return this._mass;
    }
    
    /**
     * Setter dla masy paczki.
     * @param mass
     * @return Pack
     */
    public Pack setMass(int mass) {
        this._mass = mass;
        
        return this;
    }
    
    /**
     * Getter dla flagi dostarczona.
     * @return boolean
     */
    public boolean getIsDeliveried() {
        return this._deliveried;
    }
    
    /**
     * Setter dla flagi dostarczona.
     * @param deliveried
     * @return Pack
     */
    public Pack setIsDeliveried(boolean deliveried) {
        this._deliveried = deliveried;
        
        return this;
    }
    
    /**
     * Setter dla czasu dostawy.
     * @param int deliveryTime
     * @return Pack
     */
    public Pack setDeliveryTime(int deliveryTime) {
        this._deliveryTime = deliveryTime;
        
        return this;
    }
    
    /**
     * Getter dla czasu dostawy.
     * @return int
     */
    public int getDeliveryTime() {
        return this._deliveryTime;
    }
}
