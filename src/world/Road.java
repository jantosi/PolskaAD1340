package world;

public class Road {
private int mapFrame;
private int sourceTown;
private int destinationTown;
private int type;
private boolean isFree;
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
