package world;

import CLIPSJNI.PrimitiveValue;

public class Cataclysm {

	private String id;
	private int mapFrame;
	private int treesDestroy;
	private int energyLoss;
	private int populationLoss;
	private int livingTime;

	public Cataclysm(String id, int mapFrame, int treesDestroy, int energyLoss, int populationLoss,int livingTime) {
		super();
		this.id = id;
		this.mapFrame = mapFrame;
		this.treesDestroy = treesDestroy;
		this.energyLoss = energyLoss;
		this.populationLoss = populationLoss;
		this.livingTime=livingTime;
	}

	public Cataclysm() {

	}

	public void loadFromClips(PrimitiveValue pv) {
		try {
			this.mapFrame = pv.getFactSlot("idKratki").intValue();
			this.id = pv.getFactSlot("id").getValue().toString();
			this.treesDestroy = pv.getFactSlot("niszczenieLasu").intValue();
			this.energyLoss = pv.getFactSlot("oslabianieAgentow").intValue();
			this.populationLoss = pv.getFactSlot("zabijanieMieszkancow").intValue();
			this.livingTime = pv.getFactSlot("czasTrwania").intValue();
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
		sbuf.append("(czasTrwania ").append(livingTime).append(") ");
		sbuf.append("(zabijanieMieszkancow ").append(populationLoss).append(")");
		sbuf.append(")");
		return sbuf.toString();
	}

	public int getLivingTime() {
		return livingTime;
	}

	public void setLivingTime(int livingTime) {
		this.livingTime = livingTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public int getTreesDestroy() {
		return treesDestroy;
	}

	public void setTreesDestroy(int treesDestroy) {
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
