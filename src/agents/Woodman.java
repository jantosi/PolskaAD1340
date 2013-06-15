package agents;

import items.Ax;
import items.Vehicle;
import items.Wood;

import java.util.ArrayList;

import polskaad1340.window.OknoMapy;

import statistics.WoodmanStatistics_Interface;
import world.MapFrame;
/**
 * Klasa definiująca drwala.
 * 
 * @author Piotrek
 */
public class Woodman extends Agent {
    
    /**
     * Udźwig drwala.
     * @var int
     */
    protected int _capacity;
    
    /**
     * Posiadane narzędzie.
     * @var Ax
     */
    protected Ax _ax;
    
    /**
     * Posiadany wóz.
     * @var Vehicle
     */
    protected Vehicle _vehicle;
    
    /**
     * Lista posiadanych przy sobie drzew.
     * @var ArrayList<Wood>
     */
    protected ArrayList<Wood> _woods;
    
    /**
     * Statystyki drwala.
     * @var WoodmanStatistics_Interface
     */
    protected WoodmanStatistics_Interface _statistics;
    
    public Woodman(String id, String pathToClipsFile, WoodmanStatistics_Interface stat, MapFrame mapFrame, OknoMapy om) {
        super(id, pathToClipsFile);
        this._statistics = stat;
        this.setAx(null);
        this.setVehicle(null);
        this._woods = new ArrayList<Wood>();
        this.mapFrame = mapFrame;
        this._capacity = 10;
        this._fieldOfView = 1;
        this._possibleMove = 1;
        this._velocity = 1;
        this.opp = om.nowyObiektPierwszegoPlanu(mapFrame.getX(), mapFrame.getY(), 1662);
    }
    
    /**
     * Getter dla pojemności drwala.
     * @return int
     */
    public int getCapacity() {
        if(this.getVehicle() != null) {
            return this.getVehicle().getCapacity();
        }
        return this._capacity;
    }
    
    /**
     * Setter dla pojemności drwala.
     * @param int capacity
     * @return Woodman
     */
    public Woodman setCapacity(int capacity) {
        this._capacity = capacity;
        
        return this;
    }
    
    /**
     * Getter dla siekiery.
     * @return Ax 
     */
    public Ax getAx() {
        return this._ax;
    }
    
    /**
     * Setter dla siekiery.
     * @param Ax ax
     * @return Woodman
     */
    public Woodman setAx(Ax ax) {
        this._ax = ax;
        
        return this;
    }
    
    /**
     * Zakup siekiery. Zwraca TRUE gdy ma wystarczającą ilość golda, w przeciwnym wypadku FALSE.
     * @param Ax ax
     * @return boolean
     */
    public boolean buyAx(Ax ax) {
        if(this.getGold() < ax.getPrice()) {
            return false;
        }
        this.setGold(this.getGold() - ax.getPrice());
        this.setAx(ax);
        
        return true;
    }
    
    /**
     * Getter dla wozu.
     * @return Vehicle 
     */
    public Vehicle getVehicle() {
        return this._vehicle;
    }
    
    /**
     * Setter dla wozu.
     * @param Vehicle vehicle
     * @return Woodman
     */
    public Woodman setVehicle(Vehicle vehicle) {
        this._vehicle = vehicle;
        
        return this;
    }
    
    /**
     * Zakup wozu. Zwraca TRUE, gdy ma wystarczającą ilość golda,
     * w przeciwnym wypadku FALSE.
     * @param Vehicle vehicle
     * @return boolean
     */
    public boolean buyVehicle(Vehicle vehicle) {
        if(this.getGold() < vehicle.getPrice()) {
            return false;
        }
        this.setGold(this.getGold() - vehicle.getPrice());
        this.setVehicle(vehicle);
        
        return true;
    }
    
    /**
     * Getter dla listy drzew.
     * @return ArrayList<Wood> 
     */
    public ArrayList<Wood> getWoods() {
        return this._woods;
    }
    
    /**
     * Dodawanie drzewa.
     * @param Wood Wood
     * @return Woodman
     * @TODO Obsługa przypadku, w którym nie można wziąć więcej drzew.
     */
    public Woodman addWood(Wood wood) {
        //Sprawdzenie czy można wziąć kolejną paczkę.
        if(this.getWoods().size() < this.getCapacity()) {
            this.getWoods().add(wood);
            this.getStatistics().setNumberOfShearedWoods(this.getStatistics().getNumberOfShearedWoods() + 1);
        }
        
        return this;
    }
    
    /**
     * Sprzedawanie drewna. Zwraca TRUE jeżeli uda się sprzedać drewo i FALSE gdy nie.
     * @param Wood wood
     * @return boolean
     */
    public boolean sellWood(Wood wood) {
        if(this.getWoods().indexOf(wood) != -1) {
            this.setGold(this.getGold() + wood.getPrice());
            this.getStatistics().setIncome(this.getStatistics().getIncome() + wood.getPrice());
            this.getWoods().remove(wood);
            return true;
        }
        
        return false;
    }
    
    /**
     * Przeciążenie metody ustawiającej wielkość mieszka uwzględniając zapis do statystyk.
     * @param int gold
     * @return Woodman
     */
    @Override
    public Woodman setGold(int gold) {
        super.setGold(gold);
        this.getStatistics().setProfit(this.getGold());
        
        return this;
    }
    
    /**
     * Przeciążenie getter dla zużycia energii uwzględniając obciążenie.
     * @return int
     */
    @Override
    public int getEnergyLoss() {
        if(this.getVehicle() == null) {
            return super.getEnergyLoss() + (int)Math.round(this.getWoods().size() * 0.5);
        }
        
        return super.getEnergyLoss();
    }
    
    /**
     * Metoda odpowiedzialna za ścięcie drzewa. Zwraca TRUE jeżeli udało się ściąć drzewo i FALSE
     * w przeciwnym wypadku.
     * @return boolean
     */
    public boolean shear() {
        if(this.getAx() == null || this.getWoods().size() == this.getCapacity()) {
            return false;
        }
        this.setEnergy(this.getEnergy() - super.getEnergyLoss() * 2);
        return true;
    }
    
    /**
     * Setter dla statystyk.
     * @param WoodmanStatistics_Interface statistics
     * @return Woodman
     */
    public Woodman setStatistics(WoodmanStatistics_Interface statistics) {
        this._statistics = statistics;
        
        return this;
    }
    
    /**
     * Getter dla statystyk.
     * @return WoodmanStatistics_Interface
     */
    public WoodmanStatistics_Interface getStatistics() {
        return this._statistics;
    }

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("(drwal (udzwig ");
		buffer.append(_capacity);
		buffer.append(") ");
		if (_ax != null) {
			buffer.append(" (siekiera ");
			buffer.append(_ax.getId());
			buffer.append(")");
		}
		if (_vehicle != null) {
			buffer.append(" (woz ");
			buffer.append(_vehicle.getId());
			buffer.append(")");
		}
		buffer.append(" (scieteDrewno ");
		for (int i = 0; i < this._woods.size(); i++) {
			buffer.append(_woods.get(i)).append(" ");
		}
		buffer.append(")");

		buffer.append(" (id ");
		buffer.append(_id);
		buffer.append(")");

		buffer.append(" (mozliwyRuch ");
		buffer.append(_possibleMove);
		buffer.append(")");

		buffer.append(" (idKratki ");
		buffer.append(this.mapFrame.getId());
		buffer.append(")");
		buffer.append(" (poleWidzenia ");
		buffer.append(_fieldOfView);
		buffer.append(") (predkosc ");
		buffer.append(_velocity);
		buffer.append(") (energia ");
		buffer.append(_energy);
		buffer.append(") (strataEnergii ");
		buffer.append(_energyLoss);
		buffer.append(") (odnawianieEnergii ");
		buffer.append(_energyRecovery);
		buffer.append(") (zloto ");
		buffer.append(_gold);
		buffer.append("))");
		return buffer.toString();
	}
}
