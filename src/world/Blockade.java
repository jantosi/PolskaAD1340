package world;

import javax.swing.JLabel;

import polskaad1340.window.OknoMapy;
import CLIPSJNI.PrimitiveValue;

public class Blockade {
	private String id;
	private MapFrame mapFrame;
	private JLabel tile;
	private JLabel replacedTile;

	public Blockade(String id, MapFrame mapFrame, OknoMapy om) {
		super();
		this.id = id;
		this.mapFrame = mapFrame;
		this.tile = om.tileFromNumber(1175);
		this.replacedTile = null;
	}

	public Blockade(OknoMapy om) {
		this.tile = om.tileFromNumber(1175);
		this.mapFrame = new MapFrame();
	}

	public void loadFromClips(PrimitiveValue pv) {
		try {
			this.mapFrame.setId( pv.getFactSlot("idKratki").intValue() );
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
		sbuf.append("(idKratki ").append(mapFrame.getId()).append(")");
		sbuf.append(")");
		return sbuf.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public JLabel getTile() {
		return tile;
	}

	public void setTile(JLabel tile) {
		this.tile = tile;
	}

	public JLabel getReplacedTile() {
		return replacedTile;
	}

	public void setReplacedTile(JLabel replacedTile) {
		this.replacedTile = new JLabel(replacedTile.getIcon());
		this.replacedTile.setBackground(replacedTile.getBackground());
	}

	public MapFrame getMapFrame() {
		return mapFrame;
	}

	public void setMapFrame(MapFrame mapFrame) {
		this.mapFrame = mapFrame;
	}

}
