package items;

import CLIPSJNI.PrimitiveValue;

/**
 * Klasa definiująca paczki posłańców.
 * 
 * @author Piotrek
 */
public class Pack extends Item {
    
    /**
     * Masa paczki.
     * @var int
     */
    protected int _mass;
    
    /**
     * Czas dostarczenia przesyłki.
     * @var int
     */
    protected int _deliveryTime;
    
    /**
     * Dostarczona?
     * @TODO Sprawdzić potrzebność tego parametru.
     */
    protected boolean _deliveried;
    
    private String sourceTown;
	private String destinationTown;
    
    /**
     * Konstruktor. Nadanie paczce masy.
     * @param mass 
     */
    public Pack(String id, int mass) {
    	super(mass * 5, 0, id);
    	
    	this._mass = mass;
        this._deliveried = false;
    }
    
    public Pack(String id, int mass, String sourceTown, String destinationTown) {
		this._id = id;
		this._mass = mass;
		this.sourceTown = sourceTown;
		this.destinationTown = destinationTown;
	}

	public Pack() {

	}
	public void loadFromClips(PrimitiveValue pv) {
		try {
                    super.loadFromClips(pv, 0, null);
			this._id = pv.getFactSlot("id").getValue().toString();
			this._mass = pv.getFactSlot("waga").intValue();
			this.sourceTown = pv.getFactSlot("grodStart").stringValue();
			this.destinationTown = pv.getFactSlot("grodKoniec").stringValue();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("(paczka ");
		sbuf.append("(id ").append(_id).append(") ");
		sbuf.append("(waga ").append(_mass).append(") ");
		sbuf.append("(grodStart ").append(sourceTown).append(") ");
		sbuf.append("(grodKoniec ").append(destinationTown).append(")");
		sbuf.append(")");
		return sbuf.toString();
	}
	
	public String getSourceTown() {
		return sourceTown;
	}

	public void setSourceTown(String sourceTown) {
		this.sourceTown = sourceTown;
	}

	public String getDestinationTown() {
		return destinationTown;
	}

	public void setDestinationTown(String destinationTown) {
		this.destinationTown = destinationTown;
	}
    
    /**
     * Getter dla masy paczki.
     * @return int
     */
    public int getMass() {
        return this._mass;
    }
    
    /**
     * Setter dla masy paczki.
     * @param mass
     * @return Pack
     */
    public Pack setMass(int mass) {
        this._mass = mass;
        
        return this;
    }
    
    /**
     * Getter dla flagi dostarczona.
     * @return boolean
     */
    public boolean getIsDeliveried() {
        return this._deliveried;
    }
    
    /**
     * Setter dla flagi dostarczona.
     * @param deliveried
     * @return Pack
     */
    public Pack setIsDeliveried(boolean deliveried) {
        this._deliveried = deliveried;
        
        return this;
    }
    
    /**
     * Setter dla czasu dostawy.
     * @param int deliveryTime
     * @return Pack
     */
    public Pack setDeliveryTime(int deliveryTime) {
        this._deliveryTime = deliveryTime;
        
        return this;
    }
    
    /**
     * Getter dla czasu dostawy.
     * @return int
     */
    public int getDeliveryTime() {
        return this._deliveryTime;
    }
}