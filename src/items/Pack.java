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
     * Dostarczona?
     * @TODO Sprawdzić potrzebność tego parametru.
     */
    protected boolean _deliveried;
    
    /**
     * Konstruktor. Nadanie paczce masy.
     * @param mass 
     */
    public void Package(int mass) {
        this._mass = mass;
        this._deliveried = false;
        
        super.Item(this.getMass() * 5, 0);
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
}
