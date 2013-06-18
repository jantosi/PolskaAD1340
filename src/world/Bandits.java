
package world;

import polskaad1340.window.ObiektPierwszegoPlanu;
import polskaad1340.window.OknoMapy;
import CLIPSJNI.PrimitiveValue;

public class Bandits {
	private double energyLoss;
	private double goldLoss;
	private String id;
	private MapFrame mapFrame;
	private ObiektPierwszegoPlanu opp;
	

	public Bandits(String id, double energyLoss, double goldLoss, MapFrame mapFrame, OknoMapy om) {
		this.energyLoss = energyLoss;
		this.goldLoss = goldLoss;
		this.mapFrame = mapFrame;
		this.id = id;
		this.opp = om.nowyObiektPierwszegoPlanu(this.mapFrame.getX(), this.mapFrame.getY(), this.id, 1064);
	}
	public Bandits(){
		this.mapFrame = new MapFrame();
	}
	public void loadFromClips(PrimitiveValue pv){
		try {
			this.id = pv.getFactSlot("id").toString();
			this.mapFrame.setId(pv.getFactSlot("idKratki").intValue());
			this.goldLoss = pv.getFactSlot("zabieranieEnergii").floatValue();
			this.energyLoss = pv.getFactSlot("zabieranieZlota").floatValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("(rozbojnicy ");
		sbuf.append("(id ").append(this.id).append(") ");
		sbuf.append("(idKratki ").append(mapFrame.getId()).append(") ");
		sbuf.append("(zabieranieEnergii ").append(energyLoss).append(") ");
		sbuf.append("(zabieranieZlota ").append(goldLoss).append(")");
		sbuf.append(")");
		return sbuf.toString();
	}

	public double getGoldLoss() {
		return goldLoss;
	}
	public void setGoldLoss(double goldLoss) {
		this.goldLoss = goldLoss;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getEnergyLoss() {
		return energyLoss;
	}
	public void setEnergyLoss(double energyLoss) {
		this.energyLoss = energyLoss;
	}

}
