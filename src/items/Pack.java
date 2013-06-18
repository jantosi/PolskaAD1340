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
    protected int mass;
    
    /**
     * Czas dostarczenia przesyłki.
     * @var int
     */
    protected int deliveryTime;
    
    private String sourceTown;
	private String destinationTown;
	private int startDeliveryIteration;
    
    /**
     * Konstruktor. Nadanie paczce masy.
     * @param mass 
     */
    public Pack(String id, int mass) {
    	super(mass * 5, 0, id);
    	
    	this.mass = mass;
    }
    
    public Pack(String id, int mass, String sourceTown, String destinationTown) {
		this.id = id;
		this.mass = mass;
		this.sourceTown = sourceTown;
		this.destinationTown = destinationTown;
	}
	public Pack(PrimitiveValue pv) throws Exception {
		this.id = pv.getFactSlot("id").toString();
		this.mass = pv.getFactSlot("waga").intValue();
		this.sourceTown = pv.getFactSlot("grodStart").toString();
		this.destinationTown = pv.getFactSlot("grodKoniec").toString();
		this.startDeliveryIteration = pv.getFactSlot("iteracjaStartDostarczenie").intValue();
	}
    
	public Pack() {

	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("(paczka ");
		sbuf.append("(id ").append(id).append(") ");
		sbuf.append("(waga ").append(mass).append(") ");
		sbuf.append("(grodStart ").append(sourceTown).append(") ");
		sbuf.append("(grodKoniec ").append(destinationTown).append(")");
		sbuf.append("(iteracjaStartDostarczenie ").append(this.startDeliveryIteration).append(")");
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
    
    public int getMass() {
        return this.mass;
    }
    
    public Pack setMass(int mass) {
        this.mass = mass;
        
        return this;
    }
    
    public Pack setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
        
        return this;
    }
    
    public int getDeliveryTime() {
        return this.deliveryTime;
    }
}
