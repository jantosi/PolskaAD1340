package world;

import java.util.ArrayList;

public class Town {
	private String nazwa;
	private int mapFrame;
	private int population;
	private int guardsActivity;
	
	
	public Town(String nazwa, int mapFrame, int population, int guardsActivity) {
		super();
		this.nazwa = nazwa;
		this.mapFrame = mapFrame;
		this.population = population;
		this.guardsActivity = guardsActivity;
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
	public int getGuardsActivity() {
		return guardsActivity;
	}
	public void setGuardsActivity(int guardsActivity) {
		this.guardsActivity = guardsActivity;
	}

}
