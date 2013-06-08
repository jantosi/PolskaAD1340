package items;

/**
 * Klasa definiująca uzbrojenie rycerza.
 * @author Piotrek
 */
public class Armor extends Item {
    
    /**
     * Wartość określająca wytrzymałość zbroi.
     * @var int
     */
    protected int _value;
    
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
    
    /**
     * Getter dla wytrzymałości zbroi.
     * @return int
     */
    public int getValue() {
        return this._value;
    }
    
    /**
     * Setter dla wytrzymałości zbroi.
     * @param int value
     * @return Armor
     */
    public Armor setValue(int value) {
        this._value = value;
        
        return this;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(zbroja (wytrzymalosc ");
		builder.append(_value);
		builder.append(") (");
		
		builder.append("id ");
		builder.append(_id);
		builder.append(") (");
		
		builder.append("cena ");
		builder.append(_price);
		builder.append(") (zuzycie ");
		builder.append(_levelOfWear);
		builder.append(") (szybkoscZuzycia ");
		builder.append(_wearSpeed);
		builder.append(") (grod ");
		builder.append(townId);
		builder.append("))");
		return builder.toString();
	}
}
