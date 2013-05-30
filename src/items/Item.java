package items;

/**
 * Klasa abstrakcyjna definiująca narzędnia przedawane przez kupca.
 * 
 * @author Piotrek
 */
abstract public class Item {
    
	/**
	 * Identyfikator paczki
	 */
	protected String _id;
	
    /**
     * Cena narzędnia.
     * @var int
     */
    protected int _price;
    
    /**
     * Poziom zużycia narzędzia w procentach.
     * @var int
     */
    protected int _levelOfWear;
    
    /**
     * Szybkość zużycia narzędzia.
     * @var int
     */
    protected int _wearSpeed;
    
    /**
     * Konstruktor. Ustawienie domyślnej ceny i szybkości zużycia narzędzia.
     * @param id TODO
     * @param int price
     * @param int wearSpeed 
     */
    public Item(int price, int wearSpeed, String id) {
        this._price = price;
        this._wearSpeed = wearSpeed;
        this._id = id;
    }
    
    public Item(int price) {
    	this._price = price;
    }
    
    /**
     * Getter dla ceny.
     * @return int
     */
    public int getPrice() {
        return this._price;
    }
    
    /**
     * Setter dla ceny
     * @param int price
     * @return Item
     */
    public Item setPrice(int price) {
        this._price = price;
        
        return this;
    }
    
    /**
     * Getter dla poziomu zużycia narzędzia.
     * @return int
     */
    public int getLevelOfWear() {
        return this._levelOfWear;
    }
    
    /**
     * Setter dla poziomu zużycia narzędzia.
     * @param int levelOfWear
     * @return Item
     */
    public Item setLevelOfWear(int levelOfWear) {
        this._levelOfWear = levelOfWear;
        
        return this;
    }
    
    /**
     * Getter dla szybkości zużycia narzędzia.
     * @return int
     */
    public int getWearSpeed() {
        return this._wearSpeed;
    }
    
    /**
     * Setter dla szybkości zużycia narzędzia.
     * @param int wearSpeed
     * @return Item
     */
    public Item setWearSpeed(int wearSpeed) {
        this._wearSpeed = wearSpeed;
        
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
		return _id;
	}

	public void setId(String _id) {
		this._id = _id;
	}
}
