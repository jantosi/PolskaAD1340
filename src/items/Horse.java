package items;

import CLIPSJNI.PrimitiveValue;

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
    protected int velocity;
    
    /**
     * Udźwig konia
     * @var int
     */
    protected int capacity;
    
    
    public Horse(PrimitiveValue pv) throws Exception {        
    	this.id = pv.getFactSlot("id").toString();
    	this.townId = pv.getFactSlot("grod").toString();
    	this.capacity = pv.getFactSlot("udzwig").intValue();
    	this.velocity = pv.getFactSlot("predkosc").intValue();
    	this.riderTiredness = pv.getFactSlot("zmeczenieJezdzcy").intValue();
    	this.price = pv.getFactSlot("cena").intValue();
    	this.levelOfWear = pv.getFactSlot("zuzycie").intValue();
    	this.wearSpeed = pv.getFactSlot("predkoscZuzycia").intValue();
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
    	
    	this.velocity = velocity;
        this.capacity = capacity;
        this.riderTiredness = riderTiredness;
    }
    
    public int getVelocity() {
        return this.velocity;
    }
    
    public Horse setVelocity(int velocity) {
        this.velocity = velocity;
        
        return this;
    }
    
    public int getCapacity() {
        return this.capacity;
    }
    
    public Horse setCapacity(int capacity) {
        this.capacity = capacity;
        
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
