package items;
import world.Town;
import CLIPSJNI.PrimitiveValue;
import clips.ClipsEnvironment;

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
    protected String _id;
	
    /**
     * Cena narzędnia.
     * @var double
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
        return this._price;
    }
    
    /**
     * Setter dla ceny
     * @param int price
     */
    public void setPrice(int price) {
        this._price = price;
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

    public Item setId(String id) {
	this._id = id;
        return this;
    }
    
    public void loadFromClips(PrimitiveValue pv, int agentId, ClipsEnvironment clipsEnv) {
        try {
            this.setPrice(pv.getFactSlot("cena").intValue());
            this.setId(pv.getFactSlot("id").stringValue());
            this.setWearSpeed(pv.getFactSlot("predkoscZuzycia").intValue());
            this.setLevelOfWear(pv.getFactSlot("zuzycie").intValue());
            
            if(clipsEnv != null) {
                String townFind = "(find-fact ((?k grod)) (eq ?k:nazwa "+pv.getFactSlot("grod").stringValue()+"))";
                PrimitiveValue townPv = clipsEnv.getWorldEnv().eval(townFind);
                Town itemTown = new Town(townPv);
                this.setTownId(itemTown.getNazwa());
            }
        } catch (Exception e) {
		e.printStackTrace();
	}
    }

	public String getTownId() {
		return townId;
	}

	public void setTownId(String townId) {
		this.townId = townId;
	}
}
