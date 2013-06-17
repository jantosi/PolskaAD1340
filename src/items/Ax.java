package items;

import CLIPSJNI.PrimitiveValue;

/**
 * Klasa definiująca narzędzie: Siekiera.
 * 
 * @author Piotrek
 */
public class Ax extends Item {
    
    private String type;
    
    /**
     * Konstruktor. Ustawienie domyślnej ilości ścinanych drzew, ceny i szybkości zużycia.
     * @param int numberOfShearWoods
     * @param int price
     * @param int wearSpeed 
     */
    public Ax(String id, String type, int price, int wearSpeed, String townId) {
        super(price, wearSpeed, id);
        
        this.type = type;
        this.townId = townId;
    }

    public Ax(PrimitiveValue pv) throws Exception {
    	this.id = pv.getFactSlot("id").toString();
    	this.type = pv.getFactSlot("typ").toString();
    	this.levelOfWear = pv.getFactSlot("zuzycie").intValue();
    	this.townId = pv.getFactSlot("idGrodu").toString();
    	this.price = pv.getFactSlot("cena").intValue();
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(siekiera (");
		builder.append("typ ");
		builder.append(this.type);
		builder.append(") (");
		builder.append("id ");
		builder.append(this.id);
		builder.append(") (");
		builder.append("cena ");
		builder.append(this.price);
		builder.append(") (zuzycie ");
		builder.append(this.levelOfWear);
		builder.append(") (idGrodu ");
		builder.append(this.townId);
		builder.append("))");
		return builder.toString();
	}
}
