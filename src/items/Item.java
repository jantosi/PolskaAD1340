package items;

/**
 * Klasa abstrakcyjna definiująca narzędnia przedawane przez kupca.
 * 
 * @author Piotrek
 */
abstract public class Item {
    
    /**
     * Identyfikator przedmiotu
     * @var int
     */
    protected String id;
	
    /**
     * Cena narzędnia.
     * @var double
     */
    protected int price;
    
    /**
     * Poziom zużycia narzędzia w procentach.
     * @var int
     */
    protected int levelOfWear;
    
    /**
     * Szybkość zużycia narzędzia.
     * @var int
     */
    protected int wearSpeed;
    
    protected String townId;
    
    
    /**
     * Konstruktor. Ustawienie domyślnej ceny i szybkości zużycia narzędzia.
     * @param int id
     * @param int price
     * @param int wearSpeed 
     */
    public Item(int price, int wearSpeed, String id) {
        this.setPrice(price);
        this.setWearSpeed(wearSpeed);
        this.setId(id);
    }
    
    public Item(int price, String id) {
        this.setPrice(price);
        this.setId(id);
    }
    
    public Item(){

    }
    /**
     * Getter dla ceny.
     * @return int
     */
    public int getPrice() {
        return this.price;
    }
    
    /**
     * Setter dla ceny
     * @param int price
     */
    public void setPrice(int price) {
        this.price = price;
    }
    
    /**
     * Getter dla poziomu zużycia narzędzia.
     * @return int
     */
    public int getLevelOfWear() {
        return this.levelOfWear;
    }
    
    /**
     * Setter dla poziomu zużycia narzędzia.
     * @param int levelOfWear
     * @return Item
     */
    public Item setLevelOfWear(int levelOfWear) {
        this.levelOfWear = levelOfWear;
        
        return this;
    }
    
    /**
     * Getter dla szybkości zużycia narzędzia.
     * @return int
     */
    public int getWearSpeed() {
        return this.wearSpeed;
    }
    
    /**
     * Setter dla szybkości zużycia narzędzia.
     * @param int wearSpeed
     * @return Item
     */
    public Item setWearSpeed(int wearSpeed) {
        this.wearSpeed = wearSpeed;
        
        return this;
    }
    
    /**
     * Użycie narzędzia.
     * @TODO Obsługa przypadku, w którym narzędzie zużyje się całkowicie.
     */
    public void use() {
        this.setLevelOfWear(this.getLevelOfWear()-this.getWearSpeed());
    }

    public String getId() {
	return id;
    }

    public Item setId(String id) {
	this.id = id;
        return this;
    }
    
	public String getTownId() {
		return townId;
	}

	public void setTownId(String townId) {
		this.townId = townId;
	}
}
