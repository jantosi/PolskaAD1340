package world;

import CLIPSJNI.PrimitiveValue;

public class Tree {
private int worldFrame;
private int type;
private boolean isCuted;


public Tree(int worldFrame, int type, boolean isCuted) {
	super();
	this.worldFrame = worldFrame;
	this.type = type;
	this.isCuted = isCuted;
}

public Tree(){

}

public void loadFromClips(PrimitiveValue pv){
	try {
		this.worldFrame =  pv.getFactSlot("idKratki").intValue();
		this.type =  pv.getFactSlot("rodzajDrzewa").intValue();
		this.isCuted = Boolean.parseBoolean(pv.getFactSlot("stan").getValue().toString());
		
	} catch (Exception e) {
		e.printStackTrace();
	}

}
@Override
public String toString() {
	StringBuffer sbuf = new StringBuffer();
	sbuf.append("(drzewo ");
	sbuf.append("(rodzajDrzewa ").append(type).append(") ");
	sbuf.append("(idKratki ").append(worldFrame).append(") ");
	sbuf.append("(stan ").append(String.valueOf(isCuted).toUpperCase()).append(")");
	sbuf.append(")");
	
	return sbuf.toString();
}

public int getWorldFrame() {
	return worldFrame;
}
public void setWorldFrame(int worldFrame) {
	this.worldFrame = worldFrame;
}
public int getType() {
	return type;
}
public void setType(int type) {
	this.type = type;
}
public boolean isCuted() {
	return isCuted;
}
public void setCuted(boolean isCuted) {
	this.isCuted = isCuted;
}

}
