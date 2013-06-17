package items;

/**
 * Klasa definiująca drzewo.
 */
public class Wood extends Item {
	
	private int mass;
	
	public Wood(String id, int price) {
		super(price, id);
		this.mass = 10;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("(drewno ")
		   .append("(id ").append(this.id).append(") ")
		   .append("(waga ").append(this.mass).append(") ")
		   .append("(cena ").append(this.price).append(")) ");
		
		return buf.toString();
	}
}
