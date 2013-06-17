package items;

import CLIPSJNI.PrimitiveValue;

/**
 * Klasa definiująca uzbrojenie rycerza.
 * @author Piotrek
 */
public class Armor extends Item {
    
    /**
     * Wartość określająca wytrzymałość zbroi.
     * @var int
     */
    protected int value;
    
    /**
     * Konstruktor. Ustawienie domyślnej wartości, ceny i szybkości zużycia.
     * @param int value
     * @param int price
     * @param int wearSpeed 
     */
    public Armor(String id, int value, int price, int wearSpeed, String town) {
        super(price, wearSpeed, id);
        
        this.setValue(value);
        this.setTownId(town);
    }
    public Armor(PrimitiveValue pv) throws Exception {
    	
    	this.id = pv.getFactSlot("id").toString();
    	this.townId = pv.getFactSlot("grod").toString();
    	this.levelOfWear = pv.getFactSlot("zuzycie").intValue();
    	this.price = pv.getFactSlot("cena").intValue();
    	this.value = pv.getFactSlot("wytrzymalosc").intValue();
    	this.wearSpeed = pv.getFactSlot("szybkoscZuzycia").intValue();
    
    }
    
    public int getValue() {
        return this.value;
    }
    
    public Armor setValue(int value) {
        this.value = value;
        
        return this;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(zbroja (wytrzymalosc ");
		builder.append(value);
		builder.append(") (");
		
		builder.append("id ");
		builder.append(id);
		builder.append(") (");
		
		builder.append("cena ");
		builder.append(price);
		builder.append(") (zuzycie ");
		builder.append(levelOfWear);
		builder.append(") (szybkoscZuzycia ");
		builder.append(wearSpeed);
		builder.append(") (grod ");
		builder.append(townId);
		builder.append("))");
		return builder.toString();
	}
}
