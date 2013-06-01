package world;

public class Road {
	private int id;
private int mapFrame;
private int sourceTown;
private int destinationTown;
private int type;
private boolean isFree;
private int currentPartNo;
private int maxPartNo;



public Road(int id, int mapFrame, int sourceTown, int destinationTown,
		int type, boolean isFree, int currentPartNo, int maxPartNo) {
	super();
	this.id = id;
	this.mapFrame = mapFrame;
	this.sourceTown = sourceTown;
	this.destinationTown = destinationTown;
	this.type = type;
	this.isFree = isFree;
	this.currentPartNo = currentPartNo;
	this.maxPartNo = maxPartNo;
}

@Override
public String toString() {
	StringBuffer sbuf = new StringBuffer();
	sbuf.append("(droga ");
	sbuf.append("(id ").append(id).append(") ");
	sbuf.append("(idKratki ").append(mapFrame).append(") ");
	sbuf.append("(skadGrod ").append(sourceTown).append(") ");
	sbuf.append("(dokadGrod ").append(destinationTown).append(") ");
	sbuf.append("(platna ").append(String.valueOf(isFree).toUpperCase()).append(") ");
	sbuf.append("(nawierzchnia ").append(type).append(") ");
	sbuf.append("(nrOdcinka ").append(currentPartNo).append(") ");
	sbuf.append("(maxOdcinek ").append(maxPartNo).append(")");
	sbuf.append(")");
	
	return sbuf.toString();
}

public int getMapFrame() {
	return mapFrame;
}
public void setMapFrame(int mapFrame) {
	this.mapFrame = mapFrame;
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
public int getType() {
	return type;
}
public void setType(int type) {
	this.type = type;
}
public boolean isFree() {
	return isFree;
}
public void setFree(boolean isFree) {
	this.isFree = isFree;
}

}
