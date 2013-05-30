package items;

/**
 * Klasa definiująca narzędzie: Siekiera.
 * 
 * @author Piotrek
 */
public class Ax extends Item {
    
    /**
     * Ilość ścinanych drzew na iterację.
     * @var int
     */
    protected int _numberOfShearWoods;
    
    /**
     * Konstruktor. Ustawienie domyślnej ilości ścinanych drzew, ceny i szybkości zużycia.
     * @param int numberOfShearWoods
     * @param int price
     * @param int wearSpeed 
     */
    public Ax(String id, int numberOfShearWoods, int price, int wearSpeed) {
        super(price, wearSpeed, id);
        
        this.setNumberOfShearWoods(numberOfShearWoods);
    }
    
    /**
     * Getter dla ilości ścinanych drzew.
     * @return int
     */
    public int getNumberOfShearWoods() {
        return this._numberOfShearWoods;
    }
    
    /**
     * Setter dla ilości ścinanych drzew.
     * @param int numberOfShearWoods
     * @return Ax
     */
    public Ax setNumberOfShearWoods(int numberOfShearWoods) {
        this._numberOfShearWoods = numberOfShearWoods;
        
        return this;
    }
}
