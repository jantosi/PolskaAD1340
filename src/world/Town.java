package world;

import java.util.ArrayList;

public class Town {
	private int id;
	private int upperLeftMapFrame;
	private int lowerRightMapFrame;
	private int population;
	private int guardsActivity;
	
	
	public Town(int id, int upperLeftMapFrame, int lowerRightMapFrame,
			int population, int guardsActivity) {
		super();
		this.id = id;
		this.upperLeftMapFrame = upperLeftMapFrame;
		this.lowerRightMapFrame = lowerRightMapFrame;
		this.population = population;
		this.guardsActivity = guardsActivity;
	}


	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("(grod ");
		sbuf.append("(id ").append(id).append(") ");
		sbuf.append("(kratkaLGR ").append(upperLeftMapFrame).append(") ");
		sbuf.append("(kratkaPDR ").append(lowerRightMapFrame ).append(") ");
		sbuf.append("(liczbaMieszkancow ").append(population).append(") ");
		sbuf.append("(wspAktywnosciStrazy ").append(guardsActivity).append(")");
		sbuf.append(")");
		
		return sbuf.toString();
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUpperLeftMapFrame() {
		return upperLeftMapFrame;
	}
	public void setUpperLeftMapFrame(int upperLeftMapFrame) {
		this.upperLeftMapFrame = upperLeftMapFrame;
	}
	public int getLowerRightMapFrame() {
		return lowerRightMapFrame;
	}
	public void setLowerRightMapFrame(int lowerRightMapFrame) {
		this.lowerRightMapFrame = lowerRightMapFrame;
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
