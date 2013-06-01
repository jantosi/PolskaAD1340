
package world;

import CLIPSJNI.PrimitiveValue;

public class Bandits {
	private float packageLoss;
	private float goldLoss;
	private int mapFrame;

	

	public Bandits(float packageLoss, float goldLoss, int mapFrame) {
		super();
		this.packageLoss = packageLoss;
		this.goldLoss = goldLoss;
		this.mapFrame = mapFrame;
	}
	public Bandits(){

	}
	public void loadFromClips(PrimitiveValue pv){
		try {
			this.mapFrame= pv.getFactSlot("idKratki").intValue();
			this.goldLoss = pv.getFactSlot("zabieraniePaczek").floatValue();
			this.packageLoss = pv.getFactSlot("zabieranieZlota").floatValue();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("(rozbojnicy ");
		sbuf.append("(idKratki ").append(mapFrame).append(") ");
		sbuf.append("(zabieraniePaczek ").append(packageLoss).append(") ");
		sbuf.append("(zabieranieZlota ").append(goldLoss).append(")");
		sbuf.append(")");
		return sbuf.toString();
	}

	public float getPackageLoss() {
		return packageLoss;
	}
	public void setPackageLoss(float packageLoss) {
		this.packageLoss = packageLoss;
	}
	public float getGoldLoss() {
		return goldLoss;
	}
	public void setGoldLoss(float goldLoss) {
		this.goldLoss = goldLoss;
	}
	public int getMapFrame() {
		return mapFrame;
	}
	public void setMapFrame(int mapFrame) {
		this.mapFrame = mapFrame;
	}

}
