package world;

import CLIPSJNI.PrimitiveValue;

public class Package {
private String id;
private float weight;
private String sourceTown;
private String destinationTown;

public Package (String id,float weight,String sourceTown, String destinationTown){
	this.id=id;
	this.weight=weight;
	this.sourceTown=sourceTown;
	this.destinationTown = destinationTown;
}

public Package(){

}

public void loadFromClips(PrimitiveValue pv){
	try {
		this.id = pv.getFactSlot("id").getValue().toString();
		this.weight =  pv.getFactSlot("waga").floatValue();
		this.sourceTown =  pv.getFactSlot("grodStart").getValue().toString();
		this.destinationTown =  pv.getFactSlot("grodKoniec").getValue().toString();
	} catch (Exception e) {
		e.printStackTrace();
	}

}

@Override
public String toString() {
	StringBuffer sbuf = new StringBuffer();
	sbuf.append("(paczka ");
	sbuf.append("(id ").append(id).append(") ");
	sbuf.append("(waga ").append(weight).append(") ");
	sbuf.append("(grodStart ").append(sourceTown).append(") ");
	sbuf.append("(grodKoniec ").append(destinationTown).append(")");
	sbuf.append(")");
	return sbuf.toString();
}



public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public float getWeight() {
	return weight;
}
public void setWeight(float weight) {
	this.weight = weight;
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

}
