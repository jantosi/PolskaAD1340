package world;

import CLIPSJNI.PrimitiveValue;

public class Tree {
	private int worldFrame;
	private int type;
	private String state;

	public Tree(int worldFrame, int type, String state) {
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
			this.type = pv.getFactSlot("rodzajDrzewa").intValue();
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
