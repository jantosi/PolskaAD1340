package world;

import CLIPSJNI.PrimitiveValue;

public class Tree {
	private MapFrame mapFrame;
	private String type;
	private String state;

	public Tree(MapFrame mapFrame, String type, String state) {
		super();
		this.mapFrame = mapFrame;
		this.type = type;
		this.state = state;
	}

	public Tree() {
		this.mapFrame = new MapFrame();
		this.state = "niesciete";
	}

	public void loadFromClips(PrimitiveValue pv) {
		try {
			this.mapFrame.setId(pv.getFactSlot("idKratki").intValue());
			this.type = pv.getFactSlot("rodzajDrzewa").getValue().toString();
			this.state = pv.getFactSlot("stan").getValue().toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("(drzewo ");
		sbuf.append("(rodzajDrzewa ").append(type).append(") ");
		sbuf.append("(idKratki ").append(mapFrame.getId()).append(") ");
		sbuf.append("(stan ").append(this.state).append(")");
		sbuf.append(")");

		return sbuf.toString();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public MapFrame getMapFrame() {
		return mapFrame;
	}

	public void setMapFrame(MapFrame mapFrame) {
		this.mapFrame = mapFrame;
	}

}
