package agents;

import java.util.ArrayList;
import items.*;

/**
 * Klasa definiująca posłańca.
 * 
 * @author Piotrek
 */
public class Courier extends Agent {
    
    /**
     * Udźwig.
     * @var int
     */
    protected int _capacity;
    
    /**
     * Lista paczek.
     * @var ArrayList<Pack>
     */
    protected PackCollection _packages;
    
    /**
     * Koń.
     * @var Hourse
     */
    protected Hourse _hourse;
    
    /**
     * Konstruktor. Nadanie domyślnego udźwigu.
     * @param capacity 
     */
    public void Courier(int capacity) {
        super.Agent();
        
        this._capacity = capacity;
        this._packages = new PackCollection();
        this._hourse = null;
    }
    
    /**
     * Getter dla udźwigu.
     * @return int
     */
    public int getCapacity() {
        if(this._hourse != null) {
            return this._hourse.getCapacity();
        }
        return this._capacity;
    }
    
    /**
     * Setter dla udźwigu.
     * @param int capacity
     * @return Courier
     */
    public Courier setCapacity(int capacity) {
        this._capacity = capacity;
        
        return this;
    }
    
    /**
     * Getter dla listy paczek.
     * @return ArrayList<Pack> 
     */
    public ArrayList<Pack> getPackages() {
        return this._packages.getAll();
    }
    
    /**
     * Dodawanie paczki.
     * @param Pack pack
     * @return Courier
     * @TODO Obsługa przypadku, w którym nie można wziąć więcej paczek.
     */
    public Courier addPackage(Pack pack) {
        //Sprawdzenie czy można wziąć kolejną paczkę.
        if(this._packages.getTotalWeight()+pack.getMass() <= this.getCapacity()) {
            this._packages.addPackage(pack);
        }
        
        return this;
    }
    
    /**
     * Usuwanie paczki.
     * @param Pack pack
     * @return Courier
     */
    public Courier removePackage(Pack pack) {
        this._packages.removePackage(pack);
        
        return this;
    }
    
    /**
     * Getter dla konia.
     * @return Hourse
     */
    public Hourse getHourse() {
        return this._hourse;
    }
    
    /**
     * Setter dla konia.
     * @param Hourse hourse
     * @return Courier
     */
    public Courier setHourse(Hourse hourse) {
        this._hourse = hourse;
        
        return this;
    }
    
    /**
     * Przeciążenie getter dla zużycia energii uwzględniając obciążenie i posiadanego konia.
     * @return int;
     */
    @Override
    public int getEnergyLoss() {
        int energyLoss = super.getEnergyLoss() + (int)Math.round(this.getCapacity() * 0.5);
        
        if(this._hourse != null) {
            energyLoss = Math.round(energyLoss * this._hourse.getAgentEnergyLossDecrease());
        }
        
        return energyLoss;
    }
    
    /**
     * Przeciążenie gettera dla prędkości posłańca.
     * @return int
     */
    @Override
    public int getVelocity() {
        if(this._hourse != null) {
            return this._hourse.getVelocity();
        }
        return super.getVelocity();
    }
}
