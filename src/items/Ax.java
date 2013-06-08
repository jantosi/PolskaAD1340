package items;

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
		builder.append(type);
		builder.append(") (");
		builder.append("id ");
		builder.append(_id);
		builder.append(") (");
		builder.append("cena ");
		builder.append(_price);
		builder.append(") (zuzycie ");
		builder.append(_levelOfWear);
		builder.append(") (");
		builder.append("idGrodu ");
		builder.append(townId);
		builder.append("))");
		return builder.toString();
	}
}
