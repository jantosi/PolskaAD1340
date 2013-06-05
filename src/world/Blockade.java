package world;

import CLIPSJNI.PrimitiveValue;

public class Blockade {
	private String id;
	private int mapFrame;

	public Blockade(String id, int mapFrame) {
		super();
		this.id = id;
		this.mapFrame = mapFrame;
	}

	public Blockade() {

	}

	public void loadFromClips(PrimitiveValue pv) {
		try {
			this.mapFrame = pv.getFactSlot("idKratki").intValue();
			this.id = pv.getFactSlot("id").getValue().toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("(blokada ");
		sbuf.append("(id ").append(id).append(") ");
		sbuf.append("(idKratki ").append(mapFrame).append(")");
		sbuf.append(")");
		return sbuf.toString();
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

}
