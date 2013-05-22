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
    public Armor(int value, int price, int wearSpeed) {
        super.Item(price, wearSpeed);
        
        this.setValue(value);
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
}
