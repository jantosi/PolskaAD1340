
package world;

import CLIPSJNI.PrimitiveValue;

public class Cataclysm {

	private int id;
private int mapFrame;
private float treesDestroy;
private int energyLoss;
private int populationLoss;



public Cataclysm(int id, int mapFrame, float treesDestroy, int energyLoss,
		int populationLoss) {
	super();
	this.id = id;
	this.mapFrame = mapFrame;
	this.treesDestroy = treesDestroy;
	this.energyLoss = energyLoss;
	this.populationLoss = populationLoss;
}


public Cataclysm(){

}

public void loadFromClips(PrimitiveValue pv){
	try {
		this.mapFrame= pv.getFactSlot("idKratki").intValue();
		this.id = pv.getFactSlot("id").intValue();
		this.treesDestroy =  pv.getFactSlot("niszczenieLasu").floatValue();
		this.energyLoss =  pv.getFactSlot("oslabianieAgentow").intValue();
		this.populationLoss =  pv.getFactSlot("zabijanieMieszkancow").intValue();
	} catch (Exception e) {
		e.printStackTrace();
	}

}


@Override
public String toString() {
	StringBuffer sbuf = new StringBuffer();
	sbuf.append("(kleska ");
	sbuf.append("(id ").append(id).append(") ");
	sbuf.append("(idKratki ").append(mapFrame).append(") ");
	sbuf.append("(niszczenieLasu ").append(treesDestroy).append(") ");
	sbuf.append("(oslabianieAgentow ").append(energyLoss).append(") ");
	sbuf.append("(zabijanieMieszkancow ").append(populationLoss).append(")");
	sbuf.append(")");
	return sbuf.toString();
}

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public int getMapFrame() {
	return mapFrame;
}

public void setMapFrame(int mapFrame) {
	this.mapFrame = mapFrame;
}

public int getUpperLeftMapFrame() {
	return mapFrame;
}
public void setUpperLeftMapFrame(int upperLeftMapFrame) {
	this.mapFrame = upperLeftMapFrame;
}

public float getTreesDestroy() {
	return treesDestroy;
}
public void setTreesDestroy(float treesDestroy) {
	this.treesDestroy = treesDestroy;
}
public int getEnergyLoss() {
	return energyLoss;
}
public void setEnergyLoss(int energyLoss) {
	this.energyLoss = energyLoss;
}
public int getPopulationLoss() {
	return populationLoss;
}
public void setPopulationLoss(int populationLoss) {
	this.populationLoss = populationLoss;
}
}
