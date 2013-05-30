package items;

/**
 * Klasa definiująca narzędzie: Wóz dla drwala.
 * @author Piotrek
 */
public class Vehicle extends Item {
    
    /**
     * Pojemność wozu.
     * @var int
     */
    protected int _capacity;
    
    /**
     * Konstruktor. Ustawienie domyślnej pojemności, ceny i szybkości zużycia.
     * @param int capacity
     * @param int price
     * @param int wearSpeed 
     */
    public Vehicle(String id, int capacity, int price, int wearSpeed) {
        super(price, wearSpeed, id);
        
        this.setCapacity(capacity);
    }
    
    /**
     * Getter dla pojemności wozu.
     * @return int
     */
    public int getCapacity() {
        return this._capacity;
    }
    
    /**
     * Setter dla pojemności wozu.
     * @param int capacity
     * @return Vehicle
     */
    public Vehicle setCapacity(int capacity) {
        this._capacity = capacity;
        
        return this;
    }
}
