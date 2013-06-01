package world;

import CLIPSJNI.PrimitiveValue;

public class Road {
	private String id;
private int mapFrame;
private String sourceTown;
private String destinationTown;
private String type;
private boolean isFree;
private int currentPartNo;
private int maxPartNo;



public Road(String id, int mapFrame, String sourceTown, String destinationTown,
		String type, boolean isFree, int currentPartNo, int maxPartNo) {
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

public Road(){

}

public void loadFromClips(PrimitiveValue pv){
	try {
		this.id = pv.getFactSlot("id").getValue().toString();
		this.mapFrame =  pv.getFactSlot("idKratki").intValue();
		this.sourceTown = pv.getFactSlot("skadGrod").getValue().toString();
		this.destinationTown =  pv.getFactSlot("dokadGrod").getValue().toString();
		this.type =  pv.getFactSlot("nawierzchnia").getValue().toString();
		this.isFree =  Boolean.parseBoolean(pv.getFactSlot("platna").getValue().toString());
		this.currentPartNo =  pv.getFactSlot("nrOdcinka").intValue();
		this.maxPartNo =  pv.getFactSlot("maxOdcinek").intValue();
	} catch (Exception e) {
		e.printStackTrace();
	}

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

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public int getCurrentPartNo() {
	return currentPartNo;
}

public void setCurrentPartNo(int currentPartNo) {
	this.currentPartNo = currentPartNo;
}

public int getMaxPartNo() {
	return maxPartNo;
}

public void setMaxPartNo(int maxPartNo) {
	this.maxPartNo = maxPartNo;
}

public int getMapFrame() {
	return mapFrame;
}
public void setMapFrame(int mapFrame) {
	this.mapFrame = mapFrame;
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
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public boolean isFree() {
	return isFree;
}
public void setFree(boolean isFree) {
	this.isFree = isFree;
}

}
