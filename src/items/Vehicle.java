package items;

import CLIPSJNI.PrimitiveValue;

/**
 * Klasa definiująca narzędzie: Wóz dla drwala.
 * @author Piotrek
 */
public class Vehicle extends Item {
    
    /**
     * Pojemność wozu.
     * @var int
     */
    protected int capacity;
    
    /**
     * Konstruktor. Ustawienie domyślnej pojemności, ceny i szybkości zużycia.
     * @param int capacity
     * @param int price
     * @param int wearSpeed 
     */
    public Vehicle(String id, int capacity, int price) {
        super(price, id);
        
        this.setCapacity(capacity);
    }
    public Vehicle(PrimitiveValue pv) throws Exception {
    	this.id = pv.getFactSlot("id").toString();
    	this.capacity = pv.getFactSlot("udzwig").intValue();
    	this.price = pv.getFactSlot("cena").intValue();
    	this.townId = pv.getFactSlot("idGrodu").toString();
    }
    
    public int getCapacity() {
        return this.capacity;
    }
    
    public Vehicle setCapacity(int capacity) {
        this.capacity = capacity;
        
        return this;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(woz (udzwig ");
		builder.append(this.capacity);
		builder.append(") (");
		builder.append("id ");
		builder.append(this.id);
		builder.append(") (");
		builder.append("cena ");
		builder.append(this.price);
		builder.append(") (idGrodu ");
		builder.append(this.townId);
		builder.append("))");
		
		return builder.toString();
	}
}
