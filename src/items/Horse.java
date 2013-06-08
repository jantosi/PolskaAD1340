package items;

import CLIPSJNI.PrimitiveValue;
import clips.ClipsEnvironment;

/**
 * Klasa definiująca konie.
 * 
 * @author Piotrek
 */
public class Horse extends Item {
	
    /**
     * Ułamek z przedziału (0,1) określający o ile mniej energii na ruch zużywał będzie agent
     * w porównaniu do poruszania się pieszo
     */
    protected int riderTiredness;
	
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
    
    public Horse(PrimitiveValue pv, int agentId, ClipsEnvironment clipsEnv) {        
        super.loadFromClips(pv, agentId, clipsEnv);
        
        try {
            this.setVelocity(pv.getFactSlot("predkosc").intValue());
            this.setCapacity(pv.getFactSlot("udzwig").intValue());
        } catch (Exception e) {
		e.printStackTrace();
	}
    }
    
    /**
     * Konstruktor. Ustawienie domyślnej prędkości, udźwigu, zmiejszenia zmęczenia agenta, 
     * ceny, szybkości zużycia.
     * @param int velocity
     * @param int capacity 
     * @param int agentEnergyLossDecrease 
     * @param int price 
     * @param int wearSpeed 
     */
    public Horse(String id, int velocity, int capacity, int price, int wearSpeed, int riderTiredness) {
    	super(price, wearSpeed, id);
    	
    	this._velocity = velocity;
        this._capacity = capacity;
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

    public int getRiderTiredness() {
    	return riderTiredness;
    }

    public void setRiderTiredness(int riderTiredness) {
    	this.riderTiredness = riderTiredness;
    }
    
	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("(kon ");
		sbuf.append("(id ").append(this.getId()).append(") ");
		sbuf.append("(grod ").append(this.getTownId()).append(") ");
		sbuf.append("(udzwig ").append(this.getCapacity()).append(") ");
		sbuf.append("(predkosc ").append(this.getVelocity()).append(") ");
		sbuf.append("(zmeczenieJezdzcy ").append(this.getRiderTiredness()).append(") ");
		sbuf.append("(cena ").append(this.getPrice()).append(") ");
		sbuf.append("(zuzycie ").append(this.getLevelOfWear()).append(") ");
		sbuf.append("(predkoscZuzycia ").append(this.getWearSpeed()).append(") ");
		sbuf.append(")");
		return sbuf.toString();
	}
}
