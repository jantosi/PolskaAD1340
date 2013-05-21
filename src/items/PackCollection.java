package items;

import java.util.ArrayList;

/**
 * Klasa kontenera paczek.
 * 
 * @author Piotrek
 */
public class PackCollection {
    protected ArrayList<Pack> _packages;
    
    public PackCollection() {
        this._packages = new ArrayList<Pack>();
    }
    
    /**
     * Dodawanie paczki.
     * @param Pack pack
     * @return PackCollection
     */
    public PackCollection addPackage(Pack pack) {
        this._packages.add(pack);
        
        return this;
    }
    
    /**
     * Usuwanie paczki.
     * @param Pack pack
     * @return PackCollection
     */
    public PackCollection removePackage(Pack pack) {
        this._packages.remove(pack);
        
        return this;
    }
    
    /**
     * Całkowita waga paczek w kontenerze.
     * @return int
     */
    public int getTotalWeight() {
        int weight = 0;
        for(int i = 0; i < this._packages.size(); i++) {
            weight += this._packages.get(i).getMass();
        }
        
        return weight;
    }
    
    /**
     * Lista wszystkich paczek w kontenerze.
     * @return ArrayList<Pack>
     */
    public ArrayList<Pack> getAll() {
        return this._packages;
    }
}
