package agents;

/**
 * Klasa abstrakcyjna definiująca podstawowe operacja dla każdego agenta.
 * 
 * @author Piotrek
 */
abstract public class Agent {
    
    /**
     * Pole widzenia agenta.
     * @var int
     */
    protected int _fieldOfView;
    
    /**
     * Prędkość agenta w czasie jednej iteracji.
     * @var int
     * 
     * @TODO: Zróżnicowanie prędkości względem terenu, po którym się porusza.
     */
    protected int _velocity;
    
    /**
     * Energia agenta.
     * @var int
     */
    protected int _energy;
    
    /**
     * Szybkość zużycia energii podczas ruchu o jedną kratkę.
     * @var int
     */
    protected int _energyLoss;
    
    /**
     * Szybkość regeneracji energii w czasie jednej iteracji
     * @var int
     */
    protected int _energyRecovery;
    
    /**
     * Ilość sztuk złota
     * @var int
     */
    protected int _gold;
    
    /**
     * Konstruktor. Ustawienie domyślnych parametrów dla większości agentów.
     */
    public void Agent() {
        //Domyślne parametry dla większości agentów.
        this._fieldOfView = 1;
        this._velocity = 2;
        this._energy = 100;
        this._energyLoss = 2;
        this._energyRecovery = 2;
        this._gold = 0;
    }
    
    /**
     * Getter dla pola widzenia.
     * 
     * @return int 
     */
    public int getFieldOfView() {
        return this._fieldOfView;
    }
    
    /**
     * Setter dla pola widzenia.
     * 
     * @param int fieldOfView
     * @return Agent 
     */
    public Agent setFieldOfView(int fieldOfView) {
        this._fieldOfView = fieldOfView;
        
        return this;
    }
    
    /**
     * Getter dla prędkości.
     * 
     * @return int 
     */
    public int getVelocity() {
        return this._velocity;
    }
    
    /**
     * Setter dla prędkości.
     * 
     * @param int velocity
     * @return Agent 
     */
    public Agent setVelocity(int velocity) {
        this._velocity = velocity;
        
        return this;
    }
    
    /**
     * Getter dla energii.
     * 
     * @return int 
     */
    public int getEnergy() {
        return this._energy;
    }
    
    /**
     * Setter dla energii.
     * 
     * @param int energy
     * @return Agent 
     */
    public Agent setEnergy(int energy) {
        if(energy > 100) {
            energy = 100;
        }       
        
        this._energy = energy;
        
        return this;
    }
    
    /**
     * Getter dla szybkości utraty energii.
     * 
     * @return int 
     */
    public int getEnergyLoss() {
        return this._energyLoss;
    }
    
    /**
     * Setter dla szybkości utraty energii.
     * 
     * @param int energyLoss
     * @return Agent 
     */
    public Agent setEnergyLoss(int energyLoss) {
        this._energyLoss = energyLoss;
        
        return this;
    }
    
    /**
     * Getter dla szybkości regeneracji energii.
     * 
     * @return int 
     */
    public int getEnergyRecovery() {
        return this._energyRecovery;
    }
    
    /**
     * Setter dla szybkości regeneracji energii.
     * 
     * @param int energyRecovery
     * @return Agent 
     */
    public Agent setEnergyRecovery(int energyRecovery) {
        this._energyRecovery = energyRecovery;
        
        return this;
    }
    
    /**
     * Getter dla ilości złota.
     * 
     * @return int 
     */
    public int getGold() {
        return this._gold;
    }
    
    /**
     * Setter dla ilości złota.
     * 
     * @param int gold
     * @return Agent 
     */
    public Agent setGold(int gold) {
        this._gold = gold;
        
        return this;
    }
    
    /**
     * Zużycie energii podczas ruchu.
     */
    public void run() {
        this.setEnergy(this.getEnergy()-this.getEnergyLoss());
    }
    
    /**
     * Odzyskiwanie energii podczas odpoczynku.
     */
    public void recover() {
        this.setEnergy(this.getEnergy()+this.getEnergyRecovery());
    }
}
