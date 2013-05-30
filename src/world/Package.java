package world;

public class Package {
private int id;
private float weight;
private int sourceTown;
private int destinationTown;

public Package (int id,float weight,int sourceTown, int destinationTown){
	this.id=id;
	this.weight=weight;
	this.sourceTown=sourceTown;
	this.destinationTown = destinationTown;
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



public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public float getWeight() {
	return weight;
}
public void setWeight(float weight) {
	this.weight = weight;
}
public int getSourceTown() {
	return sourceTown;
}
public void setSourceTown(int sourceTown) {
	this.sourceTown = sourceTown;
}
public int getDestinationTown() {
	return destinationTown;
}
public void setDestinationTown(int destinationTown) {
	this.destinationTown = destinationTown;
}

}
