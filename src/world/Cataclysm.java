package world;

import polskaad1340.window.ObiektPierwszegoPlanu;
import polskaad1340.window.OknoMapy;
import CLIPSJNI.PrimitiveValue;

public class Cataclysm {

	private String id;
	private MapFrame mapFrame;
	private int treesDestroy;
	private int energyLoss;
	private int populationLoss;
	private int livingTime;
	private ObiektPierwszegoPlanu opp;

	public Cataclysm(String id, MapFrame mapFrame, int treesDestroy, int energyLoss, int populationLoss, int livingTime, OknoMapy om) {
		super();
		this.id = id;
		this.mapFrame = mapFrame;
		this.treesDestroy = treesDestroy;
		this.energyLoss = energyLoss;
		this.populationLoss = populationLoss;
		this.livingTime = livingTime;
		this.opp = om.nowyObiektPierwszegoPlanu(this.mapFrame.getX(), this.mapFrame.getY(), this.id, 7);
	}

	public Cataclysm() {
		this.mapFrame = new MapFrame();
	}

	public void loadFromClips(PrimitiveValue pv) {
		try {
			this.mapFrame.setId(pv.getFactSlot("idKratki").intValue());
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
		sbuf.append("(idKratki ").append(mapFrame.getId()).append(") ");
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

	public MapFrame getMapFrame() {
		return mapFrame;
	}

	public void setMapFrame(MapFrame mapFrame) {
		this.mapFrame = mapFrame;
	}

	public ObiektPierwszegoPlanu getOpp() {
		return opp;
	}

	public void setOpp(ObiektPierwszegoPlanu opp) {
		this.opp = opp;
	}
}
