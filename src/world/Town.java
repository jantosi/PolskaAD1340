package world;

import CLIPSJNI.PrimitiveValue;

public class Town {
	private String nazwa;
	private int mapFrame;
	private int population;
	private float guardsActivity;

	public Town(String nazwa, int mapFrame, int population, float guardsActivity) {
		super();
		this.nazwa = nazwa;
		this.mapFrame = mapFrame;
		this.population = population;
		this.guardsActivity = guardsActivity;
	}

	public Town() {

	}

	public void loadFromClips(PrimitiveValue pv) {
		try {
			this.mapFrame = pv.getFactSlot("idKratki").intValue();
			this.population = pv.getFactSlot("liczbaMieszkancow").intValue();
			this.guardsActivity = pv.getFactSlot("wspAktywnosciStrazy").floatValue();
			this.nazwa = pv.getFactSlot("nazwa").getValue().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("(grod ");
		sbuf.append("(nazwa ").append(nazwa).append(") ");
		sbuf.append("(idKratki ").append(mapFrame).append(") ");
		sbuf.append("(liczbaMieszkancow ").append(population).append(") ");
		sbuf.append("(wspAktywnosciStrazy ").append(guardsActivity).append(")");
		sbuf.append(")");

		return sbuf.toString();
	}

	public int getMapFrame() {
		return mapFrame;
	}

	public void setMapFrame(int mapFrame) {
		this.mapFrame = mapFrame;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public float getGuardsActivity() {
		return guardsActivity;
	}

	public void setGuardsActivity(float guardsActivity) {
		this.guardsActivity = guardsActivity;
	}

}
