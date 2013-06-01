
package world;

import CLIPSJNI.PrimitiveValue;

public class MapFrame {
 private int id;
 private int x;
 private int y;
 
 public MapFrame(){
	 
 }
 public MapFrame(int id, int x, int y) {
	super();
	this.id = id;
	this.x = x;
	this.y = y;
}
 public void loadFromClips(PrimitiveValue pv){
	 try {
		this.id= pv.getFactSlot("id").intValue();
		 this.x = pv.getFactSlot("pozycjaX").intValue();
		 this.y = pv.getFactSlot("pozycjaY").intValue();
	} catch (Exception e) {
		e.printStackTrace();
	}
	
 }
@Override
 public String toString() {
 	StringBuffer sbuf = new StringBuffer();
 	sbuf.append("(kratka ");
 	sbuf.append("(id ").append(id).append(") ");
 	sbuf.append("(pozycjaX ").append(x).append(") ");
 	sbuf.append("(pozycjaY ").append(y).append(")");
 	sbuf.append(")");
 	
 	return sbuf.toString();
}

public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getX() {
	return x;
}
public void setX(int x) {
	this.x = x;
}
public int getY() {
	return y;
}
public void setY(int y) {
	this.y = y;
}
 
}
