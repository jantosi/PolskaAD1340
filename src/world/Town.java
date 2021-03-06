package world;

import items.Armor;
import items.Ax;
import items.Horse;
import items.Item;
import items.Pack;
import items.Vehicle;

import java.util.ArrayList;
import java.util.Random;

import CLIPSJNI.PrimitiveValue;

public class Town {
	private String id;
	private int mapFrame;
	private int population;
	private float guardsActivity;

	private ArrayList<Item> items;
	
	public Town(String id, int mapFrame, int population, float guardsActivity) {
		super();
		this.id = id;
		this.mapFrame = mapFrame;
		this.population = population;
		this.guardsActivity = guardsActivity;
		this.items = new ArrayList<Item>();
	
	}
	
	public Town(PrimitiveValue pv) {
		loadFromClips(pv);
		
		this.items = new ArrayList<Item>();
	}
	
	public void randomItems() {
		randomArmors();
		randomAxes();
		randomHorses();
		randomPacks();
		randomVehicle();
	}
	
	private void randomVehicle() {
		Random random = new Random();
		int vehiclesNum = random.nextInt(5) + 2;
		
		for (int i = 0; i < vehiclesNum; i++) {
			int capacity = random.nextInt(100) + 30;
			int price = (int)(0.7 * capacity);
			
			Vehicle vehicle = new Vehicle("woz" + (i+1) + this.getId(), capacity, price);
			vehicle.setTownId(this.getId());
			this.items.add(vehicle);
		}
	}
	
	private void randomPacks() {
		Random random = new Random();
		int packsNum = random.nextInt(10) + 4;
		
		for (int i = 0; i < packsNum; i++) {
			int mass = random.nextInt(15) + 2;
			String destTownId = "grod" + (random.nextInt(4) + 1);
			while (destTownId.equalsIgnoreCase(this.getId())) {
				destTownId = "grod" + (random.nextInt(3) + 1);
			}
			
			Pack pack = new Pack("paczka" + (i+1) + this.getId(), mass);
			pack.setSourceTown(this.getId());
			pack.setDestinationTown(destTownId);
			this.items.add(pack);
		}
	}
	
	private void randomHorses() {
		Random random = new Random();
		int axesNum = random.nextInt(5) + 2;
		
		for (int i = 0; i < axesNum; i++) {
			int levelOfWear = random.nextInt(60);
			int horseType = random.nextInt(3);
			
			int velocity = 0;
			int capacity = 0;
			int price = 0;
			int wearSpeed = random.nextInt(5) + 5;
			int riderTiredness = 0;
			
			switch (horseType) {
			case 0: {
				velocity = 2;
				capacity = 20;
				price = 30;
				riderTiredness = 30;
				break;
			}
			case 1: {
				velocity = 3;
				capacity = 40;
				price = 50;
				riderTiredness = 50;
				break;
			}
			case 2: {
				velocity = 4;
				capacity = 60;
				price = 70;
				riderTiredness = 70;
				break;
			}
			}
			
			Horse horse = new Horse("kon" + (i+1) + this.getId(), velocity, capacity, price, wearSpeed, riderTiredness);
			horse.setTownId(this.getId());
			horse.setLevelOfWear(levelOfWear);
			this.items.add(horse);
			
		}
	}
	
	private void randomAxes() {
		Random random = new Random();
		int axesNum = random.nextInt(10) + 2;
		for (int i = 0; i < axesNum; i++) {
			int levelOfWear = random.nextInt(60);
			int price = 0;
			int wearSpeed = 0;
			
			String[] axTypes = {"zelazna", "zlota", "tytanowa"};
			int axTypesIndex = random.nextInt(axTypes.length);
			switch (axTypesIndex) {
			case 0: {
				price = 10;
				wearSpeed = 2;
				break;
			}
			case 1: {
				price = 30;
				wearSpeed = 5;
				break;
			}
			case 2: {
				price = 60;
				wearSpeed = 7;
				break;
			}
			}			
			
			Ax ax = new Ax("siekiera" + (i+1) + this.getId(), axTypes[axTypesIndex], price, wearSpeed, this.getId());
			ax.setLevelOfWear(levelOfWear);
			this.items.add(ax);
		}
	}
	
	private void randomArmors() {
		Random random = new Random();
		
		int armorNum = random.nextInt(10) + 2;
		
		for (int i = 0; i < armorNum; i++) {
			int levelOfWear = random.nextInt(60);
			int value = random.nextInt(30) + 11;
			int wearSpeed = random.nextInt(5) + 5;
			int price = (int) (0.4 * (value + levelOfWear - wearSpeed)) + 2;
		
			Armor armor = new Armor("zbroja" + (i+1) + this.getId(), value, price, wearSpeed, this.id);
			armor.setLevelOfWear(levelOfWear);
			
			this.items.add(armor);
		}
	}
	
	public void loadFromClips(PrimitiveValue pv) {
		try {
			this.mapFrame = pv.getFactSlot("idKratki").intValue();
			this.population = pv.getFactSlot("liczbaMieszkancow").intValue();
			this.guardsActivity = pv.getFactSlot("wspAktywnosciStrazy").floatValue();
			this.id = pv.getFactSlot("nazwa").getValue().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("(grod ");
		sbuf.append("(nazwa ").append(id).append(") ");
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Town other = (Town) obj;
		if (mapFrame != other.mapFrame)
			return false;
		return true;
	}

	
}
