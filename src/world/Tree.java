package world;

import CLIPSJNI.PrimitiveValue;

public class Tree {
	private int worldFrame;
	private String type;
	private String state;

	public Tree(int worldFrame, String type, String state) {
		super();
		this.worldFrame = worldFrame;
		this.type = type;
		this.state = state;
	}

	public Tree() {
		this.state = "niesciete";
	}

	public void loadFromClips(PrimitiveValue pv) {
		try {
			this.worldFrame = pv.getFactSlot("idKratki").intValue();
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
		sbuf.append("(idKratki ").append(worldFrame).append(") ");
		sbuf.append("(stan ").append(this.state).append(")");
		sbuf.append(")");

		return sbuf.toString();
	}

	public int getWorldFrame() {
		return worldFrame;
	}

	public void setWorldFrame(int worldFrame) {
		this.worldFrame = worldFrame;
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

}
