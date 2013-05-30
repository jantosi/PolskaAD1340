package world;

public class Cataclysm {
private int upperLeftMapFrame;
private int range;
private float treesDestroy;
private int energyLoss;
private int populationLoss;


public Cataclysm(int upperLeftMapFrame, int range, float treesDestroy,
		int energyLoss, int populationLoss) {
	super();
	this.upperLeftMapFrame = upperLeftMapFrame;
	this.range = range;
	this.treesDestroy = treesDestroy;
	this.energyLoss = energyLoss;
	this.populationLoss = populationLoss;
}

@Override
public String toString() {
	StringBuffer sbuf = new StringBuffer();
	sbuf.append("(kleska ");
	sbuf.append("(kratkaLGR ").append(upperLeftMapFrame).append(") ");
	sbuf.append("(bokObszaru ").append(range).append(") ");
	sbuf.append("(niszczenieLasu ").append(treesDestroy).append(") ");
	sbuf.append("(oslabianieAgentow ").append(energyLoss).append(") ");
	sbuf.append("(zabijanieMieszkancow ").append(populationLoss).append(")");
	sbuf.append(")");
	return sbuf.toString();
}

public int getUpperLeftMapFrame() {
	return upperLeftMapFrame;
}
public void setUpperLeftMapFrame(int upperLeftMapFrame) {
	this.upperLeftMapFrame = upperLeftMapFrame;
}
public int getRange() {
	return range;
}
public void setRange(int range) {
	this.range = range;
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
