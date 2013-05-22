package agents;

import items.*;
import java.util.ArrayList;
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
     * Ilość ścinanych drzew na iterację.
     * @var int
     */
    protected int _numberOfShearWoods;
    
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
     */
    protected ArrayList<Wood> _woods;
    
    /**
     * Konstruktor. Ustawienie domyślnego udźwigu.
     * @param int capacity 
     */
    public void Woodman(int capacity) {
        super.Agent();
        
        this.setCapacity(capacity);
        this.setNumberOfShearWoods(0);
        this.setAx(null);
        this.setVehicle(null);
        
        this._woods = new ArrayList<Wood>();
    }
    
    /**
     * Getter dla ilości ścinanych drzew.
     * @return int
     */
    public int getNumberOfShearWoods() {
        if(this.getAx() != null) {
            return this.getAx().getNumberOfShearWoods();
        }
        return this._numberOfShearWoods;
    }
    
    /**
     * Setter dla ilości ścinanych drzew.
     * @param int numberOfShearWoods
     * @return Woodman
     */
    public Woodman setNumberOfShearWoods(int numberOfShearWoods) {
        this._numberOfShearWoods = numberOfShearWoods;
        
        return this;
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
            this.getWoods().remove(wood);
            return true;
        }
        
        return false;
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
}