package world;

public class Blockade {
private int id;
private int mapFrame;

public Blockade(int id, int mapFrame) {
	super();
	this.id = id;
	this.mapFrame = mapFrame;
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
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getMapFrame() {
	return mapFrame;
}
public void setMapFrame(int mapFrame) {
	this.mapFrame = mapFrame;
}

}
